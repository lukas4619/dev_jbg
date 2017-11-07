﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
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
							
						<!-- 检索  -->
						<form action="orderdetails/list.do" method="post" name="Form" id="Form">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入关键词" class="nav-search-input" id="nav-search-input" autocomplete="off" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
											<input type="hidden" name="BILLNO" id="BILLNO" value="${pd.BILLNO}"/>
										</span>
									</div>
								</td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="lastStart" id="lastStart"  value="" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="开始日期" title="开始日期"/></td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="lastEnd" id="lastEnd"  value="" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="结束日期" title="结束日期"/></td>
								<td style="vertical-align:top;padding-left:2px;">
								 	门店:<select class="chosen-select form-control" data="${pd.storeId }" name="storeId" id="storeId" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">请选择</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty storeList}">
															<c:forEach items="${storeList}" var="s" varStatus="vst">
																<option value="${s.ID }">${s.STORENAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
										</select>
								</td>
								<td style="vertical-align:top;padding-left:2px;">订单状态:<select
											class="chosen-select form-control" data="${pd.orderStatusID }"
											name="orderStatusID" id="orderStatusID" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">请选择</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty payMentStatusList}">
															<c:forEach items="${payMentStatusList}" var="c" varStatus="vst">
																<option value="${c.ID }">${c.NAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
										</select>
								</td>
								<td style="vertical-align:top;padding-left:2px;">付款状态:<select
											class="chosen-select form-control" data="${pd.payMentStatusID }"
											name="payMentStatusID" id="payMentStatusID" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">请选择</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty orderStatusList}">
															<c:forEach items="${orderStatusList}" var="t" varStatus="vst">
																<option value="${t.ID }">${t.NAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
										</select>
								</td>
								
								
							</tr>
						</table>
						<table style="margin-top:5px;">
							<tr>
							<td style="vertical-align:top;padding-left:2px;">商品分类:<select
											class="chosen-select form-control" data="${pd.pluClassId }"
											name="pluClassId" id="pluClassId" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">请选择</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty pluClassList}">
															<c:forEach items="${pluClassList}" var="cl" varStatus="vst">
																<option value="${cl.ID }">${cl.NAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
										</select>
								</td>
								<td style="vertical-align:top;padding-left:2px;">商品类型:<select
											class="chosen-select form-control" data="${pd.pluTypeId }"
											name="pluTypeId" id="pluTypeId" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">请选择</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty pluTypeList}">
															<c:forEach items="${pluTypeList}" var="tl" varStatus="vst">
																<option value="${tl.ID }">${tl.NAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
										</select>
								</td>
							<c:if test="${QX.cha == 1 }">
								<td style="vertical-align:top;padding-left:2px"><a class="btn btn-light btn-xs" onclick="tosearch();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								</c:if>
								<c:if test="${QX.toExcel == 1 }"><td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="ace-icon fa fa-download bigger-110 nav-search-icon blue"></i></a></td></c:if>
							</tr>
							</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;">	
							<thead>
								<tr>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">订单编号</th>
									<th class="center">商品编码</th>
									<th class="center">商品名称</th>
									<th class="center">商品条形码</th>
									<th class="center">单位</th>
									<th class="center">规格</th>
									<th class="center">售价</th>
									<th class="center">商品分类</th>
									<th class="center">商品类型</th>
									<th class="center">数量</th>
									<th class="center">订单状态</th>
									<th class="center">付款状态</th>
									<th class="center">付款时间</th>
									<th class="center">下单时间</th>
									<th class="center">所属门店</th>
									<th class="center">所属货架</th>
									<th class="center">所属货位</th>
									<th class="center">下单人</th>
								</tr>
							</thead>
													
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty varList}">
									<c:if test="${QX.cha == 1 }">
									<c:forEach items="${varList}" var="var" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class='center'>${var.BILLNO}</td>
											<td class='center'>${var.PLUCODE}</td>
											<td class='center'>${var.PLUNAME}</td>
											<td class='center'>${var.BARCODE}</td>
											<td class='center'>${var.UNITS}</td>
											<td class='center'>${var.SPEC}</td>
											<td class='center'>${var.PRICE}</td>
											<td class='center'>${var.PLUCLASSNAME}</td>
											<td class="center">${var.PLUTYPENAME}</td>
											 <td class="center">${var.PLUCOUNT} </td>
										    <td class="center">${var.STATUSNAME} </td> 
											<td class="center">${var.PAYSTATUSNAME} </td>
										   <td class='center'>
											<fmt:formatDate value="${var.PAYMENTDATE}" pattern="yyyy-MM-dd HH:mm:ss"  />
											</td>
											<td class='center'>
											<fmt:formatDate value="${var.CREATEDATE}" pattern="yyyy-MM-dd HH:mm:ss"  />
											</td>
											<td class="center">${var.STORENAME} </td>
											<td class="center">${var.SHELVESNAME} </td>
											<td class="center">${var.PLACENAME} </td>
											<td class="center">${var.WECHATNAME} </td>
										</tr>
									
									</c:forEach>
									</c:if>
									<c:if test="${QX.cha == 0 }">
										<tr>
											<td colspan="100" class="center">您无权查看</td>
										</tr>
									</c:if>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="100" class="center" >没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						<div class="page-header position-relative">
						<table style="width:100%;">
							<tr>
								<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
							</tr>
						</table>
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

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript">
		$(top.hangge());//关闭加载状态
		//检索
		function tosearch(){
			top.jzts();
			$("#Form").submit();
		}
		$(function() {
		
			//日期框
			$('.date-picker').datepicker({
				autoclose: true,
				todayHighlight: true
			});
			if($("#storeId").attr('data')!='' && $("#storeId").attr('data') !=-1){
				$("#storeId").val($("#storeId").attr('data'));
			}
			if($("#orderStatusID").attr('data')!='' && $("#orderStatusID").attr('data') !=-1){
				$("#orderStatusID").val($("#orderStatusID").attr('data'));
			}
			if($("#payMentStatusID").attr('data')!='' && $("#payMentStatusID").attr('data') !=-1){
				$("#payMentStatusID").val($("#payMentStatusID").attr('data'));
			}
			if($("#pluClassId").attr('data')!='' && $("#pluClassId").attr('data') !=-1){
				$("#pluClassId").val($("#pluClassId").attr('data'));
			}
			if($("#pluTypeId").attr('data')!='' && $("#pluTypeId").attr('data') !=-1){
				$("#pluTypeId").val($("#pluTypeId").attr('data'));
			}
		
			//下拉框
			if(!ace.vars['touch']) {
				$('.chosen-select').chosen({allow_single_deselect:true}); 
				$(window)
				.off('resize.chosen')
				.on('resize.chosen', function() {
					$('.chosen-select').each(function() {
						 var $this = $(this);
						 $this.next().css({'width': $this.parent().width()});
					});
				}).trigger('resize.chosen');
				$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
					if(event_name != 'sidebar_collapsed') return;
					$('.chosen-select').each(function() {
						 var $this = $(this);
						 $this.next().css({'width': $this.parent().width()});
					});
				});
				$('#chosen-multiple-style .btn').on('click', function(e){
					var target = $(this).find('input[type=radio]');
					var which = parseInt(target.val());
					if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
					 else $('#form-field-select-4').removeClass('tag-input-style');
				});
			}
			
			
			//复选框全选控制
			var active_class = 'active';
			$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
				var th_checked = this.checked;//checkbox inside "TH" table header
				$(this).closest('table').find('tbody > tr').each(function(){
					var row = this;
					if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
					else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
				});
			});
		});
		
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>orderdetails/goAdd.do';
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.Modal = true;				//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 top.jzts();
						 setTimeout("self.location=self.location",100);
					 }else{
						 nextPage(${page.currentPage});
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//删除
		function del(Id){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>orderdetails/delete.do?ID="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
		}
		
		//修改
		function edit(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>orderdetails/goEdit.do?ID='+Id;
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.Modal = true;				//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮 
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//批量操作
		function makeAll(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
					  if(document.getElementsByName('ids')[i].checked){
					  	if(str=='') str += document.getElementsByName('ids')[i].value;
					  	else str += ',' + document.getElementsByName('ids')[i].value;
					  }
					}
					if(str==''){
						bootbox.dialog({
							message: "<span class='bigger-110'>您没有选择任何内容!</span>",
							buttons: 			
							{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
						});
						$("#zcheckbox").tips({
							side:1,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						return;
					}else{
						if(msg == '确定要删除选中的数据吗?'){
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>orderdetails/deleteAll.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									 $.each(data.list, function(i, list){
											nextPage(${page.currentPage});
									 });
								}
							});
						}
					}
				}
			});
		};
		
		//导出excel
		function toExcel(){
			var keywords= $("#nav-search-input").val();
			var lastStart= $("#lastStart").val();
			var lastEnd= $("#lastEnd").val();
			var storeId= $("#storeId").val();
			var orderStatusID= $("#orderStatusID").val();
			var payMentStatusID= $("#payMentStatusID").val();
			var pluClassId= $("#pluClassId").val();
			var pluTypeId= $("#pluTypeId").val();
			window.location.href='<%=basePath%>orderdetails/excel.do?keywords='+keywords+'&lastStart='+lastStart+'&lastEnd='+lastEnd+'&storeId='+storeId+'&orderStatusID='+orderStatusID+'&payMentStatusID='+payMentStatusID+'&pluClassId='+pluClassId+'&pluTypeId='+pluTypeId+'';
		}
	</script>


</body>
</html>