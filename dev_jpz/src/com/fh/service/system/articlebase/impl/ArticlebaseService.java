package com.fh.service.system.articlebase.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.system.articlebase.ArticlebaseManager;
import com.fh.service.system.member.impl.MemberService;
import com.fh.service.system.revenue.impl.RevenueService;
import com.fh.util.PageData;
import com.fh.util.UuidUtil;

/** 
 * 说明： T_articlebask
 * 创建人：Lukas 18923798379
 * 创建时间：2016-09-02
 * @version
 */
@Service("articlebaseService")
public class ArticlebaseService implements ArticlebaseManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Resource(name="memberService")
	private MemberService memberService;
	
	@Resource(name="revenueService")
	private RevenueService revenueService;
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("ArticlebaseMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("ArticlebaseMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("ArticlebaseMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ArticlebaseMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ArticlebaseMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ArticlebaseMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ArticlebaseMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public PageData findByBaskNumber(String txno)throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("ArticlebaseMapper.findByBaskNumber", txno);
	}

	@Override
	public void edithandBusiness(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("开始处理业务...");
		String authorId = pd.getString("AUTHORID");
		String openId = pd.getString("OPENID");
		double baskMoney = pd.getDouble("BASKMONEY");
		edit(pd);//修改打赏记录的状态为已结算
		pd=null;
		PageData memberPd = new PageData();
		memberPd.put("MEMBERID", authorId);
		memberPd.put("balanceMoney", baskMoney/100);
		memberPd.put("revenueMoney", baskMoney/100);
		memberService.updateBalanceById(memberPd);//修改此作者的金额
		memberPd = null;
		//插入作者收益记录
		PageData revenuePd = new PageData();
		revenuePd.put("REVENUEID", UuidUtil.get32UUID());
		revenuePd.put("REVENUETYPE", 1);//给读者带来的收益
		revenuePd.put("MEMBERID", authorId);
		revenuePd.put("REVENUEMONEY", baskMoney/100);
		revenuePd.put("REVENUEDATE", new Date());
		revenuePd.put("REVENUESTATE", 2);//打赏状态
		revenuePd.put("REVENUESOURCE", "读者打赏給我的");
		revenueService.save(revenuePd);
		revenuePd = null;
	}
	
}

