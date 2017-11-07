package com.fh.wechat.gongzhong;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fh.service.system.accesstoken.AccessTokenManager;
import com.fh.util.JSONUtils;
import com.fh.util.Logger;
import com.fh.util.PageData;
import com.fh.util.Tools;
import com.fh.util.cache.Cache;
import com.fh.util.cache.CacheManager;
import com.fh.util.xml.Xml;
import com.fh.wechat.gongzhong.utils.GongZhongUtils;
import com.fh.wechat.gongzhong.vo.message.group.MessageMateria;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveClickMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveGroupMessageNotice;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveImageMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveLinkMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveLocationMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveSubscribeMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveTemplateMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveTextMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveVideoMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveVoiceMessage;
import com.fh.wechat.gongzhong.vo.user.UserInfo;

/**
 * @describe 公众号通过方法
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class GongZhongService extends GongZhongObject {
	protected static Logger log = Logger.getLogger(GongZhongService.class);
	static final String APIURL = "https://api.weixin.qq.com/cgi-bin/";
	static final String MEDIAURL = "http://file.api.weixin.qq.com/cgi-bin/media/";
	private static final String UPLOADFILE = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=";
	private static final String DOWNLOADFILE = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=";
	private static final String QRCODE_CREATE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
	private static final String QRCODE_SHOW = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

	public static String mch_id = "1396727702";

	public static String partner_key = "67e33f90966542e9817c6246ce1c9290";
	/**
	 * 自定义当前公众号令牌
	 */
	public static String token = "jbg_bj_jpz_20170822";

	/**
	 * 当前公众号标识
	 */
	public static String appId = "wxba3d6a49692ca7f9";

	/**
	 * 当前公众号秘钥
	 */
	public static String appSecret = "40ead74e3b7feeaab108ff7cdd15bd0b";
	public static final String UPLOAD_TYPE_IMAGE = "image";
	public static final String UPLOAD_TYPE_VOICE = "voice";
	public static final String UPLOAD_TYPE_VIDEO = "video";
	public static final String UPLOAD_TYPE_THUMB = "thumb";

	/**
	 * 获取当前公众号令牌、标识、秘钥
	 */
	static {
		// PropertyFile propertyFile = null;
		// try {
		// propertyFile = new PropertyFile();
		// } catch (Exception e) {
		// System.err.println(e);
		// }
		//
		// if (propertyFile != null) {
		// token = propertyFile.read("weixin.gongzhong.token");
		// appId = propertyFile.read("weixin.gongzhong.appId");
		// appSecret = propertyFile.read("weixin.gongzhong.appSecret");
		// }
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		String basePath = request.getScheme() + "://" + request.getServerName()+ request.getContextPath() + "/";
//		if (basePath.contains("localhost")
//				|| basePath.contains("ddj.java.1yg.tv")) {
//			token = "test.java.ddj";
//			appId = "wx0a78f4799978dfc3";
//			appSecret = "eecc780648b706548e28ecce2177750e";
//		}
		log.info("token:-----------" + token);
		log.info("appId:-----------" + appId);
		log.info("appSecret:-----------" + appSecret);
	}

	/**
	 * 监听微信返回机制
	 * 
	 * @param request
	 * @param response
	 * @param receiveMessageInterface
	 *            消息通道接口
	 * @return
	 * @throws Exception
	 */
	public static String execute(HttpServletRequest request,
			HttpServletResponse response,
			ReceiveMessageInterface receiveMessageInterface) throws Exception {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String method = request.getMethod();
		if ("GET".equals(method)) {
			String reslut = verifyDeveloper(request);
			response.getWriter().print(reslut);

			return null;
		}
		requestMessage(request, receiveMessageInterface);

		return null;
	}

	/**
	 * 验证开发这身份
	 * 
	 * @param request
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	private static String verifyDeveloper(HttpServletRequest request)
			throws NoSuchAlgorithmException, IOException {
		String[] arrayOfString1;
		String signature = request.getParameter("signature");
		String echostr = request.getParameter("echostr");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");

		if (StringUtils.isBlank(timestamp)) {
			System.err.println("timestamp 不能为空");
			return null;
		}
		if (StringUtils.isBlank(nonce)) {
			System.err.println("nonce 不能为空");
			return null;
		}

		String[] str = { token, timestamp, nonce };

		Arrays.sort(str);
		Arrays.sort(str);
		String total = "";
		int j = (arrayOfString1 = str).length;
		for (int i = 0; i < j; ++i) {
			String string = arrayOfString1[i];
			total = total + string;
		}

		if (StringUtils.isBlank(total)) {
			System.err.println("验证开发身份失败");
			return null;
		}

		MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
		sha1.update(total.getBytes());
		byte[] codedBytes = sha1.digest();
		String codedString = new BigInteger(1, codedBytes).toString(16);

		if (codedString.equals(signature)) {
			return echostr;
		}

		return null;
	}

	/**
	 * 获取当前公众号access_token
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getAccessToken() {
		String access_token = "";
		Cache cache = CacheManager.getCacheInfo("WeiXinUtils.getAccessToken");
		if ((cache == null) || (cache.isExpired())) {
			log.info("AccessToken没有cache");
			String result = "";
			try {
				String parameter = "grant_type=client_credential&appid="+ appId + "&secret=" + appSecret;
				result = GongZhongUtils.sendPost("https://api.weixin.qq.com/cgi-bin/token?", parameter);
				cache = new Cache();
				cache.setValue(JSONObject.fromObject(result).get("access_token"));
				CacheManager.putCacheInfo("WeiXinUtils.getAccessToken", cache,6900000L);
				access_token = cache.getValue().toString();
				log.info("保存在缓存中的access_token---" + access_token);
			} catch (Exception e) {
				log.info("获取当前公众号access_token产生异常：" + e.getMessage());
				e.printStackTrace();
			}
			
		}
		access_token = cache.getValue().toString();
//		access_token = cache.getValue().toString();
//		log.info("保存在缓存中的access_token---" + access_token);
//		try {
//			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//			String basePath = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/";
//			String openid ="";
//			if(basePath.contains("localhost") || basePath.contains("ddj.java.1yg.tv")){
//				openid="oJ0qCtzS5uv83EC30B0NgVmlywE0";
//			}else{
//				openid="oXeXowU94y21bfSd5mOtqbPr8hzY";
//			}
//			UserInfo u = new UserInfo();
//			String reslut = GongZhongUtils.sendPost(
//					"https://api.weixin.qq.com/cgi-bin/user/info?",
//					"access_token=" + access_token
//							+ "&openid="+openid);
//			System.out.println("UserInfo:" + reslut);
//			JSONObject obj = JSONObject.fromObject(reslut);
//			if (obj == null) {
//				String parameter = "grant_type=client_credential&appid="
//						+ appId + "&secret=" + appSecret;
//				String result = GongZhongUtils.sendPost(
//						"https://api.weixin.qq.com/cgi-bin/token?", parameter);
//				log.info("appid---" + appId);
//				log.info("secret---" + appSecret);
//				log.info("access_token---"
//						+ JSONObject.fromObject(result).get("access_token"));
//				cache = new Cache();
//				cache.setValue(JSONObject.fromObject(result)
//						.get("access_token"));
//				log.info("cache中的access_token---" + cache.getValue());
//				CacheManager.putCacheInfo("WeiXinUtils.getAccessToken", cache,
//						6900000L);
//				access_token = cache.getValue().toString();
//			} else if (Tools.isEmpty(obj.get("errcode") + "")) {
//				return access_token;
//			} else if (obj.get("errcode").equals("40001")) {
//					String parameter = "grant_type=client_credential&appid="
//							+ appId + "&secret=" + appSecret;
//					String	result = GongZhongUtils.sendPost(
//							"https://api.weixin.qq.com/cgi-bin/token?",
//							parameter);
//					log.info("appid---" + appId);
//					log.info("secret---" + appSecret);
//					log.info("access_token---"
//							+ JSONObject.fromObject(result).get("access_token"));
//				cache = new Cache();
//				cache.setValue(JSONObject.fromObject(result)
//						.get("access_token"));
//				log.info("cache中的access_token---" + cache.getValue());
//				CacheManager.putCacheInfo("WeiXinUtils.getAccessToken", cache,
//						6900000L);
//				access_token = cache.getValue().toString();
//			}
//
//		} catch (Exception e) {
//			log.info("验证access_token是否过期时获取微信用户信息异常：" + e.toString());
//			e.printStackTrace();
//		}

		return access_token;
	}
	
	/**
	 * 获取项目虚拟地址
	 * @return
	 */
	public String getRequertUrl(HttpServletRequest request){
	   String basePath = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/";
	   return basePath;
	}

	/**
	 * 获取当前公众号jsapi_ticke
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getTicket() {
		Cache cache = CacheManager.getCacheInfo("WeiXinUtils.getTicket");
		if ((cache == null) || (cache.isExpired())) {
			String result = "";
			try {
				String parameter = "access_token=" + getAccessToken()
						+ "&type=jsapi";
				result = GongZhongUtils.sendPost(
						"https://api.weixin.qq.com/cgi-bin/ticket/getticket?",
						parameter);
			} catch (Exception e) {
				System.err.println("获取当前公众号jsapi_ticke产生异常：" + e.getMessage());
				e.printStackTrace();
			}
			log.info("getTicket---" + System.currentTimeMillis());
			cache = new Cache();
			cache.setValue(JSONObject.fromObject(result).get("ticket"));
			CacheManager.putCacheInfo("WeiXinUtils.getTicket", cache, 6900000L);
			return cache.getValue().toString();
		}
		return cache.getValue().toString();
	}

	/**
	 * 接受微信返回内容
	 * 
	 * @param request
	 * @param receiveMessageInterface
	 * @throws Exception
	 */
	private static void requestMessage(HttpServletRequest request,
			ReceiveMessageInterface receiveMessageInterface) throws Exception {
		String receiveMsg = GongZhongUtils.readStreamParameter(request
				.getInputStream());
		Map xmlMap = Xml.extractSimpleXMLResultMap(receiveMsg);
		String msgType = (String) xmlMap.get("MsgType");
		// 接收文本消息
		if ("text".equals(msgType)) {
			// 转换文本实体对象
			ReceiveTextMessage message = (ReceiveTextMessage) GongZhongUtils
					.map2Bean(xmlMap, ReceiveTextMessage.class);
			receiveMessageInterface.receiveTextMessage(message);

			return;
		}

		if ("image".equals(msgType)) {
			ReceiveImageMessage message = (ReceiveImageMessage) GongZhongUtils
					.map2Bean(xmlMap, ReceiveImageMessage.class);
			receiveMessageInterface.receiveImageMessage(message);

			return;
		}

		if ("video".equals(msgType)) {
			ReceiveVideoMessage message = (ReceiveVideoMessage) GongZhongUtils
					.map2Bean(xmlMap, ReceiveVideoMessage.class);
			receiveMessageInterface.receiveVideoMessage(message);

			return;
		}

		if ("voice".equals(msgType)) {
			ReceiveVoiceMessage message = (ReceiveVoiceMessage) GongZhongUtils
					.map2Bean(xmlMap, ReceiveVoiceMessage.class);
			receiveMessageInterface.receiveVoiceMessage(message);

			return;
		}

		if ("location".equals(msgType)) {
			ReceiveLocationMessage message = (ReceiveLocationMessage) GongZhongUtils
					.map2Bean(xmlMap, ReceiveLocationMessage.class);
			receiveMessageInterface.receiveLocationMessage(message);

			return;
		}

		if ("link".equals(msgType)) {
			ReceiveLinkMessage message = (ReceiveLinkMessage) GongZhongUtils
					.map2Bean(xmlMap, ReceiveLinkMessage.class);
			receiveMessageInterface.receiveLinkMessage(message);

			return;
		}

		if ("event".equals(msgType)) {
			ReceiveSubscribeMessage message;
			String event = (String) xmlMap.get("Event");

			if ("subscribe".equals(event)) {
				message = (ReceiveSubscribeMessage) GongZhongUtils.map2Bean(
						xmlMap, ReceiveSubscribeMessage.class);
				receiveMessageInterface.eventSubscribeMessage(message);
				return;
			}

			if ("unsubscribe".equals(event)) {
				message = (ReceiveSubscribeMessage) GongZhongUtils.map2Bean(
						xmlMap, ReceiveSubscribeMessage.class);
				receiveMessageInterface.eventUnSubscribeMessage(message);
				return;
			}

			if (("CLICK".equals(event)) || ("VIEW".equals(event))) {
				ReceiveClickMessage receiveClickMessage = (ReceiveClickMessage) GongZhongUtils
						.map2Bean(xmlMap, ReceiveClickMessage.class);
				receiveMessageInterface.eventClickMessage(receiveClickMessage);
				return;
			}

			if ("MASSSENDJOBFINISH".equals(event)) {
				ReceiveGroupMessageNotice receiveGroupMessageNotice = (ReceiveGroupMessageNotice) GongZhongUtils
						.map2Bean(xmlMap, ReceiveGroupMessageNotice.class);
				receiveMessageInterface
						.eventGroupMessageNotice(receiveGroupMessageNotice);
			}

			if ("TEMPLATESENDJOBFINISH".equals(event)) {
				ReceiveTemplateMessage receiveTemplateMessage = (ReceiveTemplateMessage) GongZhongUtils
						.map2Bean(xmlMap, ReceiveTemplateMessage.class);
				receiveMessageInterface
						.receiveTemplateSendJobFinishMessag(receiveTemplateMessage);
			}

			return;
		}
	}

	/**
	 * 获取当前公众号二维码
	 * 
	 * @param directory
	 *            保存本地路径
	 * @param ticket
	 *            获取微信二维码标识
	 * @return
	 * @throws IOException
	 */
	public static String getQrcodeByTicket(String directory, String ticket,String imagePath)
			throws IOException {
		URL url = new URL("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="
				+ ticket);

		return download(url, directory, ticket ,imagePath);
	}
	
	/**
	 * 获取当前公众号二维码
	 * 
	 * @param directory
	 *            保存本地路径
	 * @param ticket
	 *            获取微信二维码标识
	 * @return
	 * @throws IOException
	 */
	public static String getQrcodeByTicket(String directory, String ticket)
			throws IOException {
		URL url = new URL("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="
				+ ticket);

		return download(url, directory, ticket);
	}
	/**
	 * 创建当前公众号二维码
	 * 
	 * @param info
	 *            二维码内容
	 * @param sceneId
	 *            场景标识
	 * @return
	 * @throws Exception
	 */
	public static String createTempQrcode(String info, int sceneId)
			throws Exception {
		String jsonStr = "{\"expire_seconds\": 1800, \"action_name\": \"QR_SCENE\", \""
				+ info + "\": {\"scene\": {\"scene_id\": " + sceneId + "}}}";

		return createQrcode(jsonStr);
	}

	public static String createLimitQrcode(String info, int sceneId)
			throws Exception {
		String jsonStr = "{\"action_name\": \"QR_LIMIT_SCENE\", \"" + info
				+ "\": {\"scene\": {\"scene_id\": " + sceneId + "}}}";

		return createQrcode(jsonStr);
	}

	/**
	 * 生成二维码
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	private static String createQrcode(String jsonStr) throws Exception {
		String result = GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
						+ getAccessToken(), jsonStr);
		log.info("生成二维码返回数据：" + result);
		return JSONUtils.getString(JSONObject.fromObject(result), "ticket");
	}

	/**
	 * 下载多文件 支持JPEG/JPG/MP4/MP3/AMR
	 * 
	 * @param url
	 *            多媒体地址
	 * @param directory
	 *            保存本地路径
	 * @param fileName
	 *            多媒体保存本地名称
	 * @return
	 * @throws IOException
	 */
	private static String download(URL url, String directory, String fileName) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			String contentType = connection.getContentType();
			if ("video/mpeg4".equals(contentType)) {
				fileName = fileName + ".mp4";
			} else if (("image/jpeg".equals(contentType))
					|| ("image/jpg".equals(contentType))) {
				fileName = fileName + ".jpg";
			} else if ("audio/mp3".equals(contentType)) {
				fileName = fileName + ".mp3";
			} else if ("audio/amr".equals(contentType)) {
				fileName = fileName + ".amr";
			} else {
				log.info("暂不支持 【" + contentType + "】 请联系升级接口");
				return "";
			}

			DataInputStream in = new DataInputStream(
					connection.getInputStream());
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					directory + fileName));

			byte[] buffer = new byte[4096];
			int count = 0;

			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}

			out.flush();
			out.close();
			in.close();
		} catch (FileNotFoundException e) {
			log.info("下载多文件 支持JPEG/JPG/MP4/MP3/AMR出现异常：" + e.toString());
			directory = "";
			fileName = "";
			e.printStackTrace();
		} catch (IOException e) {
			log.info("下载多文件 支持JPEG/JPG/MP4/MP3/AMR出现异常：" + e.toString());
			e.printStackTrace();
			directory = "";
			fileName = "";
		}

		return directory + fileName;
	}
	
	/**
	 * 下载多文件 支持JPEG/JPG/MP4/MP3/AMR
	 * 
	 * @param url
	 *            多媒体地址
	 * @param directory
	 *            保存本地路径
	 * @param fileName
	 *            多媒体保存本地名称
	 * @return
	 * @throws IOException
	 */
	private static String download(URL url, String directory, String fileName, String imagePath) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			String contentType = connection.getContentType();
			if ("video/mpeg4".equals(contentType)) {
				imagePath = imagePath + ".mp4";
			} else if (("image/jpeg".equals(contentType))
					|| ("image/jpg".equals(contentType))) {
				imagePath = imagePath + ".jpg";
			} else if ("audio/mp3".equals(contentType)) {
				imagePath = imagePath + ".mp3";
			} else if ("audio/amr".equals(contentType)) {
				imagePath = imagePath + ".amr";
			} else {
				log.info("暂不支持 【" + contentType + "】 请联系升级接口");
				return "";
			}

			DataInputStream in = new DataInputStream(
					connection.getInputStream());
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					directory + imagePath));

			byte[] buffer = new byte[4096];
			int count = 0;

			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}

			out.flush();
			out.close();
			in.close();
		} catch (FileNotFoundException e) {
			log.info("下载多文件 支持JPEG/JPG/MP4/MP3/AMR出现异常：" + e.toString());
			directory = "";
			imagePath = "";
			e.printStackTrace();
		} catch (IOException e) {
			log.info("下载多文件 支持JPEG/JPG/MP4/MP3/AMR出现异常：" + e.toString());
			e.printStackTrace();
			directory = "";
			imagePath = "";
		}

		return directory + imagePath;
	}
	/**
	 * 下载素材
	 * 
	 * @param mediaId
	 *            素材标识
	 * @param directory
	 *            保存本地路径
	 * @return
	 * @throws Exception
	 */
	public static String downloadMedia(String mediaId, String directory)
			throws Exception {
		URL url = new URL(
				"http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
						+ getAccessToken() + "&media_id=" + mediaId);

		return download(url, directory, mediaId);
	}

	/**
	 * 上传素材
	 * 
	 * @param type
	 *            素材类型 thumb、image、video、voice
	 * @param filePath
	 *            素材保存本地路径
	 * @return
	 * @throws Exception
	 */
	public static String uploadMedia(String type, String filePath)
			throws Exception {
		String _type = filePath.substring(filePath.lastIndexOf(".") + 1)
				.toUpperCase();

		if ((StringUtils.isBlank(filePath))
				|| ((!("JPG".equals(_type))) && (!("AMR".equals(_type)))
						&& (!("MP3".equals(_type))) && (!("MP4".equals(_type))))) {
			throw new IOException("文件类型错误");
		}

		if ((StringUtils.isBlank(type))
				|| ((!(type.equals("thumb"))) && (!(type.equals("image")))
						&& (!(type.equals("video"))) && (!(type.equals("voice"))))) {
			throw new IOException("type类型错误");
		}

		File file = new File(filePath);
		if ((!(file.exists())) || (!(file.isFile()))) {
			throw new IOException("文件不存在");
		}

		URL urlObj = new URL(
				"http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token="
						+ getAccessToken() + "&type=" + type);

		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);

		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		String boundary = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
				+ boundary);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(boundary);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filelength=\""
				+ file.length() + "\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		OutputStream out = new DataOutputStream(con.getOutputStream());

		out.write(head);

		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1)
			out.write(bufferOut, 0, bytes);

		in.close();

		// byte[] foot = boundary.getBytes("utf-8");
		byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
		out.write(foot);
		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;

		reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line = null;

		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String result = null;
		if (result == null) {
			result = buffer.toString();
		}

		if (reader != null) {
			reader.close();
		}

		return result;
	}

	/**
	 * 上传永久素材
	 * 
	 * @param type
	 *            素材类型 thumb、image、video、voice
	 * @param filePath
	 *            素材保存本地路径
	 * @return JSONObject type 1.访问成功 -1.访问异常、参数有误
	 */
	public static JSONObject uploadMaterial(String type, String filePath) {
		JSONObject j = new JSONObject();
		String _type = filePath.substring(filePath.lastIndexOf(".") + 1)
				.toUpperCase();

		if ((StringUtils.isBlank(filePath))
				|| ((!("JPG".equals(_type))) && (!("AMR".equals(_type)))
						&& (!("MP3".equals(_type))) && (!("MP4".equals(_type))))) {
			j.put("type", -1);
			j.put("msg", "文件类型错误");
			return j;
		}

		if ((StringUtils.isBlank(type))
				|| ((!(type.equals("thumb"))) && (!(type.equals("image")))
						&& (!(type.equals("video"))) && (!(type.equals("voice"))))) {
			j.put("type", -1);
			j.put("msg", "type件类型错误");
			return j;
		}

		File file = new File(filePath);
		if ((!(file.exists())) || (!(file.isFile()))) {
			j.put("type", -1);
			j.put("msg", "文件不存在");
			return j;
		}

		String url = "";
		String result = "";
		try {
			url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token="
					+ getAccessToken();
			URL urlObj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);

			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");

			String boundary = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);

			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition:form-data;name=\"media\";filelength=\""
					+ file.length()
					+ "\";filename=\""
					+ file.getName()
					+ "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");

			byte[] head = sb.toString().getBytes("utf-8");

			OutputStream out = new DataOutputStream(con.getOutputStream());

			out.write(head);

			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1)
				out.write(bufferOut, 0, bytes);

			in.close();

			byte[] foot = boundary.getBytes("utf-8");

			out.write(foot);
			out.flush();
			out.close();

			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = null;

			reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line = null;

			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			result = null;
			if (result == null) {
				result = buffer.toString();
			}

			if (reader != null) {
				reader.close();
			}
			j = JSONObject.fromObject(result);
			long r = GongZhongUtils.checkErorr(j);
			if (r == -1L) {
				j.put("type", -1);
				j.put("msg", "接口访问异常");
				return j;
			}
		} catch (MalformedURLException e) {
			log.info(e.getMessage());
			e.printStackTrace();
			j.put("type", -1);
			j.put("msg", e.getMessage());
			return j;
		} catch (ProtocolException e) {
			log.info(e.getMessage());
			e.printStackTrace();
			j.put("type", -1);
			j.put("msg", e.getMessage());
			return j;
		} catch (UnsupportedEncodingException e) {
			log.info(e.getMessage());
			e.printStackTrace();
			j.put("type", -1);
			j.put("msg", e.getMessage());
			return j;
		} catch (FileNotFoundException e) {
			log.info(e.getMessage());
			e.printStackTrace();
			j.put("type", -1);
			j.put("msg", e.getMessage());
			return j;
		} catch (IOException e) {
			log.info(e.getMessage());
			e.printStackTrace();
			j.put("type", -1);
			j.put("msg", e.getMessage());
			return j;
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			j.put("type", -1);
			j.put("msg", e.getMessage());
			return j;
		}
		j.put("type", 1);
		j.put("msg", "成功");
		return j;
	}

	/**
	 * 新增永久图文素材
	 * 
	 * @param articles
	 *            图文实体集合
	 * @return JSONObject type 1.访问成功 -1.访问异常、参数有误
	 * @throws Exception
	 */
	public static JSONObject uploadAddArticlet(List<MessageMateria> articles)
			throws Exception {
		JSONObject j = new JSONObject();
		if ((articles == null) || (articles.size() == 0)
				|| (articles.size() > 10)) {
			j.put("type", -1);
			j.put("msg", "Articles为空或超过限制");
			return j;
		}
		String url = "";
		String result = "";
		StringBuffer it = new StringBuffer();
		it.append("{\"articles\": [");
		try {
			for (int i = 0; i < articles.size(); i++) {
				MessageMateria m = new MessageMateria();
				m = articles.get(i);
				it.append("{");
				it.append("title:" + m.getTitle() + ",");
				it.append("thumb_media_id:" + m.getThumb_media_id() + ",");
				it.append("author:" + m.getAuthor() + ",");
				it.append("digest:" + m.getDigest() + ",");
				it.append("show_cover_pic:1,");
				it.append("content:" + m.getContent() + ",");
				it.append("content_source_url:" + m.getContent_source_url());
				if (articles.size() == 0 || articles.size() == i) {
					it.append("}");
				} else if (articles.size() > 0 && i < articles.size()) {
					it.append("},");
				}
			}
			it.append("]}");
			url = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token="
					+ GongZhongService.getAccessToken();
			result = GongZhongUtils.sendPost(url, it.toString());
			if (StringUtils.isBlank(result)) {
				j.put("type", -1);
				j.put("msg", "接口未返回数据");
				return j;
			}
			j = JSONObject.fromObject(result);
			long r = GongZhongUtils.checkErorr(j);
			if (r == -1L) {
				j.put("type", -1);
				j.put("msg", "接口访问异常");
				return j;
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			j.put("type", -1);
			j.put("msg", e.getMessage());
			return j;
		}
		j.put("type", 1);
		j.put("msg", "成功");
		return j;
	}

	/**
	 * 删除永久图文素材
	 * 
	 * @param media_id
	 *            图文素材标识
	 * @return JSONObject type 1.访问成功 -1.访问异常、参数有误
	 * @throws Exception
	 */
	public static JSONObject uploadDeleteArticlet(String media_id)
			throws Exception {
		JSONObject j = new JSONObject();
		String url = "";
		String result = "";
		StringBuffer it = new StringBuffer();
		it.append("{\"media_id\":" + media_id + "}");
		try {
			url = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token="
					+ GongZhongService.getAccessToken();
			result = GongZhongUtils.sendPost(url, it.toString());
			if (StringUtils.isBlank(result)) {
				j.put("type", -1);
				j.put("msg", "接口未返回数据");
				return j;
			}
			j = JSONObject.fromObject(result);
			long r = GongZhongUtils.checkErorr(j);
			if (r == -1L) {
				j.put("type", -1);
				j.put("msg", "接口访问异常");
				return j;
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			j.put("type", -1);
			j.put("msg", e.getMessage());
			return j;
		}
		j.put("type", 1);
		j.put("msg", "成功");
		return j;
	}

	/**
	 * 更新永久图文素材
	 * 
	 * @param m
	 *            图文实体
	 * @param media_id
	 *            图文标识
	 * @return JSONObject type 1.访问成功 -1.访问异常、参数有误
	 * @throws Exception
	 */
	public static JSONObject uploadEditArticlet(MessageMateria m,
			String media_id) throws Exception {
		JSONObject j = new JSONObject();
		String url = "";
		String result = "";
		StringBuffer it = new StringBuffer();
		it.append("{\"media_id\":" + media_id + ",");
		it.append("\"index\": 0,");
		try {
			it.append("\"articles\":{");
			it.append("title:" + m.getTitle() + ",");
			it.append("thumb_media_id:" + m.getThumb_media_id() + ",");
			it.append("author:" + m.getAuthor() + ",");
			it.append("digest:" + m.getDigest() + ",");
			it.append("show_cover_pic:1,");
			it.append("content:" + m.getContent() + ",");
			it.append("content_source_url:" + m.getContent_source_url());
			it.append("}}");
			url = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token="
					+ GongZhongService.getAccessToken();
			result = GongZhongUtils.sendPost(url, it.toString());
			if (StringUtils.isBlank(result)) {
				j.put("type", -1);
				j.put("msg", "接口未返回数据");
				return j;
			}
			j = JSONObject.fromObject(result);
			long r = GongZhongUtils.checkErorr(j);
			if (r == -1L) {
				j.put("type", -1);
				j.put("msg", "接口访问异常");
				return j;
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
			j.put("type", -1);
			j.put("msg", e.getMessage());
			return j;
		}
		j.put("type", 1);
		j.put("msg", "成功");
		return j;
	}

	public static void main(String[] args) throws Exception {
		getTicket();
	}
}