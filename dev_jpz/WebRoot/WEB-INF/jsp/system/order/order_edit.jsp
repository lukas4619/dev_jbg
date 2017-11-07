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
					
					<form action="order/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">标识:</td>
								<td><input type="number" name="ID" id="ID" value="${pd.ID}" maxlength="32" placeholder="这里输入标识" title="标识" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">订单编号:</td>
								<td><input type="text" name="BILLNO" id="BILLNO" value="${pd.BILLNO}" maxlength="20" placeholder="这里输入订单编号" title="订单编号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">订单状态:</td>
								<td><input type="number" name="ORDERSTATUSID" id="ORDERSTATUSID" value="${pd.ORDERSTATUSID}" maxlength="32" placeholder="这里输入订单状态" title="订单状态" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">付款状态:</td>
								<td><input type="number" name="PAYMENTSTATUSID" id="PAYMENTSTATUSID" value="${pd.PAYMENTSTATUSID}" maxlength="32" placeholder="这里输入付款状态" title="付款状态" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品数量:</td>
								<td><input type="number" name="PLUCOUNT" id="PLUCOUNT" value="${pd.PLUCOUNT}" maxlength="32" placeholder="这里输入商品数量" title="商品数量" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">订单金额:</td>
								<td><input type="text" name="TOTALPRICE" id="TOTALPRICE" value="${pd.TOTALPRICE}" maxlength="11" placeholder="这里输入订单金额" title="订单金额" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">付款时间:</td>
								<td><input class="span10 date-picker" name="PAYMENTDATE" id="PAYMENTDATE" value="${pd.PAYMENTDATE}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="付款时间" title="付款时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">订单提交时间:</td>
								<td><input class="span10 date-picker" name="CREATEDATE" id="CREATEDATE" value="${pd.CREATEDATE}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="订单提交时间" title="订单提交时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">所属门店:</td>
								<td><input type="number" name="STOREID" id="STOREID" value="${pd.STOREID}" maxlength="32" placeholder="这里输入所属门店" title="所属门店" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">所属货架:</td>
								<td><input type="number" name="SHELVESID" id="SHELVESID" value="${pd.SHELVESID}" maxlength="32" placeholder="这里输入所属货架" title="所属货架" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">所属货位:</td>
								<td><input type="number" name="PLACEID" id="PLACEID" value="${pd.PLACEID}" maxlength="32" placeholder="这里输入所属货位" title="所属货位" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">下单:</td>
								<td><input type="text" name="WECHATNAME" id="WECHATNAME" value="${pd.WECHATNAME}" maxlength="50" placeholder="这里输入下单" title="下单" style="width:98%;"/></td>
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
			if($("#ORDERSTATUSID").val()==""){
				$("#ORDERSTATUSID").tips({
					side:3,
		            msg:'请输入订单状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORDERSTATUSID").focus();
			return false;
			}
			if($("#PAYMENTSTATUSID").val()==""){
				$("#PAYMENTSTATUSID").tips({
					side:3,
		            msg:'请输入付款状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PAYMENTSTATUSID").focus();
			return false;
			}
			if($("#PLUCOUNT").val()==""){
				$("#PLUCOUNT").tips({
					side:3,
		            msg:'请输入商品数量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLUCOUNT").focus();
			return false;
			}
			if($("#TOTALPRICE").val()==""){
				$("#TOTALPRICE").tips({
					side:3,
		            msg:'请输入订单金额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TOTALPRICE").focus();
			return false;
			}
			if($("#PAYMENTDATE").val()==""){
				$("#PAYMENTDATE").tips({
					side:3,
		            msg:'请输入付款时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PAYMENTDATE").focus();
			return false;
			}
			if($("#CREATEDATE").val()==""){
				$("#CREATEDATE").tips({
					side:3,
		            msg:'请输入订单提交时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CREATEDATE").focus();
			return false;
			}
			if($("#STOREID").val()==""){
				$("#STOREID").tips({
					side:3,
		            msg:'请输入所属门店',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOREID").focus();
			return false;
			}
			if($("#SHELVESID").val()==""){
				$("#SHELVESID").tips({
					side:3,
		            msg:'请输入所属货架',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SHELVESID").focus();
			return false;
			}
			if($("#PLACEID").val()==""){
				$("#PLACEID").tips({
					side:3,
		            msg:'请输入所属货位',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLACEID").focus();
			return false;
			}
			if($("#WECHATNAME").val()==""){
				$("#WECHATNAME").tips({
					side:3,
		            msg:'请输入下单',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#WECHATNAME").focus();
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