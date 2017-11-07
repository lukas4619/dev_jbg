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
					
					<form action="store/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<input type="hidden" name="CREATEDATE" id="CREATEDATE" value="${pd.CREATEDATE}"/>
						<input type="hidden" name="ENCODER" id="ENCODER" value="${pd.ENCODER}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;">名称:</td>
								<td><input type="text" name="STORENAME" id="STORENAME" value="${pd.STORENAME}" maxlength="50" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;">区域:</td>
								<td><input type="text" name="STOREDISTRICT" id="STOREDISTRICT" value="${pd.STOREDISTRICT}" maxlength="200" placeholder="这里输入区域" title="区域" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;">地址:</td>
								<td><input type="text" name="STOREADDRESS" id="STOREADDRESS" value="${pd.STOREADDRESS}" maxlength="500" placeholder="这里输入地址" title="地址" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;">门店负责人:</td>
								<td><input type="text" name="STOREMANAGER" id="STOREMANAGER" value="${pd.STOREMANAGER}" maxlength="50" placeholder="这里输入门店负责人" title="门店负责人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;">门店负责人联系方式:</td>
								<td><input type="text" name="MANAGERCONTACT" id="MANAGERCONTACT" value="${pd.MANAGERCONTACT}" maxlength="200" placeholder="这里输入门店负责人联系方式" title="门店负责人联系方式" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;">微信标识:</td>
								<td><input type="text" name="OPENID" id="OPENID" value="${pd.OPENID}" maxlength="50" placeholder="这里输入微信标识" title="微信标识" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;">状态:</td>
								<td>
								<select
											class="chosen-select form-control" data="${pd.STORESTATUSID }"
											name="STORESTATUSID" id="STORESTATUSID" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">门店状态</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty storeStatusList}">
															<c:forEach items="${storeStatusList}" var="c" varStatus="vst">
																<option value="${c.ID }">${c.NAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
										</select>
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
			if($("#STORENAME").val()==""){
				$("#STORENAME").tips({
					side:3,
		            msg:'请输入名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STORENAME").focus();
			return false;
			}
			if($("#STOREDISTRICT").val()==""){
				$("#STOREDISTRICT").tips({
					side:3,
		            msg:'请输入区域',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOREDISTRICT").focus();
			return false;
			}
			if($("#STOREADDRESS").val()==""){
				$("#STOREADDRESS").tips({
					side:3,
		            msg:'请输入地址',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOREADDRESS").focus();
			return false;
			}
			if($("#STOREMANAGER").val()==""){
				$("#STOREMANAGER").tips({
					side:3,
		            msg:'请输入门店负责人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOREMANAGER").focus();
			return false;
			}
			if($("#MANAGERCONTACT").val()==""){
				$("#MANAGERCONTACT").tips({
					side:3,
		            msg:'请输入门店负责人联系方式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MANAGERCONTACT").focus();
			return false;
			}
			if($("#OPENID").val()==""){
				$("#OPENID").tips({
					side:3,
		            msg:'请输入微信标识',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#OPENID").focus();
			return false;
			}
			
			if($("#STORESTATUSID").val()==""){
				$("#STORESTATUSID").tips({
					side:3,
		            msg:'请选择门店状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STORESTATUSID").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
			if($("#STORESTATUSID").attr('data')!='' && $("#STORESTATUSID").attr('data') !=-1){
				$("#STORESTATUSID").val($("#STORESTATUSID").attr('data'));
			}
		});
		</script>
</body>
</html>