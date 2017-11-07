package com.fh.service.system.couponsdetail.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.CouponsDetail;
import com.fh.util.PageData;
import com.fh.service.system.couponsdetail.CouponsDetailManager;

/** 
 * 说明： 优惠券
 * 创建人：Lukas 18923798379
 * 创建时间：2016-09-01
 * @version
 */
@Service("couponsdetailService")
public class CouponsDetailService implements CouponsDetailManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("CouponsDetailMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("CouponsDetailMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("CouponsDetailMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("CouponsDetailMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CouponsDetailMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CouponsDetailMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CouponsDetailMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public void batchSave(List<PageData> lp) throws Exception {
		dao.batchSave("CouponsDetailMapper.insertBatch", lp);
	}

	@Override
	public void insertSubscribe(PageData pd) throws Exception {
		dao.save("CouponsDetailMapper.insertSubscribe", pd);
		
	}

	@Override
	public PageData findByViewId(PageData pd) throws Exception {
		return (PageData)dao.findForObject("CouponsDetailMapper.findByViewId", pd);
	}
	
}

