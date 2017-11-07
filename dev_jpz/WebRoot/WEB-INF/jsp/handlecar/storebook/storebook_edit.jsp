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
					
					<form action="storebook/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<input type="hidden" name="CREATEDATE" id="CREATEDATE" value="${pd.CREATEDATE}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">门店:</td>
								<td><input type="text" name="STORENAME" id="STORENAME" value="${pd.STORENAME}" maxlength="100" placeholder="这里输入门店" title="门店" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">远程设备名称:</td>
								<td><input type="text" name="DEVNAME" id="DEVNAME" value="${pd.DEVNAME}" maxlength="100" placeholder="这里输入远程设备名称" title="远程设备名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">远程工具名称:</td>
								<td><input type="text" name="REMOTENAME" id="REMOTENAME" value="${pd.REMOTENAME}" maxlength="100" placeholder="这里输入远程工具名称" title="远程工具名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">账号:</td>
								<td><input type="text" name="REMOTEUSER" id="REMOTEUSER" value="${pd.REMOTEUSER}" maxlength="50" placeholder="这里输入账号" title="账号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">密码:</td>
								<td><input type="text" name="REMOTEPASSWORD" id="REMOTEPASSWORD" value="${pd.REMOTEPASSWORD}" maxlength="100" placeholder="这里输入密码" title="密码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">门店电话:</td>
								<td><input type="text" name="STORETEL" id="STORETEL" value="${pd.STORETEL}" maxlength="20" placeholder="这里输入门店电话" title="门店电话" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">店长:</td>
								<td><input type="text" name="STOREMANAGER" id="STOREMANAGER" value="${pd.STOREMANAGER}" maxlength="50" placeholder="这里输入店长" title="店长" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">店长工号:</td>
								<td><input type="text" name="MANAGERNUMBER" id="MANAGERNUMBER" value="${pd.MANAGERNUMBER}" maxlength="20" placeholder="这里输入店长工号" title="店长工号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">店长联系方式:</td>
								<td><input type="text" name="MANAGERPHONE" id="MANAGERPHONE" value="${pd.MANAGERPHONE}" maxlength="20" placeholder="这里输入店长联系方式" title="店长联系方式" style="width:98%;"/></td>
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
			if($("#STORENAME").val()==""){
				$("#STORENAME").tips({
					side:3,
		            msg:'请输入门店',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STORENAME").focus();
			return false;
			}
			if($("#DEVNAME").val()==""){
				$("#DEVNAME").tips({
					side:3,
		            msg:'请输入远程设备名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEVNAME").focus();
			return false;
			}
			if($("#REMOTENAME").val()==""){
				$("#REMOTENAME").tips({
					side:3,
		            msg:'请输入远程工具名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REMOTENAME").focus();
			return false;
			}
			if($("#REMOTEUSER").val()==""){
				$("#REMOTEUSER").tips({
					side:3,
		            msg:'请输入账号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REMOTEUSER").focus();
			return false;
			}
			if($("#REMOTEPASSWORD").val()==""){
				$("#REMOTEPASSWORD").tips({
					side:3,
		            msg:'请输入密码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REMOTEPASSWORD").focus();
			return false;
			}
			if($("#STORETEL").val()==""){
				$("#STORETEL").tips({
					side:3,
		            msg:'请输入门店电话',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STORETEL").focus();
			return false;
			}
			if($("#STOREMANAGER").val()==""){
				$("#STOREMANAGER").tips({
					side:3,
		            msg:'请输入店长',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOREMANAGER").focus();
			return false;
			}
			if($("#MANAGERNUMBER").val()==""){
				$("#MANAGERNUMBER").tips({
					side:3,
		            msg:'请输入店长工号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MANAGERNUMBER").focus();
			return false;
			}
			if($("#MANAGERPHONE").val()==""){
				$("#MANAGERPHONE").tips({
					side:3,
		            msg:'请输入店长联系方式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MANAGERPHONE").focus();
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