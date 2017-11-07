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
<meta http-equiv="description" content="This is my page">
<title>提交成功</title>
<script type="text/javascript" src="../static/weixin/js/jquery-1.7.2.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<style type="text/css">
.tips{
	margin-top: 30px;
}
.txtcenter{
	text-align: center;
}
.success{
	width: 76px;
}
.green{
	color: green;
	font-size: 22px;
	margin-top: 10px;
}
.btn-group{
	width: 100%;
	margin-top: 20px;
}
button{
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
button.submit:active{
	background:#008000;
}
button.back a{
	text-decoration: none;
	color:#999
}
button.back{
	background-color: #f5f5f5;
	border: 1px solid #f5f5f5;
	color: #999;
}
</style>
</head>
<body>
	<input type="hidden" id="wxtimestamp" value="${pd.wxtimestamp}">
	<input type="hidden" id="wxnonce_str" value="${pd.wxnonce_str}">
	<input type="hidden" id="wxsignature" value="${pd.wxsignature}">
	<input type="hidden" id="hideappId" value="${pd.appId}">
	 <div class="content">
    	<div class="tips txtcenter">
    		<img class="success" src="images/chenggong.png">
    		<div class="green">提交成功</div>
    	</div>
    	<div class="btn-group">
    		<button class="back"><a href="orderlist.html">查看预定列表</a></button>
    		<button class="submit">立即支付</button>
    		
    	</div>
    </div>
</body>
<script type="text/javascript">
	var appId = $("#hideappId").val();
	var wxtimestamp = $("#wxtimestamp").val();
	var wxnonce_str = $("#wxnonce_str").val();
	var wxsignature = $("#wxsignature").val();
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

	
</script>
</html>
