package yf.post.parser.rest.scheduler;

import yf.settings.SystemSettings;
import yf.settings.SystemSettingsWorkflow;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.inject.Inject;
import java.util.Date;
import java.util.logging.Logger;

@Singleton
public class RestScheduler implements Scheduler {

    public static final String VK_SCHEDULER_ENABLED_SETTING = "vk_scheduler_enabled";
    private static final Logger LOG = Logger.getLogger(RestScheduler.class.getName());
    @Inject
    private SchedulerProcessor processor;
    @Inject
    private SystemSettingsWorkflow systemSettingsWorkflow;

    @Schedule(minute = "*/30", hour = "*")
    @Timeout
    public void process() {

        final SystemSettings setting = systemSettingsWorkflow.getSystemSettingByKey(VK_SCHEDULER_ENABLED_SETTING);

        if (setting == null || Boolean.parseBoolean(setting.getValue())) {
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
