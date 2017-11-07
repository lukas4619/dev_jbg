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
<title>提现</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
<link href="<%=basePath%>/static/weixin/css/getCash.css" rel="stylesheet">
</head>
<body>
  <div class="cashType">
    	<ul>
    		<!-- 开始循环 -->
						<c:choose>
														<c:when test="${not empty typeList}">
															<c:forEach items="${typeList}" var="t" varStatus="vst">
															<c:if test="${vst.index==0 }">
															    <li class="current"  data-type="${t.ID }">${t.TYPENAME }</li>
																<input id="cashType" type="hidden" value="${t.ID }">
															</c:if>
															<c:if test="${vst.index>0 }">
															<li  data-type="${t.ID }">${t.TYPENAME }</li>
															</c:if>
																
															</c:forEach>
														</c:when>
													</c:choose>							
    	</ul>
    </div>
    
    <div class="getCash">
    	<div class="tips">提现金额</div>
    	<div class="cashcnt font32">
    		<label class="font28 widtha">￥</label><input id="balanceMoney" maxlength="5" class="cashinput widthb" type="number" max="${pd.BALANCEMONEY}" min="1" >
    	</div>
    	<div class="cashcnt2">
    		<div class="floatl">可用余额 <span id="usefulNum">${pd.BALANCEMONEY }</span>	</div>
    		<a id="allcash" class="allcash floatr" href="javascript:;">全部提现</a>
    	</div>
    	<div class="cashcnt2 colorred" id="subWithdrawMsg">
    	</div>
    </div>
    <button id="subWithdraw">确认提现</button>
    <jsp:include page="../../include/bottom.jsp" />
</body>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/getCash.js"></script>
<script type="text/javascript">
var param={};
$(function(){
	$("#nav3").addClass('current').siblings().removeClass('current');
});

$("#subWithdraw").click(function() {
	var balanceType = $("#cashType").val();
	var balanceMoney =$("#balanceMoney").val();
	if(balanceMoney=='' || typeof(balanceMoney) == "undefined"){
		$("#subWithdrawMsg").html('请输入提现金额');
		return;
	}
	balanceMoney=parseFloat(balanceMoney).toFixed(2); 
	if(balanceMoney<=0){
		$("#subWithdrawMsg").html('输入提现金额有误');
		return;
	}
	if(balanceMoney-1<0){
		$("#subWithdrawMsg").html('提现金额不能小于1元');
		return;
	}
	$("#subWithdrawMsg").html('');
	param={};
	param["balanceType"] = balanceType;
	param["balanceMoney"] = balanceMoney;
	$.post("<%=basePath%>frontMember/subWithdraw.do",param,function(data){
			if(data=='' || typeof(data) == "undefined"){
				$("#subWithdrawMsg").html('网络繁忙，请稍后！');
				return;
			}
			if(data.type==1){
				window.location.href="<%=basePath%>frontMember/withdrawFinish.html?id="+data.id;
			}else{
				$("#subWithdrawMsg").html(data.msg);
			}
		
		});
	
});
</script>
</html>