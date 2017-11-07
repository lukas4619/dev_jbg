package com.fh.controller.app.index;

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
@RequestMapping(value = "/appstore")
public class index extends BaseController{
	
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
	
	
	
	/**首页信息
	 * @return
	 */
	@RequestMapping(value="/v1/index")
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
		List<PageData> shelvesList = new ArrayList<PageData>();// 货架列表
		List<PageData> placeList = new ArrayList<PageData>();// 货位列表
		List<PageData> tempList = new ArrayList<PageData>();
		int STOREID = -1;// 门店
		if (!StringUtil.isEmpty(pd.get("STOREID"))) {
			STOREID = Convert.strToInt(pd.getString("STOREID"), STOREID);
		} else {
			STOREID = 2;
		}
		pd.put("ID", STOREID);
		PageData cf = new PageData();
		try {
			pd = storeService.findById(pd);
			pd.put("TOTALPRICE", "0");
			pd.put("sCar", "");
			pd.put("PLUCOUNT", "0");
			data.put("store", pd);
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
					pageData.put("placeList", placeList);
					shelvesList.add(pageData);
				}
			}
			cf = new PageData();
			cf.put("CONFIGURE_ID", "b08c336024f447939b857f492a672684");
			cf=configureService.findById(cf);	//根据ID读取
			data.put("configure", cf);
			data.put("shelvesList", shelvesList);
			map.put("c", 0);
			map.put("m", "");
			map.put("d", data);
		} catch (Exception e) {
			map.put("c", -3);
			map.put("m", "服务器异常");
			map.put("d", "");
			logger.info("appIndex/index：" + e.toString());
			e.printStackTrace();
		}
		return AppUtil.returnObject(new PageData(), map);
	}
	
	
	

}
