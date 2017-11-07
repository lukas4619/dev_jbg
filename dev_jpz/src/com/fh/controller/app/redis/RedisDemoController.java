package com.fh.controller.app.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.controller.base.BaseController;
import com.fh.dao.redis.RedisDao;
import com.fh.util.AppUtil;
import com.fh.util.PageData;
import com.fh.util.cache.Cache;
import com.fh.util.cache.CacheManager;

/** RedisDemo
 * @author lukas 414024003@qq.com
 * 暂未使用
 * 2016.5.8
 */
@Controller
@RequestMapping(value="/appRedisDemo")
public class RedisDemoController extends BaseController{
	
	@Resource(name = "redisDaoImpl")
	private RedisDao redisDaoImpl;
	
	/**
	 * 请讲接口 http://127.0.0.1:8080/项目名称/appRedisDemo/redisDemo.do
	 * demo展示的在redis存储读取数据的方式，本系统暂时用不到redis，此redis接口可根据实际业务需求选择使用
	 * 具体redis的应用场景->百度下即可
	 */
	@RequestMapping(value="/redisDemo")
	@ResponseBody
	public Object redis(){
		
		Map<String,Object> map = new HashMap<String,Object>();
//		Cache cache = CacheManager.getCacheInfo("test1");
//		if ((cache == null) || (cache.isExpired())) {
//			System.err.println("没有缓存");
//			cache = new Cache();
//			cache.setValue("j_slU218fP66HjiF84MJYEkIVkMo-hv8Ext9oBijMsfK3F5SJ3UMnDrHjYQnCAByTJmR73gW0ISZn6BLPvHI0WBqUY316GYyDYUrs337FVA");
//			CacheManager.putCacheInfo("test1", cache,5L);
//			System.err.println("缓存中的AccessToken为：---" + cache.getValue().toString());
//		}
//		System.err.println("AccessToken为：---" + cache.getValue().toString());
		String result = "";
		
		redisDaoImpl.delete("fh0");											//删除
		redisDaoImpl.delete("fh");											//删除
		redisDaoImpl.delete("fh1");											//删除
		redisDaoImpl.delete("fh2");											//删除
		
		System.out.println(redisDaoImpl.addString("fh0","opopopo"));		//存储字符串
		System.out.println("获取字符串:"+redisDaoImpl.get("fh0"));			//获取字符串
		
		result += "获取字符串:"+redisDaoImpl.get("fh0")+",";
		
		Map<String, String> jmap = new HashMap<String, String>();
    	jmap.put("name", "fhadmin");
    	jmap.put("age", "22");
    	jmap.put("qq", "414024003");
		System.out.println(redisDaoImpl.addMap("fh", jmap));				//存储Map
		System.out.println("获取Map:"+redisDaoImpl.getMap("fh"));			//获取Map
		
		result += "获取Map:"+redisDaoImpl.getMap("fh")+",";
		
		List<String> list = new ArrayList<String>();
		list.add("ssss");
		list.add("bbbb");
		list.add("cccc");
		redisDaoImpl.addList("fh1", list);									//存储List
		System.out.println("获取List:"+redisDaoImpl.getList("fh1"));			//获取List		
		
		result += "获取List:"+redisDaoImpl.getList("fh1")+",";
		
		Set<String> set = new HashSet<String>();
		set.add("wwww");
		set.add("eeee");
		set.add("rrrr");
		redisDaoImpl.addSet("fh2", set);									//存储Set
		System.out.println("获取Set:"+redisDaoImpl.getSet("fh2"));			//获取Set
		
		result += "获取Set:"+redisDaoImpl.getSet("fh2")+",";
		
		map.put("result", result);
		
		return AppUtil.returnObject(new PageData(), map);
	}

}
