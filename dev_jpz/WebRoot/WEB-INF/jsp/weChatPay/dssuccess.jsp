<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<title>提交成功</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="" rel="stylesheet">
<style type="text/css">
.tips{
    margin-top: 30px;
}
.txtcenter{
    text-align: center;
}
.success{
    width: 76px;
}
.green{
    color: green;
    font-size: 22px;
    margin-top: 10px;
}
.btn-group{
    width: 100%;
    margin-top: 20px;
}
button{
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
button.submit:active{
    background:#008000;
}
button.back a{
    text-decoration: none;
    color:#999
}
button.back{
    background-color: #f5f5f5;
    border: 1px solid #f5f5f5;
    color: #999;
}
.textcenter{
    text-align: center;
}
a.skip{
    width: 44%;
    margin-left: 28%;
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
    text-decoration: none;
    display: inline-block;
    text-align: center;
}
</style>
</head>
<body>
    <div class="content">
        <div class="tips txtcenter">
            <img class="success" src="<%=basePath %>static/weixin/images/chenggong.png">
            <div class="green">
            	<c:if test="${pd.status==1 }">打赏成功</c:if>
            	<c:if test="${pd.status==0 }">打赏失败</c:if>
            </div>
        </div>
        <a id="skip" class="skip " href="<%=basePath%>news/index.do?id=${pd.ARTICLEID}&date=<%=new Date()%>">5秒后自动跳转</a>
    </div>
</body>
<script type="text/javascript">
    var time=5;
    var myInterval=setInterval(function(){
        var skip=document.getElementById('skip');
        skip.innerHTML=(time--)+"秒后自动跳转";
        if(time==-1){
        	clearInterval(myInterval);
        	document.getElementById("skip").html="正在跳转..";
            location.href="<%=basePath%>news/index.do?id=${pd.ARTICLEID}&date=new Date()";
        }
    },1000)
</script>
</html>