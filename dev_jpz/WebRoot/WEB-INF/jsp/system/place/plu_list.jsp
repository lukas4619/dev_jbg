<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<%@ include file="../index/top.jsp"%>
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
						<div id="zhongxin" style="padding-top: 13px;">
						<!-- 检索  -->
						<form action="place/pluList.do" method="post" name="pluForm" id="pluForm">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入关键词" class="nav-search-input" id="nav-search-input" autocomplete="off" name="keywords" value="${pd.keywords }" />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
									<input type="hidden" id="storeId" name="storeId" value="${pd.storeId}">
									<input type="hidden" id="shelvesId" name="shelvesId" value="${pd.shelvesId}">
									<input type="hidden" id="placeId" name="placeId" value="${pd.placeId}">
									<input type="hidden" id="USERNAME" name="USERNAME" >
								</td>
								<td>
									<c:choose>
														<c:when test="${pd.pluCode=='-1'}">
															<input type="number" min="0" placeholder="这里输入商品编码" id="pluCode"  name="pluCode" />
														</c:when>
														<c:otherwise>
													<input type="number" min="0" placeholder="这里输入商品编码" id="pluCode"  name="pluCode" value="${pd.pluCode }"/>
														</c:otherwise>
													</c:choose>
								</td>
								<td style="padding-left:2px;display: none;" ><input class="span10 date-picker" name="lastStart" id="lastStart"  value="" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="开始日期" title="开始日期"/></td>
								<td style="padding-left:2px;display: none;"><input class="span10 date-picker" name="lastEnd" name="lastEnd"  value="" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="结束日期" title="结束日期"/></td>
								<td style="vertical-align:top;padding-left:2px;"><select
											class="chosen-select form-control" data="${pd.pluClassId }"
											name="pluClassId" id="pluClassId" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
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
								<td style="vertical-align:top;padding-left:2px;"><select
											class="chosen-select form-control" data="${pd.pluTypeId }"
											name="pluTypeId" id="pluTypeId" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
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
								<td style="vertical-align:top;padding-left:2px;"><select
											class="chosen-select form-control" data="${pd.pluStatus }"
											name="pluStatus" id="pluStatus" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
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
								<c:if test="${QX.cha == 1 }">
								<td style="vertical-align:top;padding-left:2px"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								</c:if>
							</tr>
						</table>
						<!-- 检索  -->
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center">商品编码</th>
									<th class="center">商品名称</th>
									<th class="center">商品条形码</th>
									<th class="center">单位</th>
									<th class="center">规格</th>
									<th class="center">售价</th>
									<th class="center">促销价</th>
									<th class="center">商品分类</th>
									<th class="center">商品类型</th>
									<th class="center">商品状态</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty varList}">
									<c:if test="${QX.cha == 1 }">
									<c:forEach items="${varList}" var="var" varStatus="vs">
										<tr>
											<td class='center'>
											<label class="pos-rel"><input type='checkbox' name='ids' value="${var.pluCode}" class="ace" /><span class="lbl"></span></label>
											</td>
											<td class='center'>${var.pluCode}</td>
											<td class='center'>${var.pluName}</td>
											<td class='center'>${var.barCode}</td>
											<td class='center'>${var.units}</td>
											<td class='center'>${var.spec}</td>
											<td class='center'>${var.price}</td>
											<td class='center'>${var.pPrice}</td>
											<td class='center'>${var.pluClassName}</td>
											<td class='center'>${var.pluStatusName}</td>
											<td class='center'>${var.pluTypeName}</td>
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
						<table style="width:100%;">
							<tr>
								<td style="vertical-align:top;">
									<a class="btn btn-mini btn-primary" onclick="makeAll('确定要选择选中的商品吗?');">绑定</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
								<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
							</tr>
						</table>
						</form>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->
		<input type="hidden" name="USERNAME" id="USERNAME" />
		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	</body>

<script type="text/javascript">
$(top.hangge());

//检索
function searchs(){
	top.jzts();
	$("#pluForm").submit();
}

$(function() {
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
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
				if(msg == '确定要选择选中的商品吗?'){
					$("#USERNAME").val(str);
					$("#zhongxin").hide();
					$("#zhongxin2").show();
					top.Dialog.close();
				}
			}
		}
	});
};
		
</script>
</html>
