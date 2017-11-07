package com.fh.service.system.memberbalance.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fh.entity.Member;
import com.fh.entity.system.CouponsDetail;
import com.fh.service.system.coupons.impl.CouponsService;
import com.fh.service.system.couponsdetail.impl.CouponsDetailService;
import com.fh.service.system.member.impl.MemberService;
import com.fh.service.system.memberbalance.EnterPrisePlayMoneyManager;
import com.fh.util.Convert;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.Logger;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;
import com.fh.util.TwoDimensionCode;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.pay.RequestHandler;
import com.fh.wechat.pay.Sha1Util;
import com.fh.wechat.pay.TenpayUtil;

/**
 * 微信企业打款
 * @author zzx
 * 
 */
@Service("enterPrisePlayMoneyService")
public class EnterPrisePlayMoneyService implements EnterPrisePlayMoneyManager{
	
	protected Logger logger = Logger.getLogger(this.getClass());
	private final static String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	
	private final static String apiclient = "C:/certificate/cert/apiclient_cert.p12";//证书所在的绝对路径
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberBalanceService memberBalanceService;
	
	@Autowired
	private CouponsDetailService couponsdetailService;
	
	@Autowired
	private CouponsService couponsService;
	public Map<String,Object> palyMoney(String openId,double money) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		
		String mch_appid = GongZhongService.appId;
		String mchid = GongZhongService.mch_id;
		
		String nonce_str = Sha1Util.getNonceStr();
		String partner_trade_no = TenpayUtil.getCurrTime()+TenpayUtil.buildRandom(6);//商户订单号
		String openid = openId.trim();//用户openid
		String check_name = "NO_CHECK";
		String re_user_name = "";
		String amount = money*100+"";//转换为分
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
		int mapCode = 1;
		String result_code = root.element("result_code").getText();//打款成功标示
		if(result_code!=null&&result_code.equals("SUCCESS")){
			mapCode = 1;
		}else{
			logger.info("打款失败..."+root.element("err_code_des").getText());
			String err_code = root.element("err_code").getText();
			logger.info("err_code:"+err_code);
			if(!Tools.isEmpty(err_code)&&err_code.equals("NOTENOUGH")){
				mapCode = 4;
			}
			logger.info("打款失败..."+root.element("err_code_des").getText());
			map.put("code", mapCode);
			map.put("msg", "打款失败..."+root.element("err_code_des").getText());
			return map;	
		}
		//return_code 为success 并且 result_code 也为success
		if(result_code.equals("SUCCESS")&&return_code.equals("SUCCESS")){
			System.out.println("payment_time");
			logger.info("微信支付成功时间:"+root.element("payment_time").getText());
		}
		
		String trade_no = root.element("partner_trade_no").getText();//商户订单号
		
		//修改订单的状态
		
		map.put("code", mapCode);
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
	
	@Override
	public void EditAndPlayMoney(PageData memPd, PageData pd,String path) throws Exception {
		// TODO Auto-generated method stub
		int state = pd.getInt("BALANCESTATE");//页面上传过来的是完成状态
		
		int type = memPd.getInt("balanceType");//此条记录的结算类型   27现金  26 卡券
		
		double balanceMoney = memPd.getDouble("balanceMoney");//金额
		String memberId = memPd.getString("memberID");
		if(type==27){
			//现金
			if(memPd!=null&&memPd.getInt("balanceState")!=31&&state==31){
				//之前的状态不是完成状态  并且在数据库中存在这条记录  并且页面上传过来的状态是完成状态  则调用打款接口
				memPd.put("memberId", memPd.getString("memberID"));
				PageData member = memberService.findById(memPd);
				if(member==null){
					logger.info("会员信息不存:"+member);
				}
				else if(Tools.isEmpty(member.getString("OPENID"))){
					logger.info("opentId为空:"+member.getString("OPENID"));
				}else{
					Map<String,Object> map = palyMoney(member.getString("OPENID"),memPd.getDouble("balanceMoney"));
					if(Convert.strToInt(map.get("code")+"", -1)==1){
						pd.put("BALANCESTATE", 31);//完成
						pd.put("payDetail", "打款成功");
					}else{
						pd.put("BALANCESTATE", 30);//未通过
						pd.put("payDetail", "商户余额不足");
					}
				}
			}
			
		}
		
		boolean isHanlded = false;//是否处理过了卡券生成的记录了
		if(memPd!=null&&memPd.getInt("balanceState")==31){
			//非法操作
			logger.info("数据库中的状态已经打款了,而页面上传过来的还是非打款状态   此操作非法 ");
			pd.put("BALANCESTATE", 31);//已经打款后的  非法操作 ,强制变成 打款状态;
			isHanlded = true;//处理过了
		}
		
		if(state!=31){
			//不是打款的状态
			isHanlded = true;
		}
		
		if(!isHanlded&&type!=27){
			//没处理过并且此条记录的类型不是现金 是卡券  就给用户生成一张提现卡券记录
			final  String couponsId = "2decca51e7ca4229bef1fcf3d8c0b54d";//用户优惠券的主键id;
			PageData couponspd = new PageData();
			couponspd.put("COUPONSID", couponsId);
			couponspd = couponsService.findById(couponspd);
			
			PageData couponsdetailPd = new PageData(); 
			String NUMBERS = Tools.createRandomNum12();
			couponsdetailPd.put("COUPONSID", couponsId);
			couponsdetailPd.put("NUMBERS", NUMBERS);
			couponsdetailPd.put("DENOMINATION", balanceMoney);
			
			String imgName=DateUtil.getDays()+"/"+System.currentTimeMillis()+".png";
			String QRCODE="uploadFiles/twoDimensionCode/"+imgName;
			String imgPath = path+"uploadFiles/twoDimensionCode/"+imgName;
			TwoDimensionCode handler = new TwoDimensionCode();
			handler.encoderQRCode(NUMBERS, imgPath, "png");
			
			couponsdetailPd.put("QRCODE", QRCODE);
			couponsdetailPd.put("MEMBERID", memberId);
			couponsdetailPd.put("STATEID", 24);
			couponsdetailPd.put("CREATEDATE", new Date());
			couponsdetailPd.put("ACQUIREDATE", new Date());
			int VALIDITY =0;
			Date ENDDATE = new Date();
			if(!Tools.isEmpty(couponspd.get("VALIDITY")+"")){
				VALIDITY =Convert.strToInt(couponspd.get("VALIDITY")+"", VALIDITY);
				 Calendar   calendar   =   new   GregorianCalendar(); 
			     calendar.setTime(ENDDATE); 
			     calendar.add(calendar.DATE,VALIDITY);//把日期往后增加VALIDITY天.整数往后推,负数往前移动 
			     ENDDATE=calendar.getTime();   //这个时间就是日期往后推VALIDITY天的结果 
			}
			couponsdetailPd.put("ENDDATE",ENDDATE);
			
			couponsdetailPd.put("USERREMARK", "卡券备注");
			couponsdetailPd.put("REMARKS", "");
			couponsdetailService.save(couponsdetailPd);
			
			//修改卡券的主表数量等信息
			
			int COUPONSTOTALNUM = 0;
			if(Tools.notEmpty(couponspd.get("COUPONSTOTALNUM")+"")){
				COUPONSTOTALNUM =Convert.strToInt(couponspd.get("COUPONSTOTALNUM")+"", COUPONSTOTALNUM)+1;
			}
			couponspd.put("COUPONSTOTALNUM", COUPONSTOTALNUM);
			couponspd.put("EDITDATE", new Date());
			couponspd.put("ADMINID", Jurisdiction.getUsername());
			couponsService.editByCouponsTotalNum(couponspd);
		}
		//保存用户信息
		memberBalanceService.edit(pd);
	}
}
