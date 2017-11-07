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
					
					<form action="device/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<input type="hidden" name="CREATEDATE" id="CREATEDATE" value="${pd.CREATEDATE}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">设备类型:</td>
								<td>
								<select class="chosen-select form-control" data="${pd.DEVICETYPE }" name="DEVICETYPE" id="DEVICETYPE" data-placeholder="请选择" style="vertical-align:top;width: 120px;">
									<option value="-1">设备类型</option>
									<!-- 开始循环 -->
									<c:choose>
									<c:when test="${not empty typeList}">
										<c:forEach items="${typeList}" var="c" varStatus="vst">		    
										<option <c:if test="${pd.DEVICETYPE == c.ID}"> selected = "selected" </c:if> value="${c.ID }">${c.NAME }</option>
										</c:forEach>
										</c:when>
									</c:choose>
								</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">设备名称:</td>
								<td><input type="text" name="DEVICENAME" id="DEVICENAME" value="${pd.DEVICENAME}" maxlength="50" placeholder="这里输入设备名称" title="设备名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">设备归属:</td>
								<td>
								<select class="chosen-select form-control" data="${pd.DEVICEATTACH }" name="DEVICEATTACH" id="DEVICEATTACH" data-placeholder="请选择" style="vertical-align:top;width: 120px;">
									<option value="-1">设备归属</option>
									<!-- 开始循环 -->
									<c:choose>
									<c:when test="${not empty attachList}">
										<c:forEach items="${attachList}" var="c" varStatus="vst">		    
										<option <c:if test="${pd.DEVICEATTACH == c.ID}"> selected = "selected" </c:if> value="${c.ID }">${c.NAME }</option>
										</c:forEach>
										</c:when>
									</c:choose>
								</select>
								
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">设备描述:</td>
								<td><input type="text" name="DEVICEDEPICT" id="DEVICEDEPICT" value="${pd.DEVICEDEPICT}" maxlength="200" placeholder="这里输入设备描述" title="设备描述" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">设备状态:</td>
								<td>
								<select class="chosen-select form-control" data="${pd.DEVICESTATUS }" name="DEVICESTATUS" id="DEVICESTATUS" data-placeholder="请选择" style="vertical-align:top;width: 120px;">
									<option value="-1">设备状态</option>
									<!-- 开始循环 -->
									<c:choose>
									<c:when test="${not empty statusList}">
										<c:forEach items="${statusList}" var="c" varStatus="vst">		    
										<option <c:if test="${pd.DEVICESTATUS == c.ID}"> selected = "selected" </c:if> value="${c.ID }">${c.NAME }</option>
										</c:forEach>
										</c:when>
									</c:choose>
								</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="REMARKS" id="REMARKS" value="${pd.REMARKS}" maxlength="200" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
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
			if($("#DEVICETYPE").val()=="" || $("#DEVICETYPE").val()=="-1"){
				$("#DEVICETYPE").tips({
					side:3,
		            msg:'请输入设备类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEVICETYPE").focus();
			return false;
			}
			if($("#DEVICENAME").val()==""){
				$("#DEVICENAME").tips({
					side:3,
		            msg:'请输入设备名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEVICENAME").focus();
			return false;
			}
			if($("#DEVICEATTACH").val()=="" || $("#DEVICEATTACH").val()=="-1"){
				$("#DEVICEATTACH").tips({
					side:3,
		            msg:'请输入设备归属',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEVICEATTACH").focus();
			return false;
			}
			if($("#DEVICEDEPICT").val()==""){
				$("#DEVICEDEPICT").tips({
					side:3,
		            msg:'请输入设备描述',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEVICEDEPICT").focus();
			return false;
			}
			if($("#DEVICESTATUS").val()=="" || $("#DEVICESTATUS").val()=="-1"){
				$("#DEVICESTATUS").tips({
					side:3,
		            msg:'请输入设备状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEVICESTATUS").focus();
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