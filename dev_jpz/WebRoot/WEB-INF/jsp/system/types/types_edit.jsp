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
					
					<form action="types/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">类型分类:</td>
								<td>
								<select class="chosen-select form-control" data="${pd.TYPECLASS}" name="TYPECLASS" id="TYPECLASS" data-placeholder="请选择" style="vertical-align:top;width: 120px;">
									<option value="-1">全部</option>
									<option value="1">用户</option>
									<option value="2">产品</option>
									<option value="3">预订</option>
									<option value="4">软文</option>
									<option value="5">收益</option>
									<option value="6">提现</option>
									<option value="7">图片</option>
									<option value="8">微信消息</option>
									<option value="9">优惠券类型</option>
								  	</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">类型名称:</td>
								<td><input type="text" name="TYPENAME" id="TYPENAME" value="${pd.TYPENAME}" maxlength="50" placeholder="这里输入类型名称" title="类型名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">排序:</td>
								<td>
								<c:choose>
								<c:when test="${pd.SORT==null || pd.SORT==''}">
								<input type="number" min="0" name="SORT" id="SORT"  value="0" maxlength="32" placeholder="这里输入排序" title="排序" style="width:98%;"/>
								</c:when>
								<c:otherwise>
								<input type="number" min="0" name="SORT" id="SORT"  value="${pd.SORT}" maxlength="32" placeholder="这里输入排序" title="排序" style="width:98%;"/>
								</c:otherwise>
								</c:choose>
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
			
			if($("#TYPECLASS").val()==""){
				$("#TYPECLASS").tips({
					side:3,
		            msg:'请输入类型分类',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TYPECLASS").focus();
			return false;
			}
			if($("#TYPENAME").val()==""){
				$("#TYPENAME").tips({
					side:3,
		            msg:'请输入类型名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TYPENAME").focus();
			return false;
			}
			if($("#SORT").val()==""){
				$("#SORT").tips({
					side:3,
		            msg:'请输入排序',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SORT").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
			if($("#TYPECLASS").attr('data')!='' && $("#TYPECLASS").attr('data') !=-1){
				$("#TYPECLASS").val($("#TYPECLASS").attr('data'));
			}
			
		});
		</script>
</body>
</html>