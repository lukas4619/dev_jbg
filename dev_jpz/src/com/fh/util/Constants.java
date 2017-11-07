package com.fh.util;

/** 
 * 说明：
 * 创建人：lukas 414024003@qq.com
 * 修改时间：2015年11月24日
 * @version
 */
public class Constants {
	
	public static String PICTURE_VISIT_FILE_PATH = "";//图片访问的路径
	public static String PICTURE_SAVE_FILE_PATH = "";//图片存放的路径
	/**
	 * 预订模版消息ID
	 */
	public static String PRODUCT_RESERVE_TEMPLATE_ID="M-uHzPg4b1aTKcvFZbYbVTwZW_6MntdXFSGeH1CRTew";
	
	/**
	 * 预订模版消息顶部颜色
	 */
	public static String PRODUCT_RESERVE_TOPCOLOR="#08A600";
	
	
	/**
	 * 支付成功模版消息ID
	 */
	public static String PAY_TEMPLATE_ID="UvDlyG-ZrcHxpvRkthfF1igos7rhwK1BAIq91g3d5WQ";
	
	
	/**
	 * 获得优惠券模版消息ID
	 */
	public static String PRODUCT_COUPON_TEMPLATE_ID="R8kj65ngLp0rVWw3dDjGNnk5gzj2OrDBa6KN8DzbGYg";
	
	/**
	 * 获得软文进度模版消息ID
	 */
	public static String PRODUCT_ARTICLE_TEMPLATE_ID="-1jlvAVTQR0Q6-eW_0ysRpdTuINO6mL8Pgof8-yljW4";
	
	
	public static String getPICTURE_VISIT_FILE_PATH() {
		return PICTURE_VISIT_FILE_PATH;
	}

	public static void setPICTURE_VISIT_FILE_PATH(String pICTURE_VISIT_FILE_PATH) {
		PICTURE_VISIT_FILE_PATH = pICTURE_VISIT_FILE_PATH;
	}

	public static String getPICTURE_SAVE_FILE_PATH() {
		return PICTURE_SAVE_FILE_PATH;
	}

	public static void setPICTURE_SAVE_FILE_PATH(String pICTURE_SAVE_FILE_PATH) {
		PICTURE_SAVE_FILE_PATH = pICTURE_SAVE_FILE_PATH;
	}

	public static void main(String[] args) {
		Constants.setPICTURE_SAVE_FILE_PATH("D:/Tomcat 6.0/webapps/FH/topic/");
		Constants.setPICTURE_VISIT_FILE_PATH("http://192.168.1.225:8888/FH/topic/");
	}
	
}
