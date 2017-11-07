<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<title>提交成功</title>
<script type="text/javascript"
	src="<%=basePath%>static/weixin/js/jquery-1.7.2.js"></script>
<style type="text/css">
.tips {
	margin-top: 30px;
}

.txtcenter {
	text-align: center;
}

.success {
	width: 76px;
}

.green {
	color: green;
	font-size: 22px;
	margin-top: 10px;
}

.btn-group {
	width: 100%;
	margin-top: 20px;
}

button {
	margin-top: 25px;
	border-radius: 3px;
	font-size: 16px;
	background: #08A600;
	color: white;
	padding: 5px 0;
	outline: none;
	letter-spacing: 1px;
	outline: medium;
	-webkit-appearance: none;
	border: 1px solid #08A600;
	margin-left: 6%;
	width: 40%;
	box-sizing: border-box;
}

button.submit:active {
	background: #008000;
}

button.back a {
	text-decoration: none;
	color: #999
}

button.back {
	background-color: #f5f5f5;
	border: 1px solid #f5f5f5;
	color: #999;
}

.pricecnt {
	background-color: white;
	width: 100%;
	height: auto;
	padding: 10px 20px;
	float: left;
	box-sizing: border-box;
	margin-top: 15px;
}

.pricecnt span.pcleft {
	float: left;
	color: #666;
}

.pricecnt span.pcright {
	float: right;
}

.pricecnt span {
	display: inline-block;
}
</style>
</head>
<body style="background-color: #f5f5f5;">
	<div class="content">
		<div class="tips txtcenter">
			<img class="success"
				src="<%=basePath%>static/weixin/images/chenggong.png">
			<div class="green">提交成功,等待管理员审核！</div>
		</div>
		<div class="pricecnt">
			<span class="pcleft">文章标题</span> <span class="pcright"
				style="font-weight: bold;">${pd.TITLE } </span>
		</div>
		<div class="pricecnt">
			<span class="pcleft">审核状态</span> <span class="pcright"
				style="color: #f60;font-weight: bold;">审核中</span>
		</div>
		<div class="pricecnt">
			<span class="pcleft">提交时间</span> <span class="pcright">
			<fmt:formatDate value="${pd.CREATEDATE}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</span>
		</div>
		<div class="btn-group">
			<button class="back">
				<a href="<%=basePath%>myArticle/goDetails.html?id=${pd.ARTICLEID}">查看文章审核进度</a>
			</button>
			<button class="submit" id="wcPay">
			返回我的文章
			</button>

		</div>
	</div>
</body>
<script type="text/javascript">
	$("#wcPay").click(function() {
		window.location.href="<%=basePath%>myArticle/index.html";
	});
</script>
</html>
