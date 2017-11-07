package com.fh.service.system.placeplu.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.placeplu.PlacepluManager;

/** 
 * 说明： 货位商品
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-29
 * @version
 */
@Service("placepluService")
public class PlacepluService implements PlacepluManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("PlacepluMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("PlacepluMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("PlacepluMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("PlacepluMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("PlacepluMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("PlacepluMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("PlacepluMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public void batchSave(PageData pd) throws Exception {
		String [] pluCodeArray = pd.getString("pluCodes").split(",");
		for (String s : pluCodeArray) {
			PageData p = new PageData();
			p.put("PLUCODE", s);
			p.put("STOREID", pd.getInt("storeId"));
			p.put("SHELVESID", pd.getInt("shelvesId"));
			p.put("PLACEID", pd.getInt("placeId"));
			dao.save("PlacepluMapper.save", p);
		}
		
	}
	
}

