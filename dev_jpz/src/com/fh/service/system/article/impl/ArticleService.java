package com.fh.service.system.article.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.util.logging.resources.logging;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.system.article.ArticleManager;
import com.fh.service.system.likerecorde.impl.LikeRecordeService;
import com.fh.service.system.member.impl.MemberService;
import com.fh.service.system.revenue.impl.RevenueService;
import com.fh.util.Logger;
import com.fh.util.PageData;
import com.fh.util.Tools;
import com.fh.util.UuidUtil;
import com.fh.wechat.gongzhong.vo.user.UserInfo;

/** 
 * 说明： 软文管理
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-18
 * @version
 */
@Service("articleService")
public class ArticleService implements ArticleManager{
	protected Logger logger = Logger.getLogger(ArticleService.class.getClass());
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private RevenueService revenueService;
	
	@Autowired
	private LikeRecordeService likeRecordeService;
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("ArticleMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("ArticleMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("ArticleMapper.edit", pd);
	}
	
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception{
		dao.update("ArticleMapper.editState", pd);
	}
	
	/**修改推送内容
	 * @param pd
	 * @throws Exception
	 */
	public void editPush(PageData pd)throws Exception{
		dao.update("ArticleMapper.editPush", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ArticleMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ArticleMapper.listAll", pd);
	}
	

	/**列表(指定标识)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listByIds(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ArticleMapper.listByIds", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ArticleMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ArticleMapper.deleteAll", ArrayDATA_IDS);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> listByAuthor(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("ArticleMapper.listByAuthor", pd);
	}

	@Override
	public void addReadNumAndMoney(UserInfo user, String aRTICLEID ,String shareId)
			throws Exception {
		// TODO Auto-generated method stub
		//根据aRTICLEID 查询文章的信息;
		PageData pd = new PageData();
		pd.put("ARTICLEID", aRTICLEID);
		pd = this.findById(pd);
		String authorID = pd.getString("AUTHORID");
		Double artRevenuePV = pd.getDouble("REVENUEPV");//软文作者游览收益比例;
		int articlePV = pd.getInt("ARTICLEPV")==0?1:pd.getInt("ARTICLEPV")+1;//文章的阅读数量;
		double revenuePVM = pd.getDouble("REVENUEPVM");//用户分享软文游览收益比例;
		//根据会员ID查询作者会员信息;
		pd = null;
		pd = new PageData();
		pd.put("memberId", authorID);
		pd = memberService.findById(pd);
		//1.给文章的阅读数量增加
		dao.update("ArticleMapper.addReadNum", aRTICLEID);
		PageData revenuePd = null;
		if(pd!=null){
			Double memRevenuePV = pd.getDouble("REVENUEPV");//软文作者游览收益比例;
			//2.给作者添加阅读金额
			Double sumRevenuePV = artRevenuePV + memRevenuePV;//总的浏览收益比例
			pd.put("revenueMoney", sumRevenuePV*articlePV*0.01);
			pd.put("balanceMoney", sumRevenuePV*articlePV*0.01);
			memberService.updateBalanceById(pd);
			revenuePd = getRevenuePageData(pd,1,3,"会员阅读文章");
			revenueService.save(revenuePd);
		}else{
			logger.info("作者不存在 authorID:"+authorID);
		}
		
		
		//3.文章的分享用户id不为空  //判断是否为别的会员分享过来的
		if(!Tools.isEmpty(shareId)){
			PageData sharePd = new PageData();
			sharePd.put("OPENID", shareId);
			sharePd = memberService.findByOpenId(sharePd);
			Double memRevenuePVM = sharePd.getDouble("REVENUEPVM");//用户分享软文游览收益比例;
			if(sharePd!=null&&sharePd.getInt("MEMBERTYPE")==2){
				//如果分享的用户id是系统用户  给普通用户添加阅读金额
				Double sumShareRevenuePVM = revenuePVM + memRevenuePVM;
				sharePd.put("revenueMoney", sumShareRevenuePVM*articlePV*0.01);
				sharePd.put("balanceMoney", sumShareRevenuePVM*articlePV*0.01);
				memberService.updateBalanceById(sharePd);
//				if(sharePd.getInt("MEMBERTYPE")==1){
//					revenuePd = getRevenuePageData(sharePd,1,4,"会员查看了我分享的文章");
//				}else{
					revenuePd = getRevenuePageData(sharePd,2,4,"会员查看了我分享的文章");
//				}
				revenueService.save(revenuePd);
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
	public void addLikeNumAndMoney(UserInfo user,String aRTICLEID) throws Exception {
		// TODO Auto-generated method stub
		//根据aRTICLEID 查询文章的信息;
		PageData pd = new PageData();
		pd.put("ARTICLEID", aRTICLEID);
		pd = this.findById(pd);
		String authorID = pd.getString("AUTHORID");
		Double revenueLike = pd.getDouble("REVENUELIKE");//软文作者点赞的比例;
		int articlePV = pd.getInt("ARTICLELIKE")==0?1:pd.getInt("ARTICLELIKE")+1;//文章的点赞数量;
		dao.update("ArticleMapper.addLikeNum", aRTICLEID);//点赞数量加一
		//根据会员ID查询作者会员信息;
		pd = null;
		pd = new PageData();
		pd.put("memberId", authorID);
		pd = memberService.findById(pd);
		Double memRevenueLike = 0.0;
		if(pd!=null){
			memRevenueLike = pd.getDouble("REVENUELIKE");//作者点赞的收益比例;
			logger.info("authorID:"+authorID+" revenueLike:"+revenueLike+" memRevenueLike:"+memRevenueLike);
			System.out.println("authorID:"+authorID+" revenueLike:"+revenueLike+" memRevenueLike:"+memRevenueLike);
			Double sumRevenueLike = revenueLike + memRevenueLike;
			//给作者添加点赞金额
			pd.put("revenueMoney", sumRevenueLike*articlePV*0.01);
			pd.put("balanceMoney", sumRevenueLike*articlePV*0.01);
			memberService.updateBalanceById(pd);
			PageData revenuePd = getRevenuePageData(pd,1,20,"会员给我的文章点赞");
			revenueService.save(revenuePd);//添加收益记录
		}else{
			logger.info("差不多该作者...");
		}
		
		//添加点赞记录
		PageData likeRecordePd = new PageData();
		likeRecordePd.put("ID", UuidUtil.get32UUID());
		likeRecordePd.put("OPENID", user.getOpenid());
		likeRecordePd.put("ARTICLEID", aRTICLEID.trim());
		likeRecordeService.save(likeRecordePd);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> datalistPageByMember(Page page) throws Exception {
		return (List<PageData>)dao.findForList("ArticleMapper.datalistPageByMember", page);
	}

	@Override
	public PageData findByFirstId(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (PageData)dao.findForObject("ArticleMapper.findByFirstId", pd);
	}

	@Override
	public void authorInsert(PageData pd, PageData ad, List<PageData> lp)
			throws Exception {
		dao.save("ArticleMapper.authorInsert", pd);
		dao.save("ArticleauditMapper.save", ad);
		dao.save("ImagesMapper.insertBatch", lp);
	}

	@Override
	public PageData findByViewId(PageData pd) throws Exception {
		return (PageData)dao.findForObject("ArticleMapper.findByViewId", pd);
	}

	@Override
	public void authorSave(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.save("ArticleMapper.authorSave", pd);
	}

	@Override
	public void editState(PageData p, PageData pd) throws Exception {
		dao.save("ArticleauditMapper.save", p);
		dao.update("ArticleMapper.editState", pd);
		
	}
	
}

