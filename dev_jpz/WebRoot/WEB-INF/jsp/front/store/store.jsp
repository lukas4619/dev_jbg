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
<html style="font-size: 54px;">
<head lang="en">
<base href="<%=basePath%>">
  <meta name="viewport"
        content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no">
  <meta charset="UTF-8">
  <title>商城</title>
  <script src="<%=basePath%>/static/weixin/jpz/js/jquery-2.1.4.min.js"></script>
  <script src="<%=basePath%>/static/weixin/jpz/js/jquery.mobile-1.4.4.min.js"></script>
  <link rel="stylesheet" href="<%=basePath%>/static/weixin/jpz/css/index.css">
  <link rel="stylesheet" href="<%=basePath%>/static/weixin/jpz/font/iconfont.css">
</head>
<body class="" style="font-size: 12px;background:#fff">
<div id="main_plane" v-cloak>
  <div class="hd">
    <div class="h-title">
       <i class="iconfont icon-diliweizhi icon-dili">${pd.STORENAME}</i>
       <input type="hidden" id="STOREID" name="STOREID" value="${pd.ID}">
    </div>
    <div class="nav">
       <ul>
         <!-- 开始循环 -->
								<c:choose>
									<c:when test="${not empty shelvesList}">
										<c:forEach items="${shelvesList}" var="c" varStatus="vst">
										<c:choose>
										<c:when test="${vst.index==0}">
										 <li i="${vst.index}" data="${c.ID }" class="li-active">${c.SHELVESNAME }</li>
										</c:when>
										<c:otherwise>
										 <li i="${vst.index}" data="${c.ID }">${c.SHELVESNAME }</li>
										</c:otherwise>
										</c:choose>
										</c:forEach>
									</c:when>
								</c:choose>
         
         
       </ul>
    </div>
  </div>
  <!-- 菜单 -->
  <c:choose>
<c:when test="${not empty shelvesList}">
	<c:forEach items="${shelvesList}" var="s" varStatus="v">
	<span id="divPlace${s.ID}">
  </span>
	</c:forEach>
</c:when>
</c:choose>
  
  <!-- 商品列表 -->
  <div class="all_list" style="padding-bottom: 50px;">
  <c:choose>
<c:when test="${not empty shelvesList}">
	<c:forEach items="${shelvesList}" var="ss" varStatus="vv">
	<div id="plu${ss.ID}">
  </div>
	</c:forEach>
</c:when>
</c:choose>
  </div>
  <!-- 底部购物车 -->
  <div class="shopcar">
    <div class="price">
       <i class="iconfont icon-gouwuche car"></i>
       <span id="total">总计 ￥<a id="total">0</a></span>
    </div>
    <div class="btn" click="pay">去支付</div>
  </div>
</div>
</body>
<script src="<%=basePath%>/static/weixin/jpz/js/vue.min.js"></script>
<script src="<%=basePath%>/static/weixin/jpz/js/index.js"></script>
<script>
$(function(){
	findPlace();
  $(".nav>ul>li").click(function(){
    $('.nav').find('ul li').removeClass('li-active');
    $(this).addClass('li-active');
    findPlace();
  })
  // pluCode，pluCount，shelvesId，placeId&
  $(".add").click(function(){
	var placeid=  $(this).attr('placeid');//货位标识
	var price= toDecimal($(this).attr('price'));//售价
	var inventorycount = parseInt($(this).attr('c'));//当前商品可售数量
	var total =toDecimal($("#total").html());//商品总共金额
	var shelvesId = $(".li-active").attr('data');//当前货架
	var sumCount = parseInt($("#sum").html());//当前商品数量
	var placeNum = parseInt($("div"+placeid).html());//当前商品仓位数量
	if((sumCount+1)>inventorycount){
		return;
	}
	$("#total").html(total+price);
	//设置当前商品仓位数量
	$("div"+placeid).html(placeNum+1);
	$("div"+placeid).show();
	
  })
  $(".sub").click(function(){
	  var placeid=  $(this).attr('placeid');//货位标识
		var price= toDecimal($(this).attr('price'));//售价
		var inventorycount = parseInt($(this).attr('c'));//当前商品可售数量
		var total =toDecimal($("#total").html());//商品总共金额
		var shelvesId = $(".li-active").attr('data');//当前货架
		var sumCount = parseInt($("#sum").html());//当前商品数量
		var placeNum = parseInt($("div"+placeid).html());//当前商品仓位数量
		if((sumCount-1)<=0){
			return;
		}
		$("#total").html(total-price);
		//设置当前商品仓位数量
		if(placeNum-1==0){
			$("div"+placeid).hide();
		}else{
			$("div"+placeid).html(placeNum-1);
			$("div"+placeid).show();
		}
		
	  
  })
})

function toDecimal(x) { 
      var f = parseFloat(x); 
      if (isNaN(f)) { 
        return; 
      } 
      f = Math.round(x*100)/100; 
      return f; 
    } 

function findPlace(){
   var shelvesId=$(".li-active").attr("data");
   var storeId=$("#STOREID").val();
   $.ajax({
		type: "POST",
		url: '<%=basePath%>frontStore/findPlace.do?tm='+new Date().getTime(),
    	data: {shelvesId:shelvesId},
		dataType:'json',
		//beforeSend: validateData,
		cache: false,
		success: function(data){
			if(data.type==0){
				var placeIds = data.placeIds;
				var html ='';
				 $.each(data.list, function(i, list){
						html+='<ul class="menus" style="padding-bottom: 50px;"><li><a href="#l'+(i+1)+'" i='+(i+1)+' class="menu" onclick="aSkip($(this))" name='+list.PLACENAME+' data='+list.ID+'>'+list.PLACENAME+'</a></li></ul>';
						html+=' <div class="badge" id="div'+list.ID+'" style="display: none;">0</div>';
						 var h ='<div class="type" id="l1"><p class="title">'+list.PLACENAME+'</p><ul>';
						 $.each(list.pluList, function(i, pList){
								 	h+='<li><a style="display:block;"><div class="pic rel">';
									h+='<img src="/uploadFiles/uploadImgs/"></div><div class="intro">';//${pList.PLUIMAGE}
								    h+='<p class="p-title">"'+pList.PLUNAME+'"</p>';
									h+='<p>剩余: '+${pList.INVENTORYCOUNT}+'</p><div style="color:red;float: left">￥'+${pList.PRICE}+'</div>';
									h+='<div class="car-contol"><div class="sub" >';
									h+='<i class="iconfont icon-jian icon-sub"></i></div>';
									h+='<div class="sum" >0</div><div class="add" >';
									h+='<i class="iconfont icon-jia icon-add"></i></div></div></div></a></li>';
							
						 })
						 h+='</ul></div>';
						 $("#plu"+shelvesId).html(h);
						 alert($("#plu"+shelvesId).html());
						
				 });
				 $("#divPlace"+shelvesId).html(html);
				 timeId = window.setTimeout("skipHref()", 200);
				 
			}else{
				alert(data.msg);
			}
		}
	});
   
}


</script>
</html>