<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<title>提交成功</title>
<script type="text/javascript"
	src="<%=basePath%>static/weixin/js/jquery-1.7.2.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<style type="text/css">
.tips {
	margin-top: 30px;
}

.txtcenter {
	text-align: center;
}

.success {
	width: 76px;
}

.green {
	color: green;
	font-size: 22px;
	margin-top: 10px;
}

.btn-group {
	width: 100%;
	margin-top: 20px;
}

button {
	margin-top: 25px;
	border-radius: 3px;
	font-size: 16px;
	background: #08A600;
	color: white;
	padding: 5px 0;
	outline: none;
	letter-spacing: 1px;
	outline: medium;
	-webkit-appearance: none;
	border: 1px solid #08A600;
	margin-left: 6%;
	width: 40%;
	box-sizing: border-box;
}

button.submit:active {
	background: #008000;
}

button.back a {
	text-decoration: none;
	color: #999
}

button.back {
	background-color: #f5f5f5;
	border: 1px solid #f5f5f5;
	color: #999;
}

.pricecnt {
	background-color: white;
	width: 100%;
	height: auto;
	padding: 10px 20px;
	float: left;
	box-sizing: border-box;
	margin-top: 15px;
}

.pricecnt span.pcleft {
	float: left;
	color: #666;
}

.pricecnt span.pcright {
	float: right;
}

.pricecnt span {
	display: inline-block;
}
</style>
</head>
<body style="background-color: #f5f5f5;">
	<input type="hidden" id="wxtimestamp" value="${pd.wxtimestamp}">
	<input type="hidden" id="wxnonce_str" value="${pd.wxnonce_str}">
	<input type="hidden" id="wxsignature" value="${pd.wxsignature}">
	<input type="hidden" id="hideappId" value="${pd.appId}">
	<div class="content">
		<div class="tips txtcenter">
			<img class="success"
				src="<%=basePath%>static/weixin/images/chenggong.png">
			<div class="green">提交成功</div>
		</div>
		<div class="pricecnt">
			<span class="pcleft">预订套餐名称</span> <span class="pcright"
				style="font-weight: bold;">${pd.PRONAME } </span>
		</div>
		<div class="pricecnt">
			<span class="pcleft">预订套餐原价</span> <span class="pcright"
				style="color: #f60;font-weight: bold;">${pd.PROMONEY } </span>
		</div>
		<div class="pricecnt">
			<span class="pcleft">预订套餐实际金额</span> <span class="pcright"
				style="color: #f60;font-weight: bold;">${pd.ADVANCEMONEY } </span>
		</div>
		<div class="btn-group">
			<button class="back">
				<a href="<%=basePath%>frontReservate/index.html">查看预定列表</a>
			</button>
			<button class="submit" id="wcPay">立即支付</button>

		</div>
	</div>
	<input type="hidden" id="wxtimestamp" value="${wxtimestamp}">
	<input type="hidden" id="wxnonce_str" value="${wxnonce_str}">
	<input type="hidden" id="wxsignature" value="${wxsignature}">
	<input type="hidden" id="hideappId" value="${appId}">
	<input type="hidden" id="shareUrl" value="${shareUrl}">
	<input type="hidden" id="hidetimeStamp" value="${pd.timeStamp}">
	<input type="hidden" id="hidenonceStr" value="${pd.nonceStr}">
	<input type="hidden" id="hidepackage" value="${pd.packages}">
	<input type="hidden" id="hidesignType" value="${pd.signType}">
	<input type="hidden" id="hidepaySign" value="${pd.paySign}">
</body>
<script type="text/javascript">
	var appId = $("#hideappId").val();
	var wxtimestamp = $("#wxtimestamp").val();
	var wxnonce_str = $("#wxnonce_str").val();
	var wxsignature = $("#wxsignature").val();
	var timeStamp = $("#hidetimeStamp").val();
	var nonceStr = $("#hidenonceStr").val();
	var package = $("#hidepackage").val();
	var signType = $("#hidesignType").val();
	var paySign = $("#hidepaySign").val();

	$(function() {
		wx.config({
			debug : false,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			appId : appId,// 必填，公众号的唯一标识
			timestamp : wxtimestamp,// 必填，生成签名的时间戳
			nonceStr : wxnonce_str,// 必填，生成签名的随机串
			signature : wxsignature,// 必填，签名，
			jsApiList : [ 'checkJsApi', 'onMenuShareTimeline',
					'onMenuShareAppMessage', 'onMenuShareQQ',
					'onMenuShareWeibo', 'hideMenuItems', 'showMenuItems',
					'hideAllNonBaseMenuItem', 'showAllNonBaseMenuItem',
					'translateVoice', 'startRecord', 'stopRecord',
					'onRecordEnd', 'playVoice', 'pauseVoice', 'stopVoice',
					'uploadVoice', 'downloadVoice', 'chooseImage',
					'previewImage', 'uploadImage', 'downloadImage',
					'getNetworkType', 'openLocation', 'getLocation',
					'hideOptionMenu', 'showOptionMenu', 'closeWindow',
					'scanQRCode', 'chooseWXPay', 'openProductSpecificView',
					'addCard', 'chooseCard', 'openCard' ]
		});

		$("#wcPay").click(function() {
			WeixinJSBridge.invoke('getBrandWCPayRequest', {
				"appId" : appId, //公众号名称，由商户传入     
				"timeStamp" : timeStamp, //时间戳，自1970年以来的秒数     
				"nonceStr" : nonceStr, //随机串     
				"package" : package,
				"signType" : signType, //微信签名方式：     
				"paySign" : paySign
			//微信签名 
			}, function(res) {
				// 支付成功后的回调函数，详细请参见：http://pay.weixin.qq.com/wiki/doc/api/index.php?chapter=7_7
				if (res.err_msg == "get_brand_wcpay_request:ok") { // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
					//TODO：此处为安全期间，应调用商户api查询订单状态。
					window.location.href = "<%=basePath%>frontMember/index.html";//支付成功页面
				} else if (res.err_msg == "get_brand_wcpay_request:cancel") //支付过程中用户取消
				{

				} else if (res.err_msg == "get_brand_wcpay_request:fail") //支付失败
				{
					//TODO：支付失败的商户处理逻辑。
					window.location.href = "<%=basePath%>frontMember/index.html";//这里默认跳转到主页
				} else {
					window.location.href = "<%=basePath%>frontMember/index.html";//支付成功页面
				}
			});

		});
	});
</script>
</html>
