package com.imooc.miaosha.redis;

public class UserKey extends BasePrefix{

    private UserKey(String prefix) {
        // super调用父类中的构造方法
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");

}
