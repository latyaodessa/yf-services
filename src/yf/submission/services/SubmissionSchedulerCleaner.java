package yf.submission.services;

import yf.post.parser.rest.scheduler.Scheduler;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.logging.Logger;

@Singleton
public class SubmissionSchedulerCleaner extends Scheduler {
    public static final Logger LOGGER = Logger.getLogger(SubmissionSchedulerCleaner.class.getName());

    @Inject
    private SubmissionService submissionService;

    @Schedule(hour = "*/1", info = "Submission cleaner Scheduler. Every 1 hour", persistent = false)
    public void process() {
        if (isEnvVariableEnabled()) {
            LOGGER.info("Submission cleaner Scheduler");
            submissionService.cleanIncompletedSubmissions();
            LOGGER.info("Submission cleaner DONE");
        } else {
            LOGGER.info("Submission cleaner DISABLED");
        }

    }
}
