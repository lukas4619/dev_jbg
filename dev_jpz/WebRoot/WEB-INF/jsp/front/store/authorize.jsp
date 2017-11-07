<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<title>申请授权</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="" rel="stylesheet">
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
.textcenter{
    text-align: center;
}
a.skip{
    width: 44%;
    margin-left: 28%;
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
    text-decoration: none;
    display: inline-block;
    text-align: center;
}
</style>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
</head>
<body>
	<input type="hidden" id="wxtimestamp" value="${pd.wxtimestamp}">
	<input type="hidden" id="wxnonce_str" value="${pd.wxnonce_str}">
	<input type="hidden" id="wxsignature" value="${pd.wxsignature}">
	<input type="hidden" id="hideappId" value="${pd.appId}">
    <div class="content">
        <div class="tips txtcenter">
            <img class="success" src="<%=basePath %>static/weixin/images/chenggong.png">
            <div class="green" id="chooseImage">
            	请求微信授权,请稍后....
            </div>
        </div>
    </div>
</body>
<script type="text/javascript" src="../static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript">
	var appId = $("#hideappId").val();
	var wxtimestamp = $("#wxtimestamp").val();
	var wxnonce_str = $("#wxnonce_str").val();
	var wxsignature = $("#wxsignature").val();
	wx.config({
			debug : true,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			appId : '${pd.appId}',// 必填，公众号的唯一标识
			timestamp : '${pd.wxtimestamp}',// 必填，生成签名的时间戳
			nonceStr : '${pd.wxnonce_str}',// 必填，生成签名的随机串
			signature : '${pd.wxsignature}',// 必填，签名，
			jsApiList: [
			'checkJsApi',
			'onMenuShareTimeline',
			'onMenuShareAppMessage',
			'onMenuShareQQ',
			'onMenuShareWeibo',
			'onMenuShareQZone',
			'hideMenuItems',
			'showMenuItems',
			'hideAllNonBaseMenuItem',
			'showAllNonBaseMenuItem',
			'translateVoice',
			'startRecord',
			'stopRecord',
			'onVoiceRecordEnd',
			'playVoice',
			'onVoicePlayEnd',
			'pauseVoice',
			'stopVoice',
			'uploadVoice',
			'downloadVoice',
			'chooseImage',
			'previewImage',
			'uploadImage',
			'downloadImage',
			'getNetworkType',
			'openLocation',
			'getLocation',
			'hideOptionMenu',
			'showOptionMenu',
			'closeWindow',
			'scanQRCode',
			'chooseWXPay',
			'openProductSpecificView',
			'addCard',
			'chooseCard',
			'openCard'
		  ]
	});
	wx.ready(function () {
		wx.getLocation({
			  success: function (res) {
			   var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
			   var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
			   var speed = res.speed; // 速度，以米/每秒计
			   var accuracy = res.accuracy; // 位置精度
				alert(latitude);
				alert(longitude);				
			  },
			  cancel: function (res) {
				alert('取消授权');
			  }
			});
	});

	
	
	
	
		
</script>
</html>