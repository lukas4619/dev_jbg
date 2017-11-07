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
							<form action="configure/${msg }.do" name="Form" id="Form"
								method="post">
								<input type="hidden" name="CONFIGURE_ID" id="CONFIGURE_ID"
									value="${pd.CONFIGURE_ID}" /> <input type="hidden"
									name="CONFIGURETYPE" id="CONFIGURETYPE"
									value="${pd.CONFIGURETYPE}" />
									<input type="hidden" name="ATTRVALTWO" id="ATTRVALTWO" value="${pd.ATTRVALTWO }"/>
									<input type="hidden" value="no" id="hasTp1" />
								<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report"
										class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">系统名称:</td>
											<td><input type="text" name="ATTRVALONE" id="ATTRVALONE"
												value="${pd.ATTRVALONE}" maxlength="500"
												placeholder="这里输入系统名称" title="系统名称" style="width:98%;" />
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">系统LOGO:</td>
											<td>
												<div style="float:left;"><img id="imgLogo" src="<%=basePath%>uploadFiles/uploadImgs/${pd.ATTRVALTWO}"  width="100"/></div>
												<div style="clear: both;"><input type="file" name="TP_URL" id="uploadify1" keepDefaultStyle = "true"/></div>
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">系统标题:</td>
											<td><input type="text" name="ATTRVALTHREE"
												id="ATTRVALTHREE" value="${pd.ATTRVALTHREE}" maxlength="500"
												placeholder="这里输入系统标题" title="系统标题" style="width:98%;" />
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">系统关键字:</td>
											<td><input type="text" name="ATTRVALFOUR"
												id="ATTRVALFOUR" value="${pd.ATTRVALFOUR}" maxlength="500"
												placeholder="这里输入系统关键字" title="系统关键字" style="width:98%;" />
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">系统描述:</td>
											<td><input type="text" name="ATTRVALFIVE"
												id="ATTRVALFIVE" value="${pd.ATTRVALFIVE}" maxlength="500"
												placeholder="这里输入系统描述" title="系统描述" style="width:98%;" />
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">版权信息:</td>
											<td><input type="text" name="ATTRVALSIX" id="ATTRVALSIX"
												value="${pd.ATTRVALSIX}" maxlength="500"
												placeholder="这里输入版权信息" title="版权信息" style="width:98%;" />
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">客服微信:</td>
											<td><input type="text" name="ATTRVALSEVEN"
												id="ATTRVALSEVEN" value="${pd.ATTRVALSEVEN}" maxlength="500"
												placeholder="这里输入客服微信" title="客服微信" style="width:98%;" />
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">客服QQ:</td>
											<td><input type="text" name="ATTRVALEIGHT"
												id="ATTRVALEIGHT" value="${pd.ATTRVALEIGHT}" maxlength="500"
												placeholder="这里输入客服QQ" title="客服QQ" style="width:98%;" />
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">客服电话:</td>
											<td><input type="text" name="ATTRVALNINE"
												id="ATTRVALNINE" value="${pd.ATTRVALNINE}" maxlength="500"
												placeholder="这里输入客服电话" title="客服电话" style="width:98%;" />
											</td>
										</tr>
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;">酒店联系方式:</td>
											<td><input type="text" name="ATTRVALTEN" id="ATTRVALTEN"
												value="${pd.ATTRVALTEN}" maxlength="500"
												placeholder="这里输入酒店联系方式" title="酒店联系方式" style="width:98%;" />
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
	<!-- 上传图片插件 -->
<link href="plugins/uploadify/uploadify.css" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="plugins/uploadify/swfobject.js"></script>
<script type="text/javascript"
	src="plugins/uploadify/jquery.uploadify.v2.1.4.min.js"></script>
<!-- 上传图片插件 -->
<script type="text/javascript">
var jsessionid = "<%=session.getId()%>"; //勿删，uploadify兼容火狐用到
var locat = "<%=basePath%>"; 
//====================上传二维码=================
$(document).ready(function(){
	$("#uploadify1").uploadify({
		'buttonImg'	: 	locat+"/plugins/uploadify/uploadp.png",
		'uploader'	:	locat+"/plugins/uploadify/uploadify.swf",
		'script'    :	locat+"/plugins/uploadify/uploadFile.jsp;jsessionid="+jsessionid,
		'cancelImg' :	locat+"/plugins/uploadify/cancel.png",
		'folder'	:	locat+"/uploadFiles/uploadImgs",//上传文件存放的路径,请保持与uploadFile.jsp中PATH的值相同
		'queueId'	:	"fileQueue",
		'queueSizeLimit'	:	1,//限制上传文件的数量
		'fileExt'	:	"*.jpg;*.png;*.gif",//设置可以选择的文件的类型，格式如：'*.jpg;*.png;*.gif'。 *.doc;*.pdf;*.rar
		'fileDesc'	:	"请选择jpg、png、gif图片",//设置可以选择的文件的类型提示
		//'fileExt'     : '*.jpg;*.gif;*.png',
		//'fileDesc'    : 'Please choose(.JPG, .GIF, .PNG)',
		'auto'		:	true,
		'multi'		:	false,//是否允许多文件上传
		'simUploadLimit':	1,//同时运行上传的进程数量
		'buttonText':	"files",
		'scriptData':	{'uploadPath':'/uploadFiles/uploadImgs/'},//{'uploadPath':'/uploadFiles/uploadImgs/','fileNmae':'watermark.png'}这个参数用于传递用户自己的参数，此时'method' 必须设置为GET, 后台可以用request.getParameter('name')获取名字的值
		'method'	:	"GET",
		'onComplete':function(event,queueId,fileObj,response,data){
			$("#ATTRVALTWO").val(response.trim());
			var loaderUrl = locat+'/uploadFiles/uploadImgs/'+response.trim();
			$("#imgLogo").attr('src',loaderUrl);
		},
		'onAllComplete' : function(event,data) {
			//$("#Form").submit();
			//$("#zhongxin").hide();
			//$("#zhongxin2").show();
    	},
    	'onSelect' : function(event, queueId, fileObj){
    		$("#hasTp1").val("ok");
    	}
	});
			
});
//====================上传图片=================
</script>
<!--引入属于此页面的js -->
	<script type="text/javascript">
		$(top.hangge());
		//保存
		function save() {
			if ($("#ATTRVALONE").val() == "") {
				$("#ATTRVALONE").tips({
					side : 3,
					msg : '请输入系统名称',
					bg : '#AE81FF',
					time : 2
				});
				$("#ATTRVALONE").focus();
				return false;
			}
			if ($("#ATTRVALTWO").val() == "") {
				$("#imgLogo").tips({
					side : 3,
					msg : '请上传系统LOGO',
					bg : '#AE81FF',
					time : 2
				});
				return false;
			}
			if ($("#ATTRVALTHREE").val() == "") {
				$("#ATTRVALTHREE").tips({
					side : 3,
					msg : '请输入系统标题',
					bg : '#AE81FF',
					time : 2
				});
				$("#ATTRVALTHREE").focus();
				return false;
			}
			if ($("#ATTRVALFOUR").val() == "") {
				$("#ATTRVALFOUR").tips({
					side : 3,
					msg : '请输入系统关键字',
					bg : '#AE81FF',
					time : 2
				});
				$("#ATTRVALFOUR").focus();
				return false;
			}
			if ($("#ATTRVALFIVE").val() == "") {
				$("#ATTRVALFIVE").tips({
					side : 3,
					msg : '请输入系统描述',
					bg : '#AE81FF',
					time : 2
				});
				$("#ATTRVALFIVE").focus();
				return false;
			}
			if ($("#ATTRVALSIX").val() == "") {
				$("#ATTRVALSIX").tips({
					side : 3,
					msg : '请输入版权信息',
					bg : '#AE81FF',
					time : 2
				});
				$("#ATTRVALSIX").focus();
				return false;
			}
			if ($("#ATTRVALSEVEN").val() == "") {
				$("#ATTRVALSEVEN").tips({
					side : 3,
					msg : '请输入客服微信',
					bg : '#AE81FF',
					time : 2
				});
				$("#ATTRVALSEVEN").focus();
				return false;
			}
			if ($("#ATTRVALEIGHT").val() == "") {
				$("#ATTRVALEIGHT").tips({
					side : 3,
					msg : '请输入客服QQ',
					bg : '#AE81FF',
					time : 2
				});
				$("#ATTRVALEIGHT").focus();
				return false;
			}
			if ($("#ATTRVALNINE").val() == "") {
				$("#ATTRVALNINE").tips({
					side : 3,
					msg : '请输入客服电话',
					bg : '#AE81FF',
					time : 2
				});
				$("#ATTRVALNINE").focus();
				return false;
			}
			if ($("#ATTRVALTEN").val() == "") {
				$("#ATTRVALTEN").tips({
					side : 3,
					msg : '请输入酒店联系方式',
					bg : '#AE81FF',
					time : 2
				});
				$("#ATTRVALTEN").focus();
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
			if('${pd.ATTRVALTWO}'!=''){
				$("#imgLogo").attr('src',locat+'/uploadFiles/uploadImgs/'+'${pd.ATTRVALTWO}');
			}
			
		});
	</script>
</body>
</html>