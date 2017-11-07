package com.fh.service.system.skuplu.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.system.skuplu.SkupluManager;
import com.fh.util.PageData;

/** 
 * 说明： 商品基本信息
 * 创建人：Lukas 18923798379
 * 创建时间：2017-08-19
 * @version
 */
@Service("skupluService")
public class SkupluService implements SkupluManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("SkupluMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("SkupluMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("SkupluMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("SkupluMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("SkupluMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SkupluMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("SkupluMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public void findAllByIds(String[] ArrayDATA_IDS) throws Exception {
		dao.delete("SkupluMapper.findAllByIds", ArrayDATA_IDS);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findAllByCode(String[] ArrayDATA_IDS) throws Exception {
		return (List<PageData>)dao.delete("SkupluMapper.findAllByCode", ArrayDATA_IDS);
		
	}

	@Override
	public void batchSave(List<PageData> pd) throws Exception {
		dao.batchSave("SkupluMapper.save", pd);
	}

	@Override
	public void editStatus(PageData pd) throws Exception {
		dao.update("SkupluMapper.editStatus", pd);
	}
	
}

