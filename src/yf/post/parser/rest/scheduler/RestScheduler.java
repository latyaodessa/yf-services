
package yf.post.parser.rest.scheduler;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.inject.Inject;
import java.util.Date;
import java.util.logging.Logger;

@Singleton
public class RestScheduler extends Scheduler {

    private static final Logger LOG = Logger.getLogger(RestScheduler.class.getName());
    @Inject
    private SchedulerProcessor processor;

    @Schedule(minute = "*/30", hour = "*")
    @Timeout
    public void process() {

        if (isEnvVariableEnabled()) {
            LOG.info("Scheduler VK started : " + new Date());
            processor.triggerVkScraper();
            LOG.info("Scheduler VK finished : " + new Date());
        } else {
            LOG.info("Scheduler VK IS DISABLED : " + new Date());
        }
    }

    // @Schedule(hour = "11")
    // @Timeout
    // public void weeklyTop() {
    // processor.triggerWeeklyTop();
    // }

}