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
<title>打赏页面</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="" rel="stylesheet">
<style type="text/css">
*{
	font-size: 16px;
}
.content{
	padding: 0px 10px;
	background-color: #fff;
}
.title{
	font-size: 10px;
	color: #999;
}
.rewardCnt{
	
}
.rewardCnt ul{
	margin: 0;
	padding:0 ;
}
.rewardCnt ul li{
	list-style: none;
	float: left;
	width: 30%;
	text-align: center;
	color: green;
	border: 1px solid green;
	margin-left: 3%;
	box-sizing: border-box;
	margin-top: 10px;
	border-radius: 3px;
	padding: 10px 0;
	font-weight: bold;
}
.rewardCnt ul li.active{
	color: #fff;
	background-color: green;
}
.otherMoney{
	width: 100px;
	border: 1px solid green;
	height: 40px;
	border-radius: 3px;
	outline: none;
	line-height: 30px;
	font-size: 14px;
	padding-left: 5px;
}
.rewardCnt2{
	margin-top: 10px;
	margin-left: 3%;
	text-align: center;
}
.resubmit{
	width: 96%;
    margin-left: 2%;
    margin-top: 25px;
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
.resubmit:active{
	background:#008000;
}
#changeOther{
	color: green;
	margin-top: 20px;
}
#submit{
	margin-top: 30px;
}
</style>
</head>
<body>
<div class="content">
    <div class="title">选择打赏金额</div>
    <div id="rewardCnt" class="rewardCnt">
    	<ul id="selmoney">
	    		<c:forEach items="${statesList }" var="list">
		    		<li>
		    			<span class="money">${list.NAME }</span>
		    		</li>
	    		</c:forEach>
    	</ul>
    </div>
    <input type="hidden" name="lastMoney">	
    <input type="hidden" value="${pd.ARTICLEID }" name="ARTICLEID">
    <div style="clear: both"></div>
    <div class="title" id="changeOther" class="changeOther" >选择其它打赏金额</div>
    <div class="rewardCnt2" style="display: none">
    	<input type="number" onmousemove="checkNum(this);" maxlength="10" min="0" class="otherMoney" placeholder="请输入与金额">	
    </div>
    <button id="wcPay" class="resubmit" onclick="tods();" >立即打赏</button>
</div>

</body>
<script type="text/javascript" src="../static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="../static/weixin/js/common.js"></script>
<script type="text/javascript">
	$(function(){
		/*金额切换*/
		$("#rewardCnt li").click(function(){
			$(".rewardCnt2").slideUp();
			$(this).addClass("active").siblings().removeClass('active');
			$("input[name='lastMoney']").val(parseInt($(this).find('span').text()));
		})
		/*自定义金额*/
		$("#changeOther").click(function(){
			var flag=$(".rewardCnt2")[0].style.display;
			$("input[name='lastMoney']").val(0);
			if(flag=="none"){
				$(".rewardCnt2").slideDown();
				$("#rewardCnt li").removeClass('active');
			}else{
				$(".rewardCnt2").slideUp();
			}
			
		})
	})
	
	function checkNum(the){
		clearNoNum(the);
	}
	
	function tods(){
		//得到打赏金额
		var m = $("ul >li[class='active']").find("span").text();
		if(m==''||m==undefined){
			m=$("input[class='otherMoney']").val();
			if(m==''||m==undefined||m<=0){
				alert("请选择金额");
				return
			}
		}
		var ARTICLEID = $("input[name='ARTICLEID']").val();
		location.href="<%=basePath%>weChatPay/index.html?ARTICLEID="+ARTICLEID+"&money="+ encodeURI(encodeURI(m));
	}
	
</script>
</html>