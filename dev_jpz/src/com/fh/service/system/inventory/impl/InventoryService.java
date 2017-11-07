package com.fh.service.system.inventory.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.Convert;
import com.fh.util.PageData;
import com.fh.service.system.inventory.InventoryManager;

/** 
 * 说明： 门店商品库存
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-20
 * @version
 */
@Service("inventoryService")
public class InventoryService implements InventoryManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("InventoryMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("InventoryMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("InventoryMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("InventoryMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("InventoryMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("InventoryMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("InventoryMapper.deleteAll", ArrayDATA_IDS);
	}
    
	/**
	 * 批量插入
	 */
	@Override
	public void batchSave(List<PageData>  pd) throws Exception {
		dao.batchSave("InventoryMapper.save", pd);
		
	}

	@Override
	public void batchEdit(List<PageData> pd) throws Exception {
		for (Iterator iterator = pd.iterator(); iterator.hasNext();) {
			PageData pageData = (PageData) iterator.next();
			PageData repPd = new PageData();//补货记录
			PageData inpd = new PageData();//更新库存记录
			PageData  p = new PageData();
			p = (PageData)dao.findForObject("InventoryMapper.findByPlucode", pageData);
			if(p==null || p.size()==0){
				continue;
			}
			int NUM=-1;
			NUM=Convert.strToInt(p.get("NUM")+"",NUM);
			if(NUM==-1){
				continue;
			}
			repPd.put("STOREID", pageData.getInt("STOREID"));
			repPd.put("PLUCODE", pageData.getInt("PLUCODE"));
			repPd.put("REPLENISHCOUNT", pageData.getInt("REPLENISHCOUNT"));
			repPd.put("CREATEDATE", new Date());
			repPd.put("CREATENAME", pageData.getString("CREATENAME"));
			inpd.put("STOREID", pageData.getInt("STOREID"));
			inpd.put("PLUCODE", pageData.getInt("PLUCODE"));
			inpd.put("INVENTORYCOUNT", pageData.getInt("REPLENISHCOUNT"));
			dao.save("ReplenishMapper.save", repPd);//补货记录
			dao.update("InventoryMapper.edit", inpd);//更新库存记录
			
		}
	}

	@Override
	public PageData findByCount(PageData pd) throws Exception {
		return (PageData)dao.findForObject("InventoryMapper.findByCount", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findAllByCode(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("InventoryMapper.findAllByCode", pd);
	}

	@Override
	public void editTaps(PageData pd) throws Exception {
		dao.update("InventoryMapper.editTaps", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findAllByPluCode(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("InventoryMapper.findAllByPluCode", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> datalistPluPage(Page page) throws Exception {
		return (List<PageData>)dao.findForList("InventoryMapper.datalistPluPage", page);
	}
	
}

