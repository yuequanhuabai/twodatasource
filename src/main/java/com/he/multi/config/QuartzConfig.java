package com.he.multi.config;

import com.he.multi.job.MyJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Configuration
public class QuartzConfig {

    private static Logger logger = LoggerFactory.getLogger(QuartzConfig.class);

//    @Value("classpath:quartz.properties")
//    private String customQuartzProperties;

    @Bean
    public JobDetail myJobDetail(){
        return JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger myTrigger(){
        return TriggerBuilder.newTrigger()
                .withIdentity("myTrigger")
                .forJob(myJobDetail())
                .withSchedule(cronSchedule("1 0/50 * * * ?"))
                .build();
    }


//    @Autowired JobFactory jobFactory
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setJobDetails(myJobDetail());
        factory.setTriggers(myTrigger());
//        factory.setJobFactory(jobFactory);

        return factory;
    }

//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean(JobDetail myJobDetail, Trigger myTrigger) throws IOException {
//        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
//
//        schedulerFactoryBean.setJobDetails(myJobDetail);
//        schedulerFactoryBean.setTriggers(myTrigger);
//
//        logger.info("读取的配置文件数据：customQuartzProperties : "+customQuartzProperties);
////        schedulerFactoryBean.setQuartzProperties(quartzProperties());
////        factory.setJobFactory(jobFactory);
//
//
//        return schedulerFactoryBean;
//    }


    public Properties quartzProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("quartz.properties"));
        return properties;
    }

}
