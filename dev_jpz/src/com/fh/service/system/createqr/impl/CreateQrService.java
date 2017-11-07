package com.fh.service.system.createqr.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.system.article.impl.ArticleService;
import com.fh.service.system.createqr.CreateQrManager;
import com.fh.service.system.member.impl.MemberService;
import com.fh.service.system.revenue.impl.RevenueService;
import com.fh.util.Logger;
import com.fh.util.PageData;
import com.fh.util.Tools;
import com.fh.util.UuidUtil;

/** 
 * 说明： 二维码
 * 创建人：Lukas 18923798379
 * 创建时间：2016-09-18
 * @version
 */
@Service("createqrService")
public class CreateQrService implements CreateQrManager{
	
	protected Logger logger = Logger.getLogger(CreateQrService.class.getClass());
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Resource(name="articleService")
	private ArticleService articleService;
	
	@Resource(name="memberService")
	private MemberService memberService;
	
	@Resource(name="revenueService")
	private RevenueService revenueService;
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("CreateQrMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("CreateQrMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("CreateQrMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("CreateQrMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CreateQrMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CreateQrMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CreateQrMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public void handerAuthorMoneyAndUserBalance(String createId)throws Exception {
		// TODO Auto-generated method stub
		logger.info("进入关注给作者带来的收益或者给分享者带来的收益的CreateQrSErvice方法了......   CreateQr表的主键是:"+createId);
		
		PageData qrPd = new PageData();
		qrPd.put("id", createId);
		qrPd = this.findById(qrPd);
		
		PageData artPd = null;
		
		if(qrPd!=null&&!Tools.isEmpty(qrPd.getString("ARTICLEID"))){
			artPd = new PageData();
			artPd.put("ARTICLEID", qrPd.getString("ARTICLEID"));
			artPd = articleService.findById(artPd);
		}
		
		String shareOpenId ="";
		if(qrPd!=null&&!Tools.isEmpty(qrPd.getString("SHARERID"))){
			shareOpenId = qrPd.getString("SHARERID");//分享者的opentId
		}
		
		qrPd = null;
		
		String authorId ="";
		Double artAuthorRevenuesub=0.0;
		Double artMemberRevenueSubm=0.0;
		if(artPd!=null){
			authorId = artPd.getString("AUTHORID");//文章作者id
			artAuthorRevenuesub = artPd.getDouble("REVENUESUB");//从文章处关注 给作者带来的收益 
			artMemberRevenueSubm = artPd.getDouble("REVENUESUBM");//从文章处分享给 分享者带来的收益
		}
		
		artPd = null;
		PageData authorPd = new PageData();
		authorPd.put("memberId", authorId);
		authorPd = memberService.findById(authorPd);//查询作者信息
		
		if(authorPd!=null&&authorPd.getInt("MEMBERTYPE")==1){
			Double authorRevenuesub = authorPd.getDouble("REVENUESUB");//作者表中关注字段的收益比例;
			//是作者给作者添加收益记录
			Double balanceMoney = (authorRevenuesub+artAuthorRevenuesub)*0.01;
			
			logger.info("给作者带来的收益是:"+balanceMoney);
			authorPd.put("balanceMoney", balanceMoney);
			authorPd.put("revenueMoney", balanceMoney);
			authorPd.put("MEMBERID", authorId);
			memberService.updateBalanceById(authorPd);
			if(balanceMoney>0){
				//添加收益记录
				PageData revenue = getRevenuePageData(authorPd,1,1,"从作者文章处关注给作者带来的收益是:"+balanceMoney);
				revenueService.save(revenue);//添加收入流水记录;
				revenue=null;
			}
		}else{
			logger.info("作者没有找到...");
		}
		
		//给分享者带来的收益
		
		PageData mPd = new PageData();
		mPd.put("OPENID", shareOpenId);
		mPd = memberService.findByOpenId(mPd);
		
		if(mPd!=null&&mPd.getInt("MEMBERTYPE")==2){
			//普通用户
			Double  revemieSubm  = mPd.getDouble("REVENUESUBM");//别人关注给分享者带来的收益
			Double  balanceMoney = (revemieSubm+artMemberRevenueSubm)*0.01;
			mPd.put("balanceMoney", balanceMoney);
			mPd.put("revenueMoney", balanceMoney);
			mPd.put("MEMBERID", mPd.getString("MEMBERID"));
			memberService.updateBalanceById(mPd);
			if(balanceMoney>0){
				//添加收益记录
				logger.info("分享者MEMBERID:"+mPd.getString("MEMBERID"));
				PageData revenue = getRevenuePageData(mPd,2,1,"关注给分享者带来的收益是:"+balanceMoney);
				revenueService.save(revenue);//添加收入流水记录;
				revenue=null;
			}
		}else{
			logger.info("分享者没有值...");
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
	
}

