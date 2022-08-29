package com.imooc.miaosha.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

// jedis是redis连接Java的开发工具

@Service
public class RedisPoolFactory {

    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool jedisPoolFactory() {
        // 连接池内部配置对象实例
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大空闲连接数
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        // 最大连接数
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        //  获取连接时的最大等待毫秒数
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
        // 初始化连接池
        JedisPool jp = new JedisPool(poolConfig, redisConfig.getHost(),
                redisConfig.getPort(), redisConfig.getTimeout()*1000,
                redisConfig.getPassword(), 0);

        return jp;
    }
}
