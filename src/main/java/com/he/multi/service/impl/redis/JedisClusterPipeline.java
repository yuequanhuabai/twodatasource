package com.he.multi.service.impl.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Set;

public class JedisClusterPipeline extends JedisCluster {

    public JedisClusterPipeline(Set<HostAndPort> nodes, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig poolConfig) {
        super(nodes, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
        super.connectionHandler=new JedisPipeLineConnectionHandler(nodes,poolConfig,connectionTimeout,soTimeout,password);
    }

    public JedisClusterPipeline(Set<HostAndPort> nodes, int timeout, int maxAttempts,GenericObjectPoolConfig poolConfig) {
        super(nodes,timeout,maxAttempts,poolConfig);
        super.connectionHandler=new JedisPipeLineConnectionHandler(nodes,poolConfig,timeout);
    }

}
