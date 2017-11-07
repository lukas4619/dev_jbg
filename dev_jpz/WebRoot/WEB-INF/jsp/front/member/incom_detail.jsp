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
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<title>我的收益--收益详情</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
</head>
<body>
  <div class="detailsTitle">收益详情</div>
   <div class="bigdiv" >
		<label class="width4 colorgrey">收益类型：</label>
		<span class="width5 txtright">${pd.typeName}</span>
	</div>
	<div class="bigdiv" >
		<label class="width4 colorgrey">收益状态：</label>
		<span class="width5 txtright" style="color: #f60;font-weight: bold;">${pd.stateName}</span>
	</div>
	<div class="bigdiv" >
		<label class="width4 colorgrey">收益金额：</label>
		<span class="width5 txtright" style="color: #f60;font-weight: bold;">${pd.revenueMoney}</span>
	</div>
	<div class="bigdiv" >
		<label class="width4 colorgrey">收益来源：</label>
		<span class="width5 txtright">${pd.revenueSource}</span>
	</div>
    <div class="bigdiv" >
		<label class="width4 colorgrey">收益时间：</label>
		<span class="width5 txtright"><fmt:formatDate value="${pd.revenueDate}" pattern="yyyy-MM-dd HH:mm:ss"  /></span>
	</div>
	
    <jsp:include page="../../include/bottom.jsp" />
</body>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript">
$(function(){
	$("#nav3").addClass('current').siblings().removeClass('current');
});
</script>
</html>