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
<title>我的预定列表</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="../static/weixin/css/common.css" rel="stylesheet">
</head>
<body>
    <div class="nolist txtcenter">
    	<img src="../static/weixin/images/tips.png">
    	<div>无相关数据!</div>
    </div>

    <div class="haslist">
        <div class="detailsTitle">我的预定</div>
    	<header>
	    	<div class="width1">预定时间</div>
	    	<div class="width2">预定金额</div>
	    	<div class="width3">详细</div>
	    	<input type="hidden" value="${openId }" name="openId">
	    	<input type="hidden" id="hideCurrentPage" value="1">
    	</header>
    	<div class="lists">
    		<ul>
<!--     			<li><span class="width1">2016-07-18 16:56:01</span><span class="width2">￥66.00</span><span class="width3"><a class="blue" href="orderdetail.html">详细</span></a></li> -->
<!--     			<li><span class="width1">2016-07-18 16:56:01</span><span class="width2">￥66.00</span><span class="width3"><a class="blue" href="orderdetail.html">详细</span></a></li> -->
    		</ul>
    	</div>
    	<div class="loadding txtcenter"><img class="loadImg Imghide" src="../static/weixin/images/loadding.gif"><span class="loadText">上拉加载更多</span></div>
         </div>

</body>
<script type="text/javascript" src="../static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript">
	var isEnd=false;
	var pageNo= 1;
	var pageSize = 15;
	/*监听加载更多*/  
    $(window).scroll(function(){
        if(isEnd == true){
            return;
        }
        // 核心代码
        if ($(document).height() - $(this).scrollTop() - $(this).height()<=0){
        	
//         	setTimeout(function(){
//         		 for(var i=0;i<3;i++){
// 		           	$(".lists ul ").append('<li><span class="width1">2016-07-18 16:56:01</span><span class="width2">￥66.00</span><span class="width3"><a class="blue" href="orderdetail.html">详细</span></a></li>');
// 		           }
// 		        $(".loadImg").addClass("Imghide").removeClass('Imgshow')
//         		$(".loadText").text('上拉加载更多');
// 		       },1500)
	          	ajaxReservatePage($("#hideCurrentPage").val());
//           $(".lists ul ").append('<li><span class="width1">2016-07-18 16:56:01</span><span class="width2">￥66.00</span><span class="width3"><a class="blue" href="orderdetail.html">详细</span></a></li>');
          
        }
    });

	$(function(){
		ajaxReservatePage(1);
	});
	
	function ajaxReservatePage(pageNo){
		isEnd = true;
		var param = {};
		param['currentPage']=pageNo;
		param['showCount']=pageSize;
		param['memberId']=$("input[name='openId']").val();
		
		$.post("<%=basePath%>frontReservate/ajaxReservatePage.do",param,function(data){
			var list = data.data;
			var total = data.total;
			$(".nolist").hide();
			var pageHtml="";
			for(var i=0;i<list.length;i++){
				pageHtml+='<li><span class="width1">'+(new Date(list[i].createDate)).format("yyyy-MM-dd hh:mm:ss")+'</span><span class="width2">￥'+list[i].proMoney+'</span><span class="width3"><a class="blue" href="<%=basePath%>frontReservate/goToOrderDetail.html?RESERVATIONID='+list[i].reservationID+'">详细</span></a></li>';
			}
			
			var page = total%pageSize==0?(total%pageSize):(total/pageSize+1);
			if(page<=pageNo){
				$(".loadImg").addClass("Imgshow").addClass('Imghide');
				$(".loadText").text('没有更多的数据了...');
				isEnd = true;
			}else{
				$(".loadImg").addClass("Imghide").removeClass('Imgshow')
        		$(".loadText").text('上拉加载更多');
				isEnd = false;
			}
			
			$(".haslist ul ").append(pageHtml);
			$("#hideCurrentPage").val(pageNo*1+1);
			//isEnd = false;
		});
	}
	
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