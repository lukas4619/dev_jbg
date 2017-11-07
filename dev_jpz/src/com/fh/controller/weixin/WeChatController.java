package com.fh.controller.weixin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fh.controller.base.BaseController;
import com.fh.service.information.images.ImagesManager;
import com.fh.service.system.article.ArticleManager;
import com.fh.service.system.coupons.CouponsManager;
import com.fh.service.system.createqr.CreateQrManager;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.product.ProductManager;
import com.fh.service.system.reservation.ReservationManager;
import com.fh.service.system.responselog.ResponseLogManager;
import com.fh.service.system.t_menu.T_menuManager;
import com.fh.util.Constants;
import com.fh.util.Convert;
import com.fh.util.DateUtil;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;
import com.fh.util.TwoDimensionCode;
import com.fh.util.cache.Cache;
import com.fh.util.cache.CacheManager;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.gongzhong.ReceiveMessageInterface;
import com.fh.wechat.gongzhong.ReplyMessage;
import com.fh.wechat.gongzhong.TemplateMessage;
import com.fh.wechat.gongzhong.UserManage;
import com.fh.wechat.gongzhong.vo.message.Article;
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
import com.fh.wechat.gongzhong.vo.message.reply.ReplyArticlesMessage;
import com.fh.wechat.gongzhong.vo.message.reply.ReplyTextMessage;
import com.fh.wechat.gongzhong.vo.message.template.Template;
import com.fh.wechat.gongzhong.vo.user.UserInfo;

/**
 * 
 * 类名称：WeixinController.java 类描述： 微信公共平台开发
 * 
 * @author lukas 414024003@qq.com 作者单位： 联系方式： 创建时间：2014年7月10日
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/wechat")
public class WeChatController extends BaseController implements
		ReceiveMessageInterface {

	HttpServletResponse response = null;

	/**
	 * 监听微信接口
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public String weChat(HttpServletRequest request, HttpServletResponse r)
			throws Exception {
		response = r;
		System.out.println(GongZhongService.appId + "\n"+ GongZhongService.appSecret + "\n" + GongZhongService.token);
		return GongZhongService.execute(request, response, this);
	}

	@Override
	public String receiveTextMessage(ReceiveTextMessage paramReceiveTextMessage) {
		logger.info("---进入文本消息");
		logger.info("---进入文本消息,逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复");
		/**
		 * 逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复
		 */
		String toUserName = paramReceiveTextMessage.getFromUserName();
		String fromUserName = paramReceiveTextMessage.getToUserName();
		String keywords=paramReceiveTextMessage.getContent();
		String content ="";
		String articleIds="";//图文标识集合
		if (Tools.isEmpty(toUserName) || Tools.isEmpty(fromUserName) || Tools.isEmpty(keywords)) {
			return null;
		}
//		try {
//			PageData pd = new PageData();
//			//1.处理自动回复
//			pd.put("responseType", 23);//接收消息类型具体请查询t_types表中的微信消息回复类型
//			pd.put("isValid", 1);//自动回复状态是否开启
//			pd = responselogService.findByWhre(pd);//查询自动回复数据
//			//判断自动回复是否设置并且回复内容不能为空
//			if(pd!=null && pd.get("CONTENT")!=null){
//				content = pd.get("CONTENT")+"";
//			}else{
//				//TODO 2.查询作者软文指查询审核通过最近的8条记录
//				List<PageData> list = new ArrayList<PageData>();
//				pd = new PageData();
//				pd.put("author", keywords);
//				pd.put("articleStateID", 19);
//				pd.put("articleTypeID", -1);
//				list=articleService.listByAuthor(pd);
//				if(list!=null && list.size()>0){
//					String ids ="";
//					for (int i = 0; i < list.size(); i++) {
//						if(list.get(i).get("id")!=null){
//							if(Tools.isEmpty(ids)){
//								ids+=list.get(i).get("id").toString();
//							}else{
//								ids+=","+list.get(i).get("id").toString();
//							}
//						}
//					}
//					if(!Tools.isEmpty(ids)){
//						pd.put("ARTICLEIDS", ids);
//						pd.put("SENDTYPE", 2);
//					}
//				}else{
//					//3.处理关键字回复
//					pd = new PageData();
//					pd.put("responseType", 24);//接收消息类型具体请查询t_types表中的微信消息回复类型
//					pd.put("keywords", keywords);//关键字
//					pd.put("isValid", 1);//关键字状态是否开启
//					pd = responselogService.findByWhre(pd);//查询关键字回复数据
//					//判断是否有关键字记录并且回复内容不能为空
//					if(pd!=null && pd.get("CONTENT")!=null){
//						content = pd.get("CONTENT")+"";
//					}else{
//						//4.指定消息类型回复
//						pd = new PageData();
//						pd.put("responseType", 15);//接收消息类型
//						pd.put("isValid", 1);//接收消息类型状态是否开启
//						pd = responselogService.findByWhre(pd);//查询接收文本消息回复数据
//						if (pd == null || pd.get("CONTENT")==null) {
//							return null;
//						}
//						content = pd.get("CONTENT")+"";
//					}
//				}
//			}
//			if(pd.get("ARTICLEIDS")!=null){
//				articleIds = pd.getString("ARTICLEIDS");
//			}
//			int sendType = Convert.strToInt(pd.get("SENDTYPE")+"", -1);// 回复消息类型1.文本
//			if (sendType == 1) {
//				// 进行回复文本
//				logger.info("---文本消息开始进行回复文本");
//				sendTextMsg(toUserName, fromUserName, content);
//				logger.info("---文本消息结束回复文本");
//				return null;
//			} else if (sendType == 2 &&  !Tools.isEmpty(articleIds)) {
//				// 进入回复图文
//				logger.info("---文本消息开始进行回复图文");
//				sendArticleMessage(articleIds, toUserName, fromUserName);
//				logger.info("---文本消息结束回复图文");
//				return null;
//			} else {
//				//未找到对应内容回复
//				logger.info("---文本消息开始进行回复文本");
//				sendTextMsg(toUserName, fromUserName, "未能找到您输入的关键字：["+keywords+"]相关内容，您可以直接输入作者名称检索对应发布的软文!例如：发送[测试]。");
//				logger.info("---未设置回复消息开始结束");
//				return null;
//			}
//		} catch (IOException e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public String receiveImageMessage(
			ReceiveImageMessage paramReceiveImageMessage) {
		logger.info("---进入图片消息");
		logger.info("---进入图片消息,逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复");
		/**
		 * 逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复
		 */
		String toUserName = paramReceiveImageMessage.getFromUserName();
		String fromUserName = paramReceiveImageMessage.getToUserName();
		String content ="";
		String articleIds="";//图文标识集合
		if (Tools.isEmpty(toUserName) || Tools.isEmpty(fromUserName)) {
			return null;
		}
//		try {
//			PageData pd = new PageData();
//			//1.处理自动回复
//			pd.put("responseType", 23);//接收消息类型具体请查询t_types表中的微信消息回复类型
//			pd.put("isValid", 1);//自动回复状态是否开启
//			pd = responselogService.findByWhre(pd);//查询自动回复数据
//			//判断自动回复是否设置并且回复内容不能为空
//			if(pd!=null && pd.get("CONTENT")!=null){
//				content = pd.get("CONTENT")+"";
//			}else{
//				//2.指定消息类型回复
//				pd = new PageData();
//				pd.put("responseType", 16);//接收消息类型
//				pd.put("isValid", 1);//接收消息类型状态是否开启
//				pd = responselogService.findByWhre(pd);//查询接收文本消息回复数据
//				if (pd == null || pd.get("CONTENT")==null) {
//					return null;
//				}
//				content = pd.get("CONTENT")+"";
//			}
//			if(pd.get("ARTICLEIDS")!=null){
//				articleIds = pd.getString("ARTICLEIDS");
//			}
//			int sendType = Convert.strToInt(pd.get("SENDTYPE")+"", -1);// 回复消息类型1.文本
//			if (sendType == 1) {
//				// 进行回复文本
//				logger.info("---图片消息开始进行回复文本");
//				sendTextMsg(toUserName, fromUserName, content);
//				logger.info("---图片消息结束回复文本");
//				return null;
//			} else if (sendType == 2 &&  !Tools.isEmpty(articleIds)) {
//				// 进入回复图文
//				logger.info("---图片消息开始回复图文");
//				sendArticleMessage(articleIds, toUserName, fromUserName);
//				logger.info("---图片消息结束回复图文");
//				return null;
//			} else {
//				logger.info("---未设置回复消息");
//				return null;
//			}
//		} catch (IOException e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public String receiveVoiceMessage(
			ReceiveVoiceMessage paramReceiveVoiceMessage) {
		logger.info("---进入语音消息");
		logger.info("---进入语音消息,逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复");
		/**
		 * 逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复
		 */
		String toUserName = paramReceiveVoiceMessage.getFromUserName();
		String fromUserName = paramReceiveVoiceMessage.getToUserName();
		String content ="";
		String articleIds="";//图文标识集合
		if (Tools.isEmpty(toUserName) || Tools.isEmpty(fromUserName)) {
			return null;
		}
//		try {
//			PageData pd = new PageData();
//			//1.处理自动回复
//			pd.put("responseType", 23);//接收消息类型具体请查询t_types表中的微信消息回复类型
//			pd.put("isValid", 1);//自动回复状态是否开启
//			pd = responselogService.findByWhre(pd);//查询自动回复数据
//			//判断自动回复是否设置并且回复内容不能为空
//			if(pd!=null && pd.get("CONTENT")!=null){
//				content = pd.get("CONTENT")+"";
//			}else{
//				//2.指定消息类型回复
//				pd = new PageData();
//				pd.put("responseType", 17);//接收消息类型
//				pd.put("isValid", 1);//接收消息类型状态是否开启
//				pd = responselogService.findByWhre(pd);//查询接收文本消息回复数据
//				if (pd == null || pd.get("CONTENT")==null) {
//					return null;
//				}
//				content = pd.get("CONTENT")+"";
//			}
//			if(pd.get("ARTICLEIDS")!=null){
//				articleIds = pd.getString("ARTICLEIDS");
//			}
//			int sendType = Convert.strToInt(pd.get("SENDTYPE")+"", -1);// 回复消息类型1.文本
//			if (sendType == 1) {
//				// 进行回复文本
//				logger.info("---语音消息开始进行回复文本");
//				sendTextMsg(toUserName, fromUserName, content);
//				logger.info("---语音消息结束回复文本");
//				return null;
//			} else if (sendType == 2 &&  !Tools.isEmpty(articleIds)) {
//				// 进入回复图文
//				logger.info("---语音消息开始进行回复图文");
//				sendArticleMessage(articleIds, toUserName, fromUserName);
//				logger.info("---语音消息结束回复图文");
//				return null;
//			} else {
//				logger.info("---未设置回复消息");
//				return null;
//			}
//		} catch (IOException e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public String receiveVideoMessage(
			ReceiveVideoMessage paramReceiveVideoMessage) {
		logger.info("---进入视频消息");
		logger.info("---进入视频消息,逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复");
		/**
		 * 逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复
		 */
		String toUserName = paramReceiveVideoMessage.getFromUserName();
		String fromUserName = paramReceiveVideoMessage.getToUserName();
		String content ="";
		String articleIds="";//图文标识集合
		if (Tools.isEmpty(toUserName) || Tools.isEmpty(fromUserName)) {
			return null;
		}
//		try {
//			PageData pd = new PageData();
//			//1.处理自动回复
//			pd.put("responseType", 23);//接收消息类型具体请查询t_types表中的微信消息回复类型
//			pd.put("isValid", 1);//自动回复状态是否开启
//			pd = responselogService.findByWhre(pd);//查询自动回复数据
//			//判断自动回复是否设置并且回复内容不能为空
//			if(pd!=null && pd.get("CONTENT")!=null){
//				content = pd.get("CONTENT")+"";
//			}else{
//				//2.指定消息类型回复
//				pd = new PageData();
//				pd.put("responseType", 18);//接收消息类型
//				pd.put("isValid", 1);//接收消息类型状态是否开启
//				pd = responselogService.findByWhre(pd);//查询接收文本消息回复数据
//				if (pd == null || pd.get("CONTENT")==null) {
//					return null;
//				}
//				content = pd.get("CONTENT")+"";
//			}
//			if(pd.get("ARTICLEIDS")!=null){
//				articleIds = pd.getString("ARTICLEIDS");
//			}
//			int sendType = Convert.strToInt(pd.get("SENDTYPE")+"", -1);// 回复消息类型1.文本 2.多图文
//			if (sendType == 1) {
//				// 进行回复文本
//				logger.info("---视频消息开始进行回复文本");
//				sendTextMsg(toUserName, fromUserName, content);
//				logger.info("---视频消息结束回复文本");
//				return null;
//			} else if (sendType == 2 &&  !Tools.isEmpty(articleIds)) {
//				// 进入回复图文
//				logger.info("---视频消息开始回复图文");
//				sendArticleMessage(articleIds, toUserName, fromUserName);
//				logger.info("---视频消息结束回复图文");
//				return null;
//			} else {
//				logger.info("---未设置回复消息");
//				return null;
//			}
//		} catch (IOException e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public String receiveLocationMessage(
			ReceiveLocationMessage paramReceiveLocationMessage) {
		logger.info("---进入地理位置消息");
		logger.info("---进入地理位置,逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复");
		/**
		 * 逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复
		 */
		String toUserName = paramReceiveLocationMessage.getFromUserName();
		String fromUserName = paramReceiveLocationMessage.getToUserName();
		String content ="";
		String articleIds="";//图文标识集合
		System.out.println("Latitude"+paramReceiveLocationMessage.getLocation_X());
		System.err.println("Longitude"+paramReceiveLocationMessage.getLocation_Y());
		if (Tools.isEmpty(toUserName) || Tools.isEmpty(fromUserName)) {
			return null;
		}
//		try {
//			PageData pd = new PageData();
//			//1.处理自动回复
//			pd.put("responseType", 23);//接收消息类型具体请查询t_types表中的微信消息回复类型
//			pd.put("isValid", 1);//自动回复状态是否开启
//			pd = responselogService.findByWhre(pd);//查询自动回复数据
//			//判断自动回复是否设置并且回复内容不能为空
//			if(pd!=null && pd.get("CONTENT")!=null){
//				content = pd.get("CONTENT")+"";
//			}else{
//				//2.指定消息类型回复
//				pd = new PageData();
//				pd.put("responseType", 20);//接收消息类型
//				pd.put("isValid", 1);//接收消息类型状态是否开启
//				pd = responselogService.findByWhre(pd);//查询接收文本消息回复数据
//				if (pd == null || pd.get("CONTENT")==null) {
//					return null;
//				}
//				content = pd.get("CONTENT")+"";
//			}
//			if(pd.get("ARTICLEIDS")!=null){
//				articleIds = pd.getString("ARTICLEIDS");
//			}
//			int sendType = Convert.strToInt(pd.get("SENDTYPE")+"", -1);// 回复消息类型1.文本
//			if (sendType == 1) {
//				// 进行回复文本
//				logger.info("---地理位置消息开始进行回复文本");
//				sendTextMsg(toUserName, fromUserName, content);
//				logger.info("---地理位置消息结束回复文本");
//				return null;
//			} else if (sendType == 2 &&  !Tools.isEmpty(articleIds)) {
//				// 进入回复图文
//				logger.info("---地理位置消息开始回复图文");
//				sendArticleMessage(articleIds, toUserName, fromUserName);
//				logger.info("---地理位置消息结束回复图文");
//				return null;
//			} else {
//				logger.info("---未设置回复消息");
//				return null;
//			}
//		} catch (IOException e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public String receiveLinkMessage(ReceiveLinkMessage paramReceiveLinkMessage) {
		logger.info("---进入链接消息");
		logger.info("---进入链接,逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复");
		/**
		 * 逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复
		 */
		String toUserName = paramReceiveLinkMessage.getFromUserName();
		String fromUserName = paramReceiveLinkMessage.getToUserName();
		String content ="";
		String articleIds="";//图文标识集合
		if (Tools.isEmpty(toUserName) || Tools.isEmpty(fromUserName)) {
			return null;
		}
//		try {
//			PageData pd = new PageData();
//			//1.处理自动回复
//			pd.put("responseType", 23);//接收消息类型具体请查询t_types表中的微信消息回复类型
//			pd.put("isValid", 1);//自动回复状态是否开启
//			pd = responselogService.findByWhre(pd);//查询自动回复数据
//			//判断自动回复是否设置并且回复内容不能为空
//			if(pd!=null && pd.get("CONTENT")!=null){
//				content = pd.get("CONTENT")+"";
//			}else{
//				//2.指定消息类型回复
//				pd = new PageData();
//				pd.put("responseType", 19);//接收消息类型
//				pd.put("isValid", 1);//接收消息类型状态是否开启
//				pd = responselogService.findByWhre(pd);//查询接收文本消息回复数据
//				if (pd == null || pd.get("CONTENT")==null) {
//					return null;
//				}
//				content = pd.get("CONTENT")+"";
//			}
//			if(pd.get("ARTICLEIDS")!=null){
//				articleIds = pd.getString("ARTICLEIDS");
//			}
//			int sendType = Convert.strToInt(pd.get("SENDTYPE")+"", -1);// 回复消息类型1.文本
//			if (sendType == 1) {
//				// 进行回复文本
//				logger.info("---链接消息开始进行回复文本");
//				sendTextMsg(toUserName, fromUserName, content);
//				logger.info("---链接消息结束回复文本");
//				return null;
//			} else if (sendType == 2 &&  !Tools.isEmpty(articleIds)) {
//				// 进入回复图文
//				logger.info("---链接消息开始回复图文");
//				sendArticleMessage(articleIds, toUserName, fromUserName);
//				logger.info("---链接消息结束回复图文");
//				return null;
//			} else {
//				logger.info("---未设置回复消息");
//				return null;
//			}
//		} catch (IOException e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			logger.info("文本消息发生异常：" + e.getMessage());
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public String receiveTemplateSendJobFinishMessag(
			ReceiveTemplateMessage paramReceiveTemplateMessage) {
		logger.info("---进入视频消息receiveTemplateSendJobFinishMessag");
		logger.info("---进入视频消息,逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复");
		return null;
	}

	@Override
	public String eventSubscribeMessage(
			ReceiveSubscribeMessage paramReceiveSubscribeMessage) {
		logger.info("---进入关注回复");
		logger.info("---进入关注回复,逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复");
		/**
		 * 逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复
		 */
		String toUserName = paramReceiveSubscribeMessage.getFromUserName();
		String fromUserName = paramReceiveSubscribeMessage.getToUserName();
		//下面三个字段是通过扫描关注才会有的数据
		//具体说明地址为：http://mp.weixin.qq.com/wiki/2/5baf56ce4947d35003b86a9805634b1e.html
		//qrscene_为前缀，后面为二维码的参数值 这个参数值为t_create_qr表中的主键
		logger.info("---进入关注回复getEventKey:"+paramReceiveSubscribeMessage.getEventKey()); 
		logger.info("---进入关注回复getEvent:"+paramReceiveSubscribeMessage.getEvent()); //代表关注
		logger.info("---进入关注回复getTicket:"+paramReceiveSubscribeMessage.getTicket()); //扫描的二维码标识
		//上面三个字段是通过扫描关注才会有的数据
		String content ="";
		String articleIds="";//图文标识集合
		PageData tpd = new PageData();
//		try {
//			
//			UserInfo userInfo = UserManage.getUserInfo(toUserName);
//			if (userInfo == null) {
//				return null;
//			}
//			PageData pd = new PageData();
//			pd.put("OPENID", userInfo.getOpenid());
//			// 需要更新字段
//			pd = memberService.findByOpenId(pd);// 查询当前关注用是否以前关注过
//			if (pd == null) {
//				pd = this.getPageData();
//			}
//			pd.put("WECHATNAME", userInfo.getNickname());
//			pd.put("SEX", userInfo.getSex());
//			pd.put("CITY", userInfo.getCity());
//			pd.put("COUNTRY", userInfo.getCountry());
//			pd.put("PROVINCE", userInfo.getProvince());
//			pd.put("LANGUAGE", userInfo.getLanguage());
//			pd.put("HEADIMGURL", userInfo.getHeadimgurl());
//			pd.put("SUBSCRIBETIME",
//					DateUtil.formatTime(userInfo.getSubscribe_time()));
//			pd.put("SUBSCRIBE", Convert.strToInt(userInfo.getSubscribe(), 1));
//			pd.put("LANGUAGE", userInfo.getLanguage());
//			pd.put("EDITDATE", new Date());
//			pd.put("SYNCDATE", new Date());
//			pd.put("GROUPID", Convert.strToInt(userInfo.getGroupid(), 0));
//			pd.put("REMARKS", userInfo.getRemark());
//			// 验证是否以前是否关注过
//			if (pd == null || pd.get("OPENID") == null) {
//				// 第一次关注，自动注册成为普通用户
//				String MEMBERID =this.get32UUID();
//				pd.put("OPENID", userInfo.getOpenid());
//				pd.put("MEMBERID", MEMBERID);
//				pd.put("MEMBERTYPE", 2);
//				pd.put("CREATEDATE", new Date());
//				pd.put("ISLASTSYNC", 0);
//				pd.put("PHONENUMBER", "");
//				pd.put("VERIFYPHONENUMBER", 0);
//				pd.put("LASTIP", "");
//				pd.put("LASTDATE", new Date());
//				pd.put("REVENUEMONEY", 0.00);
//				pd.put("BALANCEMONEY", 0.00);
//				pd.put("FREEZEMONEY", 0.00);
//				pd.put("REVENUEPV", 0.00);
//				pd.put("REVENUELIKE", 0.00);
//				pd.put("REVENUESUB", 0.00);
//				pd.put("REVENUECON", 0.00);
//				pd.put("REVENUECONM", 0.00);
//				pd.put("REVENUESUBM", 0.00);
//				pd.put("REVENUEPVM", 0.00);
//				pd.put("PASSWORD", "");
//				pd.put("LOTTERYNUM", 0);
//				
//				// 第一次关注，自动注册成为普通用户
//				PageData ld = new PageData();//操作记录
//				ld.put("LOGTYPE", 1);
//				ld.put("CONTENTS", userInfo.getNickname()+"[关注]");
//				ld.put("CREATEDATE", new Date());
//				ld.put("LOGIP", PublicUtil.getIp());
//				ld.put("MEMBERID", MEMBERID);
//				PageData cd = new PageData();//优惠券主表回写
//				String COUPONSID ="f14fd2350a6c477583057e72272a320a";//固定不能删除
//				tpd.put("COUPONSID", COUPONSID);
//				tpd = couponsService.findById(tpd);
//				int couponsState =-1;//判断当前类型优惠券是否启用
//				if(Tools.notEmpty(tpd.get("COUPONSSTATE")+"")){
//					couponsState = Convert.strToInt(tpd.get("COUPONSSTATE")+"", couponsState);
//				}
//				if(couponsState==21){
//					int COUPONSTOTALNUM = 0;
//					if(Tools.notEmpty(tpd.get("COUPONSTOTALNUM")+"")){
//						COUPONSTOTALNUM =Convert.strToInt(tpd.get("COUPONSTOTALNUM")+"", COUPONSTOTALNUM)+1;
//					}
//					cd.put("COUPONSTOTALNUM", COUPONSTOTALNUM);
//					cd.put("EDITDATE", new Date());
//					cd.put("ADMINID", "平台系统管理员");
//					cd.put("COUPONSID", COUPONSID);
//					PageData cdd = new PageData();//优惠券详表数据
//					String NUMBERS=Tools.createRandomNum12();
//					cdd.put("COUPONSID", COUPONSID);
//					cdd.put("NUMBERS", NUMBERS);
//					double DENOMINATION =0.00;
//					if(!Tools.isEmpty(tpd.get("DENOMINATION")+"")){
//						DENOMINATION =Convert.strToDouble(tpd.get("DENOMINATION")+"", DENOMINATION);
//					}
//					cdd.put("DENOMINATION", DENOMINATION);
//					String imgName=DateUtil.getDays()+"/"+System.currentTimeMillis()+".png";
//					String QRCODE="uploadFiles/twoDimensionCode/"+imgName;
//					String imgPath = getRequest().getServletContext().getRealPath("/")+"uploadFiles/twoDimensionCode/"+imgName;
//					TwoDimensionCode handler = new TwoDimensionCode();
//					handler.encoderQRCode(NUMBERS, imgPath, "png");
//					cdd.put("QRCODE", QRCODE);
//					cdd.put("MEMBERID", MEMBERID);
//					cdd.put("STATEID", 24);
//					cdd.put("CREATEDATE",  new Date());
//					cdd.put("EDITDATE",  new Date());
//					Date ACQUIREDATE =new Date();
//					cdd.put("ACQUIREDATE", ACQUIREDATE);
//					int VALIDITY =0;
//					Date ENDDATE = new Date();
//					if(!Tools.isEmpty(tpd.get("VALIDITY")+"")){
//						VALIDITY =Convert.strToInt(tpd.get("VALIDITY")+"", VALIDITY);
//						 Calendar   calendar   =   new   GregorianCalendar(); 
//					     calendar.setTime(ENDDATE); 
//					     calendar.add(calendar.DATE,VALIDITY);//把日期往后增加VALIDITY天.整数往后推,负数往前移动 
//					     ENDDATE=calendar.getTime();   //这个时间就是日期往后推VALIDITY天的结果 
//					}
//					cdd.put("ENDDATE", ENDDATE);
//					cdd.put("USEDATE", "");
//					cdd.put("USERREMARK", "");
//					String COUPONSNAME="";
//					if(!Tools.isEmpty(tpd.get("COUPONSNAME")+"")){
//						COUPONSNAME=tpd.get("COUPONSNAME")+"";
//					}
//					cdd.put("REMARKS","新用户关注获得："+COUPONSNAME);
//					memberService.saveSubscribe(pd, ld, cd, cdd);
//					if(!getRequertUrl().contains("localhost") && !getRequertUrl().contains("ddj.java.1yg.tv")){
//						List<Template> templates = new ArrayList<Template>();
//						templates = TemplateMessage.setTemplate(templates, "#173177", "first",userInfo.getNickname()+",您获取优惠券："+COUPONSNAME+"。");
//						templates = TemplateMessage.setTemplate(templates, "#173177", "keyword1",NUMBERS);
//						templates = TemplateMessage.setTemplate(templates, "#173177", "keyword2",DENOMINATION+"元");
//						templates = TemplateMessage.setTemplate(templates, "#173177", "keyword3",Convert.dateToStr(ACQUIREDATE, "yyyy-MM-dd", "1900-01-01")+"-"+Convert.dateToStr(ENDDATE, "yyyy-MM-dd", "1900-01-01"));
//						templates = TemplateMessage.setTemplate(templates, "#173177", "remark","感谢您的关注，请及时使用您的优惠券，点击可查看优惠券详情！");
//						String url =getRequertUrl()+"myCoupon/goDetails?numbers="+NUMBERS;
//						TemplateMessage.sendTemplateMessage(userInfo.getOpenid(), Constants.PRODUCT_COUPON_TEMPLATE_ID, url, Constants.PRODUCT_RESERVE_TOPCOLOR, templates);
//					}
//				}
//				
//				/**
//				 * 以下逻辑为处理在文章页面通过识别二维码进行关注，来核算对应的作者、分享者收益部分
//				 */
//				//1.首先判断getEventKey是否代表着是通过二维码扫描关注的
//				//2.如果是，那么可以获取EventKey中的值，截取qrscene_的后缀，该后缀代表t_create_qr表中的主键
//				//3.通过t_create_qr表中的主键查询当前扫描关注的来源（文章）作者和分享者以及对应的文章得到对应的关注收益比例
//				//4.分别给作者和分享者结算对应的收益，该处可能分享者是没有的。
//				String subscribe = paramReceiveSubscribeMessage.getEvent();
//				if(!Tools.isEmpty(subscribe)&&subscribe.equals("subscribe")){
//					//关注逻辑
//					String eventKey = paramReceiveSubscribeMessage.getEventKey();
//					
//					if(!Tools.isEmpty(eventKey)){
//						String createId = eventKey.split("_")[1];//二维码表的主键id
//						createqrService.handerAuthorMoneyAndUserBalance(createId);
//					}
//				}
//				
//			} else {
//				// 仅更新微信用户基本信息
//				memberService.edit(pd);
//			}
//			// 发送关注消息
//			pd = new PageData();
//			//1.处理自动回复
//			pd.put("responseType", 23);//接收消息类型具体请查询t_types表中的微信消息回复类型
//			pd.put("isValid", 1);//自动回复状态是否开启
//			pd = responselogService.findByWhre(pd);//查询自动回复数据
//			//判断自动回复是否设置并且回复内容不能为空
//			if(pd!=null && pd.get("CONTENT")!=null){
//				content = pd.get("CONTENT")+"";
//			}else{
//				//2.指定消息类型回复
//				pd = new PageData();
//				pd.put("responseType", 21);//接收消息类型
//				pd.put("isValid", 1);//接收消息类型状态是否开启
//				pd = responselogService.findByWhre(pd);//查询接收文本消息回复数据
//				if (pd == null || pd.get("CONTENT")==null) {
//					return null;
//				}
//				content = pd.get("CONTENT")+"";
//			}
//			if(pd.get("ARTICLEIDS")!=null){
//				articleIds = pd.getString("ARTICLEIDS");
//			}
//			int sendType = Convert.strToInt(pd.get("SENDTYPE")+"", -1);// 回复消息类型1.文本
//			if (sendType == 1) {
//				// 进行回复文本
//				logger.info("---关注回复开始进行回复文本");
//				logger.info("toUserName:"+toUserName+" fromUserName:"+fromUserName+" content:"+content);
//				sendTextMsg(toUserName, fromUserName, content);
//				logger.info("---关注回复结束回复文本");
//				return null;
//			} else if (sendType == 2 &&  !Tools.isEmpty(articleIds)) {
//				// 进入回复图文
//				logger.info("---关注回复开始回复图文");
//				sendArticleMessage(articleIds, toUserName, fromUserName);
//				logger.info("---关注回复结束回复图文");
//				return null;
//			} else {
//				logger.info("---未设置回复消息");
//				return null;
//			}
//		} catch (IOException e) {
//			logger.info("关注回复异常：" + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			logger.info("关注回复异常：" + e.getMessage());
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public String eventUnSubscribeMessage(
			ReceiveSubscribeMessage paramReceiveSubscribeMessage) {
		logger.info("---进入取消关注");
		String fromuserName = paramReceiveSubscribeMessage.getFromUserName();
		if (Tools.isEmpty(fromuserName)) {
			return null;
		}
		PageData pd = new PageData();
		pd.put("SUBSCRIBETIME", new Date());
		pd.put("SUBSCRIBE", 0);
		pd.put("EDITDATE", new Date());
		pd.put("OPENID", fromuserName);
//		try {
//			memberService.editByOpenId(pd);
//		} catch (Exception e) {
//			logger.info("取消关注发送异常：" + e.getMessage());
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public String eventClickMessage(ReceiveClickMessage paramReceiveClickMessage) {
		String eventKey = paramReceiveClickMessage.getEventKey();
		logger.info("---eventKey"+eventKey);
		String toUserName = paramReceiveClickMessage.getFromUserName();
		String fromUserName = paramReceiveClickMessage.getToUserName();
		//2.查询审核通过最近的8条记录、
		String articleIds="";
		PageData pd = new PageData();
//		try {
//			if("news".equals(eventKey)){
//				logger.info("---进入new点击方法");
//				List<PageData> list = new ArrayList<PageData>();
//				pd.put("author", "");
//				pd.put("articleStateID", 19);
//				pd.put("articleTypeID", -1);
//				list=articleService.listByAuthor(pd);
//				if(list!=null && list.size()>0){
//					String ids ="";
//					for (int i = 0; i < list.size(); i++) {
//						if(list.get(i).get("id")!=null){
//							if(Tools.isEmpty(ids)){
//								ids+=list.get(i).get("id").toString();
//							}else{
//								ids+=","+list.get(i).get("id").toString();
//							}
//						}
//					}
//					if(!Tools.isEmpty(ids)){
//						pd.put("ARTICLEIDS", ids);
//						pd.put("SENDTYPE", 2);
//					}
//				}
//			}else if("text".equals(eventKey)){
//				//文本回复
//				pd.put("SENDTYPE", 3);
//				
//			}else if("voice".equals(eventKey)){
//				//语音回复
//				
//				
//			}else if("video".equals(eventKey)){
//				//视频回复
//				
//			}else if("product".equals(eventKey)){
//				//回复可预订产品
//				logger.info("---进入product点击方法");
//				List<PageData> list = new ArrayList<PageData>();
//				pd.put("proType", -1);
//				pd.put("proState", 15);
//				list=productService.listAll(pd);
//				if(list!=null && list.size()>0){
//					String ids ="";
//					for (int i = 0; i < list.size(); i++) {
//						if(list.get(i).get("productId")!=null){
//							if(Tools.isEmpty(ids)){
//								ids+="'"+list.get(i).get("productId").toString()+"'";
//							}else{
//								ids+=",'"+list.get(i).get("productId").toString()+"'";
//							}
//						}
//					}
//					if(!Tools.isEmpty(ids)){
//						pd.put("ARTICLEIDS", ids);
//						pd.put("SENDTYPE", 4);
//					}
//				}
//			}else if("DIYFOOD".equals(eventKey)){
//				logger.info("---进入自制美食点击方法");
//				List<PageData> list = new ArrayList<PageData>();
//				pd.put("author", "");
//				pd.put("articleStateID", 19);
//				pd.put("articleTypeID", 30);
//				list=articleService.listByAuthor(pd);
//				if(list!=null && list.size()>0){
//					String ids ="";
//					for (int i = 0; i < list.size(); i++) {
//						if(list.get(i).get("id")!=null){
//							if(Tools.isEmpty(ids)){
//								ids+=list.get(i).get("id").toString();
//							}else{
//								ids+=","+list.get(i).get("id").toString();
//							}
//						}
//					}
//					if(!Tools.isEmpty(ids)){
//						pd.put("ARTICLEIDS", ids);
//						pd.put("SENDTYPE", 2);
//					}
//				}
//			}
//			
//			logger.info("ARTICLEIDS:"+pd.get("ARTICLEIDS"));
//			
//			if(pd.get("ARTICLEIDS")!=null){
//				articleIds = pd.getString("ARTICLEIDS");
//			}
//			int sendType = Convert.strToInt(pd.get("SENDTYPE")+"", -1);// 回复消息类型1.文本 2.图文
//			logger.info("sendType:"+sendType);
//			if (sendType == 1) {
//				// 进行回复文本
//				logger.info("---文本消息开始进行回复文本");
////			    sendTextMsg(toUserName, fromUserName, content);
//				logger.info("---文本消息结束回复文本");
//				return null;
//			} else if (sendType == 2 &&  !Tools.isEmpty(articleIds)) {
//				// 进入回复图文
//				logger.info("---文本消息开始进行回复图文");
//				logger.info("---toUserName:"+toUserName+" fromUserName:"+fromUserName);
//				sendArticleMessage(articleIds,toUserName,fromUserName);
//				logger.info("---文本消息结束回复图文");
//				return null;
//			} else if(sendType == 3){
//				//未找到对应内容回复
//				logger.info("---文本消息开始进行回复文本");
//				PageData t_menu=null;
//				t_menu= t_menuService.findByKeyType(eventKey.trim());
//				logger.info("---查询回复的Key_Type出错");
//				if(t_menu!=null&&!Tools.isEmpty(t_menu.get("CONTENT").toString())){
//					sendTextMsg(toUserName, fromUserName, t_menu.get("CONTENT").toString());
//				}else{
//					sendTextMsg(toUserName, fromUserName, "");
//				}
//				logger.info("---未设置回复消息开始结束");
//			}else if(sendType==4){
//				// 进入产品回复图文
//				logger.info("---开始进行回复图文");
//				logger.info("---toUserName:"+toUserName+" fromUserName:"+fromUserName);
//				sendProductMessage(articleIds, toUserName,fromUserName );
//				logger.info("---结束回复图文");
//				return null;
//			}
//		} catch (Exception e) {
//			logger.info("---处理菜单消息异常："+e.getMessage());
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public String eventGroupMessageNotice(
			ReceiveGroupMessageNotice paramReceiveGroupMessageNotice) {
		logger.info("---进入视频消息eventGroupMessageNotice");
		logger.info("---进入视频消息,逻辑描述：回复消息机制优先级设定自动回复>关键字回复>指定消息类型回复");
		return null;
	}
	
	/**
	 * 被动回复文本消息
	 * @param toUserName 接收人
	 * @param fromUserName 发送人
	 * @param content 文本内容
	 */ 
	public void sendTextMsg(String toUserName,String fromUserName,String content){
		ReplyTextMessage message = new ReplyTextMessage();
		message.setToUserName(toUserName);
		message.setFromUserName(fromUserName);
		message.setCreateTime(new Date().getTime());
		message.setMsgType("text");
		message.setContent(content);
		message.setMsgId(new Date().getTime());
		try {
			logger.info("response:"+response);
			ReplyMessage.replyTextMessage(response, message);
		} catch (IOException e) {
			logger.info("回复文本消息内容异常：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 被动回复多图文消息
	 * @param articleIds 图文标识
	 * @param toUserName 接收人
	 * @param fromUserName 发送人
	 */
	public void sendArticleMessage(String articleIds,String toUserName,String fromUserName){
		PageData pd = new PageData();
		List<PageData> pdList = new ArrayList<PageData>();//查询关联的图文
		ReplyArticlesMessage news = new ReplyArticlesMessage();//回复图文对象
		pd.put("DATA_IDS", articleIds);
//		try {
//			pdList=articleService.listByIds(pd);
//			if(pdList.size()<=0 || pdList.size()>8){
//				logger.info("回复图文异常，最多推送8篇软文!");
//				return;
//			}
//			for(int i=0;i<pdList.size();i++){
//				PageData vpd = new PageData();
//				Article ac = new Article();//多图文对象
//				ac.setDescription(pdList.get(i).get("DIGEST")+"");//设置图文信息描述
//				String picUrl =getRequertUrl()+"/uploadFiles/uploadImgs/"+pdList.get(i).get("ARTICLEIMG")+"";
//				logger.info("回复图文picUrl："+picUrl);
//				ac.setPicUrl(picUrl);//设置图片路径
//				ac.setTitle(pdList.get(i).get("TITLE")+"");//设置图文标题
//				if(pdList.get(i).get("ARTICLEURL")==null){
//					ac.setUrl("http://food.366315.net/wechatfirm/newsList.html");//点击图文消息跳转链接 
//				}else{
//					ac.setUrl(pdList.get(i).get("ARTICLEURL")+"");//点击图文消息跳转链接 
//				}
//				news.addArticles(ac);//封装图文内容
//				vpd.put("PUSHNUMBER", Convert.strToInt(pdList.get(i).get("PUSHNUMBER").toString(), 0)+1);//更新推送次数
//				vpd.put("ARTICLEID", pdList.get(i).get("ARTICLEID")+"");//32位对外标识
//				vpd.put("THUMB_MEDIA_ID", pdList.get(i).get("THUMB_MEDIA_ID")+"");//软文中封面图片上传到微信服务器标识
//				vpd.put("THUMB_CREATED_AT", pdList.get(i).get("THUMB_CREATED_AT")+"");//软文中封面图片上传到微信服务器返回时间戳
//				vpd.put("MEDIA_ID", pdList.get(i).get("MEDIA_ID")+"");//软文上传到微信服务器标识
//				vpd.put("MEDIA_ID_CREATED_AT", pdList.get(i).get("MEDIA_ID_CREATED_AT")+"");//软文上传到微信服务器返回时间戳
//				articleService.editPush(vpd);//更新当前软文
//			}
//			news.setToUserName(toUserName);
//			news.setFromUserName(fromUserName);
//			news.setCreateTime(new Date().getTime());
//			news.setMsgId(new Date().getTime());
//			news.setMsgType("news");
//			ReplyMessage.replyArticleMessage(response, news);
//		} catch (IOException e) {
//			logger.info("被动回复多图文消息异常：" + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			logger.info("被动回复多图文消息异常：" + e.getMessage());
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 推送最新产品
	 * @param productIds 产品标识
	 * @param toUserName 接收人
	 * @param fromUserName 发送人
	 */
	public void sendProductMessage(String productIds,String toUserName,String fromUserName){
		PageData pd = new PageData();
		List<PageData> pdList = new ArrayList<PageData>();//查询关联的图文
		ReplyArticlesMessage news = new ReplyArticlesMessage();//回复图文对象
		pd.put("DATA_IDS", productIds);
//		try {
//			pdList=productService.listByIds(pd);
//			if(pdList.size()<=0 || pdList.size()>8){
//				logger.info("回复图文异常，最多推送8篇软文!");
//				return;
//			}
//			for(int i=0;i<pdList.size();i++){
//				Article ac = new Article();//多图文对象
//				ac.setDescription(pdList.get(i).get("proDescription")+"");//设置图文信息描述
//				String picUrl =getRequertUrl()+"/uploadFiles/uploadImgs/"+pdList.get(i).get("proImg")+"";
//				logger.info("回复图文picUrl："+picUrl);
//				ac.setPicUrl(picUrl);//设置图片路径
//				ac.setTitle(pdList.get(i).get("proTitle")+"");//设置图文标题
//				if(pdList.get(i).get("proUrl")==null){
//					ac.setUrl("http://www.wlsq.tv/");//点击图文消息跳转链接 
//				}else{
//					ac.setUrl(getRequertUrl()+pdList.get(i).get("proUrl")+"");//点击图文消息跳转链接 
//				}
//				news.addArticles(ac);//封装图文内容
//			}
//			logger.info("回复图文toUserName："+toUserName);
//			logger.info("回复图文fromUserName："+fromUserName);
//			news.setToUserName(toUserName);
//			news.setFromUserName(fromUserName);
//			news.setCreateTime(new Date().getTime());
//			news.setMsgId(new Date().getTime());
//			news.setMsgType("news");
//			ReplyMessage.replyArticleMessage(response, news);
//		} catch (IOException e) {
//			logger.info("被动回复多图文消息异常：" + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			logger.info("被动回复多图文消息异常：" + e.getMessage());
//			e.printStackTrace();
//		}
	}
}
