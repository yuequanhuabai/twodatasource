//package demo.example1;
//
//import org.quartz.*;
//import org.quartz.impl.StdSchedulerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Date;
//
//import static org.quartz.CronScheduleBuilder.cronSchedule;
//import static org.quartz.DateBuilder.evenMinuteDate;
//import static org.quartz.JobBuilder.newJob;
//import static org.quartz.TriggerBuilder.newTrigger;
//
//public class CronTriggerExample {
//
//    private static Logger logger= LoggerFactory.getLogger(CronTriggerExample.class);
//    public void run() throws SchedulerException, InterruptedException {
//        logger.info("start run... ");
//
//        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
////       stdSchedulerFactory.
//        Scheduler scheduler = stdSchedulerFactory.getScheduler();
//
//        Date runTime = evenMinuteDate(new Date());
//
//        JobDetail job1 = newJob(HelloJob.class).withIdentity("job1").build();
//
//        Trigger trigger1 = newTrigger()
//                .withIdentity("trigger1")
//                .withSchedule(cronSchedule("0/5 * * * * ?"))
//                .build();
//
//        scheduler.scheduleJob(job1,trigger1);
//
//        logger.info("开始占用cpu时间片！");
//        scheduler.start();
//
//        Thread.sleep(65*1000L);
//        logger.info("shutting down ");
//        scheduler.shutdown();
//
//        logger.info("shutdown complete");
//
//    }
//
//    public static void main(String[] args) {
//        CronTriggerExample cronTriggerExample = new CronTriggerExample();
//        try {
//            cronTriggerExample.run();
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//}
