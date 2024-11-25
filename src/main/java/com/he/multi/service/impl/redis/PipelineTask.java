package com.he.multi.service.impl.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.List;
import java.util.concurrent.Callable;

public class PipelineTask implements Callable<PipelineTask> {

    private static final Logger logger = LoggerFactory.getLogger(PipelineTask.class);

    private Pipeline pipeline;

    private Jedis jedis;

    private List<String> subKeys;

    public boolean isSyncOk() {
        return syncOk;
    }

    public void setSyncOk(boolean syncOk) {
        this.syncOk = syncOk;
    }

    private boolean syncOk=true;

    public List<String> getSubKeys(){
        return subKeys;
    }

    public PipelineTask(Pipeline pipeline, Jedis jedis, List<String> subKeys, boolean syncOk) {
        this.pipeline = pipeline;
        this.jedis = jedis;
        this.subKeys = subKeys;
        this.syncOk = syncOk;
    }

    @Override
    public PipelineTask call() throws Exception {
        if(!syncOk){
            return this;
        }
        try {
            pipeline.sync();
        }catch (Exception e){
            logger.error(e.getMessage());
            syncOk=false;
        }finally {
            if(null != pipeline){
                pipeline.close();
            }
            if(null != jedis){
                jedis.close();
            }
        }


        return null;
    }
}
