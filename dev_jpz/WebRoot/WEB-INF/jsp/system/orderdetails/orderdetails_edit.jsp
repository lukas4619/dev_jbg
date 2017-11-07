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
					
					<form action="orderdetails/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">订单编号:</td>
								<td><input type="text" name="BILLNO" id="BILLNO" value="${pd.BILLNO}" maxlength="50" placeholder="这里输入订单编号" title="订单编号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品编码:</td>
								<td><input type="number" name="PLUCODE" id="PLUCODE" value="${pd.PLUCODE}" maxlength="32" placeholder="这里输入商品编码" title="商品编码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品名称:</td>
								<td><input type="text" name="PLUNAME" id="PLUNAME" value="${pd.PLUNAME}" maxlength="50" placeholder="这里输入商品名称" title="商品名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品条形码:</td>
								<td><input type="text" name="BARCODE" id="BARCODE" value="${pd.BARCODE}" maxlength="50" placeholder="这里输入商品条形码" title="商品条形码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">单位:</td>
								<td><input type="text" name="UNITS" id="UNITS" value="${pd.UNITS}" maxlength="11" placeholder="这里输入单位" title="单位" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">规格:</td>
								<td><input type="text" name="SPEC" id="SPEC" value="${pd.SPEC}" maxlength="11" placeholder="这里输入规格" title="规格" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">售价:</td>
								<td><input type="text" name="PRICE" id="PRICE" value="${pd.PRICE}" maxlength="11" placeholder="这里输入售价" title="售价" style="width:98%;"/></td>
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
			if($("#BILLNO").val()==""){
				$("#BILLNO").tips({
					side:3,
		            msg:'请输入订单编号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BILLNO").focus();
			return false;
			}
			if($("#PLUCODE").val()==""){
				$("#PLUCODE").tips({
					side:3,
		            msg:'请输入商品编码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLUCODE").focus();
			return false;
			}
			if($("#PLUNAME").val()==""){
				$("#PLUNAME").tips({
					side:3,
		            msg:'请输入商品名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLUNAME").focus();
			return false;
			}
			if($("#BARCODE").val()==""){
				$("#BARCODE").tips({
					side:3,
		            msg:'请输入商品条形码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BARCODE").focus();
			return false;
			}
			if($("#UNITS").val()==""){
				$("#UNITS").tips({
					side:3,
		            msg:'请输入单位',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#UNITS").focus();
			return false;
			}
			if($("#SPEC").val()==""){
				$("#SPEC").tips({
					side:3,
		            msg:'请输入规格',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SPEC").focus();
			return false;
			}
			if($("#PRICE").val()==""){
				$("#PRICE").tips({
					side:3,
		            msg:'请输入售价',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PRICE").focus();
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