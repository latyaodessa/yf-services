package yf.submission.services;

import org.joda.time.DateTime;
import yf.publication.PublicationConverter;
import yf.publication.PublicationDao;
import yf.publication.bulkworkflow.PublicationBulkWorkflow;
import yf.publication.dtos.PublicationElasticDTO;
import yf.publication.entities.Publication;
import yf.submission.dtos.AllParticipantsDTO;
import yf.submission.dtos.PhotoshootingParticipantTypeEnum;
import yf.submission.dtos.SubmissionDTO;
import yf.submission.dtos.SubmissionStatusEnum;
import yf.submission.entities.Submission;
import yf.submission.entities.SubmissionParticipant;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    @Inject
    private StorageService storageService;
    @Inject
    private PublicationBulkWorkflow publicationBulkWorkflow;
    @Inject
    private PublicationConverter publicationConverter;
    @Inject
    private PublicationDao publicationDao;


    public SubmissionDTO geSubmissionByUUid(final String uuid, final Long userId) {
        final Submission submission = submissionDAO.geSubmissionByUUid(uuid, userId);
        return submissionConverter.toDto(submission);

    }

    public Map<String, Long> cleanIncompletedSubmissions() {
        final Date today = new Date();
        final Date lastDays = new DateTime(today).minusDays(2)
                .toDate();

        final List<Submission> submissions = submissionDAO.getSubmissionsWithStatus(Arrays.asList(SubmissionStatusEnum.INCOMPLETED, SubmissionStatusEnum.SEND_TO_REWORK));
        if (submissions == null) {
            return null;
        }

        Map<String, Long> submitionMap = submissions.stream().filter(submission -> {
            final Timestamp sbmtTime = new Timestamp(submission.getCreatedOn());
            return sbmtTime.before(lastDays);
        }).map(submission -> {
            submission.setStatus(SubmissionStatusEnum.DECLINED);
            submission.setComment("EXPIRED");
            submissionDAO.updateSubmission(submission);
            return submission;
        }).collect(Collectors.toMap(Submission::getUuid, sbm -> sbm.getSubmitter().getId()));


        storageService.clean(submitionMap);

        return submitionMap;
    }

        public SubmissionDTO updateEntireSubmissionAndParticipants(final SubmissionDTO dto) {

        final Submission submission = submissionDAO.getSubmissionById(dto.getId());

        if (submission == null) {
            throw new RuntimeException("Submission doesn't exist");
        }
        submissionConverter.updateExistingSubmissionData(submission, dto);

        if (dto.getAllParticipants() != null) {

            submission.getSubmissionParticipants().clear();

            Set<SubmissionParticipant> participants = convertParticipants(dto.getAllParticipants());
            submission.getSubmissionParticipants().addAll(participants);
        }

        submissionDAO.updateSubmission(submission);

        if (dto.getStatus().equals(SubmissionStatusEnum.BLOCKED) || dto.getStatus().equals(SubmissionStatusEnum.DECLINED)) {
            deleteUploads(dto.getUuid(), dto.getUser().getId());
        }

        return submissionConverter.toDto(submission);
    }

    public void deleteUploads(final String uUID, final Long userId) {
        Map<String, Long> submitionMap = new HashMap<String, Long>() {{
            put(uUID, userId);
        }};
        storageService.clean(submitionMap);

    }

    public PublicationElasticDTO publishSubmission(final Long submissionId) {

        final Submission submission = submissionDAO.getSubmissionById(submissionId);

        if (submission == null) {
            throw new RuntimeException("Submission doesn't exist");
        }

        final Publication publicationBySubmissionId = publicationDao.getPublicationBySubmissionId(submissionId);

        if(publicationBySubmissionId != null) {
            return publicationConverter.publicationToElasticDTO(publicationBySubmissionId);
        }

        submission.setStatus(SubmissionStatusEnum.ACCEPTED);
        submission.setCreatedOn(new Date().getTime());
        submission.setComment(null);
        submissionDAO.updateSubmission(submission);

        Publication publication =  submissionConverter.submissionToPublication(submission);

        publicationBulkWorkflow.execute(Collections.singletonList(publication));
        return publicationConverter.publicationToElasticDTO(publication);

    }

    public List<SubmissionDTO> getUserSubmissions(final Long userId, final int offset, final int limit) {
        final List<Submission> submissionsList = submissionDAO.getSubmissionsByUserId(userId, offset, limit);
        return submissionsList.stream().map(submission -> submissionConverter.toDto(submission)).collect(Collectors.toList());
    }

    public List<SubmissionDTO> getSubmissionsByStatus(final SubmissionStatusEnum statusEnum) {
        final List<Submission> submissionsList = submissionDAO.getSubmissionsWithStatus(Collections.singletonList(statusEnum));
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
        final Submission submission = submissionDAO.getSubmissionById(submissionDTO.getId());

        final Set<SubmissionParticipant> participants = convertParticipants(submissionDTO.getAllParticipants());

        submission.getSubmissionParticipants().clear();
        submission.getSubmissionParticipants().addAll(participants);
        submission.setCreatedOn(new Date().getTime());
        final Submission persistNewSubmission = submissionDAO.updateSubmission(submission);
        return submissionConverter.toDto(persistNewSubmission);
    }

    public SubmissionDTO submit(final SubmissionDTO submissionDTO) {
        final Submission submission = submissionDAO.getSubmissionById(submissionDTO.getId());

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
