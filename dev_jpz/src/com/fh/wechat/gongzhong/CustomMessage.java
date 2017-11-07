package com.fh.wechat.gongzhong;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import com.fh.util.JSONUtils;
import com.fh.wechat.gongzhong.utils.GongZhongUtils;
import com.fh.wechat.gongzhong.vo.message.Article;
import com.fh.wechat.gongzhong.vo.message.custom.CustomArticletMessage;
import com.fh.wechat.gongzhong.vo.message.custom.CustomImageMessage;
import com.fh.wechat.gongzhong.vo.message.custom.CustomMusicMessage;
import com.fh.wechat.gongzhong.vo.message.custom.CustomTextMessage;
import com.fh.wechat.gongzhong.vo.message.custom.CustomVideoMessage;
import com.fh.wechat.gongzhong.vo.message.custom.CustomVoiceMessage;

import net.sf.json.JSONObject;


/**
 * @describe 客服发送消息
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class CustomMessage extends GongZhongObject {
	private static final String CUSTOM_SEND = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

	public static void sendTextMessage(CustomTextMessage message)
			throws Exception {
		String jsonStr = "{\"touser\":\"" + message.getToUser()
				+ "\",\"msgtype\":\"text\",\"text\":{\"content\":\""
				+ message.getContent() + "\"}}";
		GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}

	public static void sendImageMessage(CustomImageMessage message) throws Exception {
		String jsonStr = "{\"touser\":\"" + message.getToUser()
				+ "\",\"msgtype\":\"image\",\"image\":{\"media_id\":\""
				+ message.getMediaId() + "\"}}";
		GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}

	public static void sendImageMessage(String toUser, String imageFilePath)
			throws Exception {
		CustomImageMessage message = new CustomImageMessage();
		message.setToUser(toUser);

		JSONObject obj = JSONObject.fromObject(GongZhongService.uploadMedia(
				"image", imageFilePath));
		message.setMediaId(JSONUtils.getString(obj, "thumb_media_id"));
		sendImageMessage(message);
	}

	public static void sendVoiceMessage(CustomVoiceMessage message) throws Exception {
		String jsonStr = "{\"touser\":\"" + message.getToUser()
				+ "\",\"msgtype\":\"voice\",\"voice\":{\"media_id\":\""
				+ message.getMediaId() + "\"}}";
		GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}

	public static void sendVoiceMessageAndUploadFile(String toUser,
			String voiceFilePath) throws Exception {
		CustomVoiceMessage message = new CustomVoiceMessage();
		message.setToUser(toUser);

		JSONObject obj = JSONObject.fromObject(GongZhongService.uploadMedia(
				"voice", voiceFilePath));
		message.setMediaId(JSONUtils.getString(obj, "thumb_media_id"));
		sendVoiceMessage(message);
	}

	public static void sendVideoMessage(CustomVideoMessage message) throws Exception {
		String jsonStr = "{\"touser\":\"" + message.getToUser()
				+ "\",\"msgtype\":\"video\",\"video\":{\"media_id\":\""
				+ message.getMediaId() + "\",\"title\":\"" + message.getTitle()
				+ "\",\"description\":\"" + message.getDescription() + "\"}}";
		GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}

	public static void sendVideoMessage(CustomVideoMessage message,
			String videoFilePath) throws Exception {
		JSONObject obj = JSONObject.fromObject(GongZhongService.uploadMedia(
				"video", videoFilePath));
		message.setMediaId(JSONUtils.getString(obj, "thumb_media_id"));

		sendVideoMessage(message);
	}

	public static void sendMusicMessage(CustomMusicMessage message) throws Exception {
		String jsonStr = "{\"touser\":\"" + message.getToUser()
				+ "\",\"msgtype\":\"music\"," + "\"music\":" + "{\"title\":\""
				+ message.getTitle() + "\"," + "\"description\":\""
				+ message.getDescription() + "\"," + "\"musicurl\":\""
				+ message.getMusicUrl() + "\"," + "\"hqmusicurl\":\""
				+ message.gethQMusicUrl() + "\",\"thumb_media_id\":\""
				+ message.getThumbMediaId() + "\"}}";
		GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}

	public static void sendMusicMessage(CustomMusicMessage message,
			String thumbMediaFilePath) throws Exception {
		JSONObject obj = JSONObject.fromObject(GongZhongService.uploadMedia(
				"thumb", thumbMediaFilePath));
		message.setThumbMediaId(JSONUtils.getString(obj, "thumb_media_id"));
		sendMusicMessage(message);
	}

	public static void sendArticletMessage(CustomArticletMessage message)
			throws Exception {
		if ((message.getArticles() == null)
				|| (message.getArticles().size() == 0)
				|| (message.getArticles().size() > 10)) {
			throw new RuntimeException("Articles为空或超过限制");
		}

		String item = "'{\"title\":'\"{0}\"',\"description\":'\"{1}\"',\"url\":'\"{2}\"',\"picurl\":'\"{3}\"'}',";
		StringBuffer it = new StringBuffer();

		for (Iterator localIterator = message.getArticles().iterator(); localIterator
				.hasNext();) {
			Article article = (Article) localIterator.next();
			it.append(MessageFormat.format(
					item,
					new Object[] { article.getTitle(),
							article.getDescription(), article.getUrl(),
							article.getPicUrl() }));
		}

		String jsonStr = "{\"touser\":\"" + message.getToUser()
				+ "\",\"msgtype\":\"news\",\"news\":{\"articles\": ["
				+ it.toString().substring(0, it.toString().length() - 1)
				+ "]}}";
		GongZhongUtils.sendPost(
				"https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
						+ GongZhongService.getAccessToken(), jsonStr);
	}
}
