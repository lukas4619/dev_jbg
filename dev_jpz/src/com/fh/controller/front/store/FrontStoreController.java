package com.fh.controller.front.store;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Member;
import com.fh.entity.Page;
import com.fh.service.information.images.ImagesManager;
import com.fh.service.system.article.ArticleManager;
import com.fh.service.system.articleaudit.ArticleauditManager;
import com.fh.service.system.configure.ConfigureManager;
import com.fh.service.system.inventory.InventoryManager;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.order.OrderManager;
import com.fh.service.system.orderdetails.OrderDetailsManager;
import com.fh.service.system.place.PlaceManager;
import com.fh.service.system.placeplu.PlacepluManager;
import com.fh.service.system.product.ProductManager;
import com.fh.service.system.shelves.ShelvesManager;
import com.fh.service.system.skuplu.SkupluManager;
import com.fh.service.system.states.StatesManager;
import com.fh.service.system.store.StoreManager;
import com.fh.service.system.types.TypesManager;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.Convert;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.StringUtil;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.gongzhong.GroupMessage;
import com.fh.wechat.gongzhong.vo.message.group.MessageMateria;
import com.fh.wechat.gongzhong.vo.user.UserInfo;
import com.fh.wechat.pay.Sha1Util;

/**
 * 说明：门店首页 创建人：Lukas 18923798379 创建时间：2017年8月23日 15:50:51
 */
@Controller
@RequestMapping(value = "/frontStore")
public class FrontStoreController extends BaseController {

	String menuUrl = "frontStore/list.do"; // 菜单地址(权限用)
	@Resource(name = "skupluService")
	private SkupluManager skupluService;
	@Resource(name = "inventoryService")
	private InventoryManager inventoryService;
	@Resource(name = "orderdetailsService")
	private OrderDetailsManager orderdetailsService;
	@Resource(name = "storeService")
	private StoreManager storeService;
	@Resource(name = "shelvesService")
	private ShelvesManager shelvesService;
	@Resource(name = "placeService")
	private PlaceManager placeService;
	@Resource(name="placepluService")
	private PlacepluManager placepluService;
	@Resource(name="orderService")
	private OrderManager orderService;
	@Resource(name="configureService")
	private ConfigureManager configureService;
	@Resource(name = "memberService")
	private MemberManager memberService;
	

	/**
	 * 首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public ModelAndView goEditAudit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		List<PageData> shelvesList = new ArrayList<PageData>();// 货架列表
		List<PageData> placeList = new ArrayList<PageData>();// 货位列表
		List<PageData> tempList = new ArrayList<PageData>();
		
		pd = this.getPageData();
		int STOREID = -1;// 门店
		if (!StringUtil.isEmpty(pd.get("STOREID"))) {
			STOREID = Convert.strToInt(pd.getString("STOREID"), STOREID);
		} else {
			STOREID = 2;
		}
		pd.put("ID", STOREID);
		pd = storeService.findById(pd);
		pd.put("TOTALPRICE", "0");
		pd.put("sCar", "");
		pd.put("PLUCOUNT", "0");
		pd.put("VERIFYFAVOURABLE", getFavourable(getWeChatOpenId()));//获取用户是否有优惠信息 
		pd.put("PLUCOUNT", "0");
		mv.addObject("pd", pd);
		pd.put("storeId", STOREID);
		tempList = shelvesService.listAll(pd);
		if(!StringUtil.isPdList(tempList)){
			for (PageData pageData : tempList) {
				pd =  new  PageData();
				pd.put("placeTypeId", 15);
				pd.put("shelvesId", pageData.get("ID"));
				pd.put("storeId", STOREID);
				placeList = placeService.listAll(pd);
				List<PageData> tList = new ArrayList<PageData>();
				for (PageData pl : placeList) {
					pd =  new  PageData();
					pd.put("storeId", STOREID);
					pd.put("shelvesId", pageData.get("ID"));
					pd.put("placeId", pl.get("ID"));
					List<PageData> plList =placepluService.listAll(pd);
					pl.put("plList", plList);
					tList.add(pl);
				}
				placeList=tList;
				//pageData.put("tList", tList);
				pageData.put("placeList", placeList);
				shelvesList.add(pageData);
			}
		}
		PageData cf = new PageData();
		cf.put("CONFIGURE_ID", "b08c336024f447939b857f492a672684");
		cf=configureService.findById(cf);	//根据ID读取
		mv.addObject("cf", cf);
		mv.addObject("shelvesList", shelvesList);
		logger.info(" 获取微信用户微信标识---------------------------------" + getWeChatOpenId());
		mv.setViewName("front/store/index");
		mv.addObject("msg", "goPayment");
		return mv;
	}

	
	/**
	 * 验证结算商品
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/subOrder")
	@ResponseBody
	public Object subOrder() throws Exception {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		String sCar = "";
		if (StringUtil.isEmpty(pd.get("sCar"))) {
			map.put("type", 1);
			map.put("msg", "请选择需要购买商品!");
			return AppUtil.returnObject(pd, map);
		}
		String[] car = pd.get("sCar").toString().split("＆");
		int STOREID = -1;// 门店
		if (StringUtil.isEmpty(pd.get("STOREID"))) {
			map.put("type", 1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		STOREID = Convert.strToInt(pd.get("STOREID") + "", STOREID);
		if (STOREID == -1) {
			map.put("type", 1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		double TOTALPRICE = 0;// 总金额
		if (StringUtil.isEmpty(pd.get("TOTALPRICE"))) {
			map.put("type", 1);
			map.put("msg", "结算金额有误，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		TOTALPRICE = Convert.strToDouble(pd.get("TOTALPRICE") + "", TOTALPRICE);
		if (TOTALPRICE <= 0) {
			map.put("type", 1);
			map.put("msg", "结算金额有误，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		int PLUCOUNT = 0;
		if (StringUtil.isEmpty(pd.get("PLUCOUNT"))) {
			map.put("type", 1);
			map.put("msg", "商品数量有误！");
			return AppUtil.returnObject(pd, map);
		}
		PLUCOUNT = Convert.strToInt(pd.get("PLUCOUNT") + "", PLUCOUNT);
		if (PLUCOUNT <= 0) {
			map.put("type", 1);
			map.put("msg", "商品数量有误！");
			return AppUtil.returnObject(pd, map);
		}
		if (car.length <= 0) {
			map.put("type", 1);
			map.put("msg", "请选择需要购买商品！");
			return AppUtil.returnObject(pd, map);
		}
		Map<String, Object> m = checkOrder(car, STOREID, TOTALPRICE);// 检查订单商品信息、库存、金额
		if (!m.get("type").equals(0)) {
			map.put("type", 1);
			map.put("msg", m.get("msg"));
			return AppUtil.returnObject(pd, map);
		}
		map.put("type", 0);
		map.put("msg", "成功");
		return AppUtil.returnObject(pd, map);
	}
	
	/**
	 * 去支付页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goPayment")
	public ModelAndView goPayment() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData order = new PageData();// 订单主单数据
		List<PageData> orderdetails = new ArrayList<PageData>();// 详单数据
		PageData pd = new PageData();
		pd = this.getPageData();
		String sCar = "";
		if (StringUtil.isEmpty(pd.get("sCar"))) {
			mv.setViewName("front/store/index");
			mv.addObject("msg", "store");
			mv.addObject("pd", pd);
			return mv;
		}
		String[] car = pd.get("sCar").toString().split("＆");
		int STOREID = -1;// 门店
		if (StringUtil.isEmpty(pd.get("STOREID"))) {
			mv.setViewName("front/store/index");
			mv.addObject("msg", "store");
			mv.addObject("pd", pd);
			return mv;
		}
		STOREID = Convert.strToInt(pd.get("STOREID") + "", STOREID);
		if (STOREID == -1) {
			mv.setViewName("front/store/index");
			mv.addObject("msg", "store");
			mv.addObject("pd", pd);
			return mv;
		}
		double TOTALPRICE = 0;// 总金额
		if (StringUtil.isEmpty(pd.get("TOTALPRICE"))) {
			mv.setViewName("front/store/index");
			mv.addObject("msg", "store");
			mv.addObject("pd", pd);
			return mv;
		}
		TOTALPRICE = Convert.strToDouble(pd.get("TOTALPRICE") + "",TOTALPRICE);
		if (TOTALPRICE <= 0) {
			mv.setViewName("front/store/index");
			mv.addObject("msg", "store");
			mv.addObject("pd", pd);
			return mv;
		}
		int PLUCOUNT = 0;
		if (StringUtil.isEmpty(pd.get("PLUCOUNT"))) {
			mv.setViewName("front/store/index");
			mv.addObject("msg", "store");
			mv.addObject("pd", pd);
			return mv;
		}
		PLUCOUNT = Convert.strToInt(pd.get("PLUCOUNT") + "", PLUCOUNT);
		if (PLUCOUNT <= 0) {
			mv.setViewName("front/store/index");
			mv.addObject("msg", "store");
			mv.addObject("pd", pd);
			return mv;
		}
		if (car.length <= 0) {
			mv.setViewName("front/store/index");
			mv.addObject("msg", "store");
			mv.addObject("pd", pd);
			return mv;
		}
		String BILLNO = Tools.createNumber();
		String WECHATNAME = getWeChatName();
		Map<String, Object> map = checkOrder(car, STOREID, TOTALPRICE);// 检查订单商品信息、库存、金额
		if (!map.get("type").equals(0)) {
			mv.setViewName("front/store/index");
			mv.addObject("msg", "store");
			mv.addObject("pd", pd);
			return mv;
		}
		double VERIFYFAVOURABLE =getFavourable(getWeChatOpenId());
		if(VERIFYFAVOURABLE>0){
			order.put("REMARKS", "订单总额为："+TOTALPRICE+",优惠金额为："+VERIFYFAVOURABLE);
		}
		TOTALPRICE = TOTALPRICE-VERIFYFAVOURABLE;
		order.put("VERIFYFAVOURABLE", VERIFYFAVOURABLE);
		order.put("BILLNO", BILLNO);
		order.put("ORDERSTATUSID", 18);// 订单默认状态
		order.put("PAYMENTSTATUSID", 21);// 付款默认状态
		order.put("PLUCOUNT", PLUCOUNT);
		order.put("TOTALPRICE", TOTALPRICE);
		order.put("CREATEDATE", new Date());
		order.put("STOREID", STOREID);
		if(WECHATNAME.length()>200){
			WECHATNAME = WECHATNAME.substring(0,199);
		}
		order.put("WECHATNAME", WECHATNAME);
		order.put("OPENID", getWeChatOpenId());
		loadOrderDetails(orderdetails, car, STOREID, BILLNO);// 处理详单
		if (orderdetails.size() != car.length) {
			mv.setViewName("front/store/index");
			mv.addObject("msg", "store");
			mv.addObject("pd", pd);
			return mv;
		}
		PageData cf = new PageData();
		cf.put("CONFIGURE_ID", "b08c336024f447939b857f492a672684");
		cf=configureService.findById(cf);	//根据ID读取
		mv.addObject("cf", cf);
		// 封装支付参数
		String openid = getWeChatOpenId();
		String timestamp = Sha1Util.getTimeStamp();
		String nonce_str = Sha1Util.getNonceStr();
		String pageUrl = getRequertNowUrl();
		String signature = Sha1Util.createSignature(timestamp, nonce_str, pageUrl);
		String shareUrl = pageUrl + "&shareId=" + openid;
		pd.put("wxtimestamp", timestamp);
		pd.put("wxnonce_str", nonce_str);
		pd.put("wxsignature", signature);
		pd.put("shareUrl", shareUrl);
		pd.put("appId", GongZhongService.appId);
		String TotalFee =Tools.getMoney(TOTALPRICE+"");//TOTALPRICE
		pd.put("money", TotalFee);
		pd.put("TOTALPRICE", TOTALPRICE);
		pd.put("BILLNO", BILLNO);
		pd.put("STOREID", STOREID);
		if (Tools.notEmpty(TotalFee) && Tools.notEmpty(BILLNO)) {
			JSONObject j = null;
			try {
				String packageUrl = Sha1Util.weChatPay(cf.getString("ATTRVALFOUR"), getWeChatOpenId(), TotalFee, BILLNO, "1",
						openid, getIpAddr(), getRequertUrl());
				j = JSONObject.fromObject(packageUrl);
			} catch (Exception e) {
				logger.info("发起支付请求异常：" + e.toString());
				e.printStackTrace();
			}
			if (j != null) {
				pd.put("timeStamp", j.get("timeStamp").toString());
				pd.put("nonceStr", j.get("nonceStr").toString());
				pd.put("packages", j.get("package").toString());
				pd.put("signType", j.get("signType").toString());
				pd.put("paySign", j.get("paySign").toString());
				orderdetailsService.saveOrder(order, orderdetails);
			}
		}
		// 封装支付参数
		mv.setViewName("weChatPay/wxPay");
		mv.addObject("msg", "goPay");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 处理详单数据
	 * 
	 * @param orderdetails
	 *            详单数据
	 * @param car
	 *            pluCode，pluCount，shelvesId，placeId&
	 * @param STOREID
	 *            门店标识
	 * @param BILLNO
	 *            订单编号
	 * @throws Exception
	 */
	private void loadOrderDetails(List<PageData> orderdetails, String[] car, int STOREID, String BILLNO)
			throws Exception {
		for (int i = 0; i < car.length; i++) {
			String[] s = car[i].split("＠");
			if (s.length <= 0) {
				continue;
			}
			PageData pd = new PageData();
			int pluCode = Convert.strToInt(s[0], -1);
			int pluCount = Convert.strToInt(s[1], 0);
			int shelvesId = Convert.strToInt(s[2], -1);
			int placeId = Convert.strToInt(s[3], -1);
			if (pluCode <= -1 || pluCount == 0 || shelvesId <= -1 || placeId <= -1) {
				continue;
			}
			PageData plu = new PageData();
			pd.put("pluCode", pluCode);
			plu = skupluService.findById(pd); // 根据ID读取
			if (plu == null || StringUtil.isEmpty(plu.get("ID"))) {
				continue;
			}
			PageData details = new PageData();
			details.put("BILLNO", BILLNO);
			details.put("PLUCODE", pluCode);
			details.put("PLUNAME", plu.get("PLUNAME"));
			details.put("BARCODE", plu.get("BARCODE"));
			details.put("UNITS", plu.get("UNITS"));
			details.put("SPEC", plu.get("SPEC"));
			details.put("PRICE", plu.get("PRICE"));
			details.put("PLUCLASSID", plu.get("PLUCLASSID"));
			details.put("PLUTYPEID", plu.get("PLUTYPEID"));
			details.put("PLUCOUNT", pluCount);
			details.put("SHELVESID", shelvesId);
			details.put("PLACEID", placeId);
			details.put("STOREID", STOREID);
			orderdetails.add(details);

		}
	}

	/**
	 * 检查被购买商品信息以及库存、金额
	 * 
	 * @param car
	 *            pluCode，pluCount，shelvesId，placeId&
	 * @param STOREID
	 *            门店标识
	 * @param TOTALPRICE
	 *            购买金额
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> checkOrder(String[] car, int STOREID, double TOTALPRICE) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		double tPrice = 0;
		for (int i = 0; i < car.length; i++) {
			PageData pd = new PageData();
			String[] s = car[i].split("＠");
			if (s.length <= 0) {
				map.put("type", 1);
				map.put("msg", "请选择需要购买的商品！");
				return map;
			}
			int pluCode = Convert.strToInt(s[0], -1);
			int pluCount = Convert.strToInt(s[1], 0);
			if (pluCode <= -1 || pluCount == 0) {
				map.put("type", 1);
				map.put("msg", "请选择需要购买的商品！");
				return map;
			}
			PageData plu = new PageData();
			pd.put("pluCode", pluCode);
			plu = skupluService.findById(pd); // 根据ID读取
			if (plu == null || StringUtil.isEmpty(plu.get("ID"))) {
				map.put("type", 1);
				map.put("msg", "请选择需要购买的商品！");
				return map;
			}
			double price = Convert.strToDouble(plu.get("PRICE") + "", 0) * pluCount;
			if (price <= 0) {
				map.put("type", 2);
				map.put("msg", "付款金额有误！");
				return map;
			}
			tPrice += price;
			// 检查库存是否足够
			pd = new PageData();
			pd.put("STOREID", STOREID);
			pd.put("PLUCODE", pluCode);
			pd = inventoryService.findByCount(pd);
			if (pd == null || StringUtil.isEmpty(pd.get("ID"))) {
				map.put("type", 1);
				map.put("msg", "商品库存不足！");
				return map;
			}
			int inventoryCount = Convert.strToInt(pd.get("INVENTORYCOUNT") + "", -1);
			if (inventoryCount <= 0 || (inventoryCount < pluCount)) {
				map.put("type", 1);
				map.put("msg", "商品库存不足！");
				return map;
			}

		}

		if (tPrice != TOTALPRICE) {
			map.put("type", 1);
			map.put("msg", "付款金额与商品金额不一致！");
			return map;
		}
		map.put("type", 0);
		map.put("msg", "允许购买");
		return map;
	}

	/**
	 * 批量门店货架货位列表
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/findPlace")
	@ResponseBody
	public Object findPlace() throws Exception {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		if (StringUtil.isEmpty(pd.get("shelvesId"))) {
			map.put("type", 1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		List<PageData> placeList = new ArrayList<PageData>();
		pd.put("placeTypeId", 15);
		placeList = placeService.listAll(pd);
		if (StringUtil.isPdList(placeList)) {
			map.put("type", 2);
			map.put("msg", "当前货架没有启用货位！");
			return AppUtil.returnObject(pd, map);
		}
		List<PageData> placePluList = new ArrayList<PageData>();
		for (PageData pageData : placeList) {
			PageData p = new PageData();
			p.put("pluCode", pageData.getString("PLACEPLU").split(","));
			p.put("storeId", pageData.getInt("STOREID"));
			List<PageData> pluList = inventoryService.findAllByCode(p);
			pageData.put("pluList", pluList);
			placePluList.add(pageData);
		}
		map.put("type", 0);
		map.put("msg", "成功");
		map.put("list", placePluList);
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 批量查询货位商品列表
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/findPlacePlu")
	@ResponseBody
	public Object findPlacePlu() throws Exception {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		if (StringUtil.isEmpty(pd.get("pluCode"))) {
			map.put("type", 1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		if (StringUtil.isEmpty(pd.get("storeId"))) {
			map.put("type", 1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		pd.put("pluCode", pd.getString("pluCode").split(","));
		pd.put("storeId", pd.getInt("storeId"));
		List<PageData> pluList = new ArrayList<PageData>();
		pluList = inventoryService.findAllByCode(pd);
		if (StringUtil.isPdList(pluList)) {
			map.put("type", 2);
			map.put("msg", "当前货位没有呈列商品！");
			return AppUtil.returnObject(pd, map);
		}
		map.put("type", 0);
		map.put("msg", "成功");
		map.put("list", pluList);
		return AppUtil.returnObject(pd, map);
	}
	
	/**
	 * 首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/er")
	public ModelAndView error() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("front/error");
		mv.addObject("msg", "error");
		return mv;
	}
	
	/**
	 * 获取用户优惠金额
	 * @param openID
	 * @return
	 */
	@SuppressWarnings("unused")
	private  double getFavourable(String openID){
		double verifyFavourable=0.00;//优惠金额
		if(Tools.notEmpty(openID)){
			PageData m = new PageData();
			m.put("OPENID", openID);
			try {
				m=memberService.findByOpenId(m);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(m!=null && Tools.notEmpty(m.get("ID")+"") ){
				verifyFavourable = Convert.strToDouble(m.get("VERIFYFAVOURABLE")+"", verifyFavourable);
			}	
		}
		return verifyFavourable;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
	}
}
