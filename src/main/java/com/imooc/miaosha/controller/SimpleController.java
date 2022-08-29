package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller  // 创建bean实例
// 用于建立请求URL和处理请求方法之间的对应关系 客户端一定会发一个请求过来,那么后台一定要有一个方法去执行它。 把发送请求的这个地址路径跟这个方法建立一个映射关联 你发送这个请求,那我这个方法就执行。
@RequestMapping("/demo")
public class SimpleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    @RequestMapping("/db/get")//java->class
    //用指定的格式将一个方法的返回值加载到response的body区域，向手机客户机返回数据信息
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

//    @RequestMapping("/mq")//java->class
//    //用指定的格式将一个方法的返回值加载到response的body区域，向手机客户机返回数据信息
//    @ResponseBody
//    public Result<String> mq() {
//        sender.send("hello, imooc");
//        return Result.success("hello, world");
//    }
//
//    @RequestMapping("/mq/topic")//java->class
//    //用指定的格式将一个方法的返回值加载到response的body区域，向手机客户机返回数据信息
//    @ResponseBody
//    public Result<String> topic() {
//        sender.sendTopic("hello, imooc");
//        return Result.success("hello, world");
//    }
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> fanout() {
//        sender.sendFanout("hello, imooc");
//        return Result.success("hello, world");
//    }
//
//    @RequestMapping("/mq/header")
//    @ResponseBody
//    public Result<String> header() {
//        sender.sendHeader("hello, imooc");
//        return Result.success("hello, world");
//    }


    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        // UserKey.getById生成一个UserKey的对象
        User user = redisService.get(UserKey.getById, ""+3, User.class);
        return Result.success(user); // result里面的第三个
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(2);
        user.setName("3333");
        redisService.set(UserKey.getById, ""+3, user);
        return Result.success(true);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    //1.rest api json输出 2.页面
    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
        return Result.success("hello,imooc");
        // return new Result(0, "success", "hello,imooc");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError() {
        return Result.error(CodeMsg.SERVER_ERROR);
        //return new Result(500102, "XXX");
    }

    // Thymeleaf是一款用于渲染XML/XHTML/HTML5内容的模板引擎
    @RequestMapping("/themaleaf")
    public String themaleaf(Model model) {
        model.addAttribute("name", "Joshua");
        return "hello";
    }

}
