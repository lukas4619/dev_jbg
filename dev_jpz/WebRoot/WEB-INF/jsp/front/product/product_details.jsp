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
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<title></title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="../static/weixin/css/productIndex.css" rel="stylesheet">
<link href="../static/weixin/css/LCalendar.css" rel="stylesheet">
</head>
<body>
    <div class="content">
    	<!-- 头部标题 -->
    	<div class="header">
    		<input type="hidden" value="${pd.PRODUCTID }" name="PRODUCTID" id="PRODUCTID">
    		<input type="hidden" value="${openid }" name="shareId">
    		<input type="hidden" value="${pd.PROIMG }" name="PROIMG" id="PROIMG">
    		<h3>${pd.PROTITLE }</h3>
    		<div class="wxdate"><span><fmt:formatDate value="${pd.CREATEDATE }" type="date"/>  </span>	<span>${pd.PRONAME }</span></div>
    	</div>
    	<!-- 内容 -->
    	<div class="acontent">
			${pd.PROCONTENT }
    	</div>
    	 <!-- 关注二维码 -->
        <div class="follow">
            <div class="txtcenter">
                <hr  class="ordertr"><div class="ordertitle">长按二维码关注</div><hr class="ordertr">
            </div>
            <img class="followImg" > 
        </div>
    	<!-- 提交预定表单 -->
    	<div class="aform">
    		<div class="txtcenter"><hr  class="ordertr"><div class="ordertitle">马上预订</div><hr class="ordertr"></div>

    		<div class="input-group">
    			<label>手机号码：</label><input id="RESERVEDNUMBER" name="RESERVEDNUMBER"  type="tel" name="tel" onkeydown="onlyNum()" max-length="11">
    			<div class="errormsg">*请输入正确的手机号码</div>
    		</div>
    		<div class="input-group">
    			<label>联系人：</label><input id="RESERVATIONNAME" type="text" name="RESERVATIONNAME" max-length="50">
    			<div class="errormsg">*联系人不能为空</div>
    		</div>
    		<div class="input-group">
    			<label>预定日期：</label>
    				<input  type="text" readonly="" placeholder="请选择预定日期" data-lcalendar="${toDay },${maxDay}"  class="date" name="date">
    			<div class="errormsg">*预定日期不能为空</div>
    		</div>
    		<div class="input-group">
    			<label class="floatl">预定时间：</label>
					<div class="time">
                        <ul id="dateUl">
                        	<c:forEach items="${reservationStateIDList }" var="stateList">
                        		<li id="${stateList.ID }">${stateList.NAME }</li>	
                        	</c:forEach>
<!--                             	 <li class="current">10:00</li> -->
<!-- 								 <li class="disabled">22:00</li> -->
                        </ul>
                    </div>
    			<div class="errormsg" id="dateEnd">*预定时间不能为空</div>
    		</div>
    		<button class="asubmit" id="asubmit">提交预定信息</button>
    	</div>
    </div>
</body>
<script type="text/javascript" src="../static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="../static/weixin/js/common.js"></script>
<script type="text/javascript" src="../static/weixin/js/index.js"></script>
<script type="text/javascript" src="../static/weixin/js/LCalendar.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
var param={};

$(function(){
		//关注二维码
        getQr();
		/*选择预定日期*/
        var calendar = new LCalendar();
        calendar.init({
            'trigger': '.date', //标签class
            'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
            /*'minDate': '2016-1-1' //最小日期*/
            /*'maxDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate() //最大日期*/
        });
        /*选择预定时间*/
        $("div.time ul li").click(function(){
            if($(this).hasClass("disabled")){
            	return false;
            }
            $(this).addClass('current').siblings('li').removeClass('current');
        });
        
        	$(".date").bind("propertychange input",function(){
            var date = $('.date').val();
			$.ajax({
				url:'<%=basePath%>news/checkDateDisable.do?date='+date+'&d=new Date()',
				type:'post',
				success:function(r){
					var lis=$("#dateUl >li");
					if(r.type==2){
						//不是今天的时间,那么预定的时间点全部不变灰色
						for(var j=0;j<lis.length;j++){
							$(lis[j]).removeClass("disabled");
						}
					}else{
						//是今天,判断具体的时间点,来置灰按钮
						var ids = r.data.split(",");
						for(var i=0;i<ids.length;i++){
							var id = ids[i];
							for(var j=0;j<lis.length;j++){
								var li = $(lis[j]).attr("id");
								if(li==id){
									$(lis[j]).attr("class","disabled");
								}
							}
						}
					}
				}
			});
        })
        
})
$("#asubmit").click(function(){
	  $("#asubmit").attr("disabled","disabled");  
	  $("#asubmit").text("提交中...");  
	  var PRODUCTID =  $("#PRODUCTID").val();
	  var RESERVEDNUMBER =  $("#RESERVEDNUMBER").val();
	  var flag = checkDataForm();//校验数据格式
	  if(!flag){
		  $("#asubmit").removeAttr("disabled");
		  $("#asubmit").text("提交预定信息");  
	  	return false;
	  }
// 	  if(RESERVEDNUMBER==''){
// 		  $("#RESERVEDNUMBER").next('.errormsg').text("请输入正确的联系方式").show();
// 		  return;
// 	  }
// 	  $("#RESERVEDNUMBER").next().hide();
	  var RESERVATIONNAME = $("#RESERVATIONNAME").val();
// 	  if(RESERVATIONNAME==''){
// 		  $("#RESERVATIONNAME").next().show();
// 		  $("#RESERVATIONNAME").next('.errormsg').text("请输入联系人").show();
// 		  return;
// 	  }
// 	  $("#RESERVATIONNAME").next().hide();
	  var CREATEDATE="";
// 	  if($("input[name='date']").val()==''){
// 		  $("input[name='date']").next('.errormsg').text("请选择预定日期").show();
// 		  return;
// 	  }
// 	  $("input[name='date']").next().hide();
// 	  if($("div.time ul li.current").length!=1){
// 		  $("div.time").next('.errormsg').text("请选择预定时间").show();
// 		  return;
// 	  }
// 	 	 $("div.time").next().hide();
	 	CREATEDATE = $("input[name='date']").val()+" "+$("div.time ul li.current").text();
	  	param={};
		param["PRODUCTID"] = PRODUCTID;
		param["RESERVEDNUMBER"] = RESERVEDNUMBER;
		param["RESERVATIONNAME"] = RESERVATIONNAME;
		param["CREATEDATE"] = CREATEDATE;
		$.post("<%=basePath%>frontProduct/promptlyAdd.do",param,function(data){
				if(data=='' || typeof(data) == "undefined"){
					 $("#asubmit").removeAttr("disabled");
					 $("#asubmit").text("提交预定信息");  
					return;
				}
                if(data.type==1){
                	window.location.href="<%=basePath%>frontProduct/goPayment.do?id="+data.data;
                }else{
                	$("#dateEnd").text(data.msg).show();
                	$("#asubmit").removeAttr("disabled");
                	$("#asubmit").text("提交预定信息");  
                }
               

		});
})
	
	//生成二维码src
	function getQr(){
         var subscribe = ${subscribe};
         if(subscribe==0){
	         $.ajax({
	         	url:'<%=basePath%>appuser/createTempQrcode.do?ARTICLEID=${pd.ARTICLEID }&SHARERID=${shareId }',
	         	type:'post',
	         	dataType:'json',
	         	success:function(r){
	         		var img = r.imgUrl;
	         		if(img!=''){
	         			console.log(r);
	         			
	         			$(".followImg").attr("src","<%=basePath%>uploadFiles/uploadImgs/"+img);
	         		}else{
	         			$(".followImg").attr("src","<%=basePath%>uploadFiles/uploadImgs/ddj.jpg");
	         		}
	         	}
	         });      	  
         }else{
         	$(".followImg").attr("src","<%=basePath%>uploadFiles/uploadImgs/ddj.jpg");
         }
	}
</script>

<script type="text/javascript">
	var locat = "<%=basePath%>"; 
   var imgUrl =locat+"uploadFiles/uploadImgs/"+$("#PROIMG").val();
	wx.config({
					    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
					    appId: '${appId}', // 必填，企业号的唯一标识，此处填写企业号corpid
					    timestamp: '${timestamp}', // 必填，生成签名的时间戳
					    nonceStr: '${nonce_str}', // 必填，生成签名的随机串
		    			signature: '${signature}',// 必填，签名，见附录1
		    			jsApiList: [ 'checkJsApi',
		            				'onMenuShareTimeline',
						            'onMenuShareAppMessage',
						            'onMenuShareQQ',
						            'onMenuShareWeibo'
				            		] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	});
	
	
	wx.ready(function () {
							   wx.onMenuShareTimeline({
									title: '${pd.PROTITLE}', // 分享标题
									link: '${shareUrl}', // 分享链接,将当前登录用户转为puid,以便于发展下线
									imgUrl: imgUrl, // 分享图标
									success: function () { 
										// 用户确认分享后执行的回调函数
									},
									cancel: function () { 
										// 用户取消分享后执行的回调函数
									}
								});
								wx.onMenuShareAppMessage({
									title: '${pd.PROTITLE}', // 分享标题
									link: '${shareUrl}', // 分享链接,将当前登录用户转为puid,以便于发展下线
									imgUrl: imgUrl, // 分享图标
									success: function () { 
										// 用户确认分享后执行的回调函数
									},
									cancel: function () { 
										// 用户取消分享后执行的回调函数
									}
								});
								
								wx.onMenuShareQQ({
									title: '${pd.PROTITLE}', // 分享标题
									link: '${shareUrl}', // 分享链接,将当前登录用户转为puid,以便于发展下线
									imgUrl: imgUrl, // 分享图标
									success: function () { 
										// 用户确认分享后执行的回调函数
									},
									cancel: function () { 
										// 用户取消分享后执行的回调函数
									}
								});
								
								wx.onMenuShareWeibo({
									title: '${pd.PROTITLE}', // 分享标题
									link: '${shareUrl}', // 分享链接,将当前登录用户转为puid,以便于发展下线
									imgUrl: imgUrl, // 分享图标
									success: function () { 
										// 用户确认分享后执行的回调函数
									},
									cancel: function () { 
										// 用户取消分享后执行的回调函数
									}
								});
								
						});
</script>
</html>