<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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


<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<meta http-equiv="description" content="This is my page">
<title>${cf.ATTRVALFOUR}-微信支付</title>
<script type="text/javascript" src="<%=basePath%>static/weixin/js/jquery-1.7.2.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<style type="text/css">
body {
	background: #efefef;
	font-family: "微软雅黑";
	margin: 0;
	padding: 0;
}

.title {
	margin: 0 auto;
	text-align: center;
	margin-top: 10px;
	font-weight: bold;
}

.price {
	margin: 0 auto;
	text-align: center;
	font-size: 32px;
	margin-top: 5px;
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

.pricecnt span {
	display: inline-block;
}

.pricecnt span.pcleft {
	float: left;
	color: #666;
}

.pricecnt span.pcright {
	float: right;
}

.gopay {
	width: 96%;
	margin-left: 2%;
	margin-top: 25px;
	border-radius: 3px;
	font-size: 16px;
	background: #08A600;
	color: white;
	padding: 10px 0;
	outline: none;
	letter-spacing: 1px;
	outline: medium;
	-webkit-appearance: none;
	border: 1px solid #08A600;
}

.gopay:active {
	background: #008000;
}
</style>
</head>
<body>
	<input type="hidden" id="wxtimestamp" value="${pd.wxtimestamp}">
	<input type="hidden" id="wxnonce_str" value="${pd.wxnonce_str}">
	<input type="hidden" id="wxsignature" value="${pd.wxsignature}">
	<input type="hidden" id="hideappId" value="${pd.appId}">
	<input type="hidden" id="hidetimeStamp" value="${pd.timeStamp}">
	<input type="hidden" id="hidenonceStr" value="${pd.nonceStr}">
	<input type="hidden" id="hidepackage" value="${pd.packages}">
	<input type="hidden" id="hidesignType" value="${pd.signType}">
	<input type="hidden" id="hidepaySign" value="${pd.paySign}">
	<input type="hidden" id="STOREID" value="${pd.STOREID}">
	<div class="title">${cf.ATTRVALFOUR}</div>
	<div class="price">￥${pd.TOTALPRICE }</div>
	<div class="pricecnt">
		<span class="pcleft">收款方</span> <span class="pcright">${cf.ATTRVALFOUR} </span>
	</div>
	<input class="gopay" id="wcPay" type="button" value="立即支付" />
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
var userId = $("#hideuserId").val();

var ARTICLEID = '${pd.ARTICLEID}';
function onBridgeReady() {
    $("#wcPay").attr("disabled","disabled");//再改成disabled 
     $("#wcPay").val('支付中...');
	WeixinJSBridge.invoke('getBrandWCPayRequest', {
		"appId" : appId, //公众号名称，由商户传入     
		"timeStamp" : timeStamp, //时间戳，自1970年以来的秒数     
		"nonceStr" : nonceStr, //随机串     
		"package" : package,
		"signType" :signType, //微信签名方式：     
		"paySign" : paySign //微信签名 
	}, function(res) {
		// 支付成功后的回调函数，详细请参见：http://pay.weixin.qq.com/wiki/doc/api/index.php?chapter=7_7
				if (res.err_msg == "get_brand_wcpay_request:ok") { // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
					//TODO：此处为安全期间，应调用商户api查询订单状态。
					window.location.href = "<%=basePath%>frontStore/index.html?TOREID="+${pd.STOREID};//支付成功页面
s				} else if (res.err_msg == "get_brand_wcpay_request:cancel") //支付过程中用户取消
				{

				} else if (res.err_msg == "get_brand_wcpay_request:fail") //支付失败
				{
					//TODO：支付失败的商户处理逻辑。
					window.location.href = "<%=basePath%>frontStore/index.html?TOREID="+${pd.STOREID};
				} else {
					window.location.href = "<%=basePath%>frontStore/index.html.html?TOREID="+${pd.STOREID};
				}
				$("#wcPay").removeAttr("disabled");//要变成Enable，JQuery只能这么写
				$("#wcPay").val('立即支付');
	});
}
if (typeof WeixinJSBridge == "undefined") {
	if (document.addEventListener) {
		document.addEventListener('WeixinJSBridgeReady', onBridgeReady,
				false);
	} else if (document.attachEvent) {
		document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
		document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
	}
} else {
	onBridgeReady();
}

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

	$("#wcPay").click(function(){
	$("#wcPay").attr("disabled","disabled");//再改成disabled 
	$("#wcPay").val('支付中...');
	onBridgeReady();
	});
});
</script>
</html>
