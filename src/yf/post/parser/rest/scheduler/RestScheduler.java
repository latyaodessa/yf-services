package yf.post.parser.rest.scheduler;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.inject.Inject;
import java.util.Date;
import java.util.logging.Logger;

@Singleton
public class RestScheduler implements Scheduler {

    private static final Logger LOG = Logger.getLogger(RestScheduler.class.getName());

    @Inject
    private SchedulerProcessor processor;


    @Schedule(minute = "*/30", hour = "*")
    @Timeout
    public void process() {
        LOG.info("Scheduler VK started : " + new Date());
        processor.triggerVkScraper();
        LOG.info("Scheduler VK finished : " + new Date());
    }

//    @Schedule(hour = "11")
//    @Timeout
//    public void weeklyTop() {
//        processor.triggerWeeklyTop();
//    }

}
