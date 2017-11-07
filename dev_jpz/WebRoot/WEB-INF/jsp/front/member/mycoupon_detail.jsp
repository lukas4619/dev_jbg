<%@ page language="java" contentType="text/html; charset=UTF-8"
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
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
	<meta name = "format-detection" content = "telephone=no">
<title>我的优惠券--优惠券详情</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
<style type="text/css">
body{
		font-family: "微软雅黑";
		background-color: #0d7791;
	}
	.title{
		color: #fff;
		font-size: 22px;
		padding: 10px 15px;
	}
	.content{
		background-color: #febf4d;
		color: #683e0e;
		padding: 3px 3px;
	}
	.name{
		font-size: 16px;
	}
	.no{
		font-size: 26px;
		margin-top: 10px;
	}
	.time1{
		margin-top: 5px;
		padding: 0 15px;
		font-size: 14px;
		color: #fff;
	}
	.time{
		padding: 0 15px;
		font-size: 16px;
		color: #fff;
	}
	.qrCode{
		position: absolute;
		bottom: 90px;
		left: 50%;
	}
	.qrCodeImg{
		width: 140px;
		border-radius: 
	}
	.qrContent{
		left: -50%;
		position: relative;
	}
	.content1{
		border:1px dashed #e5e5e5;
		border-width: 1px 0;
		padding: 10px 2px;
	}
</style>
</head>
<body class="padbtm70">
 	<div class="title">
    	${pd.couponsName }
    </div>
    <div class="content">
    <div class="content1">
    	<div class="name">${pd.couponsName }</div>
    	<div class="no">1101 5096 6851</div>
    	</div>
    </div>
    <div class="time1">优惠券类型<span style="float: right;color: #e8d098;font-weight: bold;font-size: 16px;">${pd.typeName }</span></div>
     <div class="time1">使用状态<span style="float: right;color: #e8d098;font-weight: bold;font-size: 16px;">${pd.stateDetailName }</span></div>
    <div class="time1">有效日期</div>
    <div class="time"><fmt:formatDate
				value="${pd.acquireDate}" pattern="yyyy.MM.dd" />-<fmt:formatDate
				value="${pd.endDate}" pattern="yyyy.MM.dd" /></div>
	<c:if test="${pd.useDate!=null && pd.useDate!=''}">
	<div class="time1">使用日期</div>
    <div class="time"><fmt:formatDate
				value="${pd.useDate}" pattern="yyyy-MM-dd HH:mm:ss" /></div>
	</c:if>
    <div class="qrCode">
    	<div class="qrContent">
    		<img class="qrCodeImg" src="<%=basePath%>${pd.qrCode}">
    	</div>
    </div>
	<jsp:include page="../../include/bottom.jsp" />
</body>
<script type="text/javascript"
	src="<%=basePath%>/static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript">
	$(function() {
		$("#nav3").addClass('current').siblings().removeClass('current');
	});
	
	var numbers = '${pd.numbers}';
	numbers = numbers.substring(0,4)+" "+numbers.substring(4,8)+" "+numbers.substring(8,12);
	$(".no").html(numbers);
</script>
</html>