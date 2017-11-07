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
					
					<form action="article/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ARTICLEID" id="ARTICLEID" value="${pd.ARTICLEID}"/>
						<input type="hidden" name="STATENAME" id="STATENAME" value="${pd.STATENAME}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
						<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>状态:</td>
								<td>
								<select
											class="chosen-select form-control" data="${pd.ARTICLESTATEID }"
											name="ARTICLESTATEID" id="ARTICLESTATEID" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<!-- 开始循环 -->
												<c:choose>
													<c:when test="${not empty stateList}">
														<c:forEach items="${stateList}" var="s" varStatus="vss">
															<option value="${s.ID }">${s.NAME }</option>
														</c:forEach>
													</c:when>
												</c:choose>
										</select>
								</td>
							</tr>
							<tr>
								<td style="width:180px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>作者浏览量收益比例:</td>
								<td>
								<input
												onkeypress='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												onkeyup='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												type="text" name="REVENUEPV" id="REVENUEPV"
												<c:choose>
												<c:when test="${pd.REVENUEPV == null ||  pd.REVENUEPV==''}">
												value="0"
												</c:when>
												<c:otherwise>
												value="${pd.REVENUEPV}"
												</c:otherwise>
												</c:choose>
												maxlength="10" placeholder="这里输入作者浏览量收益比例" title="作者浏览量收益比例" style="width:120px;" />%
								</td>
							</tr>
							<tr>
								<td style="width:180px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>作者点赞收益比例:</td>
								<td>
								<input
												onkeypress='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												onkeyup='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												type="text" name="REVENUELIKE" id="REVENUELIKE"
												<c:choose>
												<c:when test="${pd.REVENUELIKE == null ||  pd.REVENUELIKE==''}">
												value="0"
												</c:when>
												<c:otherwise>
												value="${pd.REVENUELIKE}"
												</c:otherwise>
												</c:choose>
												maxlength="10" placeholder="这里输入作者点赞收益比例" title="作者点赞收益比例" style="width:120px;" />%
								</td>
							</tr>
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>作者公众号关注收益比例:</td>
								<td>
								<input
												onkeypress='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												onkeyup='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												type="text" name="REVENUESUB" id="REVENUESUB"
												<c:choose>
												<c:when test="${pd.REVENUESUB == null ||  pd.REVENUESUB==''}">
												value="0"
												</c:when>
												<c:otherwise>
												value="${pd.REVENUESUB}"
												</c:otherwise>
												</c:choose>
												maxlength="10" placeholder="这里输入作者公众号关注收益比例" title="作者公众号关注收益比例" style="width:120px;" />%
								</td>
							</tr>
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>预订消费成功作者收益比例:</td>
								<td>
								<input
												onkeypress='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												onkeyup='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												type="text" name="REVENUECON" id="REVENUECON"
												<c:choose>
												<c:when test="${pd.REVENUECON == null ||  pd.REVENUECON==''}">
												value="0"
												</c:when>
												<c:otherwise>
												value="${pd.REVENUECON}"
												</c:otherwise>
												</c:choose>
												maxlength="10" placeholder="这里输入预订消费成功作者收益比例" title="预订消费成功作者收益比例" style="width:120px;" />%
								</td>
							</tr>
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>预订消费成功用户收益比例:</td>
								<td>
								<input
												onkeypress='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												onkeyup='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												type="text" name="REVENUECONM" id="REVENUECONM"
												<c:choose>
												<c:when test="${pd.REVENUECONM == null ||  pd.REVENUECONM==''}">
												value="0"
												</c:when>
												<c:otherwise>
												value="${pd.REVENUECONM}"
												</c:otherwise>
												</c:choose>
												maxlength="10" placeholder="这里输入预订消费成功用户收益比例" title="预订消费成功用户收益比例" style="width:120px;" />%
								</td>
							</tr>
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>用户分享公众号关注收益比例:</td>
								<td>
								<input
												onkeypress='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												onkeyup='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												type="text" name="REVENUESUBM" id="REVENUESUBM"
												<c:choose>
												<c:when test="${pd.REVENUESUBM == null ||  pd.REVENUESUBM==''}">
												value="0"
												</c:when>
												<c:otherwise>
												value="${pd.REVENUESUBM}"
												</c:otherwise>
												</c:choose>
												maxlength="10" placeholder="这里输入用户分享公众号关注收益比例" title="用户分享公众号关注收益比例" style="width:120px;" />%
								</td>
							</tr>
							<tr>
								<td style="width:200px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>用户分享软文浏览收益比例:</td>
								<td>
								<input
												onkeypress='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												onkeyup='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												type="text" name="REVENUEPVM" id="REVENUEPVM"
												<c:choose>
												<c:when test="${pd.REVENUEPVM == null ||  pd.REVENUEPVM==''}">
												value="0"
												</c:when>
												<c:otherwise>
												value="${pd.REVENUEPVM}"
												</c:otherwise>
												</c:choose>
												maxlength="10" placeholder="这里输入用户分享软文浏览收益比例" title="用户分享软文浏览收益比例" style="width:120px;" />%
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="DETAILS" id="DETAILS" value="${pd.DETAILS}" maxlength="500" placeholder="这里输入内容" title="内容" style="width:98%;"/></td>
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
			$("#STATENAME").val($("#ARTICLESTATEID option:selected").text());
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