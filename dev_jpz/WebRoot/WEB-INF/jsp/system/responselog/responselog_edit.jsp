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
					
					<form action="responselog/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<input type="hidden" name="ARTICLEIDS" id="ARTICLEIDS" value="${pd.ARTICLEIDS}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:140px;text-align: right;padding-top: 13px;">接收消息类型:</td>
								<td>
								<select
											class="chosen-select form-control" data="${pd.RESPONSETYPE }"
											name="RESPONSETYPE" id="RESPONSETYPE" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">全部</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty typeList}">
															<c:forEach items="${typeList}" var="t" varStatus="vst">
															    <c:if test="${t.ID==24}">
															    <option value="${t.ID }">${t.TYPENAME }</option>
															    </c:if>
																
															</c:forEach>
														</c:when>
													</c:choose>
										</select>
								</td>
							</tr>
							<tr>
								<td style="width:140px;text-align: right;padding-top: 13px;">发送消息类型:</td>
								<td>
								<select onchange="setSENDTYPE();"
											class="chosen-select form-control" data="${pd.SENDTYPE }"
											name="SENDTYPE" id="SENDTYPE" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">全部</option>
												<option value="1">文本</option>
												<option value="2">软文</option>
										</select>
								</td>
							</tr>
							<tr id="trArticle" style="display: none;">
								<td style="width:140px;text-align: right;padding-top: 13px;">选择软文:</td>
								<td>
								<a class="btn btn-mini btn-primary" onclick="openArticle();" id="selectArticle">选择软文</a>
								<span id="data"></span>
								</td>
							</tr>
							<tr>
								<td style="width:140px;text-align: right;padding-top: 13px;">关键字:</td>
								<td>
								<input type="text" name="KEYWORDS" id="KEYWORDS" value="${pd.KEYWORDS}" maxlength="500" placeholder="这里输入关键字" title="关键字" style="width:98%;"/>
								</td>
							</tr>
							<tr id="trCONTENT">
								<td style="width:140px;text-align: right;padding-top: 13px;">内容:</td>
								<td>
								<textarea rows="4" cols="50" name="CONTENT" id="CONTENT" value="${pd.CONTENT}"  maxlength="500" placeholder="这里输入内容" title="内容">${pd.CONTENT}</textarea>
								</td>
							</tr>
							<tr>
								<td style="width:140px;text-align: right;padding-top: 13px;">是否有效:</td>
								<td>
								<c:choose>
    							<c:when test="${pd.ISVALID=='' || pd.ISVALID==null}">
    							<input type="hidden" name="ISVALID" id="ISVALID"  value="1"/>
    							</c:when>
    							<c:otherwise>
    							<input type="hidden" name="ISVALID" id="ISVALID"  value="${pd.ISVALID}"/>
    							</c:otherwise>
    							</c:choose>
								<input onclick="setPIC();" name="setIsValid" id="setIsValid" value="${pd.ISVALID}"  title="是否有效" class="ace ace-switch ace-switch-5" type="checkbox" <c:if test="${pd.ISVALID == 1 || pd.ISVALID==null}"> checked="checked"</c:if> />
    							<span class="lbl"></span>
								</td>
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
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		
		
		//加载软文列表
		function openArticle(){
			var param={};
			param["keywords"] = $("#keywords").val();//标题
			param["articleTypeID"] = $("#articleTypeID").val();//标题
			param["lastStart"] = $("#lastStart").val();//标题
			param["lastEnd"] = $("#lastEnd").val();//标题
			 $.ajax({
					type: "POST",
					url: '<%=basePath%>responselog/findArticleList.do',
			    	data: param,
					dataType:'html',
					cache: false,
					success: function(data){
						$("#data").html(data);
						$("#data").fadeIn("slow");
						$("#selectArticle").fadeOut("slow");
						
					}
				});
		}
		
		//显示图文
		function setSENDTYPE(){
			if($("#SENDTYPE").val()=='2'){
				$("#selectArticle").fadeIn("slow");
				$("#trCONTENT").fadeOut("slow");
				$("#trArticle").fadeIn("slow");
			}else{
				$("#trArticle").fadeOut("slow");
				$("#trCONTENT").fadeIn("slow");
				$("#data").html('');
			}
		}
		//返回
		function goback(){
			top.jzts();
			window.location.href="<%=basePath%>responselog/list.do";
		}
		
		//改变是否显示
		function setPIC(){
			if($("#setIsValid").val()=="1"){
				$("#setIsValid").val(2)
				$("#ISVALID").val(2)
			}else{
				$("#SHOW_COVER_PIC").val(1)
				$("#ISVALID").val(1)
			}
		}
		//保存
		function save(){
			if($("#RESPONSETYPE").val()==""){
				$("#RESPONSETYPE").tips({
					side:3,
		            msg:'请输入接收消息类型',
		            bg:'#AE81FF',
		            time:2
		        });
			return false;
			}
			if($("#SENDTYPE").val()==""){
				$("#SENDTYPE").tips({
					side:3,
		            msg:'请输入发送消息类型',
		            bg:'#AE81FF',
		            time:2
		        });
			return false;
			}
			if($("#KEYWORDS").val()==""){
				$("#KEYWORDS").tips({
					side:3,
		            msg:'请输入关键字',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#KEYWORDS").focus();
			return false;
			}
			if($("#CONTENT").val()==""){
				$("#CONTENT").tips({
					side:3,
		            msg:'请输入内容',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONTENT").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
			
			if ($("#RESPONSETYPE").attr('data') != ''
				&& $("#RESPONSETYPE").attr('data') != -1) {
			$("#RESPONSETYPE").val($("#RESPONSETYPE").attr('data'));
			}
			if ($("#SENDTYPE").attr('data') != ''&& $("#SENDTYPE").attr('data') != -1) {
				$("#SENDTYPE").val($("#SENDTYPE").attr('data'));
				if($("#SENDTYPE").attr('data')=='2'){
					$("#selectArticle").fadeIn("slow");
					$("#trCONTENT").fadeOut("slow");
					$("#trArticle").fadeIn("slow");
					$("#selectArticle").html('变更软文');
					$("#data").html($("#CONTENT").val());
				}
			}
		});
		</script>
</body>
</html>