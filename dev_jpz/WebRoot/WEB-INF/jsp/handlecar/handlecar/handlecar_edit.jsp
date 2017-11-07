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
					
					<form action="handlecar/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<input type="hidden" name="CREATEDATE" id="CREATEDATE" value="${pd.CREATEDATE}"/>
						<input type="hidden" name="HANDLEDATE" id="HANDLEDATE" value="${pd.HANDLEDATE}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">原始卡号:</td>
								<td>
								<c:choose>
								<c:when test="${msg=='edit'}">
								<input type="hidden" name="CARNUM" id="CARNUM" value="${pd.CARNUM}"/>
								<input type="text"  disabled="disabled"  value="${pd.CARNUM}" maxlength="6"  style="width:98%;"/>
								</c:when>
								<c:otherwise>
								<input type="text" name="CARNUM"  id="CARNUM" value="${pd.CARNUM}" maxlength="6" placeholder="这里输入原始卡号" title="原始卡号" style="width:98%;"/>
								</c:otherwise>
								</c:choose>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">补发卡号:</td>
								<td><input type="text" name="NEWCARNUM" id="NEWCARNUM" value="${pd.NEWCARNUM}" maxlength="6" placeholder="这里输入补发卡号" title="补发卡号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">归属:</td>
								<td><input type="text" name="STORENAME" id="STORENAME" value="${pd.STORENAME}" maxlength="50" placeholder="这里输入归属" title="归属" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">状态:</td>
								<td><input type="text" name="STATUS" id="STATUS" value="${pd.STATUS}" maxlength="50" placeholder="这里输入状态" title="状态" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">会员名称:</td>
								<td><input type="text" name="VIPNAME" id="VIPNAME" value="${pd.VIPNAME}" maxlength="200" placeholder="这里输入会员名称" title="会员名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">联系方式:</td>
								<td><input type="text" name="CONTACT" id="CONTACT" value="${pd.CONTACT}" maxlength="50" placeholder="这里输入联系方式" title="联系方式" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="REMARKS" id="REMARKS" value="${pd.REMARKS}" maxlength="200" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">发放面额:</td>
								<td><input type="number" max="500" min="0" name="DENOMINATION" id="DENOMINATION" value="${pd.DENOMINATION}" maxlength="32" placeholder="这里输入发放面额" title="发放面额" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">已使用金额:</td>
								<td><input type="number" max="500" min="0" name="USEMONEY" id="USEMONEY" value="${pd.USEMONEY}" maxlength="32" placeholder="这里输入已使用金额" title="已使用金额" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">剩余金额:</td>
								<td><input type="number" max="500" min="0" name="SURPLUSMONEY" id="SURPLUSMONEY" value="${pd.SURPLUSMONEY}" maxlength="32" placeholder="这里输入剩余金额" title="剩余金额" style="width:98%;"/></td>
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
			if($("#CARNUM").val()==""){
				$("#CARNUM").tips({
					side:3,
		            msg:'请输入原始卡号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CARNUM").focus();
			return false;
			}
		 if($("#NEWCARNUM").val()==""){
				$("#NEWCARNUM").tips({
					side:3,
		            msg:'请输入补发卡号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NEWCARNUM").focus();
			return false;
			}
			if($("#STORENAME").val()==""){
				$("#STORENAME").tips({
					side:3,
		            msg:'请输入归属',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STORENAME").focus();
			return false;
			}
			if($("#STATUS").val()==""){
				$("#STATUS").tips({
					side:3,
		            msg:'请输入状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STATUS").focus();
			return false;
			}
			if($("#DENOMINATION").val()==""){
				$("#DENOMINATION").tips({
					side:3,
		            msg:'请输入发放面额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DENOMINATION").focus();
			return false;
			}
			if($("#USEMONEY").val()==""){
				$("#USEMONEY").tips({
					side:3,
		            msg:'请输入已使用金额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USEMONEY").focus();
			return false;
			}
			if($("#SURPLUSMONEY").val()==""){
				$("#SURPLUSMONEY").tips({
					side:3,
		            msg:'请输入剩余金额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SURPLUSMONEY").focus();
			return false;
			}
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