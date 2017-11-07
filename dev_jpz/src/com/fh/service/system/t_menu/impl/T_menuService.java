package com.fh.service.system.t_menu.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.system.t_menu.T_menuManager;
import com.fh.util.PageData;

/** 
 * 说明： 微信菜单
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-21
 * @version
 */
@Service("t_menuService")
public class T_menuService implements T_menuManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("T_menuMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("T_menuMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("T_menuMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("T_menuMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("T_menuMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("T_menuMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("T_menuMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public PageData findMaxId(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("T_menuMapper.findMaxId", pd);
	}

	@Override
	public void deleteParentAndAllChildById(PageData pd) throws Exception{
		// TODO Auto-generated method stub
		dao.delete("T_menuMapper.deleteParentAndAllChildById", pd);
	}

	@Override
	public PageData getMenuSumByParentId(PageData pd) throws Exception{
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("T_menuMapper.getMenuSumByParentId", pd);
	}

	@Override
	public PageData findByKeyType(String trim) throws Exception{
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("T_menuMapper.findByKeyType", trim);
	}
	
}

