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
					<form action="images/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="IMGID" id="IMGID" value="${pd.IMGID}"/>
						<input type="hidden" name="MEDIA_ID" id="MEDIA_ID" value="${pd.MEDIA_ID}"/>
						<input type="hidden" name="MASTERID" id="MASTERID" value="${pd.MASTERID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>图片库类型:</td>
								<td>
								<select
											class="chosen-select form-control" data="${pd.IMGTYPE }"
											name="IMGTYPE" id="IMGTYPE" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">全部</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty typeList}">
															<c:forEach items="${typeList}" var="t" varStatus="vst">
																<option value="${t.ID }">${t.TYPENAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
										</select>
								</td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>标题:</td>
								<td><input type="text" name="IMGTITLE" id="IMGTITLE" value="${pd.IMGTITLE}" maxlength="200" placeholder="这里输入标题" title="标题" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">描述:</td>
								<td><input type="text" name="IMGDESCRIPTION" id="IMGDESCRIPTION" value="${pd.IMGDESCRIPTION}" maxlength="500" placeholder="这里输入描述" title="描述" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>图片:</td>
								<td>
								<div style="float:left;">
								<c:choose>
								<c:when test="${pd.IMGPATH==null || pd.IMGPATH==''}">
								<img id="imgLogo" src="<%=basePath%>uploadFiles/uploadImgs/kode-icon.png"  width="100"/>
								</c:when>
								<c:otherwise>
								<img id="imgLogo" src="<%=basePath%>uploadFiles/uploadImgs/${pd.IMGPATH}"  width="100"/>
								</c:otherwise>
								</c:choose>
								</div>
								<div style="clear: both;"><input type="file" name="TP_URL" id="uploadify1" keepDefaultStyle = "true"/></div>
								<input type="hidden" name="IMGPATH" id="IMGPATH" value="${pd.IMGPATH }"/>
								<input type="hidden" value="no" id="hasTp1" />	
								</td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">分组名称:</td>
								<td><input type="text" name="IMGGROUPNAME" id="IMGGROUPNAME" value="${pd.IMGGROUPNAME}" maxlength="50" placeholder="这里输入分组名称" title="分组名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:140px;text-align: right;padding-top: 13px;">是否上传微信服务器:</td>
								<td>
								<c:choose>
    							<c:when test="${pd.ISUPLOAD=='' || pd.ISUPLOAD==null}">
    							<input type="hidden" name="ISUPLOAD" id="ISUPLOAD" value="0"/>
    							</c:when>
    							<c:otherwise>
    							<input type="hidden" name="ISUPLOAD" id="ISUPLOAD"  value="${pd.ISUPLOAD}"/>
    							</c:otherwise>
    							</c:choose>
								<input onclick="setPIC();" name="setIsPic" id="setIsPic" value="${pd.ISUPLOAD}"  title="是否上传微信服务器" class="ace ace-switch ace-switch-5" type="checkbox" <c:if test="${pd.ISUPLOAD == 1 }"> checked="checked"</c:if> />
    							<span class="lbl"></span>
								</td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">排序:</td>
								<td>
								<c:choose>
								<c:when test="${pd.IMGSORT==null || pd.IMGSORT==''}">
								<input type="number" min="0" name="IMGSORT" id="IMGSORT"  value="0" maxlength="32" placeholder="这里输入排序" title="排序" style="width:98%;"/>
								</c:when>
								<c:otherwise>
								<input type="number" min="0" name="IMGSORT" id="IMGSORT"  value="${pd.IMGSORT}" maxlength="32" placeholder="这里输入排序" title="排序" style="width:98%;"/>
								</c:otherwise>
								</c:choose>
								</td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="IMGREMARK" id="IMGREMARK" value="${pd.IMGREMARK}" maxlength="200" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="2">
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
		'fileExt'	:	"*.jpg;",//设置可以选择的文件的类型，格式如：'*.jpg;*.png;*.gif'。 *.doc;*.pdf;*.rar
		'fileDesc'	:	"请选择jpg图片",//设置可以选择的文件的类型提示
		//'fileExt'     : '*.jpg;*.gif;*.png',
		//'fileDesc'    : 'Please choose(.JPG, .GIF, .PNG)',
		'auto'		:	true,
		'multi'		:	false,//是否允许多文件上传
		'simUploadLimit':	1,//同时运行上传的进程数量
		'buttonText':	"files",
		'scriptData':	{'uploadPath':'/uploadFiles/uploadImgs/'},//{'uploadPath':'/uploadFiles/uploadImgs/','fileNmae':'watermark.png'}这个参数用于传递用户自己的参数，此时'method' 必须设置为GET, 后台可以用request.getParameter('name')获取名字的值
		'method'	:	"GET",
		'onComplete':function(event,queueId,fileObj,response,data){
			$("#IMGPATH").val(response.trim());
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
		<script type="text/javascript">
		$(top.hangge());
		//保存
		function save(){
			if($("#IMGTYPE").val()=="" || $("#IMGTYPE").val()=="-1"){
				$("#IMGTYPE").tips({
					side:3,
		            msg:'请选择图片库类型',
		            bg:'#AE81FF',
		            time:2
		        });
			return false;
			}
			if($("#IMGTITLE").val()==""){
				$("#IMGTITLE").tips({
					side:3,
		            msg:'请输入标题',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IMGTITLE").focus();
			return false;
			}
			if($("#IMGPATH").val()==""){
				$("#imgLogo").tips({
					side:3,
		            msg:'请上传图片',
		            bg:'#AE81FF',
		            time:2
		        });
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		//改变是否显示
		function setPIC(){
			if($("#setIsPic").val()=="1"){
				$("#setIsPic").val(0)
				$("#ISUPLOAD").val(0)
			}else{
				$("#ISUPLOAD").val(1)
				$("#setIsPic").val(1)
			}
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
			if($("#IMGTYPE").attr('data')!='' && $("#IMGTYPE").attr('data') !=-1){
				$("#IMGTYPE").val($("#IMGTYPE").attr('data'));
			}
		});
		</script>
</body>
</html>