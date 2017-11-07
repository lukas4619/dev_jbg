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
<title>我的收益</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/income.css" rel="stylesheet">
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
</head>
<body>
<div class="income">
    	<div class="title">
    		<span class="floatl">我的收益 (元)</span><a href="<%=basePath%>myRevenue/inrecord.html"><span class="floatr">明细</span></a>
    	</div>
    	<div class="count-number" id="count-number"  data-to="${pd.REVENUEMONEY}" data-speed="1500">0.00</div>
    </div>
    <div class="arrival">
        <div class="title1 triangle">到账收入 (元)</div>
        <div class="acontent">
            <div class="floatl2">
                <div class="money">${pd.BALANCEMONEY}</div>
                <div class="des">可提现</div>
            </div>
            <div class="floatr2">
                <div class="money">${pd.FREEZEMONEY}</div>
                <div class="des">冻结金额</div>
            </div>
        </div>
    </div>
    <a href="<%=basePath%>frontMember/goWithdraw.html">
	    <div class="cash">
	    	<span>提现</span>
	    </div>
    </a>
    <a href="<%=basePath%>myBalance/outrecord.html">
	    <div class="record">
	    	<span>提现记录</span>
	    </div>
    </a>
    <a href="<%=basePath%>myCoupon/index.html">
        <div class="coupon">
            <span>优惠券</span>
        </div>
    </a>
    <jsp:include page="../../include/bottom.jsp" />
</body>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/autoAdd.js"></script>
<script type="text/javascript">
$(function(){
	$("#nav3").addClass('current').siblings().removeClass('current');
});
</script>
</html>