package com.fh.controller.weixin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fh.controller.base.BaseController;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.pay.RequestHandler;
import com.fh.wechat.pay.Sha1Util;
import com.fh.wechat.pay.TenpayUtil;

/**
 * 企业打款Controller
 * @author zzx
 *
 */
@Controller
public class EnterprisePlayMoneyController extends BaseController{
	
	private final static String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	
	private final static String apiclient = "D:/certificate/cert/apiclient_cert.p12";//证书所在的绝对路径
	
	
	public Map<String,Object> palyMoney() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		
		String mch_appid = GongZhongService.appId;
		String mchid = GongZhongService.mch_id;
		
		String nonce_str = Sha1Util.getNonceStr();
		String partner_trade_no = TenpayUtil.getCurrTime()+TenpayUtil.buildRandom(6);//商户订单号
		String openid = "oXeXowfT9IVxiow5ivAK_aDRJAhE";//用户openid
		String check_name = "NO_CHECK";
		String re_user_name = "";
		String amount = "100";//分
		String desc = "打款";
		String spbill_create_ip = PublicUtil.getIp();
		
		SortedMap<String, String> params = new TreeMap<String, String>();
		
		params.put("mch_appid", mch_appid);
		params.put("mchid", mchid);
		params.put("nonce_str", nonce_str);
		params.put("partner_trade_no", partner_trade_no);
		params.put("openid", openid);
		params.put("check_name", check_name);
		params.put("re_user_name", re_user_name);
		params.put("amount", amount);
		params.put("desc", desc);
		params.put("spbill_create_ip", spbill_create_ip);
		
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(GongZhongService.appId, GongZhongService.appSecret, GongZhongService.partner_key);
		logger.info("appid：" + GongZhongService.appId + ",appsecret：" + GongZhongService.appSecret
				+ ",partnerkey：" + GongZhongService.partner_key + "");
		String sign = reqHandler.createSign(params);
		
		
		String xml = "<xml>"+ 
						"<mch_appid>"+mch_appid+"</mch_appid>"+ 
						"<mchid>"+mchid+"</mchid> "+
						"<nonce_str>"+nonce_str+"</nonce_str>"+
						"<partner_trade_no>"+partner_trade_no+"</partner_trade_no>"+ 
						"<openid>"+openid+"</openid> "+
						"<check_name><![CDATA[" + check_name + "]]></check_name>"+
						"<amount>"+amount+"</amount> "+
						"<desc><![CDATA[" + desc + "]]></desc> "+
						"<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
						"<sign>"+sign+"</sign>"+
					  "</xml>";
		logger.info("获取到的预支付IDxml数据：" + xml);

		String resultXML = sendPostToPay(xml);
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(resultXML);
		} catch (DocumentException e) {
			logger.info("获取resultXML异常");
			e.printStackTrace();
		}
		logger.info("获取到的打款resultXML:" + resultXML);
		final Element root = doc.getRootElement();
		String return_code = root.element("return_code").getText();//通信是否成功标示
		String return_msg = root.element("return_msg").getText();//空 成功，不为空签名失败原因
		if(return_code.endsWith("FAIL")){
			logger.info("通信失败");
			map.put("code", 2);
			map.put("msg", "请求微信打款接口通讯失败");
			return map;
		}
		
		if(!Tools.isEmpty(return_msg)){
			logger.info("签名失败,原因是:"+return_msg);
			map.put("code", 3);
			map.put("msg", "签名失败,原因是:"+return_msg);
			return map;
		}
		
		String result_code = root.element("result_code").getText();//打款成功标示
		if(result_code!=null&&result_code.equals("SUCCESS")){
			
		}else{
			logger.info("打款失败..."+root.element("err_code_des").getText());
			map.put("code", 4);
			map.put("msg", "打款失败..."+root.element("err_code_des").getText());
			return map;	
		}
		//return_code 为success 并且 result_code 也为success
		if(result_code.equals("SUCCESS")&&return_code.equals("SUCCESS")){
			System.out.println("payment_time");
			logger.info("微信支付成功时间:"+root.element("payment_time").getText());
		}
		
		String trade_no = root.element("trade_no").getText();//商户订单号
		
		map.put("code", 1);
		map.put("msg", "打款成功...订单号为:"+trade_no);
		return map;	
	}
	
	public static String sendPostToPay(String data) throws Exception, IOException{
		KeyStore keyStore  = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(new File(apiclient));
		 keyStore.load(instream, GongZhongService.mch_id.toCharArray());//证书密码
		 instream.close();
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, GongZhongService.mch_id.toCharArray()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,new String[] { "TLSv1" },null,
		SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom() .setSSLSocketFactory(sslsf) .build();
        HttpPost httpost = new HttpPost(url); // post请求的url
        httpost.addHeader("Connection", "keep-alive");
        httpost.addHeader("Accept", "*/*");
        httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpost.addHeader("Host", "api.mch.weixin.qq.com");
        httpost.addHeader("X-Requested-With", "XMLHttpRequest");
        httpost.addHeader("Cache-Control", "max-age=0");
        httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        httpost.setEntity(new StringEntity(data, "UTF-8"));
        CloseableHttpResponse response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();
        String jsonStr = EntityUtils .toString(response.getEntity(), "UTF-8");
        EntityUtils.consume(entity);
        System.out.println(jsonStr);
        return jsonStr;
	}
}
