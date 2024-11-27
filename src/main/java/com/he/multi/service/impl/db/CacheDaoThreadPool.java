package com.he.multi.service.impl.db;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CacheDaoThreadPool implements InitializingBean {

    ThreadPoolExecutor executor=null;

    @Override
    public void afterPropertiesSet() throws Exception {
        
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("CacheDaoThreadPool-%d").build();
        executor= new ThreadPoolExecutor(50,200,0, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(512),
                namedThreadFactory,new ThreadPoolExecutor.AbortPolicy());
    }

    public void execute(Runnable runnable){
        Assert.notNull(executor,"CacheDaoThreadPool executor is null");
        executor.execute(runnable);
    }

    public void shutdown(){
        if(executor!=null){
            if(executor.isShutdown()){
                executor.shutdown();
            }
        }
    }

}
