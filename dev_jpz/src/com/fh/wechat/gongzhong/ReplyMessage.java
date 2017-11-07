package com.fh.wechat.gongzhong;


import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.fh.util.JSONUtils;
import com.fh.wechat.gongzhong.vo.message.Article;
import com.fh.wechat.gongzhong.vo.message.reply.ReplyArticlesMessage;
import com.fh.wechat.gongzhong.vo.message.reply.ReplyImageMessage;
import com.fh.wechat.gongzhong.vo.message.reply.ReplyMusicMessage;
import com.fh.wechat.gongzhong.vo.message.reply.ReplyTextMessage;
import com.fh.wechat.gongzhong.vo.message.reply.ReplyVideoMessage;
import com.fh.wechat.gongzhong.vo.message.reply.ReplyVoiceMessage;

/**
 * @describe 回复当前公众号消息
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public class ReplyMessage extends GongZhongObject
{
	   
	/**
	 * 回复文本消息
	 * @param response
	 * @param message 文本消息对象
	 * @throws IOException
	 */
	  public static void replyTextMessage(HttpServletResponse response, ReplyTextMessage message)
	    throws IOException
	  {
		 
	    String xml = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2,number,#}</CreateTime><MsgType><![CDATA[{3}]]></MsgType><Content><![CDATA[{4}]]></Content></xml>";

	    xml = MessageFormat.format(xml, new Object[] { message.getToUserName(), 
	      message.getFromUserName(), Long.valueOf(message.getCreateTime()), 
	      "text", message.getContent() });

	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print(xml);
	  }
   
	  /**
	   * 回复图片消息
	   * @param response
	   * @param message 图片消息对象
	   * @throws IOException
	   */
	  public static void replyImageMessage(HttpServletResponse response, ReplyImageMessage message)
	    throws IOException
	  {
	    String xml = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2,number,#}</CreateTime><MsgType><![CDATA[{3}]]></MsgType><Image><MediaId><![CDATA[{4}]]></MediaId></Image></xml>";

	    xml = MessageFormat.format(xml, new Object[] { message.getToUserName(), 
	      message.getFromUserName(), Long.valueOf(message.getCreateTime()), 
	      "image", message.getMediaId() });

	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print(xml);
	  }

	  /**
	   * 回复图片消息
	   * @param response
	   * @param message 图片消息对象
	   * @param imageFilePath 本地图片路径
	   * @throws Exception
	   */
	  public static void replyImageMessage(HttpServletResponse response, ReplyImageMessage message, String imageFilePath)
	    throws Exception
	  {
	    String xml = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2,number,#}</CreateTime><MsgType><![CDATA[{3}]]></MsgType><Image><MediaId><![CDATA[{4}]]></MediaId></Image></xml>";

	    JSONObject obj = JSONObject.fromObject(GongZhongService.uploadMedia(
	      "image", imageFilePath));
	    message.setMediaId(JSONUtils.getString(obj, "thumb_media_id"));

	    xml = MessageFormat.format(xml, new Object[] { message.getToUserName(), 
	      message.getFromUserName(), Long.valueOf(message.getCreateTime()), 
	      "image", message.getMediaId() });

	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print(xml);
	  }

	  /**
	   * 回复语音消息
	   * @param response
	   * @param message 语音消息对象
	   * @throws IOException
	   */
	  public static void replyReplyVoiceMessage(HttpServletResponse response, ReplyVoiceMessage message)
	    throws IOException
	  {
	    String xml = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2,number,#}</CreateTime><MsgType><![CDATA[{3}]]></MsgType><Voice><MediaId><![CDATA[{4}]]></MediaId></Voice></xml>";

	    xml = MessageFormat.format(xml, new Object[] { message.getToUserName(), 
	      message.getFromUserName(), Long.valueOf(message.getCreateTime()), 
	      "voice", message.getMediaId() });

	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print(xml);
	  }

	  /**
	   * 回复语音消息
	   * @param response
	   * @param message 语音消息对象
	   * @param voiceFilePath 语音本地保存路径
	   * @throws Exception
	   */
	  public static void replyReplyVoiceMessage(HttpServletResponse response, ReplyVoiceMessage message, String voiceFilePath)
	    throws Exception
	  {
	    String xml = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2,number,#}</CreateTime><MsgType><![CDATA[{3}]]></MsgType><Voice><MediaId><![CDATA[{4}]]></MediaId></Voice></xml>";

	    JSONObject obj = JSONObject.fromObject(GongZhongService.uploadMedia(
	      "voice", voiceFilePath));
	    message.setMediaId(JSONUtils.getString(obj, "thumb_media_id"));

	    xml = MessageFormat.format(xml, new Object[] { message.getToUserName(), 
	      message.getFromUserName(), Long.valueOf(message.getCreateTime()), 
	      "voice", message.getMediaId() });

	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print(xml);
	  }

	  /**
	   * 回复视频消息
	   * @param response
	   * @param message 视频消息对象
	   * @throws IOException 
	   */
	  public static void replyVideoMessage(HttpServletResponse response, ReplyVideoMessage message)
	    throws IOException
	  {
	    String xml = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2,number,#}</CreateTime><MsgType><![CDATA[{3}]]></MsgType><Video><MediaId><![CDATA[{4}]]></MediaId><Title><![CDATA[{5}]]></Title><Description><![CDATA[{6}]]></Description></Video></xml>";

	    xml = MessageFormat.format(xml, new Object[] { message.getToUserName(), 
	      message.getFromUserName(), Long.valueOf(message.getCreateTime()), 
	      "video", message.getMediaId(), message.getTitle(), 
	      message.getDescription() });

	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print(xml);
	  }
       
	  /**
	   * 回复视频消息
	   * @param response
	   * @param message 视频消息对象
	   * @param videoFilePath 视频本地保存路径
	   * @throws Exception
	   */
	  public static void replyVideoMessage(HttpServletResponse response, ReplyVideoMessage message, String videoFilePath)
	    throws Exception
	  {
	    String xml = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2,number,#}</CreateTime><MsgType><![CDATA[{3}]]></MsgType><Video><MediaId><![CDATA[{4}]]></MediaId><Title><![CDATA[{5}]]></Title><Description><![CDATA[{6}]]></Description></Video></xml>";

	    JSONObject obj = JSONObject.fromObject(GongZhongService.uploadMedia(
	      "video", videoFilePath));
	    message.setMediaId(JSONUtils.getString(obj, "thumb_media_id"));

	    xml = MessageFormat.format(xml, new Object[] { message.getToUserName(), 
	      message.getFromUserName(), Long.valueOf(message.getCreateTime()), 
	      "video", message.getMediaId(), message.getTitle(), 
	      message.getDescription() });

	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print(xml);
	  }

	  /**
	   * 回复音乐消息
	   * @param response
	   * @param message 音乐消息对象
	   * @throws IOException
	   */
	  public static void replyMusicMessage(HttpServletResponse response, ReplyMusicMessage message)
	    throws IOException
	  {
	    String xml = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2,number,#}</CreateTime><MsgType><![CDATA[{3}]]></MsgType><Music><Title><![CDATA[{4}]]></Title><Description><![CDATA[{5}]]></Description><MusicUrl><![CDATA[{6}]]></MusicUrl><HQMusicUrl><![CDATA[{7}]]></HQMusicUrl><ThumbMediaId><![CDATA[{8}]]></ThumbMediaId></Music></xml>";

	    xml = MessageFormat.format(xml, new Object[] { message.getToUserName(), 
	      message.getFromUserName(), Long.valueOf(message.getCreateTime()), 
	      "music", message.getTitle(), 
	      message.getDescription(), message.getMusicUrl(), 
	      message.getHQMusicUrl(), message.getThumbMediaId() });

	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print(xml);
	  }

	  /**
	   * 回复音乐消息
	   * @param response
	   * @param message 回复音乐消息对象
	   * @param thumbMediaFilePath 音乐本地保存路径
	   * @throws Exception
	   */
	  public static void replyMusicMessage(HttpServletResponse response, ReplyMusicMessage message, String thumbMediaFilePath)
	    throws Exception
	  {
	    String xml = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2,number,#}</CreateTime><MsgType><![CDATA[{3}]]></MsgType><Music><Title><![CDATA[{4}]]></Title><Description><![CDATA[{5}]]></Description><MusicUrl><![CDATA[{6}]]></MusicUrl><HQMusicUrl><![CDATA[{7}]]></HQMusicUrl><ThumbMediaId><![CDATA[{8}]]></ThumbMediaId></Music></xml>";

	    JSONObject obj = JSONObject.fromObject(GongZhongService.uploadMedia(
	      "thumb", thumbMediaFilePath));
	    message.setMediaId(JSONUtils.getString(obj, "thumb_media_id"));

	    xml = MessageFormat.format(xml, new Object[] { message.getToUserName(), 
	      message.getFromUserName(), Long.valueOf(message.getCreateTime()), 
	      "music", message.getTitle(), 
	      message.getDescription(), message.getMusicUrl(), 
	      message.getHQMusicUrl(), message.getThumbMediaId() });

	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print(xml);
	  }
       
	  /**
	   * 回复图文消息
	   * @param response
	   * @param message 图文消息对象
	   * @throws IOException
	   */
	  public static void replyArticleMessage(HttpServletResponse response, ReplyArticlesMessage message)
	    throws IOException
	  {
	    if ((message.getArticles() == null) || (message.getArticles().size() == 0) || 
	      (message.getArticles().size() > 10)) {
	      throw new RuntimeException("Articles为空或超过限制");
	    }

	    String xml = "<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{1}]]></FromUserName><CreateTime>{2,number,#}</CreateTime><MsgType><![CDATA[{3}]]></MsgType><ArticleCount>{4,number,#}</ArticleCount><Articles>{5}</Articles></xml>";

	    StringBuffer item = new StringBuffer();
	    for (Iterator localIterator = message.getArticles().iterator(); localIterator.hasNext(); ) { Article article = (Article)localIterator.next();
	      item.append(
	        MessageFormat.format("<item><Title><![CDATA[{0}]]></Title><Description><![CDATA[{1}]]></Description><PicUrl><![CDATA[{2}]]></PicUrl><Url><![CDATA[{3}]]></Url></item>", new Object[] { 
	        article.getTitle(), article.getDescription(), 
	        article.getPicUrl(), article.getUrl() }));
	    }

	    xml = MessageFormat.format(xml, new Object[] { message.getToUserName(), 
	      message.getFromUserName(), Long.valueOf(message.getCreateTime()), 
	      "news", Integer.valueOf(message.getArticleCount()), 
	      item.toString() });

	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().print(xml);
	  }
	}
