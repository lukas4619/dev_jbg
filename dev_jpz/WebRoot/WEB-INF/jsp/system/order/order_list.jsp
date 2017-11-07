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
							
						<!-- 检索  -->
						<form action="order/list.do" method="post" name="Form" id="Form">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入关键词" class="nav-search-input" id="nav-search-input" autocomplete="off" name="keywords" id="keywords" value="${pd.keywords }" placeholder="这里输入关键词"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
								</td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="lastStart" id="lastStart"  value="" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="开始日期" title="开始日期"/></td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="lastEnd"  id="lastEnd" value="" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="结束日期" title="结束日期"/></td>
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
														<c:when test="${not empty orderStatusList}">
															<c:forEach items="${orderStatusList}" var="c" varStatus="vst">
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
														<c:when test="${not empty payMentStatusList}">
															<c:forEach items="${payMentStatusList}" var="t" varStatus="vst">
																<option value="${t.ID }">${t.NAME }</option>
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
									<th class="center">订单状态</th>
									<th class="center">付款状态</th>
									<th class="center">商品数量</th>
									<th class="center">订单金额</th>
									<th class="center">付款时间</th>
									<th class="center">订单提交时间</th>
									<th class="center">所属门店</th>
									<th class="center">下单人</th>
									<th class="center">联系电话</th>
									<th class="center">备注</th>
									<th class="center">操作</th>
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
											<td class='center'>${var.STATUSNAME}</td>
											<td class='center'>${var.PAYSTATUSNAME}</td>
											<td class='center'>${var.PLUCOUNT}</td>
											<td class='center'>${var.TOTALPRICE}</td>
											<td class='center'>
											<fmt:formatDate value="${var.PAYMENTDATE}" pattern="yyyy-MM-dd HH:mm:ss"  />
											</td>
											<td class='center'>
											<fmt:formatDate value="${var.CREATEDATE}" pattern="yyyy-MM-dd HH:mm:ss"  />
											</td>
											<td class='center'>${var.STORENAME}</td>
											<td class='center'>${var.WECHATNAME}</td>
											<td class='center'>${var.PHONENUMBER}</td>
											<td class='center'>${var.REMARKS}</td>
											<td class="center">
												<c:if test="${QX.edit != 1 && QX.del != 1 }">
												<span class="label label-large label-grey arrowed-in-right arrowed-in"><i class="ace-icon fa fa-lock" title="无权限"></i></span>
												</c:if>
												<div class="hidden-sm hidden-xs btn-group">
													<c:if test="${QX.edit == 1 }">
													<a class="btn btn-xs btn-info" title="查看订单详情" onclick="edit('${var.BILLNO}');">
														<i class="ace-icon glyphicon glyphicon-user" title="查看订单详情"></i>
													</a>
													</c:if>
												</div>
												<div class="hidden-md hidden-lg">
													<div class="inline pos-rel">
														<button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
															<i class="ace-icon fa fa-cog icon-only bigger-110"></i>
														</button>
			
														<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
															<c:if test="${QX.edit == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="edit('${var.BILLNO}');" class="tooltip-success" data-rel="tooltip" title="查看订单详情">
																	<span class="green">
																		<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
														</ul>
													</div>
												</div>
											</td>
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
		
	
		
		
		//修改
		function edit(Id){
		window.location.href='<%=basePath%>orderdetails/list.do?keywords='+Id;
			  /*
			 top.jzts();
			 window.location.href='<%=basePath%>order/excel.do';
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>order/goEdit.do?ID='+Id;
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
			 */
		}
		
		
		
		
		//导出excel
		function toExcel(){
			var keywords= $("#nav-search-input").val();
			var lastStart= $("#lastStart").val();
			var lastEnd= $("#lastEnd").val();
			var storeId= $("#storeId").val();
			var orderStatusID= $("#orderStatusID").val();
			var payMentStatusID= $("#payMentStatusID").val();
			window.location.href='<%=basePath%>order/excel.do?keywords='+keywords+'&lastStart='+lastStart+'&lastEnd='+lastEnd+'&storeId='+storeId+'&orderStatusID='+orderStatusID+'&payMentStatusID='+payMentStatusID+'';
		}
	</script>


</body>
</html>