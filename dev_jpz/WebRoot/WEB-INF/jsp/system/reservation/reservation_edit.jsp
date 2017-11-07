<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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

							<form action="reservation/${msg }.do" name="Form" id="Form"
								method="post">
								<input type="hidden" name="RESERVATIONID" id="RESERVATIONID"
									value="${pd.RESERVATIONID}" /> <input type="hidden"
									name="RESERVATIONNUMBER" id="RESERVATIONNUMBER"
									value="${pd.RESERVATIONNUMBER}" /> <input type="hidden"
									name="PRONAME" id="PRONAME" value="${pd.PRONAME}" /> <input
									type="hidden" name="PROMONEY" id="PROMONEY"
									value="${pd.PROMONEY}" /> <input type="hidden"
									name="ADVANCEMONEY" id="ADVANCEMONEY"
									value="${pd.ADVANCEMONEY}" /> <input type="hidden"
									name="CREATEDATE" id="CREATEDATE" value="${pd.CREATEDATE}" /> <input
									type="hidden" name="AUTHORREVENUE" id="AUTHORREVENUE"
									value="${pd.AUTHORREVENUE}" /> <input type="hidden"
									name="SHAREREVENUE" id="SHAREREVENUE"
									value="${pd.SHAREREVENUE}" />
									<input type="hidden"
									name="MEMBERID" id="MEMBERID"
									value="${pd.MEMBERID}" />
									<input type="hidden" name="PRODUCTID" id="PRODUCTID" value="${pd.PRODUCTID}" />
									<input type="hidden" name="PROCLASSNAME" id="PROCLASSNAME" value="${pd.PROCLASSNAME}" />
									<input type="hidden" name="PAYSTATE" id="PAYSTATE"
									value="${pd.PAYSTATE}" />
								<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report"
										class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">预订编号:</td>
											<td><label>${pd.RESERVATIONNUMBER}</label>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">预订方式:</td>
											<td><select class="chosen-select form-control"
												data="${pd.RESERVATIONTYPE }" name="RESERVATIONTYPE"
												id="RESERVATIONTYPE" data-placeholder="请选择"
												style="vertical-align:top;width: 120px;">
													<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty reservationTypeList}">
															<c:forEach items="${reservationTypeList}" var="t"
																varStatus="vst">
																<option value="${t.ID }">${t.TYPENAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
											</select></td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">预订人:</td>
											<td><input type="text" name="RESERVATIONNAME"
												id="RESERVATIONNAME" value="${pd.RESERVATIONNAME}"
												maxlength="50" placeholder="这里输入预订人" title="预订人"
												style="width:98%;" />
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">性别:</td>
											<td>
											<input type="hidden" name="RESERVATIONSEX" id="RESERVATIONSEX"
									value="${pd.RESERVATIONSEX}" />
											<div class="col-sm-9">
    <label style="float:left;padding-left: 8px;padding-top:7px;">
        <input name="form-field" type="radio" class="ace" id="form-field-radio1" onclick="setSex(1)">	<span class="lbl">先生</span>
    </label>
    <label style="float:left;padding-left: 5px;padding-top:7px;">
        <input name="form-field" type="radio" class="ace" id="form-field-radio2" onclick="setSex(2)">	<span class="lbl">女士</span>

    </label>
</div>
										</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">预留联系方式:</td>
											<td><input type="text" name="RESERVEDNUMBER"
												id="RESERVEDNUMBER" value="${pd.RESERVEDNUMBER}"
												maxlength="50" placeholder="这里输入预留联系方式" title="预留联系方式"
												style="width:98%;" />
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">预订状态:</td>
											<td><select class="chosen-select form-control"
												data="${pd.RESERVATIONSTATEID }" name="RESERVATIONSTATEID"
												id="RESERVATIONSTATEID" data-placeholder="请选择"
												style="vertical-align:top;width: 120px;">
													<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty reservationStateIDList}">
															<c:forEach items="${reservationStateIDList}" var="s"
																varStatus="vss">
																<option value="${s.ID }">${s.NAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
											</select></td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">备注:</td>
											<td><input type="text" name="STATEREMARKS"
												id="STATEREMARKS" value="${pd.STATEREMARKS}" maxlength="200"
												placeholder="这里输入备注" title="备注" style="width:98%;" />
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">产品名称:</td>
											<td><label>${pd.PRONAME}</label></td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">产品金额:</td>
											<td><label style="color: #f60;font-weight: bold;">${pd.PROMONEY}</label></td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">预付金额:</td>
											<td><label style="color: #f60;font-weight: bold;">${pd.ADVANCEMONEY}</label></td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">预订时间:</td>
											<td><label><fmt:formatDate value="${pd.CREATEDATE}" pattern="yyyy-MM-dd HH:mm:ss"  /></label></td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">预订有效期:</td>
											<td>
											<input type="text" name="VALIDITYDATE" class="date-picker" data-date-format="yyyy-mm-dd" readonly="readonly"
												id="VALIDITYDATE" value="${pd.VALIDITYDATE}" maxlength="20"
												placeholder="选择预订有效期" title="选择预订有效期" style="width:98%;"/>
											</td>
										</tr>
										<tr>
											<td style="text-align: center;" colspan="10"><a
												class="btn btn-mini btn-primary" onclick="save();">保存</a> <a
												class="btn btn-mini btn-danger"
												onclick="top.Dialog.close();">取消</a></td>
										</tr>
									</table>
								</div>
								<div id="zhongxin2" class="center" style="display:none">
									<br />
									<br />
									<br />
									<br />
									<br />
									<img src="static/images/jiazai.gif" /><br />
									<h4 class="lighter block green">提交中...</h4>
								</div>
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
		function save() {
			if ($("#RESERVATIONNAME").val() == "") {
				$("#RESERVATIONNAME").tips({
					side : 3,
					msg : '请输入预订人',
					bg : '#AE81FF',
					time : 2
				});
				$("#RESERVATIONNAME").focus();
				return false;
			}
			if ($("#RESERVEDNUMBER").val() == "") {
				$("#RESERVEDNUMBER").tips({
					side : 3,
					msg : '请输入预留联系方式',
					bg : '#AE81FF',
					time : 2
				});
				$("#RESERVEDNUMBER").focus();
				return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}

		$(function() {
			//日期框
			$('.date-picker').datepicker({
				autoclose : true,
				todayHighlight : true
			});
			if ($("#RESERVATIONTYPE").attr('data') != ''
					&& $("#RESERVATIONTYPE").attr('data') != -1) {
				$("#RESERVATIONTYPE").val($("#RESERVATIONTYPE").attr('data'));
			}
			if ($("#RESERVATIONSTATEID").attr('data') != ''
					&& $("#RESERVATIONSTATEID").attr('data') != -1) {
				$("#RESERVATIONSTATEID").val(
						$("#RESERVATIONSTATEID").attr('data'));
			}
			setSex($("#RESERVATIONSEX").val());
		});
		
		function setSex(reservationsex){
			if(reservationsex==1){
				$("#form-field-radio1").attr('checked',true);
				$("#RESERVATIONSEX").val(1);
			}else if(reservationsex==2){
				$("#form-field-radio2").attr('checked',true);
				$("#RESERVATIONSEX").val(2);
			}
		}
	</script>
</body>
</html>