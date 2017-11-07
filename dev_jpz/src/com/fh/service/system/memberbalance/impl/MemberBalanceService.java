package com.fh.service.system.memberbalance.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.memberbalance.MemberBalanceManager;

/** 
 * 说明： MemberBalanceController
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-17
 * @version
 */
@Service("memberbalanceService")
public class MemberBalanceService implements MemberBalanceManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("MemberBalanceMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("MemberBalanceMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("MemberBalanceMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("MemberBalanceMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("MemberBalanceMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("MemberBalanceMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("MemberBalanceMapper.deleteAll", ArrayDATA_IDS);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> datalistPageByMember(Page page) throws Exception {
		return (List<PageData>)dao.findForList("MemberBalanceMapper.datalistPageByMember", page);
	}

	@Override
	public void save(PageData pd,PageData member, PageData p,PageData d) throws Exception {
		dao.save("MemberBalanceMapper.save", pd);
		dao.update("MemberMapper.editByIdMoney", member);
		dao.update("MemberLogMapper.save", p);
		dao.save("BalanceDetailsMapper.save", d);
	}
	
}

