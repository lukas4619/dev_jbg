package com.fh.service.system.orderdetails.impl;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.Convert;
import com.fh.util.PageData;
import com.fh.service.system.orderdetails.OrderDetailsManager;

/** 
 * 说明： 订单详情
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-22
 * @version
 */
@Service("orderdetailsService")
public class OrderDetailsService implements OrderDetailsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("OrderDetailsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("OrderDetailsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("OrderDetailsMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("OrderDetailsMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("OrderDetailsMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("OrderDetailsMapper.findById", pd);
	}
	
	
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("OrderDetailsMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	/**增加订单
	 * @PageData pd 主单记录
	 * @List<PageData> list 详单记录
	 * @throws Exception
	 */
	public void saveOrder(PageData pd, List<PageData> list) throws Exception {
		int result=0;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			PageData pageData = (PageData) iterator.next();
			int PLUCOUNT =Convert.strToInt(pageData.get("PLUCOUNT")+"", -1);
			if(PLUCOUNT<=0){
				result++;
				break;
			}
			PageData p=	(PageData)dao.findForObject("InventoryMapper.findByCount", pageData);
			int inventoryCount = Convert.strToInt(p.get("INVENTORYCOUNT")+"", -1);
			if(inventoryCount<=0){
				result++;
				break;
			}
			if(inventoryCount<PLUCOUNT){
				result++;
				break;
			}
			dao.save("OrderDetailsMapper.save",pageData);
		}
		if(result==0){
			if(pd.getDouble("VERIFYFAVOURABLE")>0){
				dao.save("MemberMapper.editByTF", pd);
			}
			dao.save("OrderMapper.save", pd);
			
		}
		
	}

	@Override
	/**通过订单编号查询详单
	 * @PageData pd billNo
	 * @throws Exception
	 */
	public List<PageData> listByBillNo(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("OrderDetailsMapper.listByBillNo", pd);
	}
	
}

