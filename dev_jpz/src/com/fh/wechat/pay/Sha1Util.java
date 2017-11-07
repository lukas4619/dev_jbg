package com.fh.wechat.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.fh.util.Logger;
import com.fh.util.Tools;
import com.fh.util.security.Encrypt;
import com.fh.wechat.gongzhong.GongZhongService;

/**
 * 微信网页JS API支付签名
 * @author Lukas 2015年6月25日11:57:57
 *
 */
public class Sha1Util {
	/**
	 * 
	 */
	public static final String APPID =GongZhongService.appId;
	
	/**
	 * 
	 */
	public static final String MCH_ID =GongZhongService.mch_id;
	
	/**
	 * 
	 */
	public static final String APPSECRET = GongZhongService.appSecret;
	
	/**
	 * 
	 */
	public static final String KEY = GongZhongService.partner_key;
	
	
	/**
	 * 请求生成二维码接口地址
	 */
	private static final String UNIFO_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	
	/**
	 * 微信端支付成功回调地址
	 */
	private static final String WECHAT_NOTIFY_URL = "weChatPay/weChatPayResSample.do";
	
	/**
	 * 数据编码
	 */
	private static final String ENCODING = "UTF-8";
	
	public static  Logger logger = Logger.getLogger(Sha1Util.class);
	
	public static String getNonceStr() {
		Random random = new Random();
		return MD5Utils.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
	}
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	
	 
	 /**
	  * 获得js signature
	  * @param signParams该对象继承了treeMap，并且所有的键名称均为小写总共有以下键jsapi_ticket，timestamp，nonce_str，url（treeMap默认是key值asc，但是在下面方法中并没有这个效果，所以将signParams转换成数组在按asc，如果想用signParams排序从重新排序）
	  * @return  false表示生成失败
	  * @throws IOException
	  */
	 public static String getSignature(SortedMap<String, String> signParams) throws IOException {
	     /****
	      * 对 jsapi_ticket、 timestamp 和 nonce 按字典排序 对所有待签名参数按照字段名的 ASCII
	      * 码从小到大排序（字典序）后，使用 URL 键值对的格式（即key1=value1&key2=value2…）拼接成字符串
	      * string1。这里需要注意的是所有参数名均为小写字符。 接下来对 string1 作 sha1 加密，字段名和字段值都采用原始值，不进行
	      * URL 转义。即 signature=sha1(string1)。
	      * **如果没有按照生成的key1=value&key2=value拼接的话会报错
	      */
		 String jsapi_ticket= signParams.get("jsapi_ticket");
		 String timestamp= signParams.get("timestamp");
		 String nonce= signParams.get("nonce_str");
		 String jsurl= signParams.get("url");
	     String[] paramArr = new String[] {"jsapi_ticket=" + jsapi_ticket,"timestamp=" + timestamp, "noncestr=" + nonce, "url=" + jsurl };
	     Arrays.sort(paramArr);
	     // 将排序后的结果拼接成一个字符串
	     String content = paramArr[0].concat("&"+paramArr[1]).concat("&"+paramArr[2]).concat("&"+paramArr[3]);
	     System.out.println("拼接之后的content为:"+content);
	     String gensignature = null;
	     try {
	         MessageDigest md = MessageDigest.getInstance("SHA-1");
	         // 对拼接后的字符串进行 sha1 加密
	         byte[] digest = md.digest(content.toString().getBytes());
	         gensignature = byteToStr(digest);
	     } catch (NoSuchAlgorithmException e) {
	         e.printStackTrace();
	     }
	     // 将 sha1 加密后的字符串与 signature 进行对比
	     if (gensignature != null) {
	         return gensignature.toLowerCase();// 返回signature
	     } else {
	         return "false";
	     }
	 }
	 
	 
	 /**
		 * 获取签名 用于页面wx.config注入
		 * @param timestamp Sha1Util.getTimeStamp()
		 * @param nonce_str Sha1Util.getNonceStr()
		 * @param url 当前网页的URL不包含#及其后面部分(参数需要带上,必须是完整的URL)
		 * @return "" 表示获取签名出现异常
		 * @throws Exception 
		 */
		public static String createSignature(String timestamp,String nonce_str,String url) throws Exception{
			SortedMap<String, String> signParams = new TreeMap<String, String>();
			String jsapi_ticket=GongZhongService.getTicket();
			if(Tools.isEmpty(jsapi_ticket)){
				logger.info("获取jsapi_ticket异常");
				return "";
			}
			if(Tools.isEmpty(nonce_str)){
				logger.info("nonce_str参数不能为空");
				return "";
			}
			signParams.put("nonce_str", nonce_str);
			signParams.put("jsapi_ticket",jsapi_ticket);
			if(Tools.isEmpty(timestamp)){
				logger.info("timestamp参数不能为空");
				return "";
			}
			signParams.put("timestamp", timestamp);
			if(Tools.isEmpty(url)){
				logger.info("url参数不能为空");
				return "";
			}
			signParams.put("url", url);
			String signature="";
			try {
				signature =Sha1Util.getSignature(signParams);
			} catch (IOException e) {
				logger.info("wx.config签名生成异常："+e.getMessage());
				e.printStackTrace();
			}
			if(signature.equals("false")){
				return "";
			}
			return signature;
		}
		
		
		/**
		 * 生成微信支付订单
		 * 
		 * @param body
		 *            描述
		 * @param openId
		 *            微信标识
		 * @param TotalFee
		 *            金额
		 * @param all_order_number
		 *            订单编号
		 * @param wcPayType
		 *            支付类型 1.打赏 2.预订
		 * @param member_id
		 *            会员标识
		 * @param ip
		 *            IP地址
		 * @param basePath 获取项目虚拟地址
		 * @return
		 */
		public static String weChatPay(String body, String openId, String TotalFee,
				String all_order_number, String wcPayType, String member_id,
				String ip,String basePath) {
			// 得到ip
			// 微信支付jsApi
			WxPayDto tpWxPay = new WxPayDto();
			tpWxPay.setOpenId(openId);
			logger.info("setOpenId:" + openId);
			tpWxPay.setBody(body);
			logger.info("setBody:" + body);
			tpWxPay.setOrderId(all_order_number);
			logger.info("all_order_number:" + all_order_number);
			tpWxPay.setSpbillCreateIp(ip);
			logger.info("ip:" + ip);
			tpWxPay.setTotalFee(TotalFee);
			logger.info("TotalFee:" + TotalFee);
			String PayType = wcPayType + "," + member_id;
			tpWxPay.setPayType(PayType);
			logger.info("PayType:" + PayType);
			String packageUrl = getPackage(tpWxPay,basePath);
			logger.info("packageUrl-----------" + packageUrl);
			return packageUrl;
		}
		
		
		/**
		 * 获取请求预支付id报文
		 * 
		 * @return
		 */
		@SuppressWarnings("static-access")
		public static String getPackage(WxPayDto tpWxPayDto,String basePath) {
			// 1 参数
			String openId = tpWxPayDto.getOpenId();
			// 附加数据 原样返回
			String attach = tpWxPayDto.getPayType();
			// 总金额以分为单位，不带小数点
			String totalFee = tpWxPayDto.getTotalFee();
			// 订单生成的机器 IP
			String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
			// ---必须参数
			// 商品描述根据情况修改
			String body = tpWxPayDto.getBody();
			// 商户订单号
			String out_trade_no = tpWxPayDto.getOrderId();
			SortedMap<String, String> params = new TreeMap<String, String>();
			// 公众账号ID
			params.put("appid", APPID);// 微信分配的公众账号ID（企业号corpid即为此appId）
			// 商户号
			params.put("mch_id", MCH_ID);// 微信支付分配的商户号
			// 随机字符串
			String nonce_str = getNonceStr();
			params.put("nonce_str", nonce_str);// 随机字符串，不长于32位。推荐随机数生成算法:https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=4_3
			// 商品描述
			params.put("body", body);
			// 附加数据
			params.put("attach", attach);// 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
			// 商户订单号
			params.put("out_trade_no", out_trade_no);// 商户系统内部的订单号,32个字符内、可包含字母,
														// 其他说明见https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=4_2
			// 总金额 Int类型，已做单位处理
			params.put("total_fee", totalFee);// 订单总金额，单位为分，详见支付金额
			// 终端IP
			params.put("spbill_create_ip", spbill_create_ip);// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
			// 通知地址
			String notify_url = basePath + WECHAT_NOTIFY_URL;// NOTIFYURL
			params.put("notify_url", notify_url);
			String trade_type = "JSAPI";
			params.put("trade_type", trade_type);
			params.put("openid", openId);
			RequestHandler reqHandler = new RequestHandler(null, null);
			reqHandler.init(APPID, APPSECRET, KEY);
			logger.info("appid：" + APPID + ",appsecret：" + APPSECRET
					+ ",partnerkey：" + KEY + "");
			String sign = reqHandler.createSign(params);
			String xml = "<xml>" + "<appid>" + APPID + "</appid>" + "<mch_id>"
					+ MCH_ID + "</mch_id>" + "<nonce_str>" + nonce_str
					+ "</nonce_str>" + "<sign>" + sign + "</sign>"
					+ "<body><![CDATA[" + body + "]]></body>" + "<out_trade_no>"
					+ out_trade_no + "</out_trade_no>" + "<attach>" + attach
					+ "</attach>" + "<total_fee>" + totalFee + "</total_fee>"
					+ "<spbill_create_ip>" + spbill_create_ip
					+ "</spbill_create_ip>" + "<notify_url>" + notify_url
					+ "</notify_url>" + "<trade_type>" + trade_type
					+ "</trade_type>" + "<openid>" + openId + "</openid>"
					+ "</xml>";
			logger.info("获取到的预支付IDxml数据：" + xml);

			String resultXML = httpsRequest(UNIFO_URL, "POST", xml);
			Document doc = null;
			try {
				doc = DocumentHelper.parseText(resultXML);
			} catch (DocumentException e) {
				logger.info("获取resultXML异常");
				e.printStackTrace();
			}
			logger.info("获取到的预支付resultXML:" + resultXML);
			final Element root = doc.getRootElement();
			String prepay_id = root.element("prepay_id").getText();
			if (Tools.isEmpty(prepay_id)) {
				logger.info("获取到的预支付ID为空");
			}
			// 获取prepay_id后，拼接最后请求支付所需要的package
			SortedMap<String, String> finalpackage = new TreeMap<String, String>();
			String timestamp = Sha1Util.getTimeStamp();
			String packages = "prepay_id=" + prepay_id;
			finalpackage.put("appId", APPID);
			finalpackage.put("timeStamp", timestamp);
			finalpackage.put("nonceStr", nonce_str);
			finalpackage.put("package", packages);
			finalpackage.put("signType", "MD5");
			// 要签名
			String finalsign = reqHandler.createSign(finalpackage);
			String finaPackage = "\"appId\":\"" + APPID + "\",\"timeStamp\":\""
					+ timestamp + "\",\"nonceStr\":\"" + nonce_str
					+ "\",\"package\":\"" + packages + "\",\"signType\" : \"MD5"
					+ "\",\"paySign\":\"" + finalsign + "\"";
			JSONObject j = new JSONObject();
			j.put("appId", APPID);
			j.put("timeStamp", timestamp);
			j.put("nonceStr", nonce_str);
			j.put("package", packages);
			j.put("signType", "MD5");
			j.put("paySign", finalsign);
			logger.info("finaPackage :" + finaPackage);
			return j.toString();
		}
		
		
		
		/**
		 * https访问接口
		 * 
		 * @param requestUrl
		 *            地址
		 * @param requestMethod
		 *            GET/POST
		 * @param output
		 *            参数内容
		 * @return
		 */
		public static String httpsRequest(String requestUrl, String requestMethod,
				String output) {
			try {
				URL url = new URL(requestUrl);
				HttpsURLConnection connection = (HttpsURLConnection) url
						.openConnection();
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setUseCaches(false);
				connection.setRequestMethod(requestMethod);
				if (null != output) {
					OutputStream outputStream = connection.getOutputStream();
					outputStream.write(output.getBytes("UTF-8"));
					outputStream.close();
				}
				// 从输入流读取返回内容
				InputStream inputStream = connection.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream, "utf-8");
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String str = null;
				StringBuffer buffer = new StringBuffer();
				while ((str = bufferedReader.readLine()) != null) {
					buffer.append(str);
				}
				bufferedReader.close();
				inputStreamReader.close();
				inputStream.close();
				inputStream = null;
				connection.disconnect();
				return buffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return "";
		}
		
		
		/**
		 * 验证二维码支付请求前面与请求成功后返回前面是否一致
		 * 
		 * @param sbuf
		 * @param params
		 * @param root
		 * @param rsign
		 * @param key
		 * @return
		 */
		public static final boolean verifySign(final StringBuilder sbuf,
				final SortedMap<String, String> params, final Element root,
				final String rsign, final String key) {
			params.clear();
			final List<?> elems = root.elements();
			for (final Iterator<?> i = elems.iterator(); i.hasNext();) {
				final Element elem = (Element) i.next();
				final String name = elem.getName();
				if ("sign".equals(name)) {
					continue;
				}
				params.put(name, elem.getText());
			}
			final String sign = getSign(sbuf, params, key);
			logger.info("服务器签名sign：" + sign);
			logger.info("微信返回签名rsign：" + rsign);
			return (sign.equals(rsign));
		}
		
		/**
		 * 生成请求二维码支付签名
		 * 
		 * @param sbuf
		 * @param params
		 * @param key
		 * @return
		 */
		public static final String getSign(final StringBuilder sbuf,
				final SortedMap<String, String> params, final String key) {
			sbuf.setLength(0);
			int k = 0;
			for (final Iterator<String> i = params.keySet().iterator(); i.hasNext(); ++k) {
				final String name = i.next();
				sbuf.append(k == 0 ? "" : '&').append(name).append('=')
						.append(params.get(name));
			}
			sbuf.append('&').append("key").append('=').append(key);
			return Encrypt.MD5(sbuf.toString(), ENCODING).toUpperCase();
		}
		
		
		/**
		 * 对参数进行MD5
		 * 
		 * @param params
		 *            排好序的参数Map
		 * @param secret
		 *            应用的密钥
		 * @return MD5签名字符串
		 * @throws UnsupportedEncodingException
		 */
		protected static String getSign(final TreeMap<String, String> params)
				throws UnsupportedEncodingException {
			if (null == params || params.isEmpty()) {
				return (String) null;
			}

			StringBuilder sb = new StringBuilder();
			for (Iterator<Entry<String, String>> it = params.entrySet().iterator(); it
					.hasNext();) {
				Entry<String, String> entry = it.next();
				sb.append(Tools.defaultString(entry.getValue()));
			}
			sb.append(APPSECRET);
			byte[] bytes = sb.toString().getBytes("UTF-8");
			return DigestUtils.md5Hex(bytes);
		}
	 
	 /**
	  * 将字节数组转换为十六进制字符串
	  *
	  * @param byteArray
	  * @return
	  */
	 private static String byteToStr(byte[] byteArray) {
	     String strDigest = "";
	     for (int i = 0; i < byteArray.length; i++) {
	         strDigest += byteToHexStr(byteArray[i]);
	     }
	     return strDigest;
	 }
	  
	 /**
	  * 将字节转换为十六进制字符串
	  *
	  * @param mByte
	  * @return
	  */
	 private static String byteToHexStr(byte mByte) {
	     char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
	             'B', 'C', 'D', 'E', 'F' };
	     char[] tempArr = new char[2];
	     tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
	     tempArr[1] = Digit[mByte & 0X0F];
	     String s = new String(tempArr);
	     return s;
	 }
	
}
