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
					
					<form action="shelves/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<input type="hidden" name="CREATEDATE" id="CREATEDATE" value="${pd.CREATEDATE}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">货架名称:</td>
								<td><input type="text" name="SHELVESNAME" id="SHELVESNAME" value="${pd.SHELVESNAME}" maxlength="50" placeholder="这里输入货架名称" title="货架名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">货位数量:</td>
								<td><input type="number" min="1" name="RANKCOUNT" id="RANKCOUNT" value="${pd.RANKCOUNT}" maxlength="2" placeholder="这里输入货位数量" title="货位数量" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">所属门店:</td>
								<td>
								<select class="chosen-select form-control" data="${pd.STOREID }" name="STOREID" id="STOREID" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">门店</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty storeList}">
															<c:forEach items="${storeList}" var="c" varStatus="vst">
																<option value="${c.ID }">${c.STORENAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
										</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">货架类型:</td>
								<td>
									<select class="chosen-select form-control" data="${pd.SHELVESTYPEID }" name="SHELVESTYPEID" id="SHELVESTYPEID" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">货架类型</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty shelvesTypeList}">
															<c:forEach items="${shelvesTypeList}" var="t" varStatus="vst">
																<option value="${t.ID }">${t.NAME }</option>
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
			if($("#SHELVESNAME").val()==""){
				$("#SHELVESNAME").tips({
					side:3,
		            msg:'请输入货架名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SHELVESNAME").focus();
			return false;
			}
			if($("#RANKCOUNT").val()==""){
				$("#RANKCOUNT").tips({
					side:3,
		            msg:'请输入货位数量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#RANKCOUNT").focus();
			return false;
			}
			if($("#STOREID").val()=="" || $("#STOREID").val()=="-1"){
				$("#STOREID").tips({
					side:3,
		            msg:'请选择所属门店',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOREID").focus();
			return false;
			}
			if($("#SHELVESTYPEID").val()==""){
				$("#SHELVESTYPEID").tips({
					side:3,
		            msg:'请选择货架类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SHELVESTYPEID").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
			if($("#STOREID").attr('data')!='' && $("#STOREID").attr('data') !=-1){
				$("#STOREID").val($("#STOREID").attr('data'));
			}
			if($("#SHELVESTYPEID").attr('data')!='' && $("#SHELVESTYPEID").attr('data') !=-1){
				$("#SHELVESTYPEID").val($("#SHELVESTYPEID").attr('data'));
			}
		});
		</script>
</body>
</html>