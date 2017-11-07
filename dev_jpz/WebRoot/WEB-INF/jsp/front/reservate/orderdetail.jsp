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
<title>预定详情</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="../static/weixin/css/common.css" rel="stylesheet">
</head>
<body>
	<div class="detailsTitle">预定详情</div>
    <div class="bigdiv" >
		<label class="width4 colorgrey">预定时间：</label>
		<span class="width5 txtright"><fmt:formatDate value="${pd.CREATEDATE }" pattern="yyyy-MM-dd HH:mm:ss"/></span>
	</div>
	<div class="bigdiv" >
		<label class="width4 colorgrey">联系人：</label>
		<span class="width5 txtright">${pd.RESERVATIONNAME }</span>
	</div>
	<div class="detailsTitle top40">预定详情</div>
    <table>
    	<thead>
    		<tr>
    			<th>套餐名称</th>
    			<th>${pd.PRONAME}</th>
    		</tr>
    	</thead>
    	<tbody>
    		<tr>
    			<td>套餐原价</td>
    			<td>${pd.PROMONEY }</td>
    		</tr>
    		<tr>
    			<td>套餐预定金额</td>
    			<td>${pd.ADVANCEMONEY }</td>
    		</tr>
    	</tbody>
    </table>
</body>
</html>