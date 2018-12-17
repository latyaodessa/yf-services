package yf.submission.services;

import org.joda.time.DateTime;
import yf.submission.dtos.AllParticipantsDTO;
import yf.submission.dtos.PhotoshootingParticipantTypeEnum;
import yf.submission.dtos.SubmissionDTO;
import yf.submission.dtos.SubmissionStatusEnum;
import yf.submission.entities.Submission;
import yf.submission.entities.SubmissionParticipant;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class SubmissionService {

    @Inject
    private SubmissionConverter submissionConverter;
    @Inject
    private SubmissionWorkflow submissionWorkflow;
    @Inject
    private SubmissionDAO submissionDAO;

    public SubmissionDTO geSubmissionByUUid(final String uuid, final Long userId) {
        final Submission submission = submissionDAO.geSubmissionByUUid(uuid, userId);
        return submissionConverter.toDto(submission);

    }

    public List<Submission> cleanIncompletedSubmissions() {
        final Date today = new Date();
        final Date lastDays = new DateTime(today).minusDays(2)
                .toDate();

        final List<Submission> submissions = submissionDAO.getSubmissionsWithStatus(SubmissionStatusEnum.INCOMPLETED);
        if (submissions == null) {
            return null;
        }

        final List<Submission> expiredSubmittions = submissions.stream().filter(submission -> {
            final Timestamp sbmtTime = new Timestamp(submission.getCreatedOn());
            return sbmtTime.before(lastDays);
        }).map(submission -> {
            submission.setStatus(SubmissionStatusEnum.DECLINED);
            submission.setComment("EXPIRED");
            submissionDAO.updateSubmission(submission);
            return submission;
        }).collect(Collectors.toList());

        return expiredSubmittions;


    }

    public List<SubmissionDTO> getUserSubmissions(final Long userId, final int offset, final int limit) {
        final List<Submission> submissionsList = submissionDAO.getSubmissionsByUserId(userId, offset, limit);
        return submissionsList.stream().map(submission -> submissionConverter.toDto(submission)).collect(Collectors.toList());
    }

    public SubmissionDTO initSubmission(final AllParticipantsDTO allParticipantsDTO, final Long userId) {
        final Set<SubmissionParticipant> participants = convertParticipants(allParticipantsDTO);
        final Submission submission = submissionWorkflow.initEmptySubmission(userId);
        submission.setSubmissionParticipants(participants);

        final Submission persistNewSubmission = submissionDAO.persistNewSubmission(submission);
        return submissionConverter.toDto(persistNewSubmission);
    }


    public SubmissionDTO updateSubmission(final SubmissionDTO submissionDTO) {
        final Submission submission = submissionDAO.findSavedPublicationById(submissionDTO.getId());

        final Set<SubmissionParticipant> participants = convertParticipants(submissionDTO.getAllParticipants());

        submission.getSubmissionParticipants().clear();
        submission.getSubmissionParticipants().addAll(participants);
        submission.setCreatedOn(new Date().getTime());
        final Submission persistNewSubmission = submissionDAO.updateSubmission(submission);
        return submissionConverter.toDto(persistNewSubmission);
    }

    public SubmissionDTO submit(final SubmissionDTO submissionDTO) {
        final Submission submission = submissionDAO.findSavedPublicationById(submissionDTO.getId());

        final Set<SubmissionParticipant> participants = convertParticipants(submissionDTO.getAllParticipants());

        submission.getSubmissionParticipants().clear();
        submission.getSubmissionParticipants().addAll(participants);
        submissionConverter.handleSubmissionConvertion(submissionDTO, submission);
        final Submission persistNewSubmission = submissionDAO.updateSubmission(submission);
        return submissionConverter.toDto(persistNewSubmission);
    }

    private Set<SubmissionParticipant> convertParticipants(final AllParticipantsDTO allParticipantsDTO) {
        final Set<SubmissionParticipant> participants = new HashSet<>();

        participants.addAll(
                allParticipantsDTO.getHairStylists()
                        .stream().map(dto -> submissionConverter.participantDTOToEntity(dto, PhotoshootingParticipantTypeEnum.HAIR_STAILIST))
                        .collect(Collectors.toList()));

        participants.addAll(
                allParticipantsDTO.getMds()
                        .stream().map(dto -> submissionConverter.participantDTOToEntity(dto, PhotoshootingParticipantTypeEnum.MD))
                        .collect(Collectors.toList()));

        participants.addAll(
                allParticipantsDTO.getMuas()
                        .stream().map(dto -> submissionConverter.participantDTOToEntity(dto, PhotoshootingParticipantTypeEnum.MUA))
                        .collect(Collectors.toList()));

        participants.addAll(
                allParticipantsDTO.getPhs()
                        .stream().map(dto -> submissionConverter.participantDTOToEntity(dto, PhotoshootingParticipantTypeEnum.PH))
                        .collect(Collectors.toList()));

        participants.addAll(
                allParticipantsDTO.getSetDesigner()
                        .stream().map(dto -> submissionConverter.participantDTOToEntity(dto, PhotoshootingParticipantTypeEnum.SET_DESIGNER))
                        .collect(Collectors.toList()));

        participants.addAll(
                allParticipantsDTO.getWardrobeStylists()
                        .stream().map(dto -> submissionConverter.participantDTOToEntity(dto, PhotoshootingParticipantTypeEnum.WARDROBE_STYLIST))
                        .collect(Collectors.toList()));
        return participants;
    }
}
