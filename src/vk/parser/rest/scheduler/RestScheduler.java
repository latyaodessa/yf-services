package vk.parser.rest.scheduler;

import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.inject.Inject;

import vk.parser.rest.client.ParserRestClient;

@Singleton
public class RestScheduler implements Scheduler{

	@Inject
	ParserRestClient parserRestClient;
	
    private Logger logger = Logger.getLogger(RestScheduler.class.getName());
    


	
    @Schedule(second = "05", minute = "01,03,05", hour = "*")
	@Timeout
	public void process() {
    		parserRestClient.checkNewPosts();
    		logger.info("New Posts Rest Call is triggered");
			
		
	}

}
