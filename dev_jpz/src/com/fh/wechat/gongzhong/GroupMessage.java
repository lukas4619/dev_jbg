package com.fh.wechat.gongzhong;


import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.fh.util.JSONUtils;
import com.fh.wechat.gongzhong.utils.GongZhongUtils;
import com.fh.wechat.gongzhong.vo.message.group.MessageMateria;

/**
 * @describe 公众号发送微信用户消息
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class GroupMessage extends GongZhongObject {
	private static final String SENDALL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=";
	private static final String SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";
	private static final String DELETE = "https://api.weixin.qq.com/cgi-bin/message/delete?access_token=";
	private static final String UPLOADNEWS = "http://file.api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=";
	private static final String UPLOADVIDEO = "http://file.api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=";

	/**
	 * 上传多图文
	 * @param messageMaterias 图文集合
	 * @return
	 * @throws Exception
	 */
	public static String uploadNews(List<MessageMateria> messageMaterias)
			throws Exception {
		JSONObject obj = null;

		for (Iterator localIterator = messageMaterias.iterator(); localIterator
				.hasNext();) {
			MessageMateria messageMateria = (MessageMateria) localIterator
					.next();
			if (!(StringUtils.isNotBlank(messageMateria.thumbMediaFilePath)))
				break;
			obj = JSONObject.fromObject(GongZhongService.uploadMedia("thumb",
					messageMateria.thumbMediaFilePath));
			messageMateria.setThumb_media_id(JSONUtils.getString(obj,
					"thumb_media_id"));
		}

		obj = new JSONObject();
		obj.put("articles", messageMaterias);

		String reslut = GongZhongUtils.sendPost(
				"http://file.api.weixin.qq.com/cgi-bin/media/uploadnews?access_token="
						+ GongZhongService.getAccessToken(), obj.toString());
		System.out.println(reslut);

		return reslut;
	}

	/**
	 * 上传视频
	 * @param title 标题
	 * @param description 描述
	 * @param mediaId 标识
	 * @return
	 * @throws Exception
	 */
	public static String uploadVideo(String title, String description,
			String mediaId) throws Exception {
		String jsonStr = "{\"media_id\": \"" + mediaId + "\",\"title\": \""
				+ title + "\",\"description\": \"" + description + "\"}";
		String reslut = GongZhongUtils.sendPost(
				"http://file.api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);

		return reslut;
	}

	
	/**
	 * 按微信用户群组发送文本消息
	 * @param groupId
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String sendTextMessage(String groupId, String content)
			throws Exception {
		String jsonStr = "{\"filter\":{\"group_id\":\"" + groupId
				+ "\"},\"text\":{\"content\":\"" + content
				+ "\"},\"msgtype\":\"text\"}";

		return GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}
	
	/**
	 * 按微信用户OPENID发送文本消息
	 * @param openIds 微信用户OPENID数组
	 * @param content 文本内容 
	 * @return
	 * @throws Exception
	 */
	public static String sendTextMessage(String[] openIds, String content)
			throws Exception {
		String jsonStr = "{\"touser\":"
				+ JSONArray.fromObject(openIds).toString()
				+ ",\"msgtype\":\"text\",\"text\":{\"content\":\"" + content
				+ "\"}}";
		return GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}

	/**
	 * 按微信用户群组标识发送多图文消息
	 * @param groupId 微信用户群组标识
	 * @param mediaId 多图文标识
	 * @return
	 * @throws Exception
	 */
	public static String sendNewsMessage(String groupId, String mediaId)
			throws Exception {
		String jsonStr = "{\"filter\":{\"group_id\":\"" + groupId
				+ "\"},\"mpnews\":{\"media_id\":\"" + mediaId
				+ "\"},\"msgtype\":\"mpnews\"}";
		return GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}
	
	/**
	 * 按微信用户标识发送多图文消息
	 * @param openIds 微信用户OPENID数组
	 * @param mediaId 多图文标识
	 * @return
	 * @throws Exception
	 */
	public static String sendNewsMessage(String[] openIds, String mediaId)
			throws Exception {
		String jsonStr = "{\"touser\":"
				+ JSONArray.fromObject(openIds).toString()
				+ ",\"mpnews\":{\"media_id\":\"" + mediaId
				+ "\"},\"msgtype\":\"mpnews\"}";
		return GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}
	
	
	/**
	 * 发送图文预览接口
	 * @param openId 预览用户OPENID
	 * @param mediaId 图文标识
	 * @return
	 * @throws Exception
	 */
	public static String sendNewsPreViewMessage(String openId, String mediaId)
			throws Exception {
		String jsonStr = "{\"touser\":"
				+ openId
				+ ",\"mpnews\":{\"media_id\":\"" + mediaId
				+ "\"},\"msgtype\":\"mpnews\"}";
		return GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
		
	}

	/**
	 * 按微信用户群组标识发送多语音消息
	 * @param groupId 微信用户群组标识
	 * @param mediaId 语音标识
	 * @return
	 * @throws Exception
	 */
	public static String sendVoiceMessage(String groupId, String mediaId)
			throws Exception {
		String jsonStr = "{\"filter\":{\"group_id\":\"" + groupId
				+ "\"},\"voice\":{\"media_id\":\"" + mediaId
				+ "\"},\"msgtype\":\"voice\"}";
		return GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}

	/**
	 * 按微信用户标识发送多语音消息
	 * @param openIds 微信用户OPENID数组
	 * @param mediaId 语音标识
	 * @return
	 * @throws Exception
	 */
	public static String sendVoiceMessage(String[] openIds, String mediaId)
			throws Exception {
		String jsonStr = "{\"touser\":"
				+ JSONArray.fromObject(openIds).toString()
				+ ",\"voice\":{\"media_id\":\"" + mediaId
				+ "\"},\"msgtype\":\"voice\"}";
		return GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}

	/**
	 * 按微信用户群组标识发送多图片消息
	 * @param groupId 微信用户群组标识
	 * @param mediaId 图片标识
	 * @return
	 * @throws Exception
	 */
	public static String sendImageMessage(String groupId, String mediaId)
			throws Exception {
		String jsonStr = "{\"filter\":{\"group_id\":\"" + groupId
				+ "\"},\"mpvideo\":{\"media_id\":\"" + mediaId
				+ "\"},\"msgtype\":\"mpvideo\"}";
		return GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}

	/**
	 * 按微信用户标识发送多图片消息
	 * @param openIds 微信用户OPENID数组
	 * @param mediaId 图片标识
	 * @return
	 * @throws Exception
	 */
	public static String sendImageMessage(String[] openIds, String title,
			String description, String mediaId) throws Exception {
		String jsonStr = "{\"touser\":"
				+ JSONArray.fromObject(openIds).toString()
				+ ",\"image\":{\"media_id\":\"" + mediaId
				+ "\"},\"msgtype\":\"image\"}";
		return GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}

	
	/**
	 * 按微信用户群组标识发送多视频消息
	 * @param groupId 微信用户群组标识
	 * @param mediaId 视频标识
	 * @return
	 * @throws Exception
	 */
	public static String sendVideoMessage(String groupId, String mediaId)
			throws Exception {
		String jsonStr = "{\"filter\":{\"group_id\":\"" + groupId
				+ "\"},\"image\":{\"media_id\":\"" + mediaId
				+ "\"},\"msgtype\":\"image\"}";
		return GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}

	
	/**
	 * 按微信用户标识发送多视频消息
	 * @param openIds 微信用户OPENID数组
	 * @param mediaId 视频标识
	 * @return
	 * @throws Exception
	 */
	public static String sendVideoMessage(String[] openIds, String title,
			String description, String mediaId) throws Exception {
		String jsonStr = "{\"touser\":"
				+ JSONArray.fromObject(openIds).toString()
				+ ",\"video\":{\"media_id\":\"" + mediaId + "\",\"title\":\""
				+ title + "\",\"description\":\"" + description
				+ "\"},\"msgtype\":\"video\"}";
		return GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}
     
	/**
	 * 删除消息记录
	 * @param msgId 消息记录标识
	 * @throws Exception
	 */
	public static void deleteMessage(long msgId) throws Exception {
		GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/delete?access_token="
						+ GongZhongService.getAccessToken(), "{\"msgid\":"
						+ msgId + "}");
	}
}