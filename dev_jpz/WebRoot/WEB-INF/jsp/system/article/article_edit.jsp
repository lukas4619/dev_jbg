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
					<form action="article/${msg }.do" name="articleForm" id="articleForm" method="post">
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
								<td style="width:150px;text-align: right;padding-top: 13px;"><span style="color: red;">*</span>关联产品:</td>
								<td>
								<label id="proName"></label>
								<c:choose>
								<c:when test="${pd.PRODUCTID==null || pd.PRODUCTID==''}">
								<a class="btn btn-mini btn-primary" onclick="openPro();">关联产品</a>
								</c:when>
								<c:otherwise>
								<a class="btn btn-mini btn-primary" onclick="openPro();">变更关联产品</a>
								</c:otherwise>
								</c:choose>
								<span id="data"></span>
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
								<td><input type="text" name="AUTHORID" id="AUTHORID" value="${pd.AUTHORID}" maxlength="50" placeholder="这里输入作者标识" title="作者标识" style="width:98%;"/></td>
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
								<td style="width:150px;text-align: right;padding-top: 13px;">是否显示封面:</td>
								<td>
								<c:choose>
    							<c:when test="${pd.SHOW_COVER_PIC=='' || pd.SHOW_COVER_PIC==null}">
    							<input type="hidden" name="SHOW_COVER_PIC" id="SHOW_COVER_PIC"  value="0"/>
    							</c:when>
    							<c:otherwise>
    							<input type="hidden" name="SHOW_COVER_PIC" id="SHOW_COVER_PIC"  value="${pd.SHOW_COVER_PIC}"/>
    							</c:otherwise>
    							</c:choose>
								<input onclick="setPIC();" name="setIsPic" id="setIsPic" value="${pd.SHOW_COVER_PIC}"  title="是否显示封面" class="ace ace-switch ace-switch-5" type="checkbox" <c:if test="${pd.SHOW_COVER_PIC == 1 }"> checked="checked"</c:if> >
    							<span class="lbl"></span>
								</td>
							</tr>
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;">浏览量:</td>
								<td>
								<c:choose>
    							<c:when test="${pd.ARTICLEPV=='' || pd.ARTICLEPV==null}">
    							<input type="number" min="0" name="ARTICLEPV" id="ARTICLEPV" value="0" maxlength="32" placeholder="这里输入浏览量" title="浏览量" style="width:120px;"/>
    							</c:when>
    							<c:otherwise>
    							<input type="number" min="0" name="ARTICLEPV" id="ARTICLEPV" value="${pd.ARTICLEPV}" maxlength="32" placeholder="这里输入浏览量" title="浏览量" style="width:120px;"/>
							    </c:otherwise>
    							</c:choose>
								</td>
							</tr>
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;">点赞量:</td>
								<td>
								<c:choose>
    							<c:when test="${pd.ARTICLELIKE=='' || pd.ARTICLELIKE==null}">
    							<input type="number" min="0" name="ARTICLELIKE" id="ARTICLELIKE" value="0" maxlength="32" placeholder="这里输入点赞量" title="点赞量" style="width:120px;"/>
    							</c:when>
    							<c:otherwise>
    							<input type="number" min="0" name="ARTICLELIKE" id="ARTICLELIKE" value="${pd.ARTICLELIKE}" maxlength="32" placeholder="这里输入点赞量" title="点赞量" style="width:120px;"/>
							    </c:otherwise>
    							</c:choose>
								</td>
							</tr>
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;">分享量:</td>
								<td>
								<c:choose>
    							<c:when test="${pd.SHARNUMBER=='' || pd.SHARNUMBER==null}">
    							<input type="number" min="0" name="SHARNUMBER" id="SHARNUMBER" value="0" maxlength="32" placeholder="这里输入分享量" title="分享量" style="width:120px;"/>
    							</c:when>
    							<c:otherwise>
    							<input type="number" min="0" name="SHARNUMBER" id="SHARNUMBER" value="${pd.SHARNUMBER}" maxlength="32" placeholder="这里输入分享量" title="分享量" style="width:120px;"/>
							    </c:otherwise>
    							</c:choose>
								</td>
							</tr>
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;">推送次数:</td>
								<td>
								<c:choose>
    							<c:when test="${pd.PUSHNUMBER=='' || pd.PUSHNUMBER==null}">
    							<input type="number" min="0"  name="PUSHNUMBER" id="PUSHNUMBER" value="0" maxlength="32" placeholder="这里输入推送次数" title="推送次数" style="width:120px;"/>
    							</c:when>
    							<c:otherwise>
    							<input type="number" min="0"  name="PUSHNUMBER" id="PUSHNUMBER" value="${pd.PUSHNUMBER}" maxlength="32" placeholder="这里输入推送次数" title="推送次数" style="width:120px;"/>
							    </c:otherwise>
    							</c:choose>
								</td>
							</tr>
							<tr>
								<td style="width:150px;text-align: right;padding-top: 13px;">排序:</td>
								<td>
								<c:choose>
    							<c:when test="${pd.SORT=='' || pd.SORT==null}">
    							<input type="number" min="0" name="SORT" id="SORT" value="0" maxlength="32" placeholder="这里输入排序" title="排序" style="width:120px;"/>
    							</c:when>
    							<c:otherwise>
    							<input type="number" min="0" name="SORT" id="SORT" value="${pd.SORT}" maxlength="32" placeholder="这里输入排序" title="排序" style="width:120px;"/>
							    </c:otherwise>
    							</c:choose>
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
			if($("#ARTICLETYPEID").val()==""){
				$("#ARTICLETYPEID").tips({
					side:3,
		            msg:'请选择软文类型',
		            bg:'#AE81FF',
		            time:2
		        });
			return false;
			}
			if($("#PRODUCTID").val()==""){
				$("#proName").tips({
					side:3,
		            msg:'请选择关联产品',
		            bg:'#AE81FF',
		            time:2
		        });
			return false;
			}
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