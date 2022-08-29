package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;

	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;



	// 未优化前：20000 吞吐量3363  6.10   这里的优化指什么来着，好像是页面缓存优化
	// 优化后：20000   8748 0.00%   对象缓存有没有作用？提高吞吐量？
	@RequestMapping(value="/to_list", produces="text/html")
    @ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
		// Model好像是存储模型数据
		// model.addAttribute往前台传数据，可以传对象，可以传List，通过el表达式 ${}可以获取到
		// 这一步是传对象？往什么地方传呢？
		model.addAttribute("user", user);

		//取缓存
		// 这一部分是页面缓存？ 看的时候想想
		String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}

		//查询商品列表
		List<GoodsVo> goodsList = goodsService.listGoodsVo(); // 从数据库里面掉数据
		// 这一行如果没有的话，页面商品刷新不出来
		model.addAttribute("goodsList", goodsList);
		// return "goods_list";

		IWebContext ctx =new WebContext(request,response,
				request.getServletContext(),request.getLocale(),model.asMap());

		//手动渲染
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
		if(!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsList, "", html);
		}
		return html;
//       return "goods_list";
	}

	// URL缓存？
	@RequestMapping(value="/to_detail2/{goodsId}",produces="text/html")
    //  @PathVariable 可以将URL中占位符参数{xxx}绑定到处理器类的方法形参中@PathVariable(“xxx“)
	//  简而言之就是：参数goodsId取的值是{goodsId}的值
	@ResponseBody
	public String detail2(HttpServletRequest request, HttpServletResponse response, Model model,MiaoshaUser user,
						 @PathVariable("goodsId")long goodsId) {
		model.addAttribute("user", user);

		//取缓存
		String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}

		// 根据商品ID在数据库挑选商品
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		model.addAttribute("goods", goods);

		// getTime()返回距 1970 年 1 月 1 日之间的毫秒数
		long startAt = goods.getStartDate().getTime();
		long endAt = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();

		int miaoshaStatus = 0;
		int remainSeconds = 0;
		if(now < startAt ) {//秒杀还没开始，倒计时
			miaoshaStatus = 0;
			remainSeconds = (int)((startAt - now )/1000);
		}else if(now > endAt){//秒杀已经结束
			miaoshaStatus = 2;
			remainSeconds = -1;
		}else {//秒杀进行中
			miaoshaStatus = 1;
			remainSeconds = 0;
		}
		//  难道往前台传数据是往html页面上传数据？好像确实是这样？
		model.addAttribute("miaoshaStatus", miaoshaStatus);
		model.addAttribute("remainSeconds", remainSeconds);

		IWebContext ctx =new WebContext(request,response,
				request.getServletContext(),request.getLocale(),model.asMap());
		html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
		if(!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
		}
		return html;
		// return "goods_detail";

	}

	// URL缓存？
	@RequestMapping("/detail/{goodsId}")
	//  @PathVariable 可以将URL中占位符参数{xxx}绑定到处理器类的方法形参中@PathVariable(“xxx“)
	//  简而言之就是：参数goodsId取的值是{goodsId}的值
	@ResponseBody
	public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model,MiaoshaUser user,
										@PathVariable("goodsId")long goodsId) {
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		long startAt = goods.getStartDate().getTime();
		long endAt = goods.getEndDate().getTime();
		long now = System.currentTimeMillis();
		int miaoshaStatus = 0;
		int remainSeconds = 0;
		if(now < startAt ) {//秒杀还没开始，倒计时
			miaoshaStatus = 0;
			remainSeconds = (int)((startAt - now )/1000);
		}else  if(now > endAt){//秒杀已经结束
			miaoshaStatus = 2;
			remainSeconds = -1;
		}else {//秒杀进行中
			miaoshaStatus = 1;
			remainSeconds = 0;
		}
		GoodsDetailVo vo = new GoodsDetailVo();

		System.out.println(vo + "hot-fix");
		System.out.println(vo + "hot-fix 22 ");
		System.out.println(vo + "hot-fix 33 ");
		vo.setGoods(goods);
		vo.setUser(user);
		vo.setRemainSeconds(remainSeconds);
		vo.setMiaoshaStatus(miaoshaStatus);
		return Result.success(vo);
	}
}
