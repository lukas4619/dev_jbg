<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<footer class="myfooter">
	<!-- nav include  -->
	<div class="nav">
		<c:choose>
			<c:when test="${sessionScope.MEMBER.memberType==1}">
				<a id="nav1" href="<%=basePath%>myReservation/index.html?date=<%= new Date()%>">
					<div class="nav-botttom">
						<img class="navimg "
							src="<%=basePath%>static/weixin/images/orderImg.png"> <img
							class="navimg1"
							src="<%=basePath%>static/weixin/images/orderImg1.png">
						<div>预订</div>
					</div> </a>
				<a id="nav2" href="<%=basePath%>myArticle/index.html?date=<%= new Date()%>">
					<div class="nav-botttom">
						<img class="navimg "
							src="<%=basePath%>static/weixin/images/articleImg.png"> <img
							class="navimg1"
							src="<%=basePath%>static/weixin/images/articleImg1.png">
						<div>文章</div>
					</div> </a>
				<a id="nav3" href="<%=basePath%>myRevenue/index.html?date=<%= new Date()%>">
					<div class="nav-botttom">
						<img class="navimg "
							src="<%=basePath%>static/weixin/images/incomeImg.png"> <img
							class="navimg1 "
							src="<%=basePath%>static/weixin/images/incomeImg1.png">
						<div>收益</div>
					</div> </a>
				<a id="nav4" href="<%=basePath%>frontMember/index.html?date=<%= new Date()%>">
					<div class="nav-botttom">
						<img class="navimg "
							src="<%=basePath%>static/weixin/images/infoImg.png"> <img
							class="navimg1 "
							src="<%=basePath%>static/weixin/images/infoImg1.png">
						<div>我的</div>
					</div> </a>
			</c:when>
			<c:otherwise>
			<a id="nav1" href="<%=basePath%>myReservation/index.html?date=<%= new Date()%>">
								<div class="nav-botttom1">
									<img class="navimg "
										src="<%=basePath%>static/weixin/images/orderImg.png"> <img
										class="navimg1"
										src="<%=basePath%>static/weixin/images/orderImg1.png">
									<div>预订</div>
								</div> </a>
								<a id="nav3" href="<%=basePath%>myRevenue/index.html?date=<%= new Date()%>">
					<div class="nav-botttom1">
						<img class="navimg "
							src="<%=basePath%>static/weixin/images/incomeImg.png"> <img
							class="navimg1 "
							src="<%=basePath%>static/weixin/images/incomeImg1.png">
						<div>收益</div>
					</div> </a>
				<a id="nav4" href="<%=basePath%>frontMember/index.html?date=<%= new Date()%>">
					<div class="nav-botttom1">
						<img class="navimg "
							src="<%=basePath%>static/weixin/images/infoImg.png"> <img
							class="navimg1 "
							src="<%=basePath%>static/weixin/images/infoImg1.png">
						<div>我的</div>
					</div> </a>
			</c:otherwise>
		</c:choose>

	</div>
</footer>