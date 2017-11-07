﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
						<form action="member/list.do" method="post" name="Form" id="Form">
						<!-- 区别作者和用户的memberType -->
						<input type="hidden" name="memberType" value="${pd.memberType }"/>
						<table style="margin-top:5px;">
							<tr>
								<td style="vertical-align:top;padding-left:2px;">
								 	<select class="chosen-select form-control" name="keyType" id="id" data-placeholder="请选择" style="vertical-align:top;width: 120px;">
										<option value=""></option>
										<option <c:if test="${pd.keyType==1 }">selected</c:if> value="1">用户id</option>
										<option <c:if test="${pd.keyType==2 }">selected</c:if> value="2">openId</option>
										<option <c:if test="${pd.keyType==3 }">selected</c:if> value="3">微信昵称</option>
										<option <c:if test="${pd.keyType==4 }">selected</c:if> value="4">作者</option>
										<option <c:if test="${pd.keyType==5 }">selected</c:if> value="5">用户</option>
								  	</select>
								</td>		
								<td>
									<div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入关键词" class="nav-search-input" id="nav-search-input" autocomplete="off" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
								</td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="SubTime" id="SubTime"  value="" type="text" data-date-format="yyyy-mm-dd "  readonly="readonly" style="width:88px;" placeholder="关注开始时间" title="关注开始时间"/></td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="subEndTime" id="subEndTime"  value="" type="text" data-date-format="yyyy-mm-dd " readonly="readonly" style="width:88px;" placeholder="关注结束时间" title="关注结束时间"/></td>
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
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">用户类型</th>
									<th class="center">OPENID</th>
									<th class="center">微信昵称</th>
									<th class="center">性别</th>
									<th class="center">省份</th>
									<th class="center">头像</th>
									<th class="center">关注时间</th>
									<th class="center">上次登录ip</th>
									<th class="center">最近登录时间</th>
									<th class="center">历史收益</th>
									<th class="center">可结算金额</th>
									<th class="center">抽奖次数</th>
									<!-- 作者的字段start -->
									<c:if test="${pd.memberType == 1}">
										<th class="center">作者浏览量收益比例(%)</th>
										<th class="center">作者点赞收益比例(%)</th>
										<th class="center">作者获取公众号关注收益比例(%)</th>
										<th class="center">作者预订消费成功收益比例(%)</th>
									</c:if>
									<!-- 作者的字段end -->
									<!-- 普通用户的字段start -->
									<c:if test="${pd.memberType == 2}">
										<th class="center">用户分享软文预订消费成功收益比例(%)</th>
										<th class="center">用户分享软文获得公众号关注收益比例(%)</th>
										<th class="center">用户分享软文浏览收益比例(%)</th>
									</c:if>
									<!-- 普通用户的字段end -->
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
											<td class='center'>
												<label class="pos-rel"><input type='checkbox' name='ids' value="${var.MEMBER_ID}" class="ace" /><span class="lbl"></span></label>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class='center'>
												<c:if test="${var.MEMBERTYPE == 1}">作者</c:if>
												<c:if test="${var.MEMBERTYPE == 2}">用户</c:if>
											</td>
											<td class='center'>${var.OPENID}</td>
											<td class='center'>${var.WECHATNAME}</td>
											<td class='center'>
												<c:if test="${var.SEX==1}">男</c:if>
												<c:if test="${var.SEX==2}">女</c:if>
											</td>
											<td class='center'>${var.PROVINCE}</td>
											<td class='center'>
												<img alt="" src="${var.HEADIMGURL}" height="30px" width="40px">
											</td>
											<td class='center'><fmt:formatDate value="${var.SUBSCRIBETIME}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td class='center'>${var.LASTIP}</td>
											<td class='center'><fmt:formatDate value="${var.LASTDATE}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td class='center'>${var.REVENUEMONEY}</td>
											<td class='center'>${var.BALANCEMONEY}</td>
											<td class='center'>${var.LOTTERYNUM}</td>
											<!-- 作者的字段start -->
											<c:if test="${pd.memberType == 1}">
												<td class='center'>${var.REVENUEPV}</td>
												<td class='center'>${var.REVENUELIKE}</td>
												<td class='center'>${var.REVENUESUB}</td>
												<td class='center'>${var.REVENUECON}</td>
											</c:if>
											<!-- 作者字段end -->
											<c:if test="${pd.memberType == 2}">
											<!-- 普通用户的字段start -->
												<td class='center'>${var.REVENUECONM}</td>
												<td class='center'>${var.REVENUESUBM}</td>
												<td class='center'>${var.REVENUEPVM}</td>
											<!-- 普通用户的字段end -->
											</c:if>
											<td class="center">
											<c:if test="${pd.memberType == 2}">
											<a class="btn btn-xs btn-success" title="查看预订记录"  href="reservation/list.do?memberId=${var.MEMBERID}">
											查看预订记录
											</a>
											</c:if>
												<c:if test="${QX.edit != 1 && QX.del != 1 }">
												<span class="label label-large label-grey arrowed-in-right arrowed-in"><i class="ace-icon fa fa-lock" title="无权限"></i></span>
												</c:if>
												<div class="hidden-sm hidden-xs btn-group">
													<c:if test="${QX.edit == 1 }">
													<a class="btn btn-xs btn-success" title="编辑" onclick="edit('${var.MEMBERID}');">
														<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
													</a>
													<a class="btn btn-xs btn-success" title="设定抽奖次数" onclick="goEditLotterNum('${var.OPENID}',${var.LOTTERYNUM});">
													设定抽奖次数
													</a>
													<c:if test="${var.MEMBERTYPE == 1}">
														<a class="btn btn-xs btn-success" title="修改密码" onclick="modifyPassWord('${var.MEMBERID}');">
														修改密码
														</a>
													</c:if>
													</c:if>
													<%-- <c:if test="${QX.del == 1 }">
													<a class="btn btn-xs btn-danger" onclick="del('${var.MEMBER_ID}');">
														<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
													</a>
													</c:if>--%>
												</div>
												<div class="hidden-md hidden-lg">
													<div class="inline pos-rel">
														<button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
															<i class="ace-icon fa fa-cog icon-only bigger-110"></i>
														</button>
			
														<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
															<c:if test="${QX.edit == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="edit('${var.memberId}');" class="tooltip-success" data-rel="tooltip" title="修改">
																	<span class="green">
																		<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
															<c:if test="${QX.del == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="del('${var.memberId}');" class="tooltip-error" data-rel="tooltip" title="删除">
																	<span class="red">
																		<i class="ace-icon fa fa-trash-o bigger-120"></i>
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
								<td style="vertical-align:top;">
									<%-- <c:if test="${QX.add == 1 }">
									<a class="btn btn-mini btn-success" onclick="add();">新增</a>
									</c:if>
									<c:if test="${QX.del == 1 }">
									<a class="btn btn-mini btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" title="批量删除" ><i class='ace-icon fa fa-trash-o bigger-120'></i></a>
									</c:if>--%>
								</td>
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
			 diag.URL = '<%=basePath%>member/goAdd.do';
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
		
		//修改用户的密码
		function modifyPassWord(memberId){
			top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="修改密码";
			 diag.URL = '<%=basePath%>member/goModifyPass.do?memberId='+memberId;
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.Modal = true;				//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//修改抽奖次数
		function goEditLotterNum(Id,num){
			 	top.jzts();
			 	num=num+1;
			 	if(num>1){
			 		nextPage(${page.currentPage});
			 		return;
			 	}
				var url = "<%=basePath%>member/editLotterNum.do?LOTTERYNUM="+num+"&OPENID="+Id+"&tm="+new Date().getTime();
				$.get(url,function(data){
					if(data.type==1){
						nextPage(${page.currentPage});
					}
				});
		}
		
		//删除
		function del(Id){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>member/delete.do?MEMBER_ID="+Id+"&tm="+new Date().getTime();
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
			 diag.URL = '<%=basePath%>member/goEdit.do?memberId='+Id;
			 diag.Width = 800;
			 diag.Height = 600;
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
								url: '<%=basePath%>member/deleteAll.do?tm='+new Date().getTime(),
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
			window.location.href='<%=basePath%>member/excel.do';
		}
	</script>


</body>
</html>