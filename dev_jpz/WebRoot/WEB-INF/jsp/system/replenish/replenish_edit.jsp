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
					
					<form action="replenish/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">标识:</td>
								<td><input type="number" name="ID" id="ID" value="${pd.ID}" maxlength="32" placeholder="这里输入标识" title="标识" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">门店标识:</td>
								<td><input type="number" name="STOREID" id="STOREID" value="${pd.STOREID}" maxlength="32" placeholder="这里输入门店标识" title="门店标识" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品编码:</td>
								<td><input type="number" name="PLUCODE" id="PLUCODE" value="${pd.PLUCODE}" maxlength="32" placeholder="这里输入商品编码" title="商品编码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">补货数量:</td>
								<td><input type="number" name="REPLENISHCOUNT" id="REPLENISHCOUNT" value="${pd.REPLENISHCOUNT}" maxlength="32" placeholder="这里输入补货数量" title="补货数量" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td><input class="span10 date-picker" name="CREATEDATE" id="CREATEDATE" value="${pd.CREATEDATE}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="创建时间" title="创建时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">补货人:</td>
								<td><input class="span10 date-picker" name="CREATENAME" id="CREATENAME" value="${pd.CREATENAME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="补货人" title="补货人" style="width:98%;"/></td>
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
			if($("#ID").val()==""){
				$("#ID").tips({
					side:3,
		            msg:'请输入标识',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ID").focus();
			return false;
			}
			if($("#STOREID").val()==""){
				$("#STOREID").tips({
					side:3,
		            msg:'请输入门店标识',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOREID").focus();
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
			if($("#REPLENISHCOUNT").val()==""){
				$("#REPLENISHCOUNT").tips({
					side:3,
		            msg:'请输入补货数量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REPLENISHCOUNT").focus();
			return false;
			}
			if($("#CREATEDATE").val()==""){
				$("#CREATEDATE").tips({
					side:3,
		            msg:'请输入创建时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CREATEDATE").focus();
			return false;
			}
			if($("#CREATENAME").val()==""){
				$("#CREATENAME").tips({
					side:3,
		            msg:'请输入补货人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CREATENAME").focus();
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