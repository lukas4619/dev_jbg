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
					
					<form action="broadband/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:125px;text-align: right;padding-top: 13px;">所属:</td>
								<td><input type="text" name="ATTACH" id="ATTACH" value="${pd.ATTACH}" maxlength="50" placeholder="这里输入所属" title="所属" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:125px;text-align: right;padding-top: 13px;">名称:</td>
								<td><input type="text" name="WBNAME" id="WBNAME" value="${pd.WBNAME}" maxlength="100" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:125px;text-align: right;padding-top: 13px;">宽带编号:</td>
								<td><input type="text" name="WBNUMBER" id="WBNUMBER" value="${pd.WBNUMBER}" maxlength="30" placeholder="这里输入宽带编号" title="宽带编号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:125px;text-align: right;padding-top: 13px;">用户名:</td>
								<td><input type="text" name="USERNAME" id="USERNAME" value="${pd.USERNAME}" maxlength="50" placeholder="这里输入用户名" title="用户名" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:125px;text-align: right;padding-top: 13px;">密码:</td>
								<td><input type="text" name="USERPASSWORD" id="USERPASSWORD" value="${pd.USERPASSWORD}" maxlength="100" placeholder="这里输入密码" title="密码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:125px;text-align: right;padding-top: 13px;">宽带运营商:</td>
								<td><input type="text" name="WBTYPE" id="WBTYPE" value="${pd.WBTYPE}" maxlength="30" placeholder="这里输入宽带运营商" title="宽带运营商" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:125px;text-align: right;padding-top: 13px;">带宽:</td>
								<td><input type="text" name="BW" id="BW" value="${pd.BW}" maxlength="20" placeholder="这里输入带宽" title="带宽" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:125px;text-align: right;padding-top: 13px;">到期时间:</td>
								<td><input type="text" name="EXPIREDATE" id="EXPIREDATE" value="${pd.EXPIREDATE}" maxlength="20" placeholder="这里输入到期时间" title="到期时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:125px;text-align: right;padding-top: 13px;">联系人:</td>
								<td><input type="text" name="CONTACTS" id="CONTACTS" value="${pd.CONTACTS}" maxlength="20" placeholder="这里输入联系人" title="联系人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:125px;text-align: right;padding-top: 13px;">联系方式:</td>
								<td><input type="text" name="CSTEL" id="CSTEL" value="${pd.CSTEL}" maxlength="20" placeholder="这里输入联系方式" title="联系方式" style="width:98%;"/></td>
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
			if($("#ATTACH").val()==""){
				$("#ATTACH").tips({
					side:3,
		            msg:'请输入所属',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ATTACH").focus();
			return false;
			}
			if($("#WBNAME").val()==""){
				$("#WBNAME").tips({
					side:3,
		            msg:'请输入名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#WBNAME").focus();
			return false;
			}
			if($("#WBNUMBER").val()==""){
				$("#WBNUMBER").tips({
					side:3,
		            msg:'请输入宽带编号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#WBNUMBER").focus();
			return false;
			}
			if($("#USERNAME").val()==""){
				$("#USERNAME").tips({
					side:3,
		            msg:'请输入用户名',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USERNAME").focus();
			return false;
			}
			if($("#USERPASSWORD").val()==""){
				$("#USERPASSWORD").tips({
					side:3,
		            msg:'请输入密码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USERPASSWORD").focus();
			return false;
			}
			if($("#WBTYPE").val()==""){
				$("#WBTYPE").tips({
					side:3,
		            msg:'请输入宽带运营商',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#WBTYPE").focus();
			return false;
			}
			if($("#BW").val()==""){
				$("#BW").tips({
					side:3,
		            msg:'请输入带宽',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BW").focus();
			return false;
			}
			if($("#EXPIREDATE").val()==""){
				$("#EXPIREDATE").tips({
					side:3,
		            msg:'请输入到期时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#EXPIREDATE").focus();
			return false;
			}
			if($("#CONTACTS").val()==""){
				$("#CONTACTS").tips({
					side:3,
		            msg:'请输入联系人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONTACTS").focus();
			return false;
			}
			if($("#CSTEL").val()==""){
				$("#CSTEL").tips({
					side:3,
		            msg:'请输入联系方式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CSTEL").focus();
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