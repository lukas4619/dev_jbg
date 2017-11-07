<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
							<!-- 检索  -->
								<table style="margin-top:5px;">
									<tr>
										<td>
											<div class="nav-search">
												<span class="input-icon"> <input type="text"
													placeholder="这里输入关键词" class="nav-search-input"
													id="nav-search-input" autocomplete="off" name="keywords"
													value="${pd.keywords }" placeholder="这里输入关键词" /> <i
													class="ace-icon fa fa-search nav-search-icon"></i> </span>
											</div></td>
										<td style="padding-left:2px;"><input
											class="span10 date-picker" name="lastStart" id="lastStart"
											value="${pd.lastStart }" type="text"
											data-date-format="yyyy-mm-dd" readonly="readonly"
											style="width:88px;" placeholder="开始日期" title="开始日期" />
										</td>
										<td style="padding-left:2px;"><input
											class="span10 date-picker" name="lastEnd" name="lastEnd"
											value="${pd.lastEnd }" type="text"
											data-date-format="yyyy-mm-dd" readonly="readonly"
											style="width:88px;" placeholder="结束日期" title="结束日期" />
										</td>
										<td style="vertical-align:top;padding-left:2px;"><select
											class="chosen-select form-control" data="${pd.proType }"
											name="proType" id="proType" data-placeholder="请选择"
											style="vertical-align:top;width: 120px;">
												<option value="-1">产品类型</option>
												<!-- 开始循环 -->
													<c:choose>
														<c:when test="${not empty proTypeList}">
															<c:forEach items="${proTypeList}" var="t" varStatus="vst">
																<option value="${t.ID }">${t.TYPENAME }</option>
															</c:forEach>
														</c:when>
													</c:choose>
										</select></td>
										<c:if test="${QX.cha == 1 }">
											<td style="vertical-align:top;padding-left:2px"><a
												class="btn btn-light btn-xs" onclick="openPro();"
												title="检索"><i id="nav-search-icon"
													class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i>
											</a>
											</td>
										</c:if>
									</tr>
								</table>
								<!-- 检索  -->

								<table id="simple-table"
									class="table table-striped table-bordered table-hover"
									style="margin-top:5px;">
									<thead>
										<tr>
											<th class="center" style="width:50px;"><label
												class="pos-rel">序号
											</label></th>
											<th class="center">产品类型</th>
											<th class="center">产品名称</th>
											<th class="center">产品金额</th>
											<th class="center">产品预付金额</th>
											<th class="center">产品有效期</th>
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
															<td class='center' style="width: 30px;vertical-align: middle;">${vs.index+1}</td>
															<td class='center' style="vertical-align: middle;">${var.typeName}</td>
															<td class='center' style="vertical-align: middle;">${var.proName}</td>
															<td class='center' style="vertical-align: middle;">${var.proMoney}</td>
															<td class='center' style="vertical-align: middle;">${var.proAdvanceMoney}</td>
															<td class='center' style="vertical-align: middle;">${var.proValidity} 天</td>
															<td class="center" style="vertical-align: middle;">
															<c:if test="${QX.edit == 1}">
															<a onclick="setProductId('${var.productId}','${var.proName}');" class="btn btn-warning btn-mini" onclick="save();">设定</a>
															</c:if>
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
													<td colspan="100" class="center">没有相关数据</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>

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
		<a href="#" id="btn-scroll-up"
			class="btn-scroll-up btn btn-sm btn-inverse"> <i
			class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i> </a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<script type="text/javascript">
		$(function() {
			//日期框
			$('.date-picker').datepicker({
				autoclose: true,
				todayHighlight: true
			});
			if($("#proType").attr('data')!='' && $("#proType").attr('data') !=-1){
				$("#proType").val($("#proType").attr('data'));
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
			
		});
		
		
		
		
	</script>
