﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
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
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
</head>
<body class="padbtm70">
<div class="nolist txtcenter">
    	<img src="<%=basePath%>/static/weixin/images/tips.png">
    	<div>没有收益记录哦~!</div>
    </div>

    <div class="haslist">
        <div class="detailsTitle">我的收益</div>
    	<header>
	    	<div class="width1">获得时间</div>
	    	<div class="width2">收益金额</div>
	    	<div class="width3">收益状态</div>
    	</header>
    	<div class="lists">
    		<ul>
    		</ul>
    	</div>
    	<div class="loadding txtcenter"><img class="loadImg Imghide" src="<%=basePath%>/static/weixin/images/loadding.gif"><span class="loadText">上拉加载更多</span></div>
        <input type="hidden" id="hideCurrentPage" value="1" />
         <input type="hidden" id="hidetotalPage" value="1" />
         </div>
          <jsp:include page="../../include/bottom.jsp" />
</body>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript">
var param={};
var isEnd=false;
var ShowCount=15;
function listPage(currentPage){
	var totalPage = parseInt($("#hidetotalPage").val());
	if(currentPage>totalPage){
		$(".loadText").text('没有更多记录啦~');
		return;
	}
	isEnd =true;
	param={};
	param["CurrentPage"] = currentPage;
	param["ShowCount"] = ShowCount;
	$.post("<%=basePath%>myRevenue/listPage.do",param,function(data){
			var list = data.data;
			if(list=='' || typeof(list) == "undefined"){
				if(currentPage==1){
				  $(".nolist").show();
				  $(".haslist").hide();
				  $(".loadding").hide();
				}
				$(".loadText").text('没有更多收益啦~');
				return;
			}
			$(".nolist").hide();
			$("#hidetotalPage").val(data.totalPage);////总页数
			var totalResult=parseInt(data.totalResult)//总条数
			var pageHtml="";
			for(var i=0;i<list.length;i++){
				pageHtml+='<a style="color: #777;" href="<%=basePath%>myRevenue/goDetails?id='+list[i].revenueID+'">';
				pageHtml+='<li><span class="width1">'+(new Date(list[i].revenueDate)).format("yyyy-MM-dd hh:mm:ss")+'</span>';
				pageHtml+='<span class="width2" style="color: #f60;font-weight: bold;">￥'+list[i].revenueMoney+'</span><span class="width3">';
				pageHtml+='<a class="blue" href="<%=basePath%>myRevenue/goDetails?id='+list[i].revenueID+'">'+list[i].typeName+'</a></span></li>';
				pageHtml+='</a>';
			}
			$(".lists ul ").append(pageHtml);
			$("#hideCurrentPage").val(parseInt(currentPage)+1);
			//如果列表条数大于、等于总条数提示没有更多记录
			if($(".lists ul li").length>=totalResult){
				$(".loadText").text('没有更多收益记录啦~');
				return;
			}
			isEnd = false;
		});
	
}

$(function(){
	$("#nav3").addClass('current').siblings().removeClass('current');
	listPage(1);
});
/*监听加载更多*/  
$(window).scroll(function(){
	var isEnd=false;
    if(isEnd == true){
        return;
    }
    // 核心代码
    if ($(document).height() - $(this).scrollTop() - $(this).height()<=0){
    	$(".loadImg").addClass("Imgshow").removeClass('Imghide')
    	$(".loadText").text('加载中..');
    	setTimeout(function(){
    		$(".loadText").text('上拉加载更多');
    		listPage($("#hideCurrentPage").val());
	        $(".loadImg").addClass("Imghide").removeClass('Imgshow')
    		
	       },1500)
      
    }
});
/** 
 * 时间对象的格式化; 
 */  
Date.prototype.format = function(format) {  
    /* 
     * eg:format="yyyy-MM-dd hh:mm:ss"; 
     */  
    var o = {  
        "M+" : this.getMonth() + 1, // month  
        "d+" : this.getDate(), // day  
        "h+" : this.getHours(), // hour  
        "m+" : this.getMinutes(), // minute  
        "s+" : this.getSeconds(), // second  
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S" : this.getMilliseconds()  
        // millisecond  
    }  
  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4  
                        - RegExp.$1.length));  
    }  
    for (var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1  
                            ? o[k]  
                            : ("00" + o[k]).substr(("" + o[k]).length));  
        }  
    }  
    return format;  
} 
</script>
</html>