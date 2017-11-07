package com.fh.service.system.member;

import java.util.List;
import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 微信用户Controller接口
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-16
 * @version
 */
public interface MemberManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	
	/**新增关注用户
	 * @param pd 用户数据
	 * @param ld 操作数据
	 * @param cd 优惠券主表数据
	 * @param cdd 优惠券详细表数据
	 * @throws Exception
	 */
	public void saveSubscribe(PageData pd,PageData ld,PageData cd,PageData cdd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**修改抽奖次数和增加抽奖记录
	 * @param type 回写类型
	 * @param pd 修改抽奖次数
	 * @param l 增加抽奖记录
	 * @param cd 优惠券主表数据
	 * @param cdd 优惠券详细表数据
	 * @throws Exception
	 */
	public void editByLotteryNum(int type,PageData m,PageData l, PageData cd,
			PageData cdd)throws Exception;
	
	
	/**修改抽奖次数
	 * @param pd 修改抽奖次数
	 * @throws Exception
	 */
	public void editByLotteryNum(PageData m)throws Exception;
	
	/**
	 * 修改用户绑定手机
	 * @param pd 用户更新数据
	 * @throws Exception
	 */
	public void editPhoneNumber(PageData pd)throws Exception;
	
	
	/**
	 * 作者用户认证并绑定手机
	 * @param pd 用户更新数据
	 * @param p 用户操作记录数据
	 * @throws Exception
	 */
	public void editMemberType(PageData pd,PageData p)throws Exception;
	
	
	/**微信用户取消关注修改
	 * @param pd
	 * @throws Exception
	 */
	public void editByOpenId(PageData pd)throws Exception;
	
	/**修改冻结金额和可提现金额 
	 * @param pd 修改修改的用户冻结金额、可提现金额
	 * @throws Exception
	 */
	public void editByIdMoney(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**列表(分组排序)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listGroupId(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByOpenId(PageData pd)throws Exception;
	
	/**通过手机号码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByPhoneNumber(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	/**
	 * 编辑保存作者管理字段信息
	 * @param pd
	 */
	public void editSoftAuthor(PageData pd)throws Exception;
	
	/**获取总数
	 * @param value
	 * @throws Exception
	 */
	public PageData getMemberCount(int value)throws Exception;

	/**
	 * 登陆校验
	 * @param pd
	 * @return
	 */
	public PageData getMemberByNameAndPwd(PageData pd)throws Exception;

	/**
	 * 作者修改密码
	 * @param pd
	 */
	public void editPassWord(PageData pd)throws Exception;

	/**
	 * 修改用户登陆ip和时间等信息
	 * @param pd
	 */
	public void editLoginInfo(PageData pd)throws Exception;
	

	/**通OPENID获取是否存在
	 * @param pd
	 * @throws Exception
	 */
	public int findByOpenIdCount(PageData pd)throws Exception;
	
	
	/**修改关注信息
	 * @param pd
	 * @throws Exception
	 */
	public void editOpenID(PageData pd)throws Exception;
}

