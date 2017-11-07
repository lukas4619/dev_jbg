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
					<form action="frontArticle/${msg }.do" name="articleForm" id="articleForm" method="post">
						<input type="hidden" name="ARTICLEID" id="ARTICLEID" value="${pd.ARTICLEID}"/>
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<input type="hidden" name="PRODUCTID" id="PRODUCTID" value="${pd.PRODUCTID}"/>
						<input type="hidden" name="THUMB_MEDIA_ID" id="THUMB_MEDIA_ID" value="${pd.THUMB_MEDIA_ID}"/>
						<input type="hidden" name="MEDIA_ID" id="MEDIA_ID" value="${pd.MEDIA_ID}"/>
						<input type="hidden" name="CONTENT_SOURCE_URL" id="CONTENT_SOURCE_URL" value="${pd.CONTENT_SOURCE_URL}"/>
						<input type="hidden" name="MEDIA_ID_CREATED_AT" id="MEDIA_ID_CREATED_AT" value="${pd.MEDIA_ID_CREATED_AT}"/>
						<input type="hidden" name="THUMB_CREATED_AT" id="THUMB_CREATED_AT" value="${pd.THUMB_CREATED_AT}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>软文类型:</td>
								<td>
							<select
											class="chosen-select form-control" data="${pd.ARTICLETYPEID }"
											name="ARTICLETYPEID" id="ARTICLETYPEID" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
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
								<td style="width:150px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>标题:</td>
								<td><input type="text" name="TITLE" id="TITLE" value="${pd.TITLE}" maxlength="200" placeholder="这里输入标题" title="标题" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>封面图片:</td>
								<td>
								<div style="float:left;">
								<c:choose>
								<c:when test="${pd.ARTICLEIMG==null || pd.ARTICLEIMG==''}">
								<img id="imgLogo" src="<%=basePath%>uploadFiles/uploadImgs/kode-icon.png"  width="100"/>
								</c:when>
								<c:otherwise>
								<img id="imgLogo" src="<%=basePath%>uploadFiles/uploadImgs/${pd.ARTICLEIMG}"  width="100"/>
								</c:otherwise>
								</c:choose>
								</div>
								<div style="clear: both;"><input type="file" name="TP_URL" id="uploadify1" keepDefaultStyle = "true"/></div>
								<input type="hidden" name="ARTICLEIMG" id="ARTICLEIMG" value="${pd.ARTICLEIMG }"/>
								<input type="hidden" value="no" id="hasTp1" />	
								<span style="color: red;">只能上传*.jpg格式图片</span>		
								</td>
							</tr>
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>作者标识:</td>
								<td><input type="text" name="AUTHORID" id="AUTHORID" value="${member.memberId}" maxlength="50" readonly="readonly" placeholder="这里输入作者标识" title="作者标识" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;">作者名称:</td>
								<td><input type="text" name="AUTHOR" id="AUTHOR" value="${pd.AUTHOR}" maxlength="50" placeholder="这里输入作者名称" title="作者名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>描述:</td>
								<td><input type="text" name="DIGEST" id="DIGEST" value="${pd.DIGEST}" maxlength="500" placeholder="这里输入描述" title="描述" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>内容:</td>
								<td>
								<div  id="containerMsg" class="form-group draggable">
							      <div class="col-sm-9">
								<div class="ueQ313596790Que"></div>
								<script id="container" type="text/plain" style="width:100%;height:400px;"></script>
								<div class="ueQ313596790Que"></div>
								</div>
								</div>
								</td>
							</tr>
							<c:if test="${not empty imgList}">
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;">相关图片:</td>
								<td>
								  <c:choose>
														<c:when test="${not empty imgList}">
															<c:forEach items="${imgList}" var="i" varStatus="il">
																<img title="${i.IMGTITLE }" style="cursor: pointer;"  src="<%=basePath%>uploadFiles/uploadImgs/${i.IMGPATH}"  width="100"/>
																<span>标题：${i.IMGTITLE }</span>
																<span>描述：${i.IMGDESCRIPTION }</span>
															</c:forEach>
														</c:when>
													</c:choose>
									<span style="color: red;">备注：可以将相关图片直接拖到软文内容中</span>
								</td>
							</tr>
							</c:if>
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>状态:</td>
								<td>
								<select
											class="chosen-select form-control" disabled="disabled" data="${pd.ARTICLESTATEID }"
											name="ARTICLESTATEID" id="ARTICLESTATEID" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<!-- 开始循环 -->
												<c:choose>
													<c:when test="${not empty stateList}">
														<c:forEach items="${stateList}" var="s" varStatus="vss">
															<option <c:if test="${s.ID == pd.ARTICLESTATEID}">selected</c:if> value="${s.ID }">${s.NAME }</option>
														</c:forEach>
													</c:when>
												</c:choose>
								</select>
								</td>
							</tr>
							
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="ARTICLEREMARK" id="ARTICLEREMARK" value="${pd.ARTICLEREMARK}" maxlength="500" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="goback();">取消</a>
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
	<div id="fwb" style="display: none;">
	  ${pd.CONTENT}
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
			$("#ARTICLEIMG").val(response.trim());
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
		<script type="text/javascript">
		$(top.hangge());
		var ue;
		//百度富文本
		setTimeout("ueditor()",500);
		function ueditor(){
			ue =UE.getEditor('container');
			ue.ready(function() {
			    //设置编辑器的内容
			    if('${msg }'!='save' && $("#fwb").html().length>0){
					ue.setContent($("#fwb").html());
			    }
			   
			});
		}
		
		//改变是否显示
		function setPIC(){
			if($("#setIsPic").val()=="1"){
				$("#setIsPic").val(0)
				$("#SHOW_COVER_PIC").val(0)
			}else{
				$("#SHOW_COVER_PIC").val(1)
				$("#setIsPic").val(1)
			}
		}
		
		function setProductId(id,name){
			$("#PRODUCTID").val(id);
			$("#proName").html(name);
			$("#data").html('');
			$("#data").hide();
		}
		
		//返回
		function goback(){
			top.jzts();
			window.location.href="<%=basePath%>article/list.do";
		}
		
		//加载产品列表
		function openPro(){
			var param={};
			param["keywords"] = $("#keywords").val();//标题
			param["articleTypeID"] = $("#articleTypeID").val();//标题
			param["lastStart"] = $("#lastStart").val();//标题
			param["lastEnd"] = $("#lastEnd").val();//标题
			 $.ajax({
					type: "POST",
					url: '<%=basePath%>article/findProList.do',
			    	data: param,
					dataType:'html',
					cache: false,
					success: function(data){
						$("#data").show();
						$("#data").html(data);
					}
				});
		}
		//保存
		function save(){
			debugger;
			if($("#ARTICLETYPEID").val()==""){
				$("#ARTICLETYPEID").tips({
					side:3,
		            msg:'请选择软文类型',
		            bg:'#AE81FF',
		            time:2
		        });
			return false;
			}
// 			if($("#PRODUCTID").val()==""){
// 				$("#proName").tips({
// 					side:3,
// 		            msg:'请选择关联产品',
// 		            bg:'#AE81FF',
// 		            time:2
// 		        });
// 			return false;
// 			}
			if($("#TITLE").val()==""){
				$("#TITLE").tips({
					side:3,
		            msg:'请输入标题',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TITLE").focus();
			return false;
			}
			if($("#ARTICLEIMG").val()==""){
				$("#imgLogo").tips({
					side:3,
		            msg:'请上传封面图片',
		            bg:'#AE81FF',
		            time:2
		        });
			return false;
			}
			if($("#AUTHORID").val()==""){
				$("#AUTHORID").tips({
					side:3,
		            msg:'请输入作者标识',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#AUTHORID").focus();
			return false;
			}
			if($("#DIGEST").val()==""){
				$("#DIGEST").tips({
					side:3,
		            msg:'请输入描述',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DIGEST").focus();
			return false;
			}
			if(!ue.hasContents()){
				$("#containerMsg").tips({
					side:3,
		            msg:'请输入内容',
		            bg:'#AE81FF',
		            time:2
		        });
			return false;
			}
			if($("#ARTICLESTATEID").val()==""){
				$("#ARTICLESTATEID").tips({
					side:3,
		            msg:'请选择软文状态',
		            bg:'#AE81FF',
		            time:2
		        });
				return false;
			}
			$("#articleForm").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
			if ($("#ARTICLETYPEID").attr('data') != ''
				&& $("#ARTICLETYPEID").attr('data') != -1) {
			$("#ARTICLETYPEID").val($("#ARTICLETYPEID").attr('data'));
			}
			if ($("#ARTICLESTATEID").attr('data') != ''
					&& $("#ARTICLESTATEID").attr('data') != -1) {
				$("#ARTICLESTATEID").val($("#ARTICLESTATEID").attr('data'));
			}
		});
		</script>
</body>
</html>