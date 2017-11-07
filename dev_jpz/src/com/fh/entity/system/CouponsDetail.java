package com.fh.entity.system;

import java.util.Date;

import com.fh.entity.Page;

/**
 * 
* 类名称：优惠券
* 类描述： 
* @author lukas 414024003@qq.com
* 作者单位： 
* 联系方式：
* 创建时间：2014年6月28日
* @version 1.0
 */
public class CouponsDetail {
	private String couponsId;		
	public String getCouponsId() {
		return couponsId;
	}
	public void setCouponsId(String couponsId) {
		this.couponsId = couponsId;
	}
	public String getNumbers() {
		return numbers;
	}
	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	public double getDenomination() {
		return denomination;
	}
	public void setDenomination(double denomination) {
		this.denomination = denomination;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public int getStateId() {
		return stateId;
	}
	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	private String numbers;	
	private double denomination; 	
	private String qrCode;		
	private int stateId;		
	private Date createDate;		
	private Date endDate;	
	
	

}
