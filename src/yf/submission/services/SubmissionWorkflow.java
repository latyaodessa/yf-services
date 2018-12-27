package yf.submission.services;

import yf.submission.dtos.SubmissionStatusEnum;
import yf.submission.entities.Submission;
import yf.user.UserDao;

import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;

public class SubmissionWorkflow {

    @Inject
    private UserDao userDao;

    public Submission initEmptySubmission(final Long userId) {
        Submission submission = new Submission();
        submission.setCreatedOn(new Date().getTime());
        submission.setUuid(UUID.randomUUID()
                .toString());
        submission.setSubmitter(userDao.getUserById(userId));
        submission.setStatus(SubmissionStatusEnum.INCOMPLETED);
        return submission;
    }
}
