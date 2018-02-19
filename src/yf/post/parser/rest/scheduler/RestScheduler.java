package yf.post.parser.rest.scheduler;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.inject.Inject;

@Singleton
public class RestScheduler implements Scheduler {


    @Inject
    private SchedulerProcessor processor;


    @Schedule(second = "05", minute = "01,03,05", hour = "*")
    @Timeout
    public void process() {
//    		parserRestClient.checkNewPosts();


        processor.triggerVkScraper();


    }

}
