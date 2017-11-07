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
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<title>我的文章--发布文章</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/common.css" rel="stylesheet">
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<style type="text/css">
	.header{
		height: 48px;
    	line-height: 48px;
	}
	.headedImg{
		width: 48px;
	}
	textarea{
		border: 1px solid #e5e5e5;
		overflow-y:auto;
		max-width: 75%
	}
	.des{
		max-height: 50px;
		max-width: 75%
	}
	.cnt{
		height: 150px;
		max-height: 300px;
	}
	.bigdiv img {
        float: left;
	}
	.width6{
		width: 25%;
	    float: left;
	    display: inline-block;
	    box-sizing: border-box;
	    font-size: 14px;
	    height: 30px;
	    line-height: 30px;
	}
	.width10 {
	    width: 75%;
	    float: left;
	    display: inline-block;
	    box-sizing: border-box;
	    padding: 5px 0 5px 5px;
	    outline: none;
	    font-size: 14px;
	    border:1px solid #e5e5e5;
	    border-radius: 5px;
	}
	button{
	    margin-top: 35px;
	    border-radius: 3px;
	    font-size: 16px;
	    background: #08A600;
	    color: white;
	    padding: 10px 0;
	    outline: none;
	    letter-spacing: 1px;
	    outline: medium;
	    -webkit-appearance: none;
	    border: 1px solid #08A600;
	    margin-left: 2%;
	    width: 96%;
	    box-sizing: border-box;
	    margin-bottom: 30px;
	}
	button:active{
		background:#008000;
	}
	.addpic{
		width: 47%;
		margin-left: 3%;
	}
	.uploadDiv{
		overflow: auto;
		padding-left: 2%;
	}
	.uploadPic{
		width: 47%;
	    margin-left: 2%;
	   
	    margin-bottom: 5px;
	    display: inline-block;
	    float: left;
	    position: relative;
	}
	.uploadPic img{
		width: 100%;
		border-radius: 4px;
	}
	.coverImg{
		width: 64px;
	    height: 64px;
	    display: block;
	    position: absolute;
	    background: url(<%=basePath%>/static/weixin/images/cover.jpg) no-repeat center;
	    background-size:100%;
	    z-index: 1001;
	    top: 0px;
	    left: 0px;
	    overflow: hidden;
	}
	.textright{
		text-align: right;
		display: inline-block;
		width: 100%;
		font-size: 12px;
		color: #999;
		margin-top: 5px;
	}
	.textright i{
		color:#08A600
	}
	.colorred{
		color: red;
	}
	.bordered{
		border-color: red;
	}
	.bigdiv a {
    	padding-right: 0px;
	}
</style>
</head>
<body class="padbtm70">
	<div class="detailsTitle">发表文章</div>
	<!-- 标题 -->
    <div class="bigdiv" >
		<label class="width6 colorgrey"><span class="colorred">*</span>标题：</label>
		<input maxlength="15" class="width10" name="title" value="" placeholder="请输入标题">
	</div>
	<!-- 描述 -->
	<div class="bigdiv" >
		<label class="width6 colorgrey"><span class="colorred">*</span>描述：</label>
		<textarea class="width10 des" name="des" placeholder="请输入描述"></textarea> 
	</div>
	<!-- 正文 -->
	<div class="bigdiv" >
		<label class="width6 colorgrey"><span class="colorred">*</span>正文：</label>
		<textarea id="cnt" name="cnt" class="width10 cnt" placeholder="软文内容不能超过5000字"></textarea>
		<span class="textright">您还可以输入<i id="cnts">5000</i> 字</span>
	</div>
	<!-- 上传图片 -->
	<div class="bigdiv" >
		<label class="width6 colorgrey"><span class="colorred">*</span>上传图片：</label>
		<div class="uploadDiv">
			<div class="uploadPic" id="uploadPic0" style="display: none;">
				<img class="uploadImg0" src="<%=basePath%>/static/weixin/images/photo.jpg">
				<span class="coverImg"></span>
			</div>
			<div class="uploadPic" id="uploadPic1" style="display: none;">
				<img class="uploadImg1" src="<%=basePath%>/static/weixin/images/photo.jpg">
			</div>
			<div class="uploadPic" id="uploadPic2" style="display: none;">
				<img class="uploadImg2" src="<%=basePath%>/static/weixin/images/photo.jpg">
			</div>
			<div class="uploadPic" id="uploadPic3" style="display: none;">
				<img class="uploadImg3" src="<%=basePath%>/static/weixin/images/photo.jpg">
			</div>
			<a id="chooseImage">
				<img class="addpic" src="<%=basePath%>/static/weixin/images/addpic.png"></img>
			</a>
		</div>
		<span class="textright">首图作为封面图,最多上传<i>4</i> 张图片</span>
	</div>
	<!-- 备注 -->
	<div class="bigdiv" >
		<label class="width6 colorgrey">备注：</label>
		<textarea class="width10 des" name="articleRemark" placeholder="请输入描述" ></textarea> 
	</div>
	<div class="bigdiv" id="showMsg" style="text-align: center;color: #f60;display: none;"></div>
	<button class="button">发表</button>
	<input type="hidden" id="hideappId" value="${pd.appId}">
	<input type="hidden" id="hidetimeStamp" value="${pd.timestamp}">
	<input type="hidden" id="hidenonceStr" value="${pd.nonceStr}">
	<input type="hidden" id="hidesignature" value="${pd.signature}">
	<input type="hidden" id="imgList">
	<input type="hidden" id="thumbmediaid">
</body>
<script type="text/javascript"
	src="<%=basePath%>/static/weixin/js/jquery-1.7.2.js"></script>
	<script type="text/javascript"
	src="<%=basePath%>/static/weixin/js/common.js"></script>
<script type="text/javascript">
/**
 * 上传图片接口
 */
 var syncUpload = function(localIds){
	  var localId = localIds.pop();
	  wx.uploadImage({
	    localId: localId,
	    isShowProgressTips: 1,
	    success: function (res) {
	      var serverId = res.serverId; // 返回图片的服务器端ID
	      var nowthumbmediaid = delHtmlTag($("#thumbmediaid").val());
	    	if(isNull(nowthumbmediaid)){
	    		$("#thumbmediaid").val(serverId);
	    	}else{
	    		$("#thumbmediaid").val(nowthumbmediaid+","+serverId);
	    	}
	      //其他对serverId做处理的代码
	      if(localIds.length > 0){
	        syncUpload(localIds);
	      }
	    }
	  });
	};


/**
 * 下载图片接口
 */
function downloadImage(serverId,i){
	wx.downloadImage({
	    serverId: serverId, // 需要下载的图片的服务器端ID，由uploadImage接口获得
	    isShowProgressTips: 1, // 默认为1，显示进度提示
	    success: function (res) {
	    	var localId = res.localId; // 返回图片下载后的本地ID
	    	
	    }
	});
	
}

$(function(){
	var appId = $("#hideappId").val();
	var timestamp = $("#hidetimeStamp").val();
	var nonce_str = $("#hidenonceStr").val();
	var signature = $("#hidesignature").val();
	wx.config({
		debug : false,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		appId : appId,// 必填，公众号的唯一标识
		timestamp : timestamp,// 必填，生成签名的时间戳
		nonceStr : nonce_str,// 必填，生成签名的随机串
		signature : signature,// 必填，签名，
		jsApiList : [ 'checkJsApi', 'onMenuShareTimeline',
				'onMenuShareAppMessage', 'onMenuShareQQ',
				'onMenuShareWeibo', 'hideMenuItems', 'showMenuItems',
				'hideAllNonBaseMenuItem', 'showAllNonBaseMenuItem',
				'translateVoice', 'startRecord', 'stopRecord',
				'onRecordEnd', 'playVoice', 'pauseVoice', 'stopVoice',
				'uploadVoice', 'downloadVoice', 'chooseImage',
				'previewImage', 'uploadImage', 'downloadImage',
				'getNetworkType', 'openLocation', 'getLocation',
				'hideOptionMenu', 'showOptionMenu', 'closeWindow',
				'scanQRCode', 'chooseWXPay', 'openProductSpecificView',
				'addCard', 'chooseCard', 'openCard' ]
	});
	
	$("#chooseImage").click(function(){
		wx.chooseImage({
		    count: 4, // 默认9
		    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
		    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
		    success: function (res) {
		        var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
		        $("#imgList").val(localIds);
		        for(var i=0;i<localIds.length;i++){
		        	if(i==3){
		        		 $("#chooseImage").hide();
		        	 }
		        	 $("#uploadPic"+i).show();
		        	 $(".uploadImg"+i).attr('src',localIds[i]);
		        }
		        syncUpload(localIds); 
	           
		    }
		});
	});
	
	/*正文字数限制*/
	$(document).on('propertychange input','.cnt',function(){
		var maxlength=5000;
		var length=$(this).val().length;
		if(length > maxlength){
        	$(this).val($(this).val().substring(0,maxlength));
    	}else{
    		$("#cnts").text(maxlength-length);
    	}
	})
	
	/*提交事件*/
	$(".button").click(function(){
		 $(".button").attr("disabled","disabled");  
		 $(".button").text("发表中...");  
		var title=$("input[name='title']").val();
		if($.trim(title)==""){
			$("input[name='title']").focus().addClass('bordered');
			$(".button").removeAttr("disabled");
			$(".button").text("发表");  
			return false;
		}
		var des=$("textarea[name='des']").val();
		if($.trim(des)==""){
			$("textarea[name='des']").focus().addClass('bordered');
			$(".button").removeAttr("disabled");
			$(".button").text("发表");  
			return false;
		}
		var cnt=$("textarea[name='cnt']").val();
		if($.trim(cnt)==""){
			$("textarea[name='cnt']").focus().addClass('bordered');
			$(".button").removeAttr("disabled");
			$(".button").text("发表");  
			return false;
		}
		var imglist =$("#thumbmediaid").val().split(',');
		if(isNull(imglist)){
			$("#showMsg").show();
			$("#showMsg").html('请上传图片');
			$(".button").removeAttr("disabled");
			$(".button").text("发表");  
			return false;
		}
		if(imglist.length>4){
			$("#showMsg").show();
			$("#showMsg").html('最多上传4张图片');
			$(".button").removeAttr("disabled");
			$(".button").text("发表");  
			return false;
		}
		var articleRemark=$("textarea[name='articleRemark']").val();
		$("#showMsg").hide();
		var param={};
		param["TITLE"] = title;
		param["DIGEST"] = des;
		param["CONTENT"] = cnt;
		param["ARTICLEREMARK"] = articleRemark;
		param["imglist"] = $("#thumbmediaid").val();
		$.post("<%=basePath%>myArticle/save.do",param,function(data){
				if(data=='' || typeof(data) == "undefined"){
					$("#showMsg").show();
        			$("#showMsg").html("网络繁忙，请稍后再试！");
        			$(".button").removeAttr("disabled");
        			$(".button").text("发表");  
					return false;
				}
                if(data.type==1){
                	window.location.href="<%=basePath%>myArticle/articleAddFinish.do?id="+data.data;
                }else{
                	$("#showMsg").show();
        			$("#showMsg").html(data.msg);
        			$(".button").removeAttr("disabled");
        			$(".button").text("发表");  
                }

		});
		
	})
	$(document).on('propertychange input','input,textarea',function(){
		$(this).removeClass('bordered');
	})
	
	
	
})




</script>
</html>