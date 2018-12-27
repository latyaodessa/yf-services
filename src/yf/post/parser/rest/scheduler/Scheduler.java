package yf.post.parser.rest.scheduler;

public abstract class Scheduler {
    public static final String ENV_SCHEDULER_TASK = "TASK_NAME";
    public static final String FIREST_TASKE = "backend.1";

    public boolean isEnvVariableEnabled() {
        final String envSchedulerTaskEnvVeriable = System.getenv(ENV_SCHEDULER_TASK);
        return envSchedulerTaskEnvVeriable == null || envSchedulerTaskEnvVeriable.contains(FIREST_TASKE);

    }
}