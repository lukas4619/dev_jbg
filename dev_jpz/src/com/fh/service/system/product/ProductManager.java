package com.fh.service.system.product;

import java.util.List;
import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 产品模块接口
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-17
 * @version
 */
public interface ProductManager{

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
	
	/**修改预订次数
	 * @param pd
	 * @throws Exception
	 */
	public void editByProReservationNum(PageData pd)throws Exception;
	
	/**修改预订成功消费次数
	 * @param pd
	 * @throws Exception
	 */
	public void editByProConsumeNum(PageData pd)throws Exception;
	
	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void editByState(PageData pd)throws Exception;
	
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
	
	/**列表(指定标识)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listByIds(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByViweId(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

