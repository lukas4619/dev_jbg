package com.fh.service.system.coupons.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.coupons.CouponsManager;

/** 
 * 说明： 优惠券主表
 * 创建人：Lukas 18923798379
 * 创建时间：2016-09-01
 * @version
 */
@Service("couponsService")
public class CouponsService implements CouponsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("CouponsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("CouponsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("CouponsMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("CouponsMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CouponsMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CouponsMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CouponsMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public void editByCouponsTotalNum(PageData pd) throws Exception {
		dao.update("CouponsMapper.editByCouponsTotalNum", pd);
	}

	@Override
	public void editByCouponsUse(PageData pd, PageData pdd) throws Exception {
		dao.update("CouponsMapper.editByCouponsUseNum", pd);
		dao.update("CouponsDetailMapper.editNumbersUse", pdd);
	}
	
}

