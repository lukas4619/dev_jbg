﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<title>我要注册</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
<style type="text/css">
	*{
		font-size: 14px;
	}
	.bindtips{
		padding: 20px 15px;
	}
	.newmobile{
		width: 70%;
		border:1px solid #ccc;
		border-right: 0;
		height: 40px;
		border-radius: 4px 0 0 4px;
		box-sizing: border-box;
		padding-left: 5px;
		vertical-align: middle;
	}
	.newmobile:focus{
		border: 1px solid #08A600;
		border-right: 0;
		outline: none;
	}
	.sendCode{
		height: 40px;
		border: 1px solid #08A600;
		color: white;
		background-color: #08A600;
		border-radius:  0 4px 4px 0;
		width: 30%;
		outline: medium;
		vertical-align: middle;
	}
	.yzcode{
		width: 100%;
		border:1px solid #ccc;
		height: 40px;
		border-radius: 4px;
		box-sizing: border-box;
		padding-left: 5px;
	}
	.yzcode:focus{
		border: 1px solid #08A600;
		outline: none;
	}
	.top25{
		margin-top: 25px;
	}
	.bind{
		width: 90%;
	    margin-left: 5%;
	    margin-top: 35px;
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
	.bind:active{
		background:#008000;
	}
	.content{
		padding: 0px 15px;
		background-color: #fff;
	}
	.errormsg{
		color: red;
		padding-left: 10px;
		font-size: 13px;
		display: none;
		margin-top: 3px;
	}
	.greybtn{
		border: 1px solid grey;
		background-color: grey;
	}
</style>
</head>
<body>
	<div class="detailsTitle">立即注册</div>
	<div class="bindtips">
	</div>
	<div class="content">
		<input onkeydown="onlyNum()" class="newmobile" placeholder="请输入新的手机号码" type="number" maxlength="11"><button id="sendCode" class="sendCode">发送验证码</button>
		<div class="errormsg">*请输入手机号</div>
	</div>
	<div class="content top25">
		<input onkeydown="onlyNum()" class="yzcode" placeholder="请输入四位验证码" type="number" maxlength="4">
		<div class="errormsg">*验证码错误</div>
	</div>
	<button class="bind">马上注册</button>
	<div class="content top25" style="color: orange;">
		<p>注册说明：</p>
		<p>1、立即注册，享受下单立减1元优惠，仅限首次注册。</p>
		<p>2、成为会员，享有更多的线上线下便利店折扣优惠。</p>
		<p>更多详情，敬请在微信公众号中获取。</p>
	</div>
	
	<input type="hidden" id="STOREID" name="STOREID"  value="${pd.STOREID}">
</body>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/common.js"></script>
<script type="text/javascript">
var param = {};
var validCode = true;
$(function(){
	$("#nav4").addClass('current').siblings().removeClass('current');
	$("#sendCode").click(function(){
		var newmobile=$(".newmobile");
		if(newmobile.val()==""){
			newmobile.siblings('.errormsg').text('*请输入手机号').show();
			return false;
		}else if(checkMobile(newmobile.val())){
			newmobile.siblings('.errormsg').text('*输入手机号有误').show();
			return false;
		}else{
			param = {};
			param['phone']=newmobile.val();
			var url ="<%=basePath%>frontMember/sendPhoneCode.do";
			$.post(url,param,function(data){
				if(data=='' || typeof(data) == "undefined"){
					newmobile.siblings('.errormsg').text('网络繁忙，请稍后').show();
					return false;
				}
				if(data.type==1){
					setTimes();
				}else{
					newmobile.siblings('.errormsg').text(data.msg).show();
					return false;
				}
			});
		}
	})
	
	$(".bind").click(function(){
		var phone=$(".newmobile").val();
		if(isNull(phone)){
			$(".newmobile").siblings('.errormsg').text('*请输入手机号').show();
			return false;
		}
		var code =$(".yzcode").val();
		if(isNull(code) || code.length!=4){
			$(".yzcode").siblings('.errormsg').text('*请输入输入四位验证码').show();
			return false;
		}
		param = {};
		param['phone']=phone;
		param['code']=code;
		var url ="<%=basePath%>frontMember/codeVerify.do";
		$.post(url,param,function(data){
			if(data=='' || typeof(data) == "undefined"){
				$(".yzcode").siblings('.errormsg').text('网络繁忙，请稍后').show();
				return false;
			}
			if(data.type==1){
				location.href="<%=basePath%>frontStore/index.html?STOREID="+$("#STOREID").val();
			}else{
				$(".yzcode").siblings('.errormsg').text(data.msg).show();
				return false;
			}
		});
	});
	
});

function setTimes(){
	var newmobile=$(".newmobile");
	var code = $("#sendCode");
	newmobile.siblings('.errormsg').text('').hide();
	code.addClass('greybtn');
	var time = 300;
	if (validCode) {
		validCode = false;
		code.attr("disabled", true);
		var t = setInterval(function() {
			time--;
			code.html(time + "(s)");
			if (time == 0) {
				clearInterval(t);
				code.html("重新获取");
				validCode = true;
				code.removeClass('greybtn');
				code.attr("disabled", false);
			}
		}, 1000);
	}
}

</script>
</html>