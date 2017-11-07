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
					
					<form action="revenue/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="REVENUE_ID" id="REVENUE_ID" value="${pd.REVENUE_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">id:</td>
								<td><input type="text" name="REVENUEID" id="REVENUEID" value="${pd.REVENUEID}" maxlength="50" placeholder="这里输入id" title="id" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">收益类型:</td>
								<td><input type="number" name="REVENUETYPE" id="REVENUETYPE" value="${pd.REVENUETYPE}" maxlength="32" placeholder="这里输入收益类型" title="收益类型" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户id:</td>
								<td><input type="text" name="MEMBERID" id="MEMBERID" value="${pd.MEMBERID}" maxlength="50" placeholder="这里输入用户id" title="用户id" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">收益金额:</td>
								<td><input type="number" name="REVENUEMONEY" id="REVENUEMONEY" value="${pd.REVENUEMONEY}" maxlength="32" placeholder="这里输入收益金额" title="收益金额" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">收益时间:</td>
								<td><input class="span10 date-picker" name="REVENUEDATE" id="REVENUEDATE" value="${pd.REVENUEDATE}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="收益时间" title="收益时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">收益状态:</td>
								<td><input type="number" name="REVENUESTATE" id="REVENUESTATE" value="${pd.REVENUESTATE}" maxlength="32" placeholder="这里输入收益状态" title="收益状态" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">收益来源:</td>
								<td><input type="text" name="REVENUESOURCE" id="REVENUESOURCE" value="${pd.REVENUESOURCE}" maxlength="200" placeholder="这里输入收益来源" title="收益来源" style="width:98%;"/></td>
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
			if($("#REVENUEID").val()==""){
				$("#REVENUEID").tips({
					side:3,
		            msg:'请输入id',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REVENUEID").focus();
			return false;
			}
			if($("#REVENUETYPE").val()==""){
				$("#REVENUETYPE").tips({
					side:3,
		            msg:'请输入收益类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REVENUETYPE").focus();
			return false;
			}
			if($("#MEMBERID").val()==""){
				$("#MEMBERID").tips({
					side:3,
		            msg:'请输入用户id',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBERID").focus();
			return false;
			}
			if($("#REVENUEMONEY").val()==""){
				$("#REVENUEMONEY").tips({
					side:3,
		            msg:'请输入收益金额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REVENUEMONEY").focus();
			return false;
			}
			if($("#REVENUEDATE").val()==""){
				$("#REVENUEDATE").tips({
					side:3,
		            msg:'请输入收益时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REVENUEDATE").focus();
			return false;
			}
			if($("#REVENUESTATE").val()==""){
				$("#REVENUESTATE").tips({
					side:3,
		            msg:'请输入收益状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REVENUESTATE").focus();
			return false;
			}
			if($("#REVENUESOURCE").val()==""){
				$("#REVENUESOURCE").tips({
					side:3,
		            msg:'请输入收益来源',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REVENUESOURCE").focus();
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