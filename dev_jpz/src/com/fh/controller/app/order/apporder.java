package com.fh.controller.app.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.system.configure.ConfigureManager;
import com.fh.service.system.inventory.InventoryManager;
import com.fh.service.system.order.OrderManager;
import com.fh.service.system.orderdetails.OrderDetailsManager;
import com.fh.service.system.place.PlaceManager;
import com.fh.service.system.placeplu.PlacepluManager;
import com.fh.service.system.shelves.ShelvesManager;
import com.fh.service.system.skuplu.SkupluManager;
import com.fh.service.system.store.StoreManager;
import com.fh.util.AppUtil;
import com.fh.util.Convert;
import com.fh.util.PageData;
import com.fh.util.StringUtil;

@Controller
@RequestMapping(value = "/apporder")
public class apporder extends BaseController{
	
	@Resource(name = "skupluService")
	private SkupluManager skupluService;
	@Resource(name = "inventoryService")
	private InventoryManager inventoryService;
	@Resource(name = "orderdetailsService")
	private OrderDetailsManager orderdetailsService;
	
	/**首页信息
	 * @return
	 */
	@RequestMapping(value="/v1/suborder")
	@ResponseBody
	public Object index(){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> data = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		if(pd!=null && pd.size()>0){
			map = AppUtil.cParam(pd);
			if(!map.get("c").equals(0)){
				return AppUtil.returnObject(new PageData(), map);
			}
		}
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
		
		return AppUtil.returnObject(new PageData(), map);
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
	private Map<String, Object> checkOrder(String[] car, int STOREID, double TOTALPRICE){
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
			try {
				plu = skupluService.findById(pd);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // 根据ID读取
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
			try {
				pd = inventoryService.findByCount(pd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

}
