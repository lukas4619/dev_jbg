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
<title>我的提现--提现详情</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
</head>
<body class="padbtm70">
	<div class="detailsTitle">提现详情</div>
	<div class="bigdiv">
		<label class="width4 colorgrey">提现编号：</label> <span
			class="width5 txtright">${pd.balanceNumber}</span>
	</div>
	<div class="bigdiv">
		<label class="width4 colorgrey">提现类型：</label> <span
			class="width5 txtright">${pd.typeName}</span>
	</div>
	<div class="bigdiv">
		<label class="width4 colorgrey">提现状态：</label> <span
			class="width5 txtright" style="color: #f60;font-weight: bold;">${pd.name}</span>
	</div>
	<div class="bigdiv">
		<label class="width4 colorgrey">提现金额：</label> <span
			class="width5 txtright" style="color: #f60;font-weight: bold;">${pd.balanceMoney}</span>
	</div>
	<div class="bigdiv">
		<label class="width4 colorgrey">预定时间：</label> <span
			class="width5 txtright"><fmt:formatDate
				value="${pd.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
		</span>
	</div>

	<div class="detailsTitle top40">审核进度</div>
	<table>
		<thead>
			<tr>
			<th>时间</th>
				<th>内容</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty varList}">
					<c:forEach items="${varList}" var="var" varStatus="vs">
					<tr>
					<td>
						<span style="color:#00bb9c;"><fmt:formatDate value="${var.CREATEDATE}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
						</td>
						<td>
						${var.DETAILS} 
						</td>
					</tr>
					</c:forEach>
				</c:when>
			</c:choose>
			
		</tbody>
	</table>
	<jsp:include page="../../include/bottom.jsp" />
</body>
<script type="text/javascript"
	src="<%=basePath%>/static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript">
	$(function() {
		$("#nav3").addClass('current').siblings().removeClass(
				'current');
	});
</script>
</html>