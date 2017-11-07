package com.fh.service.system.responselog.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.responselog.ResponseLogManager;

/** 
 * 说明： 微信消息回复
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-23
 * @version
 */
@Service("responselogService")
public class ResponseLogService implements ResponseLogManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("ResponseLogMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("ResponseLogMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("ResponseLogMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ResponseLogMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ResponseLogMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ResponseLogMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ResponseLogMapper.deleteAll", ArrayDATA_IDS);
	}

	/**通过指定条件获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findByWhre(PageData pd) throws Exception {
		return (PageData)dao.findForObject("ResponseLogMapper.findByWhre", pd);
	}

	@Override
	public void editByIsValId(PageData pd) throws Exception {
		dao.update("ResponseLogMapper.editByIsValId", pd);
	}
	
}

