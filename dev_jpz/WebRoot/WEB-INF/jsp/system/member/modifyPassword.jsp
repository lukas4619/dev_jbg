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
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">

<!-- jsp文件头和头部 -->
<%@ include file="../index/top.jsp"%>
<style type="text/css">
.lh30{
	line-height:30px;
	padding-left:25px;
}
.pwd{
	width:100%
}
.textcenter{
	text-align:center;
}
</style>
</head>
<body class="no-skin">

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
				

					<div class="row">
						<div class="col-xs-12" style="padding-top: 30px">
						
						<form  action="member/modifyPass.do" name="menuForm" id="menuForm" method="post" class="form-horizontal">
							<input type="hidden" name="memberId" value="${pd.memberId }">
							<div class="form-group">
							</div>
							<div class="form-group">
								<label class="col-sm-3 col-xs-3 control-label no-padding-right lh30" for="form-field-1"> 密码 :</label>
								<span class="col-sm-9 col-xs-9">
									<input type="password" name="passWord" id="passWord" value="${pd.MENU_NAME }" placeholder="这里输入新的密码" class="col-xs-10 col-sm-5 pwd" />
								</span>
							</div>
							
							<div class="form-group">
								<label class="col-sm-3 col-xs-3 control-label no-padding-right lh30" for="form-field-1"> 确认密码 :</label>
								<span class="col-sm-9 col-xs-9">
									<input type="password" name="repassWord" id="repassWord" value="${pd.MENU_NAME }" placeholder="这里重复输入密码" class="col-xs-10 col-sm-5 pwd" />
								</span>
							</div>
							
							<div class="clearfix form-actions textcenter" style="margin-top: 50px">
								<div class="col-md-offset-3 col-md-9">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</div>
							</div>
						</form>
						<div id="zhongxin" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
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
			class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../index/foot.jsp"%>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<!-- inline scripts related to this page -->
	<script type="text/javascript">
		$(top.hangge());
		
		//返回
		function goback(){
			top.jzts();
			window.location.href="<%=basePath%>member/list.do";
		}
		
		//保存
		function save(){
			if($("#passWord").val()==""){
				$("#passWord").tips({
					side:3,
		            msg:'请输入密码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#passWord").focus();
				return false;
			}
			
			if($("#repassWord").val()==""){
				$("#repassWord").tips({
					side:3,
		            msg:'请重复输入密码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#repassWord").focus();
				return false;
			}
			
			if($("#repassWord").val()!=$("#passWord").val()){
				alert("2次密码不一样,请重新输入");
				return false;
			}
			
			$("#menuForm").submit();
		}
		
		//设置菜单类型or状态
		function setType(type,value){
			if(type == '1'){
				$("#MENU_TYPE").val(value);
			}else{
				$("#MENU_STATE").val(value);
			}
		}
	</script>


</body>
</html>