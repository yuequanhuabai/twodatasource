package com.he.multi.service.impl.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;

import java.util.Set;

public class JedisPipeLineConnectionHandler extends JedisSlotBasedConnectionHandler {
    public JedisPipeLineConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password) {
        super(nodes, poolConfig, connectionTimeout, soTimeout, password);
    }


    public JedisPipeLineConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig, int soTimeout) {
        super(nodes, poolConfig, soTimeout, soTimeout);
    }

//    public JedisPipeLineConnectionHandler(HostAndPort node, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password) {
//        super(node,poolConfig,connectionTimeout,soTimeout,password);
//    }

    public JedisPool getJedisPool(int slot) {
        JedisPool slotPool = cache.getSlotPool(slot);
        if (slotPool != null) {
            return slotPool;
        } else {
            renewSlotCache();
            cache.getSlotPool(slot);
            if (slotPool != null) {
                return slotPool;
            } else {
                throw new RuntimeException("slotPool is null");
            }
        }
    }

}
