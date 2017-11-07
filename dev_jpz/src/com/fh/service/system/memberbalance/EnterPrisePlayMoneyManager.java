package com.fh.service.system.memberbalance;

import java.util.Map;

import com.fh.util.PageData;

/**
 * 用户打款接口
 * @author zzx
 *	
 */
public interface EnterPrisePlayMoneyManager {
	/**
	 * 
	 * @param memPd 查询出来的结算记录
	 * @param pd 页面传过来的参数
	 * @throws Exception
	 */
	public void EditAndPlayMoney(PageData memPd,PageData pd,String path)throws Exception;
	
}
