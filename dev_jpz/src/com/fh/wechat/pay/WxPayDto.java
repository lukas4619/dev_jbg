package com.fh.wechat.pay;


public class WxPayDto {

	private String orderId;//订单�?
	private String totalFee;//金额
	private String spbillCreateIp;//订单生成的机�?IP
	private String notifyUrl;//这里notify_url�?支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状�?�?
	private String body;// 商品描述根据情况修改
	private String openId;//微信用户对一个公众号唯一
	private String payType;//payType=1 �?��订单�?payType=2 多个订单
	
	/**
	 * @return the orderId
	 */
	public String getPayType() {
		return payType;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the totalFee
	 */
	public String getTotalFee() {
		return totalFee;
	}
	/**
	 * @param totalFee the totalFee to set
	 */
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	/**
	 * @return the spbillCreateIp
	 */
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	/**
	 * @param spbillCreateIp the spbillCreateIp to set
	 */
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	/**
	 * @return the notifyUrl
	 */
	public String getNotifyUrl() {
		return notifyUrl;
	}
	/**
	 * @param notifyUrl the notifyUrl to set
	 */
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the openId
	 */
	public String getOpenId() {
		return openId;
	}
	/**
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
}
