package demo.example1;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HelloJob implements Job {

  private static Logger _log= LoggerFactory.getLogger(HelloJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        _log.info("正在运行作业!"+new Date());
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<Object, Map<String, Object>> collect = list.stream().collect(Collectors.toMap(data -> data.getOrDefault("jobType", "null"), Function.identity(), (data1, data2) -> data2));
    }



}
