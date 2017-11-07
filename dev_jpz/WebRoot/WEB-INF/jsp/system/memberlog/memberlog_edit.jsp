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
					
					<form action="memberlog/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="MEMBERLOG_ID" id="MEMBERLOG_ID" value="${pd.MEMBERLOG_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">id:</td>
								<td><input type="number" name="ID" id="ID" value="${pd.ID}" maxlength="32" placeholder="这里输入id" title="id" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">登录类型:</td>
								<td><input type="number" name="LOGTYPE" id="LOGTYPE" value="${pd.LOGTYPE}" maxlength="32" placeholder="这里输入登录类型" title="登录类型" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">请求参数:</td>
								<td><input type="text" name="CONTENTS" id="CONTENTS" value="${pd.CONTENTS}" maxlength="21845" placeholder="这里输入请求参数" title="请求参数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">请求时间:</td>
								<td><input class="span10 date-picker" name="CREATEDATE" id="CREATEDATE" value="${pd.CREATEDATE}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="请求时间" title="请求时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">登陆ip:</td>
								<td><input type="text" name="LOGIP" id="LOGIP" value="${pd.LOGIP}" maxlength="50" placeholder="这里输入登陆ip" title="登陆ip" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">会员id:</td>
								<td><input type="text" name="MEMBERID" id="MEMBERID" value="${pd.MEMBERID}" maxlength="50" placeholder="这里输入会员id" title="会员id" style="width:98%;"/></td>
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
		            msg:'请输入id',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ID").focus();
			return false;
			}
			if($("#LOGTYPE").val()==""){
				$("#LOGTYPE").tips({
					side:3,
		            msg:'请输入登录类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOGTYPE").focus();
			return false;
			}
			if($("#CONTENTS").val()==""){
				$("#CONTENTS").tips({
					side:3,
		            msg:'请输入请求参数',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONTENTS").focus();
			return false;
			}
			if($("#CREATEDATE").val()==""){
				$("#CREATEDATE").tips({
					side:3,
		            msg:'请输入请求时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CREATEDATE").focus();
			return false;
			}
			if($("#LOGIP").val()==""){
				$("#LOGIP").tips({
					side:3,
		            msg:'请输入登陆ip',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOGIP").focus();
			return false;
			}
			if($("#MEMBERID").val()==""){
				$("#MEMBERID").tips({
					side:3,
		            msg:'请输入会员id',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBERID").focus();
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