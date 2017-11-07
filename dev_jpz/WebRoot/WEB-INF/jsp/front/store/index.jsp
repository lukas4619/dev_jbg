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
  <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no">
  <meta charset="UTF-8">
  <title>${cf.ATTRVALTHREE}</title>
  <script src="<%=basePath%>/static/weixin/jpz/js/jquery-2.1.4.min.js"></script>
  <script src="<%=basePath%>/static/weixin/jpz/js/jquery.mobile-1.4.4.min.js"></script>
  <link rel="stylesheet" href="<%=basePath%>/static/weixin/jpz/css/index.css">
  <link rel="stylesheet" href="<%=basePath%>/static/weixin/jpz/font/iconfont.css">
</head>

<body class="" style="font-size: 12px;background:#fff">
<form action="frontStore/goPayment.do" name="Form" id="Form" method="post">
<div data-role="page" data-ajax="true">
<div id="main_plane">
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
	<ul class="menus one${v.index+1}" id="menus${s.ID }" shelvesId="${s.ID}" style="padding-bottom: 50px;">
	      <c:choose>
	      <c:when test="${not empty s.placeList}">
	      <c:forEach items="${s.placeList}" var="p" varStatus="ps">
	       <li>
		        <!-- <a  place="a${p.ID}"  href="#l${ps.index+1}" storeId="${p.STOREID}"   shelvesId="${p.SHELVESID}"  class="menu" onclick="aSkip($(this))">${p.PLACENAME}</a> -->
		           <a  placeid="${p.ID}"  storeId="${p.STOREID}"   shelvesId="${p.SHELVESID}"  class="menu" onclick="aSkip($(this))">${p.PLACENAME}</a>
		       <div class="badge" style="display: none;" id="divss${p.ID}"><span id="spanss${p.ID}" storeId="${p.STOREID}"   shelvesId="${p.SHELVESID}">0</span></div>
		    </li>
	      </c:forEach>
	      
	      </c:when>
	      </c:choose>
	</ul>
	</c:forEach>
</c:when>
</c:choose>
  <!-- 商品列表 -->
  
   <c:choose>
	<c:when test="${not empty shelvesList}">
	<c:forEach items="${shelvesList}" var="sl" varStatus="vs">
	  <c:choose>
		<c:when test="${vs.index==0}">
		 <div class="all_list list${vs.index+1}" id="list${sl.ID }" shelvesId="${sl.ID }" style="padding-bottom: 50px;">
		     <c:forEach items="${sl.placeList}" var="ppp" varStatus="ppps">
		       <div class="type" id="a${ppp.ID}">
				<p class="title">${ppp.PLACENAME}</p>
    			<ul>
    			<c:forEach items="${ppp.plList}" var="plp" varStatus="plps">
    			<li>
		       		 <a style="display:block;">
			          <div class="pic rel">
			            <img src="<%=basePath%>uploadFiles/uploadImgs/${plp.PLUIMAGE}">
			          </div>
			          <div class="intro">
			            <p class="p-title">${plp.PLUNAME}</p>
			            <p>剩余:${plp.INVENTORYCOUNT}</p>
			            <div style="color:red;float: left">￥${plp.PRICE}</div>
			            <div class="car-contol">
			               <c:choose>
							<c:when test="${plp.INVENTORYCOUNT>0}">
							 <div id="divsub${plp.ID}" style="display:none;"  class="sub" placepluId="${plp.ID}" inventorycount="${plp.INVENTORYCOUNT}" price="${plp.PRICE}" shelvesId="${plp.SHELVESID}" placeId="${plp.PLACEID}" pluCode="${plp.PLUCODE }">
				                <i class="iconfont icon-jian icon-sub"></i>
				              </div>
				              <div class="sum" id="sum${plp.ID}" style="display:none;">0</div>
				              <div id="divadd${plp.ID}" class="add" placepluId="${plp.ID}" inventorycount="${plp.INVENTORYCOUNT}" price="${plp.PRICE}" shelvesId="${plp.SHELVESID}" placeId="${plp.PLACEID}" pluCode="${plp.PLUCODE }">
				                <i class="iconfont icon-jia icon-add"></i>
				              </div>
							</c:when>
							<c:otherwise></c:otherwise>
							</c:choose>
							
			             
			            </div>
			          </div>
			         </a>
    			 </li>
    			</c:forEach>
    			</ul>
    			</div>
    			
		     </c:forEach>
		  </div>
		</c:when>
		<c:otherwise>
		<div class="all_list list${vs.index+1}" id="list${sl.ID }" shelvesId="${sl.ID }" style="padding-bottom: 50px;display: none"> 
		 <c:forEach items="${sl.placeList}" var="ppp" varStatus="ppps">
		       <div class="type" id="a${ppp.ID}">
				<p class="title">${ppp.PLACENAME}</p>
    			<ul>
    			<c:forEach items="${ppp.plList}" var="plp" varStatus="plps">
    			<li>
		       		 <a style="display:block;">
			          <div class="pic rel">
			            <img src="<%=basePath%>uploadFiles/uploadImgs/${plp.PLUIMAGE}">
			          </div>
			          <div class="intro">
			            <p class="p-title">${plp.PLUNAME}</p>
			            <p>剩余:${plp.INVENTORYCOUNT}</p>
			            <div style="color:red;float: left">￥${plp.PRICE}</div>
			            <div class="car-contol">
			               <c:choose>
							<c:when test="${plp.INVENTORYCOUNT>0}">
							 <div id="divsub${plp.ID}" style="display:none;" class="sub" placepluId="${plp.ID}" inventorycount="${plp.INVENTORYCOUNT}" price="${plp.PRICE}" shelvesId="${plp.SHELVESID}" placeId="${plp.PLACEID}" pluCode="${plp.PLUCODE }">
				                <i class="iconfont icon-jian icon-sub"></i>
				              </div>
				              <div class="sum" id="sum${plp.ID}" style="display:none;">0</div>
				              <div id="divadd${plp.ID}" class="add" placepluId="${plp.ID}" inventorycount="${plp.INVENTORYCOUNT}" price="${plp.PRICE}" shelvesId="${plp.SHELVESID}" placeId="${plp.PLACEID}" pluCode="${plp.PLUCODE }">
				                <i class="iconfont icon-jia icon-add"></i>
				              </div>
							</c:when>
							<c:otherwise></c:otherwise>
							</c:choose>
							
			            </div>
			          </div>
			         </a>
    			 </li>
    			</c:forEach>
    			</ul>
    			</div>
    			
		     </c:forEach>
		</div>
		</c:otherwise>
		</c:choose>
	</c:forEach>
</c:when>
</c:choose>
  
  <!-- 底部购物车 -->
  <div class="shopcar">
    <div class="price">
       <i class="iconfont icon-gouwuche car"></i>
       <span>总计 ￥<span id="total" style="color:red;">0</span>
       <c:if test="${pd.VERIFYFAVOURABLE>0}">
       <span>-<a style="color: orange;">  ${pd.VERIFYFAVOURABLE}</a></span>
       </c:if>
       </span>
    </div>
    <div class="btn" id="pay">去支付</div>
    <input type="hidden" id="TOTALPRICE" name="TOTALPRICE" value="${pd.TOTALPRICE}">
    <input type="hidden" id="STOREID" name="STOREID" value="${pd.ID}">
    <input type="hidden" id="sCar" name="sCar" value="${pd.sCar}">
    <input type="hidden" id="PLUCOUNT" name="PLUCOUNT"  value="${pd.PLUCOUNT}">
     <input type="hidden" id="subUrl" name="subUrl"  value="<%=basePath%>">
     <input type="hidden" id="VERIFYFAVOURABLE" name="VERIFYFAVOURABLE"  value="${pd.VERIFYFAVOURABLE}">
   
  </div>
 
</div>

</div>
 </form>

</body>

<script src="<%=basePath%>/static/weixin/jpz/js/vue.min.js"></script>
<script src="<%=basePath%>/static/weixin/jpz/js/index.js"></script>
<!--<script src="<%=basePath%>/static/weixin/jpz/js/store/store_back.js"></script> -->
<script src="<%=basePath%>/static/weixin/jpz/js/store/store.js"></script>
</html>