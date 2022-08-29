package com.imooc.miaosha.controller;

import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller  // 创建bean实例
// 用于建立请求URL和处理请求方法之间的对应关系 客户端一定会发一个请求过来,那么后台一定要有一个方法去执行它。 把发送请求的这个地址路径跟这个方法建立一个映射关联 你发送这个请求,那我这个方法就执行。
@RequestMapping("/login")
public class LoginController {

    // 日志记录的意思
    // LoggerFactory.getLogger 使用指定类初始化日志对象 在日志输出的时候，可以打印出日志信息所在的类
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    //1.rest api json输出 2.页面
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login"; // 注意login下面的横线
    }

    @RequestMapping("/do_login")
    // @ResponseBody注解的作用是将controller的方法返回的对象 通过适当的转换器 转换为指定的格式之后，
    // 写入到response对象的body区（响应体中），通常用来返回JSON数据或者是XML。
    @ResponseBody
    // HttpServletResponse 封装响应消息
    // Servlet需要对客户端的HTTP请求进行一个HTTP响应。
    // 而 Http的响应正是由本文中的主角HttpServletResponse来完成的
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        // @Valid 注解用于校验参数，表示对象属性需要验证， 即loginVo需要验证，
        // 结合后面的LoginVo类一起看
        log.info(loginVo.toString());

        String token = userService.login(response, loginVo);
        return Result.success(token);
    }



}
