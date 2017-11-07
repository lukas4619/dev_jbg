package com.fh.service.system.t_menu;

import java.util.List;

import net.sf.json.JSONArray;

import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 微信菜单接口
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-21
 * @version
 */
public interface T_menuManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	public PageData findMaxId(PageData pd)throws Exception;
	
	/**
	 * 删除一级菜单和旗下的所有子菜单
	 * @param pd
	 */
	public void deleteParentAndAllChildById(PageData pd)throws Exception;
	/**
	 * 根据pid查询菜单个数
	 * @param pd
	 * @return
	 */
	public PageData getMenuSumByParentId(PageData pd)throws Exception;
	/**
	 * 根据key_type 查询content
	 * @param trim
	 * @return
	 */
	public PageData findByKeyType(String trim) throws Exception;
	
}

