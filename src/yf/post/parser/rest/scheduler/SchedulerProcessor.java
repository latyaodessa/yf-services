package yf.post.parser.rest.scheduler;

import java.util.logging.Logger;

import javax.inject.Inject;

import yf.post.parser.ParserService;

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
