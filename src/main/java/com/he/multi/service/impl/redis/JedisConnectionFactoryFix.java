package com.he.multi.service.impl.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.ClusterCommandExecutor;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class JedisConnectionFactoryFix extends JedisConnectionFactory {

    private JedisClusterPipeline cluster;

    public JedisConnectionFactoryFix(RedisClusterConfiguration clusterConfiguration) {
        super(clusterConfiguration);
    }

    public JedisClusterPipeline getCluster() {
        return cluster;
    }

    public void setCluster(JedisClusterPipeline cluster) {
        this.cluster = cluster;
    }

    public JedisCluster createCluster(RedisClusterConfiguration clusterConfiguration, GenericObjectPoolConfig poolConfig) {
        Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
        for (RedisNode node : clusterConfiguration.getClusterNodes()) {
            hostAndPorts.add(new HostAndPort(node.getHost(), node.getPort()));
        }
        int redirects = clusterConfiguration.getMaxRedirects() != null ? clusterConfiguration.getMaxRedirects() : 5;
        JedisClusterPipeline cluster = StringUtils.hasText(this.getPassword()) ? new JedisClusterPipeline(hostAndPorts, getTimeout(), getTimeout(), redirects, getPassword(), poolConfig) : new JedisClusterPipeline(hostAndPorts, getTimeout(), redirects, poolConfig);
        this.setCluster(cluster);
        return cluster;
    }

    @Override
    public void afterPropertiesSet() {
                super.afterPropertiesSet();
                try {
                    Field clusterCommandExecutorField = JedisConnectionFactory.class.getDeclaredField("clusterCommandExecutor");
                    clusterCommandExecutorField.setAccessible(true);
                    ClusterCommandExecutor clusterCommandExecutor = (ClusterCommandExecutor) clusterCommandExecutorField.get(this);
                    Field executorField = ClusterCommandExecutor.class.getDeclaredField("executor");
                    executorField.setAccessible(true);
                    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
                    int maxTotal = this.getPoolConfig().getMaxTotal();
                    threadPoolTaskExecutor.setCorePoolSize(maxTotal);
                    threadPoolTaskExecutor.initialize();
                    executorField.set(clusterCommandExecutor, threadPoolTaskExecutor);
                }catch (Exception e){
                    e.printStackTrace();
                }
    }
}
