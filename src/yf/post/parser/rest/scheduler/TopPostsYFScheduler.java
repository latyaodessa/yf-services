package yf.post.parser.rest.scheduler;

import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.inject.Inject;

import yf.post.parser.workflow.PostParserWorkflow;

@Singleton
public class TopPostsYFScheduler implements Scheduler{
	@Inject
	private PostParserWorkflow postWorkflow;
    private Logger logger = Logger.getLogger(RestScheduler.class.getName());


    @Schedule(second = "0", minute = "30", hour = "0", dayOfMonth = "*")
	@Timeout
	public void process() {
		postWorkflow.saveUpdateWeeklyTop();
		logger.info("Weekly Top Updated");		
	}
	
}
