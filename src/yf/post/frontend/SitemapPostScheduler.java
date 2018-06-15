package yf.post.frontend;

import org.joda.time.DateTime;
import yf.post.parser.rest.scheduler.Scheduler;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.util.Date;
import java.util.logging.Logger;

@Singleton
public class SitemapPostScheduler implements Scheduler {
    public static final Logger LOGGER = Logger.getLogger(SitemapPostScheduler.class.getName());

    @EJB
    private SitemapGeneratorService service;


    @Schedule(hour = "1", persistent = false)
    public void process() {
        LOGGER.info("Sitemap Scheduler");
        final Date date = new Date();
        final Date yersterday = new DateTime(date).minusDays(1).toDate();
        service.execute(yersterday, date);
        LOGGER.info("Sitemap Scheduler DONE");
    }
}
