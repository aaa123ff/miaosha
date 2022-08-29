package com.imooc.miaosha.redis;

public interface KeyPrefix {

    // expire过期
    public int expireSeconds();

    // Prefix前缀
    public String getPrefix();

}
