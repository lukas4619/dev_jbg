package com.fh.controller.activity.lottery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.dao.redis.RedisDao;
import com.fh.entity.Page;
import com.fh.service.system.coupons.CouponsManager;
import com.fh.service.system.lottery.LotteryManager;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.memberbalance.MemberBalanceManager;
import com.fh.service.system.memberlog.MemberLogManager;
import com.fh.service.system.notelog.NoteLogManager;
import com.fh.service.system.types.TypesManager;
import com.fh.sms.TaoBaoDaYu;
import com.fh.util.AppUtil;
import com.fh.util.Convert;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;
import com.fh.util.TwoDimensionCode;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.pay.Sha1Util;

/**
 * 说明：抽奖 创建人：Lukas 18923798379 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value = "/frontLottery")
public class frontLotteryController extends BaseController {

	@Resource(name = "memberService")
	private MemberManager memberService;
	@Resource(name="lotteryService")
	private LotteryManager lotteryService;
	@Resource(name="couponsService")
	private CouponsManager couponsService;
	@Resource(name = "redisDaoImpl")
	private RedisDao redisDaoImpl;
	//id,min(角度),max(角度),prize【奖项】,v【中奖率】
	private static final Object[][] prizeArr = new  Object[][]{
			{1,1,36,"获得价值20元优惠券!",20},
			{2,37,72,"获得9折优惠!",18},
			{3,73,108,"获得8折优惠!",16},
			{4,109,144,"获得7折优惠!",7},
			{5,145,180,"获得6折优惠!",6},
			{6,181,216,"获得5折优惠!",5},
			{7,217,252,"获得4折优惠!",4},
			{8,253,288,"获得3折优惠!",3},
			{9,289,324,"获得2折优惠!",2},
			{10,325,360,"获得1折优惠!",1}
	};

	/**
	 * 大转盘初始化页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index() throws Exception {
		int LOTTERYNUM=0;
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd.put("OPENID", getWeChatOpenId());
		pd = memberService.findByOpenId(pd);
		if(pd==null || Tools.isEmpty(pd.get("LOTTERYNUM")+"")){
			pd = new PageData();
			pd.put("LOTTERYNUM", LOTTERYNUM);
			mv.setViewName("front/lottery/index");
			mv.addObject("pd", pd);
			return mv;
		}
		int verifyPhoneNumber=0;
		if(!Tools.isEmpty(pd.get("VERIFYPHONENUMBER")+"")){
			verifyPhoneNumber = Convert.strToInt(pd.get("VERIFYPHONENUMBER")+"", verifyPhoneNumber);
		}
		if(verifyPhoneNumber==0){
			mv.setViewName("front/member/bindMobile");
			return mv;
		}
		LOTTERYNUM = pd.getInt("LOTTERYNUM");
		pd.put("LOTTERYNUM", LOTTERYNUM);
		List<PageData> varList =lotteryService.listAllBy50(pd);
		mv.addObject("varList", varList);
		//封装用户分享参数
		String openid = getWeChatOpenId();
		String timestamp = Sha1Util.getTimeStamp();
		String nonce_str = Sha1Util.getNonceStr();
		String pageUrl = getRequertNowUrl();
		String signature = Sha1Util.createSignature(timestamp,nonce_str,pageUrl);
		String shareUrl = pageUrl+"&shareId="+openid;
		mv.addObject("timestamp", timestamp);
		mv.addObject("nonce_str", nonce_str);
		mv.addObject("signature", signature);
		mv.addObject("shareUrl", shareUrl);
		mv.addObject("openid", openid);
		mv.addObject("appId", GongZhongService.appId);
		mv.setViewName("front/lottery/index");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * mp4
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/indexPaly")
	public ModelAndView indexPaly() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		mv.setViewName("front/lottery/mp4_index");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * mp4
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/indexFlv")
	public ModelAndView indexFlv() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		mv.setViewName("front/lottery/flv_index");
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	/**
	 * m3u8
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/indexM3u8")
	public ModelAndView indexM3u8() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		mv.setViewName("front/lottery/m3u8_index");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 进行抽奖
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/lottery")
	@ResponseBody
	public Object lottery() throws Exception {
		PageData pd = new PageData();
		int LOTTERYNUM=0;
		String  openId="";
		String  MEMBERID="";
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		if(Tools.isEmpty(getWeChatOpenId())){
			map.put("type", -1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		openId=getWeChatOpenId();
		if(Tools.isEmpty(getMemberId())){
			map.put("type", -1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		MEMBERID=getMemberId();
		pd.put("OPENID", openId);
		pd = memberService.findByOpenId(pd);
		if(pd==null || Tools.isEmpty(pd.get("LOTTERYNUM")+"")){
			pd = new PageData();
			map.put("type", -1);
			map.put("msg", "抽奖次数不足！");
			return AppUtil.returnObject(pd, map);
		}
		LOTTERYNUM = pd.getInt("LOTTERYNUM");
		if(LOTTERYNUM==0){
			map.put("type", -1);
			map.put("msg", "抽奖次数不足！");
			return AppUtil.returnObject(pd, map);
		}
		PageData result = new PageData();
		result =  award(prizeArr);//抽奖后返回角度和奖品等级
		if(result==null){
			map.put("type", -1);
			map.put("msg", "抽奖次数不足！");
			return AppUtil.returnObject(pd, map);
		}
		PageData tpd = new PageData();
		PageData cd = new PageData();//优惠券主表回写
		PageData cdd = new PageData();//优惠券详表数据
		String NUMBERS="";//优惠券码
		int type =2;//保存数据类型 1.抽中优惠券 2.抽中折扣
		String LOTTERYCONTENT =result.getString("msg");
		String angle=result.get("angle")+"";
		int awards=result.getInt("id");//奖品标识
		if(awards==1){
			//代金券
			type =awards;
			String COUPONSID ="936248e3a81f4c30ae2acd07a55527e5";//固定不能删除
			tpd.put("COUPONSID", COUPONSID);
			tpd = couponsService.findById(tpd);
			int couponsState =-1;//判断当前类型优惠券是否启用
			if(Tools.notEmpty(tpd.get("COUPONSSTATE")+"")){
				couponsState = Convert.strToInt(tpd.get("COUPONSSTATE")+"", couponsState);
			}
			if(couponsState==21){
				int COUPONSTOTALNUM = 0;
				if(Tools.notEmpty(tpd.get("COUPONSTOTALNUM")+"")){
					COUPONSTOTALNUM =Convert.strToInt(tpd.get("COUPONSTOTALNUM")+"", COUPONSTOTALNUM)+1;
				}
				cd.put("COUPONSTOTALNUM", COUPONSTOTALNUM);
				cd.put("EDITDATE", new Date());
				cd.put("ADMINID", "抽奖");
				cd.put("COUPONSID", COUPONSID);
			    NUMBERS=Tools.createRandomNum12();
				cdd.put("COUPONSID", COUPONSID);
				cdd.put("NUMBERS", NUMBERS);
				double DENOMINATION =20.00;
				if(!Tools.isEmpty(tpd.get("DENOMINATION")+"")){
					DENOMINATION =Convert.strToDouble(tpd.get("DENOMINATION")+"", DENOMINATION);
				}
				logger.info("DENOMINATION----------------"+DENOMINATION);
				cdd.put("DENOMINATION", DENOMINATION);
				String imgName=DateUtil.getDays()+"/"+System.currentTimeMillis()+".png";
				String QRCODE="uploadFiles/twoDimensionCode/"+imgName;
				String imgPath = getRequest().getServletContext().getRealPath("/")+"uploadFiles/twoDimensionCode/"+imgName;
				TwoDimensionCode handler = new TwoDimensionCode();
				handler.encoderQRCode(NUMBERS, imgPath, "png");
				cdd.put("QRCODE", QRCODE);
				cdd.put("MEMBERID", MEMBERID);
				cdd.put("STATEID", 24);
				cdd.put("CREATEDATE",  new Date());
				cdd.put("EDITDATE",  new Date());
				Date ACQUIREDATE =new Date();
				cdd.put("ACQUIREDATE", ACQUIREDATE);
				int VALIDITY =0;
				Date ENDDATE = new Date();
				if(!Tools.isEmpty(tpd.get("VALIDITY")+"")){
					VALIDITY =Convert.strToInt(tpd.get("VALIDITY")+"", VALIDITY);
					 Calendar   calendar   =   new   GregorianCalendar(); 
				     calendar.setTime(ENDDATE); 
				     calendar.add(calendar.DATE,VALIDITY);//把日期往后增加VALIDITY天.整数往后推,负数往前移动 
				     ENDDATE=calendar.getTime();   //这个时间就是日期往后推VALIDITY天的结果 
				}
				cdd.put("ENDDATE", ENDDATE);
				cdd.put("USEDATE", "");
				cdd.put("USERREMARK", "");
				String COUPONSNAME="";
				if(!Tools.isEmpty(tpd.get("COUPONSNAME")+"")){
					COUPONSNAME=tpd.get("COUPONSNAME")+"";
				}
				cdd.put("REMARKS","抽奖获得："+COUPONSNAME);
			}
		}
		PageData m = new PageData();//回写会员抽奖次数
		m.put("LOTTERYNUM", LOTTERYNUM-1);
		m.put("OPENID", openId);
		PageData l = new PageData();//保存中奖记录数据
		l.put("OPENDID", openId);
		l.put("LOTTERYDATE", new Date());
		l.put("LOTTERYCONTENT", LOTTERYCONTENT);
		l.put("LOTTERYSTATE", 47);
		try {
			memberService.editByLotteryNum(type,m, l,cd,cdd);
		} catch (Exception e) {
			logger.info("抽奖异常"+e.toString());
			e.printStackTrace();
			map.put("type", -1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		map.put("type", 1);
		map.put("msg", LOTTERYCONTENT);
		map.put("angle", angle);
		map.put("numbers", NUMBERS);
		map.put("awards", type);
		map.put("level", awards);
		return AppUtil.returnObject(pd, map);
	}
	
	
	
	// 抽奖并返回角度和奖项
	public PageData award(Object[][] prizeArr) {
		PageData pd = new PageData();
		// 概率数组
		Integer obj[] = new Integer[prizeArr.length];
		for (int i = 0; i < prizeArr.length; i++) {
			obj[i] = (Integer) prizeArr[i][4];
		}
		Integer prizeId = getRand(obj); // 根据概率获取奖项id
		Integer id = (Integer) prizeArr[prizeId][0];
		// 旋转角度
		int angle = new Random().nextInt((Integer) prizeArr[prizeId][2]
				- (Integer) prizeArr[prizeId][1])
				+ (Integer) prizeArr[prizeId][1];
		String msg = (String) prizeArr[prizeId][3];// 提示信息
		pd.put("id", id);//奖品标识
		pd.put("angle", angle);//页面奖品旋转停留角度
		pd.put("msg", msg);//奖品内容
		pd.put("prizeId", prizeId);//随机码
		return pd;
	}

	// 根据概率获取奖项
	public Integer getRand(Integer obj[]) {
		Integer result = null;
		try {
			int sum = 0;// 概率数组的总概率精度
			for (int i = 0; i < obj.length; i++) {
				sum += obj[i];
			}
			for (int i = 0; i < obj.length; i++) {// 概率数组循环
				int randomNum = new Random().nextInt(sum);// 随机生成1到sum的整数
				if (randomNum < obj[i]) {// 中奖
					result = i;
					break;
				} else {
					sum -= obj[i];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,
				true));
	}
}
