package com.fh.service.system.article;

import java.util.List;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.wechat.gongzhong.vo.user.UserInfo;

/** 
 * 说明： 软文管理接口
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-18
 * @version
 */
public interface ArticleManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	
	/**作者新增
	 * @param pd 软文数据
	 * @param ad 进度数据
	 * @param lp 图片数据
	 * @throws Exception
	 */
	public void authorInsert(PageData pd,PageData ad,List<PageData> lp)throws Exception;
	
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
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception;
	
	/**修改状态
	 * @param p 审核记录
	 * @param pd 状态回写
	 * @throws Exception
	 */
	public void editState(PageData p,PageData pd)throws Exception;
	
	/**修改推送内容
	 * @param pd
	 * @throws Exception
	 */
	public void editPush(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**作者软文列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> datalistPageByMember(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**列表(指定标识)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listByIds(PageData pd)throws Exception;
	
	/**列表(指定作者名称)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listByAuthor(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByViewId(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**
	 * 添加阅读数量 增加用户收益或者作者收益
	 * @param user 用户信息
	 * @param aRTICLEID 文章id
	 * @param shareId 分享者的id
	 */
	public void addReadNumAndMoney(UserInfo user, String aRTICLEID, String shareId)throws Exception;
	/**
	 * 添加点赞的数量 和增加作者点赞的收益
	 * @param user 
	 * @param aRTICLEID 文章id 32位
	 */
	public void addLikeNumAndMoney(UserInfo user, String aRTICLEID)throws Exception;
	
	/**通过主键id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByFirstId(PageData pd)throws Exception;

	/**
	 * 作者保存软文
	 * @param pd
	 */
	public void authorSave(PageData pd)throws Exception;
	
}

