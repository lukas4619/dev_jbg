package com.fh.service.system.reservation.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.system.article.impl.ArticleService;
import com.fh.service.system.member.impl.MemberService;
import com.fh.service.system.reservation.ReservationManager;
import com.fh.service.system.revenue.RevenueManager;
import com.fh.util.Logger;
import com.fh.util.PageData;
import com.fh.util.Tools;
import com.fh.util.UuidUtil;

/** 
 * 说明： 预订记录
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-17
 * @version
 */
@Service("reservationService")
public class ReservationService implements ReservationManager{
	protected Logger logger = Logger.getLogger(ReservationService.class.getClass());
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Resource(name="memberService")
	private MemberService memberService;
	@Resource(name="revenueService")
	private RevenueManager revenueService;
	@Resource(name="articleService")
	private ArticleService articleService;
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData yd,PageData pd)throws Exception{
		dao.save("ReservationMapper.save", yd);
		dao.update("ProductMapper.editByProReservationNum", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("ReservationMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("ReservationMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ReservationMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ReservationMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ReservationMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ReservationMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public void editPayState(PageData pd,PageData p) throws Exception {
		dao.update("ReservationMapper.editPayState", pd);
		dao.update("ProductMapper.editByProConsumeNum", p);
	}

	@Override
	public PageData findByIdNumber(PageData pd) throws Exception {
		return (PageData)dao.findForObject("ReservationMapper.findByIdNumber", pd);
	}

	@Override
	public PageData findByViewId(PageData pd) throws Exception {
		return (PageData)dao.findForObject("ReservationMapper.findByViewId", pd);
	}

	@Override
	public void editPayStateAndAddMoney(PageData pd, PageData p)
			throws Exception {
		// TODO Auto-generated method stub
		long ID = pd.getInt("ARTICLEID");//得到文章的id  主键id
		String shareId = pd.getString("shareId");//得到分享者的会员id
		Double shareRevenue = pd.getDouble("shareRevenue");//分享者获得金额
		Double authorRevenue = pd.getDouble("authorRevenue");//作者收益
		
		logger.info("文章的主键ID.............."+ID);
		logger.info("进入分享预定service........");
		
		dao.update("ReservationMapper.editPayState", pd);//修改订单支付状态
		dao.update("ProductMapper.editByProConsumeNum", p);//修改商品的支付成功数量;
		
		if(!Tools.isEmpty(shareId)){
			PageData memPd = new PageData();
			memPd.put("OPENID", shareId);
			memPd = memberService.findByOpenId(memPd);
			if(memPd!=null&&memPd.getInt("MEMBERTYPE")==2){
				memPd.put("revenueMoney", shareRevenue);
				memPd.put("balanceMoney", shareRevenue);
				memberService.updateBalanceById(memPd);
				//type和状态暂定
				if(shareRevenue>0){
					PageData revenue = getRevenuePageData(memPd,2,4,"分享的广告有用户消费成功 给分享用户带来收入:"+shareRevenue);
					revenueService.save(revenue);//添加收入流水记录;
					revenue=null;
				}
				memPd=null;
			}
		}else{
			logger.info("editPayStateAndAddMoney  分享者shareId不存在");
		}
		
		PageData artPd = new PageData();
		artPd.put("ID", ID);
		artPd = articleService.findByFirstId(artPd);
		logger.info("文章的信息................"+artPd);
		if(ID!=-1){
			String memberId = artPd.getString("AUTHORID");
			//根据memberId查询
			PageData mPd = new PageData();
			mPd.put("memberId", memberId);
			mPd = memberService.findById(mPd);
			if(mPd!=null&&mPd.getInt("MEMBERTYPE")==1){
				mPd.put("revenueMoney", authorRevenue);
				mPd.put("balanceMoney", authorRevenue);
				memberService.updateBalanceById(mPd);
				//type和状态暂定
				if(authorRevenue>0){
					PageData re = getRevenuePageData(mPd,1,32,"在广告位预定有用户消费成功 给作者带来收入:"+authorRevenue);
					revenueService.save(re);//添加收入流水记录;
					re=null;
				}
				mPd=null;
			}else{
				logger.info("作者不存在");
			}
		}
	}
	
	/**
	 * 封装会员对象的pd 为 收益记录的pd
	 * @param pd 会员对象的pd
	 * @param remark  备注
	 * @param status 状态
	 * @param type 类型
	 * @return
	 */
	private PageData getRevenuePageData(PageData pd, int type, int status, String remark) {
		// TODO Auto-generated method stub
		PageData revenuePd = new PageData();
		revenuePd.put("REVENUEID", UuidUtil.get32UUID());
		revenuePd.put("REVENUETYPE", type);
		revenuePd.put("MEMBERID", pd.getString("MEMBERID"));
		revenuePd.put("REVENUEMONEY", pd.getDouble("balanceMoney"));
		revenuePd.put("REVENUEDATE", new Date());
		revenuePd.put("REVENUESTATE", status);
		revenuePd.put("REVENUESOURCE", remark);
		return revenuePd;
	}

	@Override
	public void editState(PageData pd, PageData p) throws Exception {
		dao.update("ReservationMapper.editState", pd);
	}
	
}

