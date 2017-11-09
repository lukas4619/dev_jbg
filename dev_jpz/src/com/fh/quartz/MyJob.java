package com.fh.quartz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import com.fh.service.system.inventory.InventoryManager;
import com.fh.service.system.store.StoreManager;
import com.fh.util.Convert;
import com.fh.util.Logger;
import com.fh.util.PageData;
import com.fh.util.StringUtil;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.vo.message.template.Template;
import com.fh.wechat.gongzhong.TemplateMessage;

import org.springframework.scheduling.annotation.Scheduled;  
import org.springframework.stereotype.Component; 

public class MyJob {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "storeService")
	private StoreManager storeService;
	@Resource(name="inventoryService")
	private InventoryManager inventoryService;
	private static final String template_id = "A7jjocd5YClHBu_r3PT8DwyR0rUI7P7mEMRFNFWavQM";
	
	public void work() {  
        System.out.println("date:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));  
        List<PageData> pdList = new ArrayList<PageData>();
        PageData pd = new PageData();
        try {
        	pdList = inventoryService.findAllByPluCode(pd);
			if(!StringUtil.isPdList(pdList)){
				int storeId =-1;
				String openId="";
				for (PageData pageData : pdList) {
					storeId = Convert.strToInt(pageData.get("storeId")+"", storeId);
					if(!StringUtil.isEmpty(pageData.get("openId"))){
						openId = pageData.getString("openId");
					}
					if(storeId!=-1 && Tools.notEmpty(openId)){
						String url="https://www.baidu.com/";
						String topcolor="#8EC31F";
//						{{first.DATA}}
//						仓库：{{keyword1.DATA}}
//						商品：{{keyword2.DATA}}
//						库存：{{keyword3.DATA}}
//						时间：{{keyword4.DATA}}
//						{{remark.DATA}}
						PageData m = new PageData();
						m.put("first", "您好，您的门店商品低于安全库存啦！");
						m.put("keyword1", pageData.getString("storeName"));
						m.put("keyword2", pageData.getString("storeDistrict")+pageData.getString("storeAddress"));
						m.put("keyword3", new SimpleDateFormat("yyyy年MM月dd日 HH:mm ").format(new Date()));
						m.put("remark", "请及时期望门店补充库存，以免影响您的销售！");
						List<Template> t = loadTemplateList(m); 
						logger.info("show PageData m: "+m);
						logger.info("show List<Template> t: "+t);
						TemplateMessage.sendTemplateMessage(openId,template_id,url,topcolor,t);
						
					}else{
						logger.info("[storeId:"+storeId+"],[storeName:"+pageData.get("storeName")+"] is not openId WeChat messaging failed ");
					}
				} 
				
				
			}
		} catch (Exception e) {
			logger.info("inventoryService.findAllByPluCode Exception："+e.toString());
			e.printStackTrace();
		}
        
        
    } 
	
	private List<Template> loadTemplateList(PageData pd){
//		{{first.DATA}}
//		仓库：{{keyword1.DATA}}
//		商品：{{keyword2.DATA}}
//		库存：{{keyword3.DATA}}
//		时间：{{keyword4.DATA}}
//		{{remark.DATA}}
		String[] keys = {"first","keyword1","keyword2", "keyword3","remark"};  
		String color="#173177";
		String key ="";
		String Value ="";
		List<Template> t = new ArrayList<Template>();
		for (int i = 0; i < keys.length; i++) {
			 key = keys[i].toString();
			 Value= pd.getString(key);
			 t=TemplateMessage.setTemplate(t, color, key, Value);
		}
		return t;
	}
	
	
	
	

}
