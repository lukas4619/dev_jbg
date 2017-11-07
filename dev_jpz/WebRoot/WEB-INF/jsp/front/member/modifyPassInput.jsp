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
<title>输入密码</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
<style type="text/css">
	*{
		font-size: 14px;
	}
	body{
		background:#f5f5f5;
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
		padding: 5px 15px;
		background-color: #fff;
		overflow:hidden;
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
	.passWord,.repassWord{
		width: 70%;
		height: 40px;
		border-radius: 4px;
		box-sizing: border-box;
		padding-left: 5px;
		float: left;
	}
	.width30{
		width: 30%;
		box-sizing: border-box;
		display: inline-block;
		float: left;
		line-height: 40px;
		font-size: 16px;
		padding-left: 10px;
	}
	.clearfix{
		clear: both;
	}
</style>
</head>
<body>
	<div class="detailsTitle">修改密码</div>
	<!-- <div class="bindtips">
	</div> -->
	<div class="content top25">
		<span class="width30">密码:</span>
		<input  class="passWord" placeholder="请输入密码" name="passWord" type="password" maxlength="8">
		<div class="errormsg">*密码不能为空</div>
	</div>
	<div class="content top25">
		<span class="width30">重复密码:</span>
		<input   class="repassWord" placeholder="请重复输入密码" name="repassWord" type="password" maxlength="8">
		<div class="errormsg">*密码不能为空</div>
	</div>
	<button class="bind">确定</button>
	<jsp:include page="../../include/bottom.jsp" />
</body>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/common.js"></script>
<script type="text/javascript">
var param = {};
$(function(){
	
	$(".bind").click(function(){
		var passWord=$(".passWord").val();
		if(isNull(passWord)){
			$(".passWord").siblings('.errormsg').text('*密码不能为空').show();
			return false;
		}else{
			$(".passWord").siblings('.errormsg').text('').show();
		}
		
		var repassWord=$(".repassWord").val();
		if(isNull(repassWord)){
			$(".repassWord").siblings('.errormsg').text('*密码不能为空').show();
			return false;
		}else{
			$(".repassWord").siblings('.errormsg').text('').show();
		}
		
		if(passWord!=repassWord){
			$(".repassWord").siblings('.errormsg').text('*2次输入的密码不一致').show();
			return false;
		}else{
			$(".repassWord").siblings('.errormsg').text('').show();
		}
		
		param = {};
		param['passWord']=passWord;
		var url ="<%=basePath%>frontMember/modifyPass.do";
		$.post(url,param,function(data){
			if(data.type==1){
				location.href="<%=basePath%>frontMember/index.html";
			}else{
				$(".repassWord").siblings('.errormsg').text(data.msg).show();
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
	var time = 120;
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