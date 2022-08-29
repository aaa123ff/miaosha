package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.MiaoshaUserDao;
import com.imooc.miaosha.domain.MiaoshaUser;

import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

	public static final String COOKI_NAME_TOKEN = "token";

	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	
	@Autowired
	RedisService redisService;
	// 在这个地方获取mysql中的用户数据
	// 对象缓存，应该是对象缓存
	public MiaoshaUser getById(long id) {
		//取缓存
		MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, ""+id, MiaoshaUser.class);
		if(user != null) {
			return user;
		}
		//取数据库
		user = miaoshaUserDao.getById(id);
		if(user != null) {
			redisService.set(MiaoshaUserKey.getById, ""+id, user);
		}
		return user;
	}

	// http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
	public boolean updatePassword(String token, long id, String formPass) {
		//取user
		MiaoshaUser user = getById(id);
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//更新数据库
		MiaoshaUser toBeUpdate = new MiaoshaUser();
		toBeUpdate.setId(id);
		toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
		miaoshaUserDao.update(toBeUpdate);
		//处理缓存
		redisService.delete(MiaoshaUserKey.getById, ""+id);
		user.setPassword(toBeUpdate.getPassword());
		redisService.set(MiaoshaUserKey.token, token, user);
		return true;
	}

	public MiaoshaUser getByToken(HttpServletResponse response, String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
		//延长有效期
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;

	}

	public String login(HttpServletResponse response, LoginVo loginVo) {
		if (loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		//  这里是登录密码
		String formPass = loginVo.getPassword();
		//  判断手机号是否存在

		//  从mysql里面调数据，看手机号码是不是在里面
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}

        // 验证密码   这里是数据库里面的密码
		String dbPass = user.getPassword();
        String slatDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, slatDB);
        if (!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
         // token 是javaJDK 自动生成的主键
		String token = UUIDUtil.uuid();
		addCookie(response, token, user);
        return token;
	}

	// 在redis里面生成对应的记录
	private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {

		// 这里是存入 redis 中
		// MiaoshaUserKey.token 是 MiaoshaUserKey, 可以理解为前缀对象
		redisService.set(MiaoshaUserKey.token, token, user);
		// Cookie指某些网站为了辨别用户身份、进行 session 跟踪而储存在用户本地终端上的数据（通常经过加密）
		// Cookie的构造方法，
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		// 有效期
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		//  正常的cookie只能在一个应用中共享，即一个cookie只能由创建它的应用获得。
		//  可在同一应用服务器内共享方法：设置cookie.setPath("/");
		//  目前理解就是所有应用都能用？
		cookie.setPath("/");
		// HttpServletResponse类中的addCookie方法
		response.addCookie(cookie);
	}
}
