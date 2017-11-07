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
									
									<input type="hidden"
									name="ATTRVALONE" id="ATTRVALONE"
									value="${pd.ATTRVALONE}" />
								<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report"
										class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:120px;text-align: right;padding-top: 13px;" id="tipsMsg">分享福利:</td>
											<td> <div class="form-group draggable">
											                                <div class="col-sm-9">
											                                	<div class="ueQ313596790Que"></div>
											                                   <script id="container" type="text/plain" style="width:96%;height:400px;"></script>
											                                    <div class="ueQ313596790Que"></div>
											                                </div>
											                            </div>
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
		<!-- 表单构建组建 -->
    <script src="plugins/fhform/js/bootstrap.min.js?v=3.3.5"></script>
    <script src="plugins/fhform/js/content.min.js?v=1.0.0"></script>
    <script src="plugins/fhform/js/jquery-ui-1.10.4.min.js"></script>
    <script src="plugins/fhform/js/beautifyhtml/beautifyhtml.js"></script>
	<!-- 百度富文本编辑框-->
	<script type="text/javascript" charset="utf-8">window.UEDITOR_HOME_URL = "<%=path%>/plugins/ueditor/";</script>
	<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- 百度富文本编辑框-->
<!--引入属于此页面的js -->
	<script type="text/javascript">
		$(top.hangge());
		var ue;
		//百度富文本
		setTimeout("ueditor()",500);
		function ueditor(){
			ue =UE.getEditor('container');
			ue.ready(function() {
			    //设置编辑器的内容
			    if($("#ATTRVALONE").val()!=''){
			    	ue.setContent($("#ATTRVALONE").val());
			    }
			});
		}
		//保存
		function save() {
			$("#ATTRVALONE").val(ue.getContent());
			if ($("#ATTRVALONE").val() == "") {
				bootbox.dialog({
					message: "<span class='bigger-110'>请输入分享福利内容!</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				      });
				return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}

		
	</script>
</body>
</html>