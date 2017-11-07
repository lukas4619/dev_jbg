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
<link href="../static/weixin/css/index.css" rel="stylesheet">
<link href="../static/weixin/css/LCalendar.css" rel="stylesheet">
</head>
<body>
    <div class="content">
    	<!-- 头部标题 -->
    	<div class="header">
    		<input type="hidden" value="${islike }" id="islike">
    		<h3>${pd.TITLE }</h3>
    		<div class="wxdate"><span><fmt:formatDate value="${pd.CREATEDATE }" type="date"/>  </span>	<span style="color: #3385ff">${pd.AUTHOR }</span></div>
    	</div>
    	<!-- 内容 -->
    	<div class="acontent">
<!--     		<p>有一种香水</p> -->
<!--     		<p><img src="http://mmbiz.qpic.cn/mmbiz/LHaFeXXnMeF85THMSm8pXpV1lPtTbUSyeYlEWHN1xgDhDkVOfpQQ4AdJnv5FTm89trYrUcj4xia4HzV5XUkASmw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1"></p> -->
<!--     		<p>有一种香水</p> -->
<!--     		<p><img src="http://mmbiz.qpic.cn/mmbiz/LHaFeXXnMeF85THMSm8pXpV1lPtTbUSyF3SLXqJKJNZXI43iaicd58vCvr4niacP6aeM05QuUd1DE3KZZu7IpCXlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1"></p> -->
<!--     		<p>有一种香水</p> -->
<!--     		<p><img src="http://mmbiz.qpic.cn/mmbiz/LHaFeXXnMeF85THMSm8pXpV1lPtTbUSyF3SLXqJKJNZXI43iaicd58vCvr4niacP6aeM05QuUd1DE3KZZu7IpCXlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1"></p> -->
<!--     		<p>有一种香水</p> -->
<!--     		<p><img src="http://mmbiz.qpic.cn/mmbiz/LHaFeXXnMeF85THMSm8pXpV1lPtTbUSylyof0xcLwF1sBCUapUdhXO2cqicbfHL68jT5EdM0zG8Udicd25NpGKwg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1"></p> -->
			${pd.CONTENT }
    	</div>
    	<!-- 打赏 href="reward.html"
    	<a  href="<%=basePath%>news/reward.html?ARTICLEID=${pd.ARTICLEID }">
    		<div class="reward txtcenter">
    		<div style="color:#f60">点击下方图片打赏</div>
    		<img class="rewardImg" src="../static/weixin/images/dashang.png">
    		</div>
    	<a>
		-->
		<!-- 关注二维码 
        <div class="follow">
            <div class="txtcenter">
                <hr  class="ordertr"><div class="ordertitle">长按二维码关注</div><hr class="ordertr">
            </div>
            <img class="followImg" src="<%=basePath%>uploadFiles/uploadImgs/ddj.jpg">
        </div>-->
        <div class="wcnt">
           <!-- 点击下方图片打赏 -->
           <div class="reward txtcenter" style="">
               <div style="color:#f60">点击下方图片打赏</div>
               <img style="width:100%" src="../static/weixin/images/dashang.png">
           </div>
           <!-- 关注二维码 -->
           <div class="follow txtcenter" style="">
               <div style="color:#f60">扫描二维码关注</div>
               <img style="width:100%" class="followImg" 
               	 
               > 
           </div>
        </div>
         <div class="content2">
            <div class="title">选择打赏金额</div>
            <div id="rewardCnt" class="rewardCnt">
                <ul>
                    <li>
                        <span class="money">10元</span>
                    </li>
                    <li><span class="money">20元</span></li>
                    <li><span class="money">30元</span></li>
                    <li><span class="money">40元</span></li>
                    <li><span class="money">50元</span></li>
                    <li><span class="money">60元</span></li>
                </ul>
            </div>
            <input type="hidden" name="lastMoney">  
            <div style="clear: both"></div>
            <div class="title" id="changeOther" class="changeOther" >选择其它打赏金额</div>
            <div class="rewardCnt2" style="display: none">
                <input type="number"  maxlength="10" min="0" class="otherMoney" placeholder="请输入与金额">
            </div>
            <button id="submit" class="resubmit" onclick="tods();">立即打赏</button>
        </div>
    	<div class="aread">
    		阅读 <span id="readNum">${pd.ARTICLEPV }</span> <!-- <img class="praise" src="images/yidian-02-16-16.png"> --> <img onclick="addLikeNum();" class="praise dz" src="<%=basePath%>/static/weixin/images/dianzan-01-16-16.png"> <span id="dzCount">${pd.ARTICLELIKE }</span>
    	</div>
    	
    	
        <!-- 广告 -->
        <a href="<%=basePath%>/frontProduct/goDetails.do?id=${productPd.PRODUCTID}">
        <div class="ad">
            <div class="adtop">
                <span class="adleft">${productPd.PROTITLE }</span>   
                <span class="adright">广告</span>
            </div>
            
            <div class="adcnt">
                <img class="adimg" src="<%=basePath%>uploadFiles/uploadImgs/${productPd.PROIMG }">
                <div class="adword">${productPd.PRODESCRIPTION }</div>
            </div>
        </div>
    	</a>
    	<!-- 提交预定表单 -->
    	<div class="aform">
    		<div class="txtcenter"><hr  class="ordertr"><div class="ordertitle">马上预订</div><hr class="ordertr"></div>
				<input type="hidden" value="${pd.ARTICLEID }" name="ARTICLEID">
    			<input type="hidden" value="${shareId }" name="shareId">
    			<input type="hidden" value="${productPd.PRODUCTID }" name="PRODUCTID">
    			<!-- AUTHOR -->
	    		<div class="input-group">
	    			<label>手机号码：</label><input  type="tel" name="RESERVEDNUMBER" onkeydown="onlyNum()" max-length="11">
	    			<div class="errormsg">*请输入正确的手机号码</div>
	    		</div>
	    		<div class="input-group">
	    			<label>联系人：</label><input type="text"  name="RESERVATIONNAME">
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
                <!-- <input  placeholder="请输入时间" class="time" type="time" name="time"> -->
    				<div class="errormsg">*请选择预定时间</div>
    			</div>
	    		<button type="submit" class="asubmit" id="orderSub">提交预定信息</button>
    	</div>
    </div>
</body>
<script type="text/javascript" src="../static/weixin/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="../static/weixin/js/common.js"></script>
<script type="text/javascript" src="../static/weixin/js/index.js"></script>
<script type="text/javascript" src="../static/weixin/js/LCalendar.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	var ARTICLEID = $("input[name='ARTICLEID']").val().trim();
	$(function(){
		//生成二维码src
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
         /*打赏下拉框*/  
        $(".reward").click(function(){
            if($('.content2').css('display')=='none'){
                 $('.content2').slideDown();
                 return;
            }
            $('.content2').slideUp();
        })
        /*金额切换*/
        $("#rewardCnt li").click(function(){
            $(".rewardCnt2").slideUp();
            $(this).addClass("active").siblings().removeClass('active');
            $("input[name='lastMoney']").val(parseInt($(this).find('span').text()));
        })
        /*自定义金额*/
        $("#changeOther").click(function(){
            var flag=$(".rewardCnt2")[0].style.display;
            $("input[name='lastMoney']").val(0);
            if(flag=="none"){
                $(".rewardCnt2").slideDown();
                $("#rewardCnt li").removeClass('active');
            }else{
                $(".rewardCnt2").slideUp();
            }
        })    
		//阅读数加一  增加作者收益和用户收益
		addReadNum();
		
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
        
	});
	
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
	
	//打赏跳转方法
	function tods(){
	//得到打赏金额
	var m = $("ul >li[class='active']").find("span").text();
	if(m==''||m==undefined){
		m=$("input[class='otherMoney']").val();
		if(m==''||m==undefined||m<=0){
			alert("请选择金额");
			return
		}
	}
	var ARTICLEID = $("input[name='ARTICLEID']").val();
	location.href="<%=basePath%>weChatPay/index.html?ARTICLEID="+ARTICLEID+"&money="+ encodeURI(encodeURI(m));
	}
	
	//添加阅读数量	
	function addReadNum(){
		var shareId = $("input[name='shareId']").val().trim();
		$.ajax({
			url:"<%=basePath%>news/addReadNum.html?ARTICLEID="+ARTICLEID+"&SHARERID="+shareId,
			type:'post',
			dataType:'json',
			success:function(r){
				if(r.code==1){
					var num = $("#readNum").text();
					$("#readNum").text(num*1+1);
				}
			}
		});
	}
	
	//用户点赞数量添加
	function addLikeNum(){
		var isLike = $("#islike").val();
		if(isLike=='1'){
			return false;
		}
		$.ajax({
			url:"<%=basePath%>news/addLikeNum.html?ARTICLEID="+ARTICLEID,
			type:'post',
			dataType:'json',
			success:function(r){
				if(r.code==1){
					$("#islike").val(1);
				}
			}
		});
	}
	
	var param={};

$("#orderSub").click(function(){
	  var PRODUCTID =  $("input[name='PRODUCTID']").val();
	  var RESERVEDNUMBER =  $("input[name='RESERVEDNUMBER']").val();
	  var flag = checkDataForm();//校验数据格式
	  if(!flag){
	  	return false;
	  }
// 	  if(RESERVEDNUMBER==''){
// 		  $("input[name='RESERVEDNUMBER']").next('.errormsg').text("请输入正确的联系方式").show();
// 		  return;
// 	  }
// 	  $("input[name='RESERVEDNUMBER']").next().hide();
 	  var RESERVATIONNAME = $("input[name='RESERVATIONNAME']").val();
// 	  if(RESERVATIONNAME==''){
// 		  $("input[name='RESERVATIONNAME']").next().show();
// 		  $("input[name='RESERVATIONNAME']").next('.errormsg').text("请输入联系人").show();
// 		  return;
// 	  }
// 	  $("input[name='RESERVATIONNAME']").next().hide();
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
	  	param={};
		param["PRODUCTID"] = PRODUCTID;
		param["RESERVEDNUMBER"] = RESERVEDNUMBER;
		param["RESERVATIONNAME"] = RESERVATIONNAME;
		param['SHAREID']=$("input[name='shareId']").val();
		param['date']=$("input[name='date']").val();
		param['time']=$("div.time ul li.current").text();
		param['ARTICLEID']=ARTICLEID;
		$.post("<%=basePath%>news/readerReservation.do",param,function(data){
				if(data=='' || typeof(data) == "undefined"){
					return;
				}
                if(data.type==1){
                	window.location.href="<%=basePath%>weChatPay/goPayment.do?id="+data.data;
                }else{
                	$("#dateEnd").next('.errormsg').text(data.msg).show();
                }

		});
})

</script>

<script type="text/javascript">
   var imgUrl ="<%=basePath%>uploadFiles/uploadImgs/${productPd.PROIMG}";
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
									title: '${pd.TITLE}', // 分享标题
									link: '${shareUrl}', // 分享链接,将当前登录用户转为puid,以便于发展下线
									imgUrl: imgUrl, // 分享图标
									success: function () { 
										// 用户确认分享后执行的回调函数
										alert('分享成功');
									},
									cancel: function () { 
										// 用户取消分享后执行的回调函数
									}
								});
								wx.onMenuShareAppMessage({
									title: '${pd.TITLE}', // 分享标题
									link: '${shareUrl}', // 分享链接,将当前登录用户转为puid,以便于发展下线
									imgUrl: imgUrl, // 分享图标
									success: function () { 
										// 用户确认分享后执行的回调函数
										alert('分享成功');
									},
									cancel: function () { 
										// 用户取消分享后执行的回调函数
									}
								});
								
								wx.onMenuShareQQ({
									title: '${pd.TITLE}', // 分享标题
									link: '${shareUrl}', // 分享链接,将当前登录用户转为puid,以便于发展下线
									imgUrl: imgUrl, // 分享图标
									success: function () { 
										// 用户确认分享后执行的回调函数
										alert('分享成功');
									},
									cancel: function () { 
										// 用户取消分享后执行的回调函数
									}
								});
								
								wx.onMenuShareWeibo({
									title: '${pd.TITLE}', // 分享标题
									link: '${shareUrl}', // 分享链接,将当前登录用户转为puid,以便于发展下线
									imgUrl: imgUrl, // 分享图标
									success: function () { 
										// 用户确认分享后执行的回调函数
										alert('分享成功');
									},
									cancel: function () { 
										// 用户取消分享后执行的回调函数
									}
								});
								
						});
</script>
</html>