package com.fh.service.system.articlebase;

import java.util.List;
import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 文章打赏接口
 * 创建人：Lukas 18923798379
 * 创建时间：2016-09-02
 * @version
 */
public interface ArticlebaseManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
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
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	/**
	 * 根据订单号查询未处理的打赏订单
	 * @param txno
	 * @return
	 */
	public PageData findByBaskNumber(String txno)throws Exception;
	/**
	 * 处理打赏订单和修改作者金额,插入收益记录的service
	 * @param pd
	 */
	public void edithandBusiness(PageData pd)throws Exception;
	
}

