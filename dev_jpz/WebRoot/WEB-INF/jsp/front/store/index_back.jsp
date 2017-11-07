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
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- 下拉框 -->
<link rel="stylesheet" href="<%=basePath%>/static/ace/css/chosen.css" />
<!-- 日期框 -->
<link rel="stylesheet" href="<%=basePath%>/static/ace/css/datepicker.css" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=1.0" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>手机微信订餐购物车结算模板 - 源码之家</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/weixin/css/swiper.min.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/weixin/css/style1.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/weixin/css/index1.css">
</head>
<body>

<div class="swiper-container">
    <div class="swiper-wrapper">
        <div class="swiper-slide"><img src="<%=basePath%>/static/weixin/images/index/shop_1.jpg"></div>
        <div class="swiper-slide"><img src="<%=basePath%>/static/weixin/images/index/shop_1.jpg"></div>
        <div class="swiper-slide"><img src="<%=basePath%>/static/weixin/images/index/shop_1.jpg"></div>
    </div>
</div>

<div class="nav-lf">
<ul id="nav">
  <li class="current"><a href="#st1">分类一</a><b></b></li>
  <li><a href="#st2">分类二</a><b>1</b></li>
  <li><a href="#st3">分类三</a><b>3</b></li>
  <li><a href="#st4">分类四</a><b>6</b></li>
</ul>
</div>

<div id="container" class="container">

  <div class="section" id="st1">
  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品1</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>
    
  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_2.jpg"></div>
        <div class="lt-ct">
        	<p>商品1</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>

  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_3.jpg"></div>
        <div class="lt-ct">
        	<p>商品1</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>

  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_4.jpg"></div>
        <div class="lt-ct">
        	<p>商品1</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>

  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_5.jpg"></div>
        <div class="lt-ct">
        	<p>商品1</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>
    
  </div>
    
    

  
  <div class="section" id="st2">
  
  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品2</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>
    
  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品2</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>

  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品2</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>

  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品2</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>

  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品2</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>
    
  </div>
  
  <div class="section" id="st3">
  
  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品3</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>
    
  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品3</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>

  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品3</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>

  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品3</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>

  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品3</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>
    
  </div>
  
  <div class="section" id="st4">
  
  	<div class="prt-lt">
    	<div class="lt-lt"><img src="<%=basePath%>/static/weixin/images/index/prt_1.jpg"></div>
        <div class="lt-ct">
        	<p>商品4</p>
            <p class="pr">¥<span class="price">60.00</span></p>
        </div>
        <div class="lt-rt">
        	<input type="button" class="minus"  value="-">
        	<input type="text" class="result" value="0">
        	<input type="button" class="add" value="+">
        </div>
    </div>
      
  </div>
  
</div>
</div>
</div>
<footer>
	<div class="ft-lt">
        <p>合计:<span id="total" class="total">163.00元</span><span class="nm">(<label class="share"></label>份)</span></p>
    </div>
    <div class="ft-rt">
    	<p id="goPay">选好了</p>
    </div>
</footer>


<script type="text/javascript" src="<%=basePath%>/static/weixin/js/Adaptive.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/swiper.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/jquery.nav.js"></script>
<script type="text/javascript">
var swiper = new Swiper('.swiper-container', {
	pagination: '.swiper-pagination',
	paginationClickable: true,
	spaceBetween: 30,
});

$(function(){
	$('#nav').onePageNav();
});

</script>
<script> 
$(function(){ 
	
	$("#goPay").click(function(){
		window.location.href="<%=basePath%>frontStore/goPayment.html";
   })

$(".add").click(function(){
var t=$(this).parent().find('input[class*=result]'); 
t.val(parseInt(t.val())+1);
setTotal(); 
})
 
$(".minus").click(function(){ 
var t=$(this).parent().find('input[class*=result]'); 
t.val(parseInt(t.val())-1);
if(parseInt(t.val())<0){ 
t.val(0); 
} 
setTotal(); 


})
 
 
 
function setTotal(){ 
var s=0;
var v=0;
var n=0;
<!--计算总额--> 
$(".lt-rt").each(function(){ 
s+=parseInt($(this).find('input[class*=result]').val())*parseFloat($(this).siblings().find('span[class*=price]').text()); 

});

<!--计算菜种-->
var nIn = $("li.current a").attr("href");
$(nIn+" input[type='text']").each(function() {
	if($(this).val()!=0){
		n++;
	}
});

<!--计算总份数-->
$("input[type='text']").each(function(){
	v += parseInt($(this).val());
});
if(n>0){
	$(".current b").html(n).show();		
	}else{
	$(".current b").hide();		
		}	
$(".share").html(v);
$("#total").html(s.toFixed(2)); 
} 
setTotal(); 

}) 
</script> 
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/waypoints.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/weixin/js/navbar2.js"></script>
</body>
</html>