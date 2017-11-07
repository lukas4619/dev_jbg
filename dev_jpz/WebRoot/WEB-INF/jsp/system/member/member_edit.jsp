<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
					
					<form action="member/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="MEMBERID" id="MEMBERID" value="${pd.MEMBERID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">用户类型:</td>
								<td>
									<c:if test="${pd.MEMBERTYPE==1 }">作者</c:if>
									<c:if test="${pd.MEMBERTYPE==2 }">普通用户</c:if>
								 </td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">OPENID:</td>
								<td>
									${pd.OPENID}
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">微信昵称:</td>
								<td>
									${pd.WECHATNAME}
								</td>
							</tr>
							
							<!-- 作者字段start -->
							<c:if test="${pd.MEMBERTYPE ==1 }">
								<tr>
									<td style="width:250px;text-align: right;padding-top: 13px;">软文作者浏览量收益比例(%):</td>
									<td><input type="number" min="0" name="REVENUEPV" id="REVENUEPV"  value="${pd.REVENUEPV}" maxlength="32" placeholder="这里输入软文作者浏览量收益比例" title="软文作者浏览量收益比例" style="width:98%;"/></td>
								</tr>
								<tr>
									<td style="width:250px;text-align: right;padding-top: 13px;">软文作者点赞收益比例(%):</td>
									<td><input type="number" min="0" name="REVENUELIKE" id="REVENUELIKE"  value="${pd.REVENUELIKE}" maxlength="32" placeholder="这里输入软文作者点赞收益比例" title="软文作者点赞收益比例" style="width:98%;"/></td>
								</tr>
								<tr>
									<td style="width:250px;text-align: right;padding-top: 13px;">软文作者获取公众号关注收益比例(%):</td>
									<td><input type="number"  min="0" name="REVENUESUB" id="REVENUESUB"  value="${pd.REVENUESUB}" maxlength="32" placeholder="这里输入软文作者获取公众号关注收益比例" title="软文作者获取公众号关注收益比例" style="width:98%;"/></td>
								</tr>
								<tr>
									<td style="width:250px;text-align: right;padding-top: 13px;">软文作者预订消费成功收益比例(%):</td>
									<td><input type="number" min="0" name="REVENUECON" id="REVENUECON"  value="${pd.REVENUECON}" maxlength="32" placeholder="这里输入软文作者预订消费成功收益比例" title="软文作者预订消费成功收益比例" style="width:98%;"/></td>
								</tr>
							</c:if>
							<!-- 作者字段end -->
							
							<!-- 普通用户start -->
							<c:if test="${pd.MEMBERTYPE ==2 }">
								<tr>
									<td style="width:250px;text-align: right;padding-top: 13px;">用户分享软文预订消费成功收益比例(%):</td>
									<td><input type="number" min="0" name="REVENUECONM" id="REVENUECONM"  value="${pd.REVENUECONM}" maxlength="32" placeholder="用户分享软文预订消费成功收益比例" title="用户分享软文预订消费成功收益比例" style="width:98%;"/></td>
								</tr>
								<tr>
									<td style="width:250px;text-align: right;padding-top: 13px;">用户分享软文获得公众号关注收益比例(%):</td>
									<td><input type="number"  min="0" name="REVENUESUBM" id="REVENUESUBM"  value="${pd.REVENUESUBM}" maxlength="32" placeholder="用户分享软文获得公众号关注收益比例" title="用户分享软文获得公众号关注收益比例" style="width:98%;"/></td>
								</tr>
								<tr>
									<td style="width:250px;text-align: right;padding-top: 13px;">用户分享软文浏览收益比例(%):</td>
									<td><input type="number" min="0" name="REVENUEPVM" id="REVENUEPVM"  value="${pd.REVENUEPVM}" maxlength="32" placeholder="用户分享软文浏览收益比例" title="用户分享软文浏览收益比例" style="width:98%;"/></td>
								</tr>
							</c:if>
							<!-- 普通用户start -->
							
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">性别:</td>
								<td>
									<c:if test="${pd.SEX==1}">男</c:if>
									<c:if test="${pd.SEX==2}">女</c:if>
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">城市:</td>
								<td>
									${pd.CITY}
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">头像:</td>
								<td>
									<img alt="图片" src="${pd.HEADIMGURL}" width="70px;" height="50px;">
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">关注时间:</td>
								<td>
									${pd.SUBSCRIBETIME}
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">备注:</td>
								<td>
									${pd.REMARKS}
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td>
									${pd.CREATEDATE}
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">微信同步时间:</td>
								<td>
								${pd.SYNCDATE}
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">上次登录ip:</td>
								<td>
									${pd.LASTIP}
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">最近登录时间:</td>
								<td>
									${pd.LASTDATE}
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">历史收益:</td>
								<td>
								${pd.REVENUEMONEY}
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">可结算金额:</td>
								<td>
									${pd.BALANCEMONEY}
								</td>
							</tr>
							<tr>
								<td style="width:250px;text-align: right;padding-top: 13px;">关注状态:</td>
								<td>
									<c:if test="${pd.SUBSCRIBE==1}">已关注</c:if>
									<c:if test="${pd.SUBSCRIBE==0}">未关注</c:if>
								</td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr>
						</table>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
					</form>
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.page-content -->
		</div>
	</div>
	<!-- /.main-content -->
</div>
<!-- /.main-container -->


	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		//保存
		function save(){
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>