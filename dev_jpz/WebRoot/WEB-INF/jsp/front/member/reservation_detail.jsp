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
<meta name = "format-detection" content = "telephone=no">
<title>我的预订--预订详情</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
</head>
<body class="padbtm70">
  <div class="detailsTitle">预定详情</div>
   <div class="bigdiv" >
		<label class="width4 colorgrey">预定编号：</label>
		<span class="width5 txtright">${pd.reservationNumber}</span>
	</div>
	<div class="bigdiv" >
		<label class="width4 colorgrey">预定类型：</label>
		<span class="width5 txtright">${pd.typeName}</span>
	</div>
	<div class="bigdiv" >
		<label class="width4 colorgrey">预定状态：</label>
		<span class="width5 txtright" style="color: #f60;font-weight: bold;">${pd.stateName}</span>
	</div>
	<div class="bigdiv" >
		<label class="width4 colorgrey">支付金额：</label>
		<span class="width5 txtright" style="color: #f60;font-weight: bold;">${pd.advanceMoney}</span>
	</div>
	<div class="bigdiv" >
		<label class="width4 colorgrey">支付状态：</label>
		<span class="width5 txtright" style="color: #f60;font-weight: bold;">
		<c:choose>
		<c:when test="${pd.payState==0}">
		等待付款
		</c:when>
		<c:when test="${pd.payState==1}">
		已付款
		</c:when>
		<c:when test="${pd.payState==2}">
		取消
		</c:when>
		<c:when test="${pd.payState==3}">
		已退款
		</c:when>
		<c:otherwise>
		未知
		</c:otherwise>
		</c:choose>
		</span>
	</div>
		<c:choose>
		<c:when test="${pd.payState==1}">
		<div class="bigdiv" >
		<label class="width4 colorgrey">付款时间：</label>
		<span class="width5 txtright"><fmt:formatDate value="${pd.payDate}" pattern="yyyy-MM-dd HH:mm:ss"  /></span>
		</div>
		</c:when>
		<c:otherwise>
		
		</c:otherwise>
		</c:choose>
	
	<div class="bigdiv" >
		<label class="width4 colorgrey">联系人：</label>
		<span class="width5 txtright">${pd.reservationName}</span>
	</div>
	<div class="bigdiv" >
		<label class="width4 colorgrey">联系方式：</label>
		<span class="width5 txtright">${pd.reservedNumber}</span>
	</div>
    <div class="bigdiv" >
		<label class="width4 colorgrey">预定时间：</label>
		<span class="width5 txtright"><fmt:formatDate value="${pd.createDate}" pattern="yyyy-MM-dd HH:mm"  /></span>
	</div>
	<div class="bigdiv" >
		<label class="width4 colorgrey">预定有效期：</label>
		<span class="width5 txtright"><fmt:formatDate value="${pd.validityDate}" pattern="yyyy-MM-dd"  /></span>
	</div>
	
	<div class="detailsTitle top40">套餐内容</div>
    <table>
    	<thead>
    	</thead>
    	<tbody>
    		<tr>
    			<td>套餐类型</td>
    			<td>${pd.proClassName}</td>
    		</tr>
    		<tr>
    			<td>套餐名称</td>
    			<td>${pd.proName}</td>
    		</tr>
    		<tr>
    			<td>套餐原价</td>
    			<td><span style="color: #f60;font-weight: bold;">${pd.proMoney}</span></td>
    		</tr>
    		<tr>
    			<td>套餐预订金额</td>
    			<td><span style="color: #f60;font-weight: bold;">${pd.advanceMoney}</span></td>
    		</tr>
    	</tbody>
    </table>
    <jsp:include page="../../include/bottom.jsp" />
</body>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript">
$(function(){
	$("#nav1").addClass('current').siblings().removeClass('current');
});
</script>
</html>