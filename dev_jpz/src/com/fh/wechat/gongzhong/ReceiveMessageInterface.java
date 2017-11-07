package com.fh.wechat.gongzhong;

import com.fh.wechat.gongzhong.vo.message.receive.ReceiveClickMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveGroupMessageNotice;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveImageMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveLinkMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveLocationMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveSubscribeMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveTemplateMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveTextMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveVideoMessage;
import com.fh.wechat.gongzhong.vo.message.receive.ReceiveVoiceMessage;

/**
 * @describe 接收当前公众号消息接口
 * @author lukas Lukas 414024003@qq.com
 * @date 2016年8月13日 10:56:14
 * @version 1.0
 */
public abstract interface ReceiveMessageInterface
{
  /**
   * 接收文本消息
   * @param paramReceiveTextMessage 文本消息对象
   * @return 
   */
  public abstract String receiveTextMessage(ReceiveTextMessage paramReceiveTextMessage);
  
  /**
   * 接收图片消息
   * @param paramReceiveImageMessage 图片消息对象
   * @return
   */
  public abstract String receiveImageMessage(ReceiveImageMessage paramReceiveImageMessage);

  /**
   * 接收语音消息
   * @param paramReceiveVoiceMessage 语音消息对象
   * @return
   */
  public abstract String receiveVoiceMessage(ReceiveVoiceMessage paramReceiveVoiceMessage);

  /**
   * 接收视频消息
   * @param paramReceiveVideoMessage 视频消息对象
   * @return
   */
  public abstract String receiveVideoMessage(ReceiveVideoMessage paramReceiveVideoMessage);

  /**
   * 接收地理位置消息
   * @param paramReceiveLocationMessage 地理位置消息对象
   * @return
   */
  public abstract String receiveLocationMessage(ReceiveLocationMessage paramReceiveLocationMessage);

  /**
   * 接收地址消息
   * @param paramReceiveLinkMessage 地址消息对象
   * @return
   */
  public abstract String receiveLinkMessage(ReceiveLinkMessage paramReceiveLinkMessage);

  /**
   * 接收模版消息
   * @param paramReceiveTemplateMessage 模版消息对象
   * @return
   */
  public abstract String receiveTemplateSendJobFinishMessag(ReceiveTemplateMessage paramReceiveTemplateMessage);

  /**
   * 接收关注消息
   * @param paramReceiveSubscribeMessage 关注消息对象
   * @return
   */
  public abstract String eventSubscribeMessage(ReceiveSubscribeMessage paramReceiveSubscribeMessage);

  /**
   * 接收取消关注消息
   * @param paramReceiveSubscribeMessage 关注消息对象
   * @return
   */
  public abstract String eventUnSubscribeMessage(ReceiveSubscribeMessage paramReceiveSubscribeMessage);

  /**
   * 接收菜单消息
   * @param paramReceiveClickMessage 菜单消息对象
   * @return
   */
  public abstract String eventClickMessage(ReceiveClickMessage paramReceiveClickMessage);

  /**
   * 接收群发推送结果消息 
   * @param paramReceiveGroupMessageNotice 群发推送结果消息对象
   * @return
   */
  public abstract String eventGroupMessageNotice(ReceiveGroupMessageNotice paramReceiveGroupMessageNotice);
}