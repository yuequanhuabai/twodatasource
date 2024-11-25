//package demo.example1;
//
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.quartz.Trigger;
//import org.quartz.impl.StdSchedulerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Date;
//
//import static org.quartz.DateBuilder.evenMinuteDate;
//import static org.quartz.JobBuilder.newJob;
//import static org.quartz.TriggerBuilder.newTrigger;
//
//public class SimpleExample {
//
//   public void run() throws SchedulerException, InterruptedException {
//       Logger logger = LoggerFactory.getLogger(SimpleExample.class);
//
//       StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
////       stdSchedulerFactory.
//       Scheduler scheduler = stdSchedulerFactory.getScheduler();
//
//       Date runTime = evenMinuteDate(new Date());
//
//       JobDetail job1 = newJob(HelloJob.class).withIdentity("job1").build();
//
//       Trigger trigger1 = newTrigger().withIdentity("trigger1").startAt(runTime).build();
//
//       scheduler.scheduleJob(job1,trigger1);
//
//       logger.info("开始占用cpu时间片！");
//       scheduler.start();
//
//       Thread.sleep(65*1000L);
//       logger.info("shutting down ");
//       scheduler.shutdown();
//
//       logger.info("shutdown complete");
//
//   }
//
//    public static void main(String[] args) {
//        SimpleExample simpleExample = new SimpleExample();
//        try {
//            simpleExample.run();
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//}
