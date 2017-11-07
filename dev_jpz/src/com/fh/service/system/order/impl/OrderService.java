package com.fh.service.system.order.impl;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.Convert;
import com.fh.util.PageData;
import com.fh.service.system.order.OrderManager;

/** 
 * 说明： 订单管理
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-22
 * @version
 */
@Service("orderService")
public class OrderService implements OrderManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("OrderMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("OrderMapper.delete", pd);
	}
	
	/**修改主单付款状态 
	 * @param pd 主单记录 ORDERSTATUSID，PAYMENTSTATUSID，PAYMENTDATE，BILLNO
	 * @param list 详单记录  STOREID，PLUCODE，INVENTORYCOUNT
	 * @throws Exception
	 */
	public void edit(PageData pd,List<PageData> list)throws Exception{
		int result =0;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			PageData inpd = (PageData) iterator.next();
			PageData  p = new PageData();
			int STOREID =-1;
			p = (PageData)dao.findForObject("InventoryMapper.findByCount", inpd);
			if(p==null || p.size()==0){
				result++;
				break;
			}
			STOREID=Convert.strToInt(p.get("STOREID")+"",STOREID);
			if(STOREID==-1){
				result++;
				break;
			}
			dao.update("InventoryMapper.batchEdit", inpd);//更新库存记录
			
		}
		if(result==0){
			dao.update("OrderMapper.edit", pd);
		}
		
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("OrderMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("OrderMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("OrderMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("OrderMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

