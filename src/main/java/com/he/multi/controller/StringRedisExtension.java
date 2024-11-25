package com.he.multi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class StringRedisExtension implements InitializingBean {

    @Value("${redis.scan.count:10000}")
    private String REDIS_SCAN_COUNT;

    private static String redisScanCount;

    private static final Logger logger = LoggerFactory.getLogger(StringRedisExtension.class);

    public void keys(RedisTemplate redisTemplate, final String pattern, final StringRedisExtensionMethod redisExtensionMethod) {
        Assert.notNull(pattern, "pattern must not be null");

        redisTemplate.execute(new RedisCallback<Integer>() {
            @Override
            public Integer doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Map<String, JedisPool> clusterNodes = ((JedisCluster) redisConnection.getNativeConnection()).getClusterNodes();
                List<Jedis> jedisList = getMasterJedisClusterNodes(clusterNodes);
                int size = jedisList.size();
                if (jedisList.size() > 0) {
                    try {


                        logger.info("jedisList size is " + jedisList.size());
                        ExecutorService executorService = Executors.newFixedThreadPool(jedisList.size());
                        CountDownLatch countDownLatch = new CountDownLatch(jedisList.size());
                        for (Jedis jedis : jedisList) {
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        getScan(jedis, pattern, redisExtensionMethod, Integer.valueOf(REDIS_SCAN_COUNT));
                                    } finally {
                                        countDownLatch.countDown();
                                        jedis.close();
                                    }
                                }
                            });
                        }
                        countDownLatch.await();
                        logger.debug("all sub thread is done ");
                        executorService.shutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        logger.debug("master thread is done ");
                    }
                }
                return size;
            }
        });


    }

    public void keys(RedisTemplate redisTemplate, final String pattern, final StringRedisExtensionMethod redisExtensionMethod, Integer redisScanCount) {
        Assert.notNull(pattern, "pattern must not be null");

        redisTemplate.execute(new RedisCallback<Integer>() {
            @Override
            public Integer doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Map<String, JedisPool> clusterNodes = ((JedisCluster) redisConnection.getNativeConnection()).getClusterNodes();
                List<Jedis> jedisList = getMasterJedisClusterNodes(clusterNodes);
                int size = jedisList.size();
                if (jedisList.size() > 0) {
                    try {
                        logger.info("jedisList size is " + jedisList.size());
                        ExecutorService executorService = Executors.newFixedThreadPool(jedisList.size());
                        CountDownLatch countDownLatch = new CountDownLatch(jedisList.size());
                        for (Jedis jedis : jedisList) {
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        getScan(jedis, pattern, redisExtensionMethod, redisScanCount);
                                    } finally {
                                        countDownLatch.countDown();
                                        jedis.close();
                                    }
                                }
                            });
                        }
                        countDownLatch.await();
                        logger.debug("all sub thread is done ");
                        executorService.shutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        logger.debug("master thread is done ");
                    }
                }
                return size;
            }
        });


    }

    public int count(RedisTemplate redisTemplate, final String pattern) {
        Assert.notNull(pattern, "pattern must not be null");

        return (Integer) redisTemplate.execute(new RedisCallback<Integer>() {

            @Override
            public Integer doInRedis(RedisConnection redisConnection) throws DataAccessException {
                AtomicInteger atomicInteger = new AtomicInteger();
                Map<String, JedisPool> clusterNodes = ((JedisCluster) redisConnection.getNativeConnection()).getClusterNodes();
                List<Jedis> jedisList = getMasterJedisClusterNodes(clusterNodes);
                if (jedisList.size() > 0) {
                    try {
                        logger.info("jedis master num is :{}", jedisList.size());
                        ExecutorService executorService = Executors.newFixedThreadPool(jedisList.size());
                        List<Future<Integer>> futureList = new ArrayList<>();
                        for (Jedis jedis : jedisList) {
                            Future<Integer> future = executorService.submit(new Callable<Integer>() {
                                @Override
                                public Integer call() throws Exception {
                                    try {
                                        return getScan(jedis, pattern, Integer.valueOf(REDIS_SCAN_COUNT));
                                    } finally {
                                        if (jedis != null) {
                                            jedis.close();
                                        }
                                    }

                                }
                            });
                            futureList.add(future);
                            if (!futureList.isEmpty()) {
                                long start = System.currentTimeMillis();
                                for (Future<Integer> future1 : futureList) {
                                    Integer get = future1.get();
                                    atomicInteger.addAndGet(get);
                                }
                                long end = System.currentTimeMillis();
                                logger.info("keys count query user time : {} ms", end - start);
                            }
                            executorService.shutdown();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return atomicInteger.get();
            }
        });
    }

    public int count(RedisTemplate redisTemplate, final String pattern, Integer redisScanCount) {
        Assert.notNull(pattern, "pattern must not be null");

        return (Integer) redisTemplate.execute(new RedisCallback<Integer>() {

            @Override
            public Integer doInRedis(RedisConnection redisConnection) throws DataAccessException {
                AtomicInteger atomicInteger = new AtomicInteger();
                Map<String, JedisPool> clusterNodes = ((JedisCluster) redisConnection.getNativeConnection()).getClusterNodes();
                List<Jedis> jedisList = getMasterJedisClusterNodes(clusterNodes);
                if (jedisList.size() > 0) {
                    try {
                        logger.info("jedis master num is :{}", jedisList.size());
                        ExecutorService executorService = Executors.newFixedThreadPool(jedisList.size());
                        List<Future<Integer>> futureList = new ArrayList<>();
                        for (Jedis jedis : jedisList) {
                            Future<Integer> future = executorService.submit(new Callable<Integer>() {
                                @Override
                                public Integer call() throws Exception {
                                    try {
                                        return getScan(jedis, pattern, redisScanCount);
                                    } finally {
                                        if (jedis != null) {
                                            jedis.close();
                                        }
                                    }

                                }
                            });
                            futureList.add(future);
                            if (!futureList.isEmpty()) {
                                long start = System.currentTimeMillis();
                                for (Future<Integer> future1 : futureList) {
                                    Integer get = future1.get();
                                    atomicInteger.addAndGet(get);
                                }
                                long end = System.currentTimeMillis();
                                logger.info("keys count query user time : {} ms", end - start);
                            }
                            executorService.shutdown();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return atomicInteger.get();
            }
        });
    }


    private List<Jedis> getMasterJedisClusterNodes(Map<String, JedisPool> clusterNodes) {
        List<Jedis> jedisList = new ArrayList<Jedis>();
        for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {
            try {
                JedisPool jedisPool = entry.getValue();
                Jedis jedis = jedisPool.getResource();
                if (!jedis.info("replication").contains("role:slave")) {
                    jedisList.add(jedis);
                } else {
                    jedis.close();
                }
            } catch (Exception e) {
                logger.warn("getMasterJedisClusterNodes error", e);
            }
        }
        return jedisList;
    }

    private int getScan(final Jedis jedis, final String pattern, StringRedisExtensionMethod redisExtensionMethod, Integer redisScanCount) {

        int count = 0;
        ScanParams params = new ScanParams();
        params.match(pattern);
        if (redisScanCount != null && redisScanCount > 0) {
            params.count(redisScanCount);
        } else {
            params.count(Integer.valueOf(REDIS_SCAN_COUNT));
        }

        String cursor = "0";

        do {
            ScanResult<String> scanResult = jedis.scan(cursor, params);
            cursor = scanResult.getStringCursor();
            List<String> scanResultList = scanResult.getResult();
            if (scanResultList != null && scanResultList.size() > 0) {
                int countTemp = scanResultList.size();
                count = count + countTemp;
                for (String key : scanResultList) {
                    redisExtensionMethod.execute(key);
                }
            }
        } while (!"0".equals(cursor));

        return count;
    }


    private int getScan(final Jedis jedis, final String pattern, Integer redisScanCount) {
        int count = 0;
        ScanParams param = new ScanParams();
        param.match(pattern);
        if (null != redisScanCount && redisScanCount > 0) {
            param.count(redisScanCount);
        } else {
            param.count(Integer.valueOf(REDIS_SCAN_COUNT));
        }
        String cursor = "0";
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, param);
            scanResult.getStringCursor();
            List<String> scanResultList = scanResult.getResult();
            if (scanResultList != null && !scanResultList.isEmpty()) {
                int countTemp = scanResultList.size();
                count = countTemp + count;
            }
        } while (!"0".equals(cursor));
        return count;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisScanCount = REDIS_SCAN_COUNT;
    }
}
