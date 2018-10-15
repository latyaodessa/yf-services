package yf.submission.services;

import yf.submission.dtos.SubmissionParticipantDTO;
import yf.submission.entities.Submission;
import yf.submission.entities.SubmissionParticipant;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class SubmissionService {

    @Inject
    private SubmissionConverter submissionConverter;
    @Inject
    private SubmissionWorkflow submissionWorkflow;


    public Submission initSubmission(final List<SubmissionParticipantDTO> submissionParticipants, final Long userId) {

        final List<SubmissionParticipant> participants = submissionParticipants
                .stream()
                .map(dto -> submissionConverter.participantDTOToEntity(dto))
                .collect(Collectors.toList());

        final Submission submission = submissionWorkflow.initEmptySubmission(userId);
        submission.setSubmissionParticipants(participants);

        return submission;

    }
}
