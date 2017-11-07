package com.fh.service.handlecar.broadband.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.handlecar.broadband.BroadbandManager;

/** 
 * 说明： 宽带信息
 * 创建人：Lukas 18923798379
 * 创建时间：2017-10-10
 * @version
 */
@Service("broadbandService")
public class BroadbandService implements BroadbandManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("BroadbandMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("BroadbandMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("BroadbandMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("BroadbandMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("BroadbandMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("BroadbandMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("BroadbandMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public void batchSave(List<PageData> listPD) throws Exception {
		dao.batchSave("BroadbandMapper.save", listPD);
	}

	@Override
	public void editDate(PageData pd) throws Exception {
		PageData p = new PageData();
		pd.put("RENEWDATE", new Date());
		p.put("BID", pd.getInt("ID"));
		p.put("RENEWPRICE", pd.getDouble("RENEWPRICE"));
		p.put("CREATEDATE", new Date());
		p.put("CONTENT", pd.getString("CONTENT"));
		p.put("EXPIREDATE", pd.getString("EXPIREDATE"));
		dao.update("BroadbandMapper.editDate", pd);
		dao.update("BanddetailMapper.save", p);
		
	}
	
}

