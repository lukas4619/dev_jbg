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
					
					<form action="memberbalance/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="BALANCEID" id="BALANCEID" value="${pd.balanceID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">结算类型:</td>
								<td>
									<select name="BALANCETYPE" disabled="disabled">
										<c:forEach items="${reservationTypeList }" var="typeList">
											<option <c:if test="${pd.balanceType == typeList.ID }" >selected</c:if> value="${typeList.ID }" >${typeList.TYPENAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">结算编号:</td>
								<td>
									<input type="text" readonly="readonly" name="BALANCENUMBER" id="BALANCENUMBER" value="${pd.balanceNumber}" style="width:98%;"/>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户id:</td>
								<td>
									${pd.memberID}
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">结算金额:</td>
								<td>
									${pd.balanceMoney}
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">结算状态:</td>
								<td>
									<select name="BALANCESTATE" >
										<c:forEach items="${reservationStateIDList }" var="stateList">
											<option  <c:if test="${pd.balanceState == stateList.ID }">selected</c:if> value="${stateList.ID }">${stateList.NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">结算时间:</td>
								<td>
									${pd.editDate}	
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">创建时间:</td>
								<td>
									${pd.createDate}
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">结算备注:</td>
								<td><input type="text" name="BALANCEREMARK" id="balanceRemark" value="${pd.balanceRemark}" maxlength="200" placeholder="这里输入结算备注" title="结算备注" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">操作人名称:</td>
								<td><input readonly="readonly" type="text" name="OPERATIONNAME" id="operationName" value="${pd.operationName}" maxlength="50"  style="width:98%;"/></td>
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
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		
		function getNumber(type){
			if(type !=28 ){
				alert("此记录当前结算状态为未处理的才能获取编号");
				return;
			}
			$.ajax({
				url:'<%=basePath%>memberbalance/getNumber.do',
				dataType:'json',
				success:function(r){
					$("input[name='BALANCENUMBER']").val(r.code);
				}
			});
			
		}
		</script>
</body>
</html>