package yf.post.parser.rest.scheduler;

import yf.post.parser.ParserService;

import javax.inject.Inject;
import java.util.logging.Logger;

public class SchedulerProcessor {

    @Inject
    private ParserService parserService;
    private Logger logger = Logger.getLogger(RestScheduler.class.getName());

    void triggerVkScraper() {
        try {
            parserService.triggerPostParserForNewPosts();
            logger.info("New Posts Rest Call is triggered");
        } catch (Exception e) {
            logger.severe("New Posts Rest Call EXCEPTION! resexp" + e);
        }
    }

    void triggerWeeklyTop() {
        try {
            parserService.getAndSaveWeeklyTop();
            logger.info("WEEKLY TOP Rest Call is triggered");
        } catch (Exception e) {
            logger.severe("WEEKLY TOP Rest Call EXCEPTION! resexp" + e);
        }
    }

}
