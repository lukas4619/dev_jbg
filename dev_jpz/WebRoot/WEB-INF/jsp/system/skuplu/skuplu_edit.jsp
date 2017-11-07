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
					
					<form action="skuplu/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<input type="hidden" name="PROURL"
									id="PROURL" value="${pd.PROURL}" />
									<input type="hidden" name="CREATEDATE"
									id="CREATEDATE" value="${pd.CREATEDATE}" />
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">商品编码:</td>
								<td><input type="number" name="PLUCODE" id="PLUCODE" value="${pd.PLUCODE}" maxlength="8" placeholder="这里输入商品编码" title="商品编码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">商品名称:</td>
								<td><input type="text" name="PLUNAME" id="PLUNAME" value="${pd.PLUNAME}" maxlength="50" placeholder="这里输入商品名称" title="商品名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">商品条形码:</td>
								<td><input type="text" name="BARCODE" id="BARCODE" value="${pd.BARCODE}" maxlength="50" placeholder="这里输入商品条形码" title="商品条形码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">单位:</td>
								<td><input type="text" name="UNITS" id="UNITS" value="${pd.UNITS}" maxlength="50" placeholder="这里输入单位" title="单位" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">规格:</td>
								<td><input type="text" name="SPEC" id="SPEC" value="${pd.SPEC}" maxlength="50" placeholder="这里输入规格" title="规格" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">图片:</td>
											<td>
												<div style="float:left;">
													<c:choose>
														<c:when test="${pd.PLUIMAGE==null || pd.PLUIMAGE==''}">
															<img id="imgLogo"
																src="<%=basePath%>uploadFiles/uploadImgs/kode-icon.png"
																width="100" />
														</c:when>
														<c:otherwise>
															<img id="imgLogo"
																src="<%=basePath%>uploadFiles/uploadImgs/${pd.PLUIMAGE}"
																width="100" />
														</c:otherwise>
													</c:choose>
												</div>
												<div style="clear: both;">
													<input type="file" name="TP_URL" id="uploadify1" keepDefaultStyle="true" />
												</div> 
												<input type="hidden" name="PLUIMAGE" id="PLUIMAGE" value="${pd.PLUIMAGE }" />
												 <input type="hidden" value="no" id="hasTp1" /></td>
							</tr> 
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">售价:</td>
								<td>
								<input onkeypress='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												onkeyup='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
								type="text" name="PRICE" id="PRICE" value="${pd.PRICE}" maxlength="11" placeholder="这里输入售价" title="售价" style="width:98%;"/>
								</td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">促销价:</td>
								<td>
								<input onkeypress='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
												onkeyup='if(!this.value.match(/^[\+\-]?\d*?\.?\d*?$/))this.value=this.t_value;else this.t_value=this.value;if(this.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/))this.o_value=this.value'
								type="text" name="PPRICE" id="PPRICE" value="${pd.PPRICE}" maxlength="11" placeholder="这里输入促销价" title="促销价" style="width:98%;"/>
								
								</td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">商品分类:</td>
								<td>
								<select class="chosen-select form-control" data="${pd.PLUCLASSID }" name="PLUCLASSID" id="PLUCLASSID" data-placeholder="请选择" style="vertical-align:top;width: 120px;">
								 <option value="-1">商品分类</option>
								 <!-- 开始循环 -->
								<c:choose>
									<c:when test="${not empty pluClassList}">
										<c:forEach items="${pluClassList}" var="c" varStatus="vst">
											<option value="${c.ID }">${c.NAME }</option>
										</c:forEach>
									</c:when>
								</c:choose>
					           </select>
								</td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">商品类型:</td>
								<td>
								<select class="chosen-select form-control" data="${pd.PLUTYPEID }" name="PLUTYPEID" id="PLUTYPEID" data-placeholder="请选择" style="vertical-align:top;width: 120px;">
									<option value="-1">商品类型</option>
									<!-- 开始循环 -->
										<c:choose>
											<c:when test="${not empty pluTypeList}">
												<c:forEach items="${pluTypeList}" var="t" varStatus="vst">
													<option value="${t.ID }">${t.NAME }</option>
												</c:forEach>
											</c:when>
										</c:choose>
							   </select>
								</td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">商品状态:</td>
								<td>
								<select class="chosen-select form-control" data="${pd.PLUSTATUS }" name="PLUSTATUS" id="PLUSTATUS" data-placeholder="请选择" style="vertical-align:top;width: 120px;">
									<option value="-1">商品状态</option>
									<!-- 开始循环 -->
										<c:choose>
											<c:when test="${not empty pluStatusList}">
												<c:forEach items="${pluStatusList}" var="p" varStatus="vst">
													<option value="${p.ID }">${p.NAME }</option>
												</c:forEach>
											</c:when>
										</c:choose>
							   </select>
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
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 上传控件 -->
	<script src="static/ace/js/ace/elements.fileinput.js"></script>
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
		'fileExt'	:	"*.jpg;*.gif;*.png",//设置可以选择的文件的类型，格式如：'*.jpg;*.png;*.gif'。 *.doc;*.pdf;*.rar
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
			$("#PLUIMAGE").val(response.trim());
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
			if($("#PLUCODE").val()==""){
				$("#PLUCODE").tips({
					side:3,
		            msg:'请输入商品编码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLUCODE").focus();
			return false;
			}
			if($("#PLUNAME").val()==""){
				$("#PLUNAME").tips({
					side:3,
		            msg:'请输入商品名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLUNAME").focus();
			return false;
			}
			if($("#BARCODE").val()==""){
				$("#BARCODE").tips({
					side:3,
		            msg:'请输入商品条形码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BARCODE").focus();
			return false;
			}
			if($("#PLUIMAGE").val()==""){
				$("#imgLogo").tips({
					side:3,
		            msg:'请上传图片',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLUIMAGE").focus();
			return false;
			}
			if($("#PRICE").val()==""){
				$("#PRICE").tips({
					side:3,
		            msg:'请输入售价',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PRICE").focus();
			return false;
			}
			if($("#PLUCLASSID").val()=="" || $("#PLUCLASSID").val()=="-1"){
				$("#PLUCLASSID").tips({
					side:3,
		            msg:'请选择商品分类',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLUCLASSID").focus();
			return false;
			}
			if($("#PLUTYPEID").val()=="" || $("#PLUTYPEID").val()=="-1"){
				$("#PLUTYPEID").tips({
					side:3,
		            msg:'请选择商品类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLUTYPEID").focus();
			return false;
			}
			if($("#PLUSTATUS").val()=="" || $("#PLUSTATUS").val()=="-1"){
				$("#PLUSTATUS").tips({
					side:3,
		            msg:'请选择商品状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLUSTATUS").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
			if($("#PLUCLASSID").attr('data')!='' && $("#PLUCLASSID").attr('data') !=-1){
				$("#PLUCLASSID").val($("#PLUCLASSID").attr('data'));
			}
			if($("#PLUTYPEID").attr('data')!='' && $("#PLUTYPEID").attr('data') !=-1){
				$("#PLUTYPEID").val($("#PLUTYPEID").attr('data'));
			}
			if($("#PLUSTATUS").attr('data')!='' && $("#PLUSTATUS").attr('data') !=-1){
				$("#PLUSTATUS").val($("#PLUSTATUS").attr('data'));
			}
		});
		
		//过滤类型
		function fileType(obj){
			var fileType=obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
		    if(fileType != '.gif' && fileType != '.png' && fileType != '.jpg' && fileType != '.jpeg'){
		    	$("#tp").tips({
					side:3,
		            msg:'请上传图片格式的文件',
		            bg:'#AE81FF',
		            time:3
		        });
		    	$("#tp").val('');
		    	document.getElementById("tp").files[0] = '请选择图片';
		    }
		}
		
		//删除图片
		function delP(PATH,PICTURES_ID){
			 if(confirm("确定要删除图片？")){
				var url = "pictures/deltp.do?PATH="+PATH+"&PICTURES_ID="+PICTURES_ID+"&guid="+new Date().getTime();
				$.get(url,function(data){
					if(data=="success"){
						alert("删除成功!");
						document.location.reload();
					}
				});
			} 
		}
		</script>
</body>
</html>