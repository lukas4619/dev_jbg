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
<title>我的优惠券</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
<style type="text/css">
	*{
		box-sizing: border-box
	}
	body{
		background-color: #fff;
	}
	.hasList{
		padding: 10px 15px;
	}
	.hasList ul li{
		overflow: auto;
		list-style: none;
		font-size: 14px;
		-webkit-box-shadow: 0 0 15px rgba(0,0,0,0.3);
    	-moz-box-shadow: 0 0 15px rgba(0,0,0,0.3);
    	box-shadow: 0 0 15px rgba(0,0,0,0.3);
		background-color: #fff;
		margin-top: 12px;
		border-radius: 3px;
	}
	.floatl{
		width: 70%;
		float: left;
		box-sizing: border-box;
		padding: 10px 10px;
	}
	.floatr{
		width: 30%;
	    float: left;
	    box-sizing: border-box;
	    background: url(images/package.png) no-repeat left;
	    background-color: #eb4f38;
	    background-position-x: 10px;
	    height: 81px;
	    font-size: 23px;
	    padding-top: 25px;
	    padding-right: 10px;
	    text-align: right;
	    color: #fff;
	}
	.floatrUse{
		width: 30%;
	    float: left;
	    box-sizing: border-box;
	    background: url(images/package.png) no-repeat left;
	    background-color: #999;
	    background-position-x: 10px;
	    height: 81px;
	    font-size: 23px;
	    padding-top: 25px;
	    padding-right: 10px;
	    text-align: right;
	    color: #fff;
	}
	@media only  screen and (max-width: 320px) {
	    .floatr {
	        background-size:22px;
	        font-size: 20px;
	        height: 75px;
	        padding-top: 24px;
	  	}
	  	.floatl{
			padding: 8px 10px;
		}
  	}
	.date{
		font-size: 12px;
		color: #999;
		margin-top: 5px;
	}
	.loadding{
		background-color: #fff;
		display: none;
	}
	.name{
		height: 38px;
		overflow: hidden;
	}
</style>
</head>
<body class="padbtm70">
<div class="nolist txtcenter">
    	<img src="<%=basePath%>/static/weixin/images/tips.png">
    	<div>您还没有优惠券哦!</div>
    </div>
    <div class="hasList">
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
var ShowCount=8;
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
	$.post("<%=basePath%>myCoupon/listPage.do",param,function(data){
			var list = data.data;
			if(list=='' || typeof(list) == "undefined"){
				if(currentPage==1){
				  $(".nolist").show();
				  $(".hasList").hide();
				  $(".loadding").hide();
				}
				$(".loadText").text('没有更多优惠券啦~');
				return;
			}
			$(".nolist").hide();
			$("#hidetotalPage").val(data.totalPage);////总页数
			var totalResult=parseInt(data.totalResult)//总条数
			var pageHtml="";
			for(var i=0;i<list.length;i++){
				pageHtml+='<a style="color: #777;" href="<%=basePath%>myCoupon/goDetails?id='+list[i].id+'"><li>';
				pageHtml+='<div class="floatl">';
				pageHtml+='<div class="name">'+list[i].couponsName+'</div>';
				pageHtml+='<div class="date">有效期限:'+(new Date(list[i].acquireDate)).format("yyyy.MM.dd")+'-'+(new Date(list[i].endDate)).format("yyyy.MM.dd")+'</div></div>';
				var stateId=parseInt(list[i].stateId);
				if(stateId==24){
					pageHtml+='<div class="floatr">￥'+list[i].denomination+'</div></li></a>';
				}else{
					pageHtml+='<div class="floatrUse">￥'+list[i].denomination+'</div></li></a>';
				}
				
			}
			$(".hasList ul ").append(pageHtml);
			$("#hideCurrentPage").val(parseInt(currentPage)+1);
			//如果列表条数大于、等于总条数提示没有更多记录
			if($(".hasList ul li").length>=totalResult){
				$(".loadText").text('没有更多优惠券啦~');
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