package com.fh.controller.weixin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.service.system.article.ArticleManager;
import com.fh.service.system.articlebase.ArticlebaseManager;
import com.fh.service.system.likerecorde.LikeRecordeManager;
import com.fh.service.system.order.OrderManager;
import com.fh.service.system.orderdetails.OrderDetailsManager;
import com.fh.service.system.product.ProductManager;
import com.fh.service.system.reservation.ReservationManager;
import com.fh.util.Constants;
import com.fh.util.Convert;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.StringUtil;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.gongzhong.TemplateMessage;
import com.fh.wechat.gongzhong.vo.message.template.Template;
import com.fh.wechat.pay.Sha1Util;
import com.fh.wechat.pay.TenpayUtil;



/** 
 * 说明：微信支付
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value="/weChatPay")
public class WeChatPayController extends BaseController {
	
	@Resource(name="orderService")
	private OrderManager orderService;
	@Resource(name="orderdetailsService")
	private OrderDetailsManager orderdetailsService;
	/**
	 * 数据编码
	 */
	private static final String ENCODING = "UTF-8";
	
	/**
	 * 元转换成分
	 * @param money
	 * @return
	 */
	public static String getMoney(String amount) {
		if(amount==null){
			return "";
		}
		// 金额转化为分为单位
		String currency =  amount.replaceAll("[ +a-zA-Z\\u4e00-\\u9fa5]", "");  //处理包含, ￥ 或者$的金额  
        int index = currency.indexOf(".");  
        int length = currency.length();  
        Long amLong = 0l;  
        if(index == -1){  
            amLong = Long.valueOf(currency+"00");  
        }else if(length - index >= 3){  
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));  
        }else if(length - index == 2){  
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);  
        }else{  
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");  
        }  
        return amLong.toString(); 
	}
	


	@ResponseBody
	@RequestMapping(value = "/weChatPayResSample", method = {RequestMethod.GET, RequestMethod.POST }, produces = "application/xml;charset=UTF-8")
	/**
	 * 微信回调
	 * @return
	 * @throws Exception 
	 */
	public String weChatPayResSample(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("微信端支付成功进入回调 - ");
		try {
			final String notify = readNotify();
			logger.info("微信端支付成功进入回调：- " + notify);
			final Document doc = DocumentHelper.parseText(notify);
			final Element root = doc.getRootElement();
			// check - return_code
			final String ret = root.element("return_code").getText();
			logger.info("微信端支付成功进入回调 ret：- " + notify);
			if ("SUCCESS".equals(ret) == false) {
				final String msg = root.element("return_msg").getText();
				logger.info("weixin notify: return - " + msg);
				return null;
			}
			// check - result_code
			final String res = root.element("result_code").getText();
			logger.info("微信端支付成功进入回调 res：- " + res);
			if ("SUCCESS".equals(res) == false) {
				final String msg = root.element("return_msg").getText();
				logger.info("weixin notify: result - " + msg);
				return null;
			}
			// check - sign
			final String sign = root.element("sign").getText();
			logger.info("微信端支付成功进入回调sign: " + sign);
			if (Sha1Util.verifySign(new StringBuilder(), new TreeMap<String, String>(),
					root, sign, GongZhongService.partner_key) == false) {
				logger.info("返回参数异常weixin notify: sign error");
				return null;
			}
			// xid
			final String txno = root.element("out_trade_no").getText();
			final String [] attach= root.element("attach").getText().split(",");
			final String wcPayType=attach[0];
			final String openID = attach[1];
			logger.info("返回参数订单编号wcPayType:(1.打赏 2.预订  3.分享预订) " + wcPayType);
			logger.info("返回参数订单编号txno: " + txno);
			logger.info("返回参数订单编号openID: " + openID);
			//根据系统的订单号  去执行业务逻辑
			if(!Tools.isEmpty(txno) ){
				
				// TODO 逻辑处理
				int result =updateOrder(txno);
				if(result<0){
					return null;
				}
				// TODO 逻辑处理
				//打赏
				if(wcPayType.equals("1")){
					
					
				}else if(wcPayType.equals("2")){
					
					
				}else if(wcPayType.equals("3")){
					
					
				}
				
			}
			final String xid = root.element("transaction_id").getText();
			logger.info("返回参数微信支付订单编号xid: " + xid);
			// 发送成功通知
			final PrintWriter out = response.getWriter();
			out.println("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
			out.flush();
			out.close();
		
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	
	public  int  updateOrder(String BILLNO){
		
		/**修改主单付款状态 
		 * @param pd 主单记录 ORDERSTATUSID，PAYMENTSTATUSID，PAYMENTDATE，BILLNO
		 * @param list 详单记录  STOREID，PLUCODE，INVENTORYCOUNT
		 * @throws Exception
		 */
		int result =0;
		PageData pd = new PageData();
		pd.put("BILLNO", BILLNO);
		List<PageData> list = new ArrayList<PageData>();
		try {
			pd = orderService.findById(pd);
			if(pd==null || StringUtil.isEmpty(pd.get("PAYMENTSTATUSID"))){
				return result;//该订单已支付
			}
			if(pd.get("PAYMENTSTATUSID").equals(22)){
				return result;//该订单已支付
			}
			pd.put("ORDERSTATUSID", 19);
			pd.put("PAYMENTSTATUSID", 22);
			pd.put("PAYMENTDATE", new Date());
			PageData p = new PageData();
			p.put("billNo", BILLNO);
			list = orderdetailsService.listByBillNo(p);
			orderService.edit(pd, list);
		} catch (Exception e) {
			result =-1;
			logger.info("回写订单状态失败订单编号BILLNO: " + BILLNO);
			logger.info("错误如下: " + e.toString());
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	@RequestMapping(value="/resultSuccess")
	public ModelAndView resultSuccess(){
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("weChatPay/dssuccess");
		PageData pd = this.getPageData();
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 获取回调内容信息
	 * 
	 * @return
	 * @throws IOException
	 */
	private String readNotify() throws IOException {
		logger.debug("request content-type:"+ getRequest().getContentType()+", content-length:"+getRequest().getContentLength()+"");
		final InputStream in = getRequest().getInputStream();
		final Reader reader = new InputStreamReader(in, ENCODING);
		try {
			final int MAX = 1 << 12, xmlsz = "</xml>".length(); // read max 4k!
			StringBuilder sbuf = new StringBuilder();
			for (int c = reader.read(), i = 0, k = 0;; c = reader.read(), ++i) {
				if (c == -1 || i >= MAX) {
						logger.info("notify after reading:" + i+ " times:" + sbuf.toString());
					throw new IOException("weixin notify: messy");
				}
				sbuf.append((char) c); // bugfix-0: c cast to char!
				switch (c) {
				case '<':
					++k;
					if (k != 1) { // bugfix-1: test current k!
						k = 0;
					}
					break;
				case '/':
					++k;
					if (k != 2) {
						k = 0;
					}
					break;
				case 'x':
					++k;
					if (k != 3) {
						k = 0;
					}
					break;
				case 'm':
					++k;
					if (k != 4) {
						k = 0;
					}
					break;
				case 'l':
					++k;
					if (k != 5) {
						k = 0;
					}
					break;
				case '>':
					++k;
					if (k != 6) {
						k = 0;
					}
					break;
				default:
					k = 0;
				}
				if (k == xmlsz) {
					break;
				}
			}
			final String notify = sbuf.toString();
			sbuf = null;
			logger.info("返回参数内容notify content: " + notify);
			return notify;
		} finally {
			reader.close();
		}
	}
	
	
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
