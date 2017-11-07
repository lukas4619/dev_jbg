package com.fh.wechat.gongzhong.utils;

import net.sf.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import javax.servlet.ServletInputStream;

import com.fh.util.Convert;
import com.fh.util.Logger;
import com.fh.util.Utility;
import com.fh.wechat.gongzhong.GongZhongObject;
import com.fh.wechat.gongzhong.WebOAuthManage;

/**
 * @describe 微信公共类
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class GongZhongUtils extends GongZhongObject {

	  protected static Logger log = Logger.getLogger(GongZhongUtils.class);
	/**
	 * 校验接口访问返回的错误代码
	 * 
	 * @param jsonStr
	 *            JSON类型字符窜
	 * @return 
	 */
	public static String checkErorr(String jsonStr) {
		Object errcode = JSONObject.fromObject(jsonStr).get("errcode");

		if (errcode != null) {
			String errmsg = JSONObject.fromObject(jsonStr).get("errmsg")
					.toString();

			if (!"0".equals(errcode.toString().trim())) {
				System.err.println("errcode：" + errcode.toString()
						+ "   errmsg：" + errmsg);
				log.info("校验接口访问返回的错误代码--errcode:"+ errcode.toString()+ "----errmsg：" + errmsg);
				throw new RuntimeException(getErrorDetail(errcode.toString()));
			}
		}

		System.out.println(jsonStr);
		return jsonStr;
	}

	/**
	 * 校验接口访问返回的错误代码
	 * 
	 * @param json JSON
	 *            
	 * @return 1成功 -1.接口访问异常
	 */
	public static long checkErorr(JSONObject json) {
		long result = 1L;
		Object errcode = json.get("errcode");
		if (errcode == null) {
			return result;
		}
		String errmsg = json.get("errmsg").toString();
		if (!"0".equals(errcode.toString().trim())) {
			System.err.println("errcode：" + errcode.toString() + "   errmsg："+ errmsg);
			System.err.println(getErrorDetail(errcode.toString()));
			return -1L;
		}
		return result;
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            地址
	 * @param parameter
	 *            参数
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(String url, String parameter)
			throws Exception {
		log.info("url----------------------:"+url);
		log.info("parameter----------------------:"+parameter);
		String result = Utility.post(url, parameter);
		log.info("result----------------------:"+result);
		checkErorr(result);
		return result;
	}

	public static String readStreamParameter(ServletInputStream in) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = null;

			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		}

		return buffer.toString();
	}

	public static Object map2Bean(Map<String, Object> map, Class<?> cls) {
		Object obj = null;
		try {
			obj = cls.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Method[] methods = cls.getMethods();
		for (int i = 0; i < methods.length; i++) {
			String method = methods[i].getName();

			Class[] cc = methods[i].getParameterTypes();
			if (cc.length != 1) {
				continue;
			}
			if (method.indexOf("set") < 0)
				continue;
			String type = cc[0].getSimpleName();
			try {
				Object value = method.substring(3, 4) + method.substring(4);
				if ((map.containsKey(value)) && (map.get(value) != null)) {
					setValue(type, map.get(value), i, methods, obj);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return obj;
	}

	private static void setValue(String type, Object value, int i,
			Method[] method, Object bean) {
		if ((value != null) && (!value.equals("")))
			try {
				if (type.equals("String")) {
					method[i].invoke(bean, new Object[] { value });
				} else if ((type.equals("int")) || (type.equals("Integer"))) {
					method[i].invoke(
							bean,
							new Object[] { new Integer(Convert.strToInt(
									value.toString(), 0)) });
				} else if ((type.equals("long")) || (type.equals("Long"))) {
					method[i].invoke(
							bean,
							new Object[] { new Long(Convert.strToLong(
									value.toString(), 0L)) });
				} else if ((type.equals("boolean")) || (type.equals("Boolean"))) {
					method[i].invoke(bean, new Object[] { Boolean
							.valueOf(Convert.strToBoolean(value.toString(),
									false)) });
				} else if (type.equals("BigDecimal")) {
					method[i].invoke(bean, new Object[] { new BigDecimal(
							(char[]) value) });
				} else if (type.equals("Date")) {
					Date date = null;
					if (value.getClass().getName().equals("java.util.Date"))
						date = (Date) value;
					else {
						date = Convert.strToDate(value.toString(), new Date());
					}
					if (date != null) {
						method[i].invoke(bean, new Object[] { date });
					}
				} else if (type.equals("byte[]")) {
					method[i].invoke(bean, new Object[] { new String(
							(String) value).getBytes() });
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	}

	public static String getErrorDetail(String _errcode) {
		int errcode = 0;
		try {
			errcode = Integer.parseInt(_errcode);
		} catch (Exception e) {
			throw new RuntimeException("返回码错误");
		}

		switch (errcode) {
		case -1:
			return "系统繁忙。";
		case 0:
			return "请求成功。";
		case 40001:
			return "获取access_token时AppSecret错误，或者access_token无效。";
		case 40002:
			return "不合法的凭证类型。";
		case 40003:
			return "不合法的OpenID。";
		case 40004:
			return "不合法的媒体文件类型。";
		case 40005:
			return "不合法的文件类型。";
		case 40006:
			return "不合法的文件大小。";
		case 40007:
			return "不合法的媒体文件id。";
		case 40008:
			return "不合法的消息类型。";
		case 40009:
			return "不合法的图片文件大小。";
		case 40010:
			return "不合法的语音文件大小。";
		case 40011:
			return "不合法的视频文件大小。";
		case 40012:
			return "不合法的缩略图文件大小。";
		case 40013:
			return "不合法的APPID。";
		case 40014:
			return "不合法的access_token。";
		case 40015:
			return "不合法的菜单类型。";
		case 40016:
			return "不合法的按钮个数。";
		case 40017:
			return "不合法的按钮个数。";
		case 40018:
			return "不合法的按钮名字长度。";
		case 40019:
			return " 不合法的按钮KEY长度。";
		case 40020:
			return " 不合法的按钮URL长度。";
		case 40021:
			return " 不合法的菜单版本号。";
		case 40022:
			return " 不合法的子菜单级数。";
		case 40023:
			return " 不合法的子菜单按钮个数。";
		case 40024:
			return " 不合法的子菜单按钮类型。";
		case 40025:
			return " 不合法的子菜单按钮名字长度。";
		case 40026:
			return " 不合法的子菜单按钮KEY长度。";
		case 40027:
			return " 不合法的子菜单按钮URL长度。";
		case 40028:
			return "不合法的自定义菜单使用用户。";
		case 40029:
			return " 不合法的oauth_code。";
		case 40030:
			return " 不合法的refresh_token。";
		case 40031:
			return " 不合法的openid列表。";
		case 40032:
			return "不合法的openid列表长度。";
		case 40033:
			return "不合法的请求字符，不能包含\\uxxxx格式的字符。";
		case 40035:
			return "不合法的参数。";
		case 40038:
			return "不合法的请求格式。";
		case 40039:
			return "不合法的URL长度。";
		case 40050:
			return "不合法的分组id。";
		case 40051:
			return "分组名字不合法。";
		case 41001:
			return "缺少access_token参数。";
		case 41002:
			return "缺少appid参数。";
		case 41003:
			return "缺少refresh_token参数。";
		case 41004:
			return "缺少secret参数。";
		case 41005:
			return "缺少多媒体文件数据。";
		case 41006:
			return "缺少media_id参数。";
		case 41007:
			return "缺少子菜单数据。";
		case 41008:
			return "缺少oauth code。";
		case 41009:
			return "缺少openid。";
		case 42001:
			return "access_token超时。";
		case 42002:
			return "refresh_token超时。";
		case 42003:
			return "oauth_code超时。";
		case 43001:
			return "需要GET请求。";
		case 43002:
			return "需要POST请求。";
		case 43003:
			return "需要HTTPS请求。";
		case 43004:
			return " 需要接收者关注。";
		case 43005:
			return " 需要好友关系。";
		case 44001:
			return " 多媒体文件为空。";
		case 44002:
			return " POST的数据包为空。";
		case 44003:
			return " 图文消息内容为空。";
		case 44004:
			return " 文本消息内容为空。";
		case 45001:
			return "多媒体文件大小超过限制。";
		case 45002:
			return "消息内容超过限制。";
		case 45003:
			return " 标题字段超过限制。";
		case 45004:
			return " 描述字段超过限制。";
		case 45005:
			return " 链接字段超过限制。";
		case 45006:
			return "图片链接字段超过限制。";
		case 45007:
			return "语音播放时间超过限制。";
		case 45008:
			return "图文消息超过限制。";
		case 45009:
			return "接口调用超过限制。";
		case 45010:
			return "创建菜单个数超过限制。";
		case 45015:
			return "回复时间超过限制。";
		case 45016:
			return "系统分组，不允许修改。";
		case 45017:
			return "分组名字过长。";
		case 45018:
			return "分组数量超过上限。";
		case 46001:
			return "不存在媒体数据。";
		case 46002:
			return "不存在的菜单版本。";
		case 46003:
			return "不存在的菜单数据。";
		case 46004:
			return "不存在的用户。";
		case 47001:
			return "解析JSON/XML内容错误。";
		case 48001:
			return "api功能未授权。";
		case 50001:
			return "用户未授权该api。";
		}
		return "未知错误。";
	}
}
