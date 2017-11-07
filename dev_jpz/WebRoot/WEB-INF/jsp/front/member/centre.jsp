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
<title>账户信息</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
<style type="text/css">
	.header{
		height: 48px;
    	line-height: 48px;
	}
	.headedImg{
		width: 48px;
	}
</style>
</head>
<body>
  <div class="detailsTitle">账户信息</div>
  <div class="bigdiv" >
		<label class="width6 colorgrey header" >头像：</label>
		<img class="headedImg" src="${pd.HEADIMGURL }">
	</div>
    <div class="bigdiv" >
		<label class="width6 colorgrey">昵称：</label>
		<span class="width7">${pd.WECHATNAME }</span>
	</div>
	<div class="bigdiv" >
		<label class="width6 colorgrey">性别：</label>
		<span class="width7">
		<c:choose>
		<c:when test="${pd.SEX==1 }">
		男
		</c:when>
		<c:otherwise>
		女
		</c:otherwise>
		</c:choose>
		</span>
	</div>
	<div class="bigdiv" >
		<label class="width6 colorgrey">地区：</label>
		<span class="width7">${pd.COUNTRY }${pd.CITY }</span>
	</div>
	<div class="bigdiv" >
		<label class="width6 colorgrey">上次登录时间：</label>
		<span class="width7"><fmt:formatDate value="${pd.LASTDATE}" pattern="yyyy-MM-dd HH:mm:ss"  /></span>
	</div>
	<c:choose>
	<c:when test="${pd.VERIFYPHONENUMBER==1 }">
	<!-- 已经绑定 -->
	<div class="bigdiv" >
		<label class="width6 colorgrey">手机号码：</label>
		<a href="goVerify.html" class="width7 change">${pd.PHONENUMBER }</a>
	</div>
	</c:when>
	<c:otherwise>
	<!-- 未绑定 -->
	<div class="bigdiv" >
		<label class="width6 colorgrey">手机号码：</label>
		<a href="goVerify.html" class="width7 change1 color9c">立即绑定</a>
	</div>
	</c:otherwise>
	</c:choose>
	<c:if test="${pd.MEMBERTYPE == 1 }">
		<div class="bigdiv" >
			<label class="width6 colorgrey">修改密码：</label>
			<a href="goModifyPass.html" class="width7 changePwd"></a>
		</div>
	</c:if>
	<jsp:include page="../../include/bottom.jsp" />
</body>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript">
$(function(){
	$("#nav4").addClass('current').siblings().removeClass('current');
});
</script>
</html>