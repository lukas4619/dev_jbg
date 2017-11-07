package com.fh.service.system.product.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.product.ProductManager;

/** 
 * 说明： 产品模块
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-17
 * @version
 */
@Service("productService")
public class ProductService implements ProductManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("ProductMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("ProductMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("ProductMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ProductMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ProductMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ProductMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ProductMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public void editByState(PageData pd) throws Exception {
		dao.update("ProductMapper.editByState", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> listByIds(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("ProductMapper.listByIds", pd);
	}

	@Override
	public void editByProReservationNum(PageData pd) throws Exception {
		dao.update("ProductMapper.editByProReservationNum", pd);
		
	}

	@Override
	public void editByProConsumeNum(PageData pd) throws Exception {
		dao.update("ProductMapper.editByProConsumeNum", pd);
		
	}

	@Override
	public PageData findByViweId(PageData pd) throws Exception {
		return (PageData)dao.findForObject("ProductMapper.findByViweId", pd);
	}
	
}

