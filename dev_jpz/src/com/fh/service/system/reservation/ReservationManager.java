package com.fh.service.system.reservation;

import java.util.List;
import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 预订记录接口
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-17
 * @version
 */
public interface ReservationManager{

	/**新增
	 * @param yd 预订数据
	 * @param pd 修改产品预订次数
	 * @throws Exception
	 */
	public void save(PageData yd,PageData pd)throws Exception;
	
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
	
	/**修改预订付款状态
	 * @param pd
	 * @throws Exception
	 */
	public void editPayState(PageData pd,PageData p)throws Exception;
	
	/**修改预订状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd,PageData p)throws Exception;
	
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
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByIdNumber(PageData pd)throws Exception;
	
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
	 * 修改预付状态 并且增加作者收益和分享者的收益
	 * @param pd
	 * @param p
	 */
	public void editPayStateAndAddMoney(PageData pd, PageData p)throws Exception;
	
}

