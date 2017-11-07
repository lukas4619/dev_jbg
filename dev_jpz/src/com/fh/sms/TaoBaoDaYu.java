package com.fh.sms;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.fh.util.Convert;
import com.fh.util.Logger;
import com.fh.util.Tools;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;


/**
 * 阿里大于发送短信
 * @author Administrator
 *
 */
public class TaoBaoDaYu {
	
	protected static Logger LOG = Logger.getLogger(TaoBaoDaYu.class);

	/**
	 * 接口请求地址
	 */
	private static final String URL = "http://gw.api.taobao.com/router/rest";
	/**
	 * 接口提供访问key
	 */
	private static final String APPKEY = "LTAI9vHcxiVLBYnb";

	/**
	 * 接口提供访问签名
	 */
	private static final String SECRET = "oCNdptyvBVhwjViYGK2slM6pY4XPgK";

	/**
	 * 通知签名
	 */
	private static final String FREESIGNNAME = "阿里云短信测试专用";

	/**
	 * 注册短信模版ID
	 */
	private static final String REGISTERCODE = "SMS_105230715";

	/**
	 * 忘记密码模版ID
	 */
	private static final String FORGETCODE = "SMS_12976595";

	/**
	 * 验证码模版ID
	 */
	private static final String VERIFICATIONCODE = "SMS_14271372";

	
	
	/**
	 * 发送短信验证码
	 * 
	 * @param sendPhone短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。群发短信需传入多个号码，
	 *            以英文逗号分隔，
	 *            一次调用最多传入200个号码。
	 * @param Code
	 *            验证码
	 * @param sendType
	 *            1.注册 2.忘记密码 3.验证码
	 * @return
	 */
	public static Map<String, Object> sendMsgByAutoCode(String sendPhone, String Code, int sendType) {
		Map<String, Object> map = new HashMap<String, Object>();
		AlibabaAliqinFcSmsNumSendResponse rsp;
		if (Tools.isEmpty(sendPhone)) {
			map.put("type", -1);
			map.put("msg", "请输入需要发送短信的手机号码");
			return map;
		}
		if (Tools.isEmpty(Code)) {
			map.put("type", -1);
			map.put("msg", "请输入需要发送的验证码");
			return map;
		}
		if (sendPhone.startsWith(",")) {
			sendPhone = sendPhone.substring(1, sendPhone.length());
		}
		if (sendPhone.endsWith(",")) {
			sendPhone = sendPhone.substring(0, sendPhone.length() - 1);
		}
		try {
			TaobaoClient client = new DefaultTaobaoClient(URL, APPKEY, SECRET);
			AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
			// 短信内容
			String msgContent = "{\"code\":\"" + Code + "\",\"product\":\"手机绑定\"}";
			// 公共回传参数，在“消息返回”中会透传回该参数；举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，用户可以根据该会员ID识别是哪位会员使用了你的应用
			req.setExtend("123456");// 可选
			// 短信类型，传入值请填写normal
			req.setSmsType("normal");// 必选
			// 短信签名，传入的短信签名必须是在阿里大于“管理中心-短信签名管理”中的可用签名
			req.setSmsFreeSignName(FREESIGNNAME);// 必选
			// 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致
			// 示例：针对模板“验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":"alidayu"}
			req.setSmsParamString(msgContent);// 必选
			// 短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。
			// 群发短信需传入多个号码，以英文逗号分隔，一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
			req.setRecNum(sendPhone);// 必选
			// 短信模板ID
			String SmsTemplateCode = "";
			if (sendType == 1) {
				SmsTemplateCode = REGISTERCODE;
			} else if (sendType == 2) {
				SmsTemplateCode = FORGETCODE;
			} else if (sendType == 3) {
				SmsTemplateCode = VERIFICATIONCODE;
			}
			req.setSmsTemplateCode(SmsTemplateCode);// 必选
			rsp = client.execute(req);
			String resultJson = rsp.getBody();//返回的JSON数据
			LOG.info("resultJson:" + resultJson);
			if (Tools.isEmpty(resultJson)) {
				LOG.info("请求接口未获取到返回数据");
				map.put("type", -1);
				map.put("msg", "网络繁忙，请稍后再试！");
				return map;
			}
			JSONObject j = JSONObject.fromObject(resultJson);
			if (j == null) {
				LOG.info("请求接口未获取到返回数据");
				map.put("type", -1);
				map.put("msg", "网络繁忙，请稍后再试！");
				return map;
			}
			// 处理请求异常逻辑
			if (j.get("error_response")!=null) {
				LOG.info("接口请求异常：" + j.get("error_response"));
				map.put("type", -101);
				map.put("msg", j.get("error_response"));
				return map;
			} else if (j.get("alibaba_aliqin_fc_sms_num_send_response")!=null) {
				String alibaba_aliqin_fc_sms_num_send_response = j.getString("alibaba_aliqin_fc_sms_num_send_response");
				LOG.info("接口请求成功返回参数内容：" + alibaba_aliqin_fc_sms_num_send_response);
				JSONObject json = JSONObject.fromObject(alibaba_aliqin_fc_sms_num_send_response);
				if(json==null){
					LOG.info("请求接口未获取到返回数据");
					map.put("type", -1);
					map.put("msg", "网络繁忙，请稍后再试！");
					return map;
				}
				if(json.get("result")==null){
					LOG.info("请求接口未获取到返回数据");
					map.put("type", -1);
					map.put("msg", "网络繁忙，请稍后再试！");
					return map;
				}
				String resJSON = json.getString("result");
				if (Tools.isEmpty(resJSON)) {
					LOG.info("请求接口未获取到返回数据");
					map.put("type", -1);
					map.put("msg", "网络繁忙，请稍后再试！");
					return map;
				}
				JSONObject result = JSONObject.fromObject(resJSON);
				if(result==null){
					LOG.info("请求接口未获取到返回数据");
					map.put("type", -1);
					map.put("msg", "网络繁忙，请稍后再试！");
					return map;
				}
				long err_code = -1L;
				if (result.get("err_code")==null) {
					LOG.info("请求接口未获取到返回数据");
					map.put("type", -1);
					map.put("msg", "网络繁忙，请稍后再试！");
					return map;
				}
				err_code = Convert.strToLong(result.getString("err_code"), err_code);
				if (err_code != 0L) {
					LOG.info("发送短信失败，返回内容：" + resJSON);
					map.put("type", -101);
					map.put("msg", "网络繁忙，请稍后再试！");
					map.put("data", resJSON);
					return map;
				}
			} else {
				LOG.info("请求接口未获取到返回数据");
				map.put("type", -1);
				map.put("msg", "网络繁忙，请稍后再试！");
				return map;
			}

		} catch (ApiException e) {
			LOG.info("发送短信发生异常：" + e.getMessage());
			e.printStackTrace();
			map.put("type", -101);
			map.put("msg", "发送短信发生异常：" + e.getMessage());
			return map;
		}
		map.put("type", 1);
		map.put("msg", "成功");
		return map;
	}

	

	
	
	public static void main(String[] args) {
		String sendPhone ="18923798349";
		sendMsgByAutoCode(sendPhone, "1224", 1);
//		sendMsgByAutoCode(sendPhone, "4321", 2);
//		sendMsgByAutoCode(sendPhone, "1324", 3);
//		sendMsgByLottery(sendPhone);

	}
	
	
}
