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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no;">
<meta name="viewport" content="initial-scale=1,target-densitydpi=device-dpi,minimum-scale=1,maximum-scale=1,user-scalable=1" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<title>Demo: play flv video</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=basePath%>static/weixin/js/jquery-1.10.2.js"></script>
</head>
<body style="overflow-x:hidden;">
	 <div id="playercontainer"></div>
    <script type="text/javascript" src="<%=basePath%>static/player/cyberplayer.js"></script>
    <script type="text/javascript">
        var player = cyberplayer("playercontainer").setup({
            width: 320,
            height: 320,
            stretching: "uniform",
            file: "http://live.366315.net/testapp/1111.flv",
            autostart: true,
            repeat: false,
            volume: 90,
            controls: true,
            ak: '87f03f734e234899b6417dc76506e1eb' // 公有云平台注册即可获得accessKey
        });
    </script>
    <%--
    http://demo-video.bj.bcebos.com/AdrenalineRush.flv
	--%>
</body>

</html>