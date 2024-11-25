package com.he.multi.service.impl.redis;

import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;

import java.io.IOException;
import java.util.HashSet;

public class EcifRedisClusterConfiguration extends RedisClusterConfiguration {

    public EcifRedisClusterConfiguration(String nodes, int maxRedirects) throws IOException {
        super();
        HashSet<RedisNode> redisNodes   = new HashSet<>();

        String[] nodeStr = nodes.split(":");
        for (String node : nodeStr) {
            String[] split = node.split(":");
            RedisNode redisNode = new RedisNode(split[0], Integer.parseInt(split[1]));
            redisNodes.add(redisNode);
        }
        this.setClusterNodes(redisNodes);
        this.setMaxRedirects(maxRedirects);

    }
}
