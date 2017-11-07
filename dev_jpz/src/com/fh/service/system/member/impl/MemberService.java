package com.fh.service.system.member.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.member.MemberManager;

/** 
 * 说明： 微信用户Controller
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-16
 * @version
 */
@Service("memberService")
public class MemberService implements MemberManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("MemberMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("MemberMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("MemberMapper.edit", pd);
	}
	
	
	/**微信用户取消关注修改
	 * @param pd
	 * @throws Exception
	 */
	public void editByOpenId(PageData pd)throws Exception{
		dao.update("MemberMapper.editByOpenId", pd);
	}
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("MemberMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("MemberMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("MemberMapper.findById", pd);
	}
	
	/**通过OpenId获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByOpenId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("MemberMapper.findByOpenId", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("MemberMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public void editSoftAuthor(PageData pd) throws Exception{
		dao.update("MemberMapper.editSoftAuthor", pd);
	}

	@Override
	public PageData getMemberCount(int value) throws Exception {
		return (PageData)dao.findForObject("MemberMapper.getMemberCount", value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> listGroupId(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("MemberMapper.listGroupId", pd);
	}
	
	/**
	 * 修改用户的收益
	 * @param pd
	 */
	public void updateBalanceById(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.update("MemberMapper.updateBalanceById", pd);
	}

	@Override
	public void editPhoneNumber(PageData pd) throws Exception {
		dao.update("MemberMapper.editPhoneNumber", pd);
	}

	@Override
	public void editByIdMoney(PageData pd) throws Exception {
		dao.update("MemberMapper.editByIdMoney", pd);
	}

	@Override
	public void saveSubscribe(PageData pd, PageData ld, PageData cd,
			PageData cdd) throws Exception {
		dao.save("MemberMapper.save", pd);
		dao.save("MemberLogMapper.save", ld);
		dao.save("CouponsDetailMapper.insertSubscribe", cdd);
		dao.update("CouponsMapper.editByCouponsTotalNum", cd);
		
	}

	@Override
	public PageData getMemberByNameAndPwd(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("MemberMapper.getMemberByNameAndPwd", pd);
	}

	@Override
	public PageData findByPhoneNumber(PageData pd) throws Exception {
		return (PageData)dao.findForObject("MemberMapper.findByPhoneNumber", pd);
	}

	@Override
	public void editMemberType(PageData pd, PageData p) throws Exception {
		dao.update("MemberMapper.editMemberType", pd);
		dao.update("MemberLogMapper.save", p);
	}

	@Override
	public void editPassWord(PageData pd)throws Exception {
		// TODO Auto-generated method stub
		dao.update("MemberMapper.editPassWord", pd);
	}

	@Override
	public void editByLotteryNum(int type,PageData m,PageData l, PageData cd,
			PageData cdd) throws Exception {
		dao.update("MemberMapper.editByLotteryNum", m);
		dao.update("LotteryMapper.save", l);
		if(type==1){
			dao.save("CouponsDetailMapper.insertSubscribe", cdd);
			dao.update("CouponsMapper.editByCouponsTotalNum", cd);
		}
	}

	@Override
	public void editByLotteryNum(PageData m) throws Exception {
		dao.update("MemberMapper.editByLotteryNum", m);
	}

	@Override
	public void editLoginInfo(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.update("MemberMapper.editLoginDate", pd);
	}

	@Override
	public int findByOpenIdCount(PageData pd) throws Exception {
		pd =(PageData)dao.findForObject("MemberMapper.findByOpenIdCount", pd);
		if(pd.getInt("COUNTNUMBER")>=1){
			return 1;
		}
		return 0;
	}

	@Override
	public void editOpenID(PageData pd) throws Exception {
		dao.update("MemberMapper.editOpenID", pd);
		
	}
	
}

