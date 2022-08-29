package com.imooc.miaosha.redis;

public abstract class BasePrefix implements KeyPrefix{

    private int expireSeconds; // expireSeconds默认为0，永不过期
    private String prefix;

    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() { //默认永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        //  getClass().getSimpleName() 返回类名
        // https://blog.csdn.net/jusang486/article/details/22276489?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-1.no_search_link&spm=1001.2101.3001.4242.2&utm_relevant_index=4
        String className = getClass().getSimpleName();
        return className +":" + prefix;
    }



}
