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
					
					<form action="place/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<input type="hidden" name="CREATEDATE" id="CREATEDATE" value="${pd.CREATEDATE}"/>
						<input type="hidden" name="PLACEPLU" id="PLACEPLU" value="${pd.PLACEPLU}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">所属门店:</td>
								<td>
								<select onchange="loadShelves()" class="chosen-select form-control" data="${pd.STOREID }"  name="STOREID" id="STOREID" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">所属门店</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty storeList}">
															<c:forEach items="${storeList}" var="c" varStatus="vst">
																<option value="${c.ID }">${c.STORENAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
										</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">所属货架:</td>
								<td>
								<select  class="chosen-select form-control" onchange="checkPlace()" data="${pd.SHELVESID }"  name="SHELVESID" id="SHELVESID" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">请选择货架</option>
												<!-- 开始循环
													<c:choose>
														<c:when test="${not empty shelvesList}">
															<c:forEach items="${shelvesList}" var="s" varStatus="vst">
																<option value="${s.ID }">${s.SHELVESNAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>  -->
										</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">架位名称:</td>
								<td><input type="text" name="PLACENAME" id="PLACENAME" value="${pd.PLACENAME}" maxlength="50" placeholder="这里输入架位名称" title="架位名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">架位状态:</td>
								<td>
								<select class="chosen-select form-control" data="${pd.PLACETYPEID }" name="PLACETYPEID" id="PLACETYPEID" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">架位状态</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty placeTypeList}">
															<c:forEach items="${placeTypeList}" var="t" varStatus="vst">
																<option value="${t.ID }">${t.NAME }</option>
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
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		//返回
		function goback(){
			top.jzts();
			window.location.href="<%=basePath%>place/list.do";
		}
		
		
		//保存
		function save(){
			
			if($("#STOREID").val()=="" || $("#STOREID").val()=='-1'){
				$("#STOREID").tips({
					side:3,
		            msg:'请选择所属门店',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOREID").focus();
			return false;
			}
			if($("#SHELVESID").val()=="" || $("#SHELVESID").val()=='-1'){
				$("#SHELVESID").tips({
					side:3,
		            msg:'请选择所属货架',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SHELVESID").focus();
			return false;
			}
			if($("#PLACENAME").val()==""){
				$("#PLACENAME").tips({
					side:3,
		            msg:'请输入架位名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLACENAME").focus();
			return false;
			}
			if($("#PLACETYPEID").val()=="" || $("#PLACETYPEID").val()=="-1"){
				$("#PLACETYPEID").tips({
					side:3,
		            msg:'请选择架位状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PLACETYPEID").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		
		if($("#STOREID").attr('data')!='' && $("#STOREID").attr('data') !=-1){
			$("#STOREID").val($("#STOREID").attr('data'));
		}
		if($("#PLACETYPEID").attr('data')!='' && $("#PLACETYPEID").attr('data') !=-1){
			$("#PLACETYPEID").val($("#PLACETYPEID").attr('data'));
		}
		if(($("#ID").val()!='' && $("#ID").val() !=-1) && $("#STOREID").attr('data')!='' && $("#STOREID").attr('data') !=-1){
			loadShelves();
			$("#SHELVESID").val($("#SHELVESID").attr('data'));
		}
		
		function loadShelves(){
			var storeId=$("#STOREID").val();
			if(storeId=='' || storeId=='-1'){
				$("#STOREID").tips({
					side:3,
		            msg:'请选择所属货架',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOREID").focus();
				return false;
			}
			$.ajax({
				url:"<%=basePath%>place/loadShelvesList.do?storeId="+storeId,
				type:'post',
				dataType:'json',
				success:function(r){
					if(r.type==0){
						var html ='<option value="-1">选择所属货架</option>';
						var list=r.list;
						for(var i=0;i<list.length;i++){
							html+='<option value="'+list[i].ID+'" rankCount="'+list[i].RANKCOUNT+'">'+list[i].SHELVESNAME+'</option>';
						}
						$("#SHELVESID").html(html);
						if(($("#ID").val()!='' && $("#ID").val() !=-1) && $("#STOREID").attr('data')!='' && $("#STOREID").attr('data') !=-1){
							$("#SHELVESID").val($("#SHELVESID").attr('data'));
						}
					}else{
						$("#STOREID").tips({
							side:3,
				            msg:r.msg,
				            bg:'#AE81FF',
				            time:2
				        });
						$("#STOREID").focus();
					}
				}
			});
			
		}
		
		
		function checkPlace(){
			var shelvesId=$("#SHELVESID").val();
			var rankCount=$("#SHELVESID").find("option:selected").attr("rankCount");;
			if(parseInt(rankCount)<=0){
				$("#SHELVESID").tips({
					side:3,
		            msg:'当前货架下没有任何货位！请指定当前货架货位数量！',
		            bg:'#AE81FF',
		            time:2
		        });
				return false;
				
			}
			if(shelvesId=='' || shelvesId=='-1'){
				$("#SHELVESID").tips({
					side:3,
		            msg:'请选择所属货架',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SHELVESID").focus();
				return false;
			}
			
			$.ajax({
				url:"<%=basePath%>place/checkPlace.do?rankCount="+rankCount+"&shelvesId="+shelvesId,
				type:'post',
				dataType:'json',
				success:function(r){
					if(r.type==0){
						
						
					}else{
						$("#SHELVESID").tips({
							side:3,
				            msg:r.msg,
				            bg:'#AE81FF',
				            time:5
				        });
						$("#SHELVESID").focus();
					}
				}
			});
			
		}
		</script>
</body>
</html>