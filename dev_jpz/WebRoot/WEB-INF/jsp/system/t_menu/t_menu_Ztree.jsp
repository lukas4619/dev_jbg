<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<meta charset="utf-8" />
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />
	<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
	<link type="text/css" rel="stylesheet" href="plugins/zTree/3.5/zTreeStyle.css"/>
	<script type="text/javascript" src="plugins/zTree/3.5/jquery.ztree.core-3.5.js"></script>
<body>
	
<table style="width:100%;" border="0">
	<tr style="vertical-align:top;">
		<a class="btn btn-mini btn-success" onclick="addParent();">新增</a>
		<a class="btn btn-mini btn-success" onclick="deleteParent();" style="margin-left: 10px;">删除</a>
		<a class="btn btn-mini btn-success" onclick="modefyParent();" style="margin-left: 10px;">修改</a>
		<a class="btn btn-mini btn-success" onclick="setMenu();" style="margin-left: 10px;">设置菜单</a>
		<a class="btn btn-mini btn-success" onclick="clearMenu();" style="margin-left: 10px;">清除菜单</a>
		<span style="color: red">此处为一级菜单的操作</span>
	</tr>
	<tr>
		<td style="width:15%;" valign="top" bgcolor="#F9F9F9">
			
			<div style="width:15%;">
				<ul id="leftTree" class="ztree"></ul>
			</div>
		</td>
		<td style="width:85%;" valign="top" >
			<iframe name="treeFrame" id="treeFrame" frameborder="0" src="<%=basePath%>t_menu/list.do?pid=${pd.pid}" style="margin:0 auto;width:100%;height:100%;"></iframe>
		</td>
	</tr>
</table>
<script type="text/javascript">
		$(top.hangge());
		var zTree;
		var setting = {
			data: {
				simpleData: {
					enable: true
				}
			},callback: {
				onClick: onClick
			}
		};
		
		$(document).ready(function(){
			initTree();
		});
		
		function initTree(){
			var zn = '${zTreeNodes}';
			var zTreeNodes = eval(zn);
			$.fn.zTree.init($("#leftTree"), setting, zTreeNodes);
			//id为pid的节点默认选中;
			var zTree = $.fn.zTree.getZTreeObj("leftTree");
			var pid = ${pd.pid};
			zTree.selectNode(zTree.getNodeByParam("id", pid));
		}
		
		function onClick(event, treeId, treeNode, clickFlag) {
			var  id = treeNode.id;
			var pid = treeNode.tpid;
			if(pid==0){
				document.getElementById("treeFrame").src="<%=basePath%>t_menu/list.do?pid="+id; 
			}
		}	
		
		function addParent(){
			 //判断一级菜单的个数
			 var isMaxSum=false;
			 $.ajax({
			 	url:"<%=basePath%>t_menu/getMenuSumByParentId.do?PID=0&tm="+new Date().getTime(),
			 	type:'post',
			 	async:false,
			 	dataType:'json',
			 	success:function(r){
			 		if(r.sum>=3){
			 			isMaxSum=true;
			 		}
			 	}
			 });
			 if(isMaxSum){
			 	alert("一级菜单最多只能添加3个");
			 	return false;
			 }
			 
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>t_menu/goAdd.do?PID=0';
			 diag.Width = 600;
			 diag.Height = 500;
			 diag.Modal = true;				//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 location.href="t_menu/tmenu_ztreeMenu.do";
				}
				diag.close();
			 };
			 diag.show();
		}
		
		
		var id = '';
		function  checkIsParent(){
			var treeObj = $.fn.zTree.getZTreeObj("leftTree");
			var nodes = treeObj.getSelectedNodes();
			if(nodes==undefined){
				alert("请选中要删除的一级菜单");
				return false;
			}
			id = nodes[0].id;
			var pid = nodes[0].tpid;
			if(pid!=0){   
				//alert("此按钮只能操作一级菜单");
				return false;
			}
			return true;
		}
		
		//删除一级菜单
		function deleteParent(){
			var is = checkIsParent();
			if(!is){
				alert("此按钮只能操作一级菜单");
				return false;
			}
			var result = confirm("将删除此菜单下的所有菜单,确定要删除吗?");
			if(result==true){
				var url = "<%=basePath%>t_menu/deleteParentAndAllChild.do?ID="+id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						//nextPage(${page.currentPage});
						window.parent.location.href="<%=basePath%>t_menu/tmenu_ztreeMenu.do?pid=${pd.pid }";
					});
			}
		}
		
		//修改一级菜单
		function modefyParent(){
			 var is = checkIsParent();
			 if(!is) return false;
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>t_menu/goEdit.do?ID='+id;
			 diag.Width = 600;
			 diag.Height = 500;
			 diag.Modal = true;				//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮 
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 //nextPage(${page.currentPage});
					 location.href="<%=basePath%>t_menu/tmenu_ztreeMenu.do";
				}
				diag.close();
			 };
			 diag.show();
		
		}
		//设置微信菜单
		function setMenu(){
			$.ajax({
				url:'<%=basePath%>t_menu/setMenu.do',
				type:'post',
				dataType:'json',
				success:function(r){
					if(r.code==1){
						alert("设置成功");
					}else if(r.code==2){
						alert("查询菜单树出错");
					}else{
						alert("菜单url错误,或者接口参数错误");
					}
				}
			});
		}
		
		//清除菜单
		function clearMenu(){
			var url = "<%=basePath%>t_menu/clearMenu.do";
			$.post(url,function(data){
				if(data.code == 1){
					alert("删除成功");
				}else{
					alert("删除失败");
				}
			});
		}
		
		function treeFrameT(){
			var hmainT = document.getElementById("treeFrame");
			var bheightT = document.documentElement.clientHeight;
			hmainT .style.width = '100%';
			hmainT .style.height = (bheightT-26) + 'px';
		}
		treeFrameT();
		window.onresize=function(){  
			treeFrameT();
		};
</SCRIPT>
</body>
</html>
