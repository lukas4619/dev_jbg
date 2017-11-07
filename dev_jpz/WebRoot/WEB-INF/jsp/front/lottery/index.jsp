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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no;">
<meta name="viewport" content="initial-scale=1,target-densitydpi=device-dpi,minimum-scale=1,maximum-scale=1,user-scalable=1" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<title>抽奖--大转盘抽奖获得赢消费折扣</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="<%=basePath%>/static/weixin/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=basePath%>static/weixin/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="<%=basePath%>static/weixin/js/awardRotate.js"></script>
<script type="text/javascript" src="<%=basePath%>static/weixin/js/Marquee.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="<%=basePath%>static/weixin/js/common.js"></script>
<script type="text/javascript">
var turnplate={
		restaraunts:[],				//大转盘奖品名称
		colors:[],					//大转盘奖品区块对应背景颜色
		outsideRadius:192,			//大转盘外圆的半径
		textRadius:155,				//大转盘奖品位置距离圆心的距离
		insideRadius:68,			//大转盘内圆的半径
		startAngle:180,				//开始角度
		bRotate:false				//false:停止;ture:旋转
};
$(document).ready(function(){
	//动态添加大转盘的奖品与奖品区域背景颜色
	turnplate.restaraunts = ["20元优惠券", "1折优惠券", "2折优惠券", "3折优惠券", "4折优惠券", "5折优惠券", "6折优惠券 ", "7折优惠券", "8折优惠券", "9折优惠券"];
	turnplate.colors = ["#FFF4D6", "#FFFFFF", "#FFF4D6", "#FFFFFF","#FFF4D6", "#FFFFFF", "#FFF4D6", "#FFFFFF","#FFF4D6", "#FFFFFF"];
	var rotateTimeOut = function (){
		$('#wheelcanvas').rotate({
			animateTo:2160,
			duration:8000,
			callback:function (){
				alert('网络超时，请检查您的网络设置！');
			}
		});
	};
	//旋转转盘 item:奖品位置; txt：提示语;
	var rotateFn = function (data){
		var angle = parseInt(data.angle); //角度 
		var msg = data.msg; //奖项内容
		var award=parseInt(data.awards); //奖项
		var numbers=data.numbers; //优惠券码
		var level=data.level;
		$('#wheelcanvas').stopRotate();
		$("#wheelcanvas").rotate({ //inner内部指针转动，outer外部转盘转动
				duration : 3000, //转动时间 
				angle : 0, //开始角度 
				animateTo : 3600 + angle, //转动角度 
				easing : $.easing.easeOutSine, //动画扩展 
			callback:function (){
				if(award==1){
					$("#couponsMsg").html(msg);
					var ahtml='<a style="text-decoration:none;color: #444444;" href="<%=basePath%>myCoupon/goDetails?numbers='+numbers+'">你的优惠券码：<span class="red">'+numbers+'</span> 点击查看详情</a>';
					$("#numbers").html(ahtml);
					$("#discount").hide();
					$("#coupons").show();
				}else{
					$("#discountMsg").html(msg);
					$("#discount").show();
					$("#coupons").hide();
				}
				showDialogue(data);
				turnplate.bRotate = !turnplate.bRotate;
			}
		});
	};
	$('.pointer').click(function (){
		$(".pointer").unbind('click');
		if(turnplate.bRotate)return;
		turnplate.bRotate = !turnplate.bRotate;
		var param = {};
		var url ="<%=basePath%>frontLottery/lottery.do";
		$.post(url,param,function(data){
			if(data=='' || typeof(data) == "undefined"){
				alert("网络繁忙，请稍后！");;
				return false;
			}
			if(data.type==1){
				rotateFn(data);
			}else{
				alert(data.msg);
			}
			
			
		})
		
	});
	/*关闭中奖弹框*/
	$('#closeImg').click(function(){
		$("#priceDiologue").hide();
	})
	/*立即分享按钮*/
	$('.sharBtn').click(function(){
		$(".wxshare").show();
		$(".wxshare").click(function(){
			$(".wxshare").hide();
		})
	})
});
//页面所有元素加载完毕后执行drawRouletteWheel()方法对转盘进行渲染
window.onload=function(){
	drawRouletteWheel();
	$('.scroll').kxbdSuperMarquee({
		isMarquee:true,
		isEqual:false,
		scrollDelay:20,
		direction:'up'
	});

};
var showDialogue=function(data){
	//弹出遮罩层
	$('.red-img').attr('src','<%=basePath%>static/weixin/images/red5.png');
	$("#priceDiologue").fadeIn();
	$('.red-img').load(function(){
		var top=parseInt($('.red-img').offset().top)-$(window).scrollTop()+parseInt($(".red-img").height())-$('.sharBtn').height()-25;
		$('.sharBtn').css('top',top);//设置弹框按钮的位置
		var top0=parseInt($(".red-img").height()/2)-5;
		$("#awardText").css('top',top0);//设置弹框文字的位置
		switch(data.level){
			case 1:$("#couponMsg").html('20元优惠券');break;
			case 2:$("#couponMsg").html('9折优惠');break;
			case 3:$("#couponMsg").html('8折优惠');break;
			case 4:$("#couponMsg").html('7折优惠');break;
			case 5:$("#couponMsg").html('6折优惠');break;
			case 6:$("#couponMsg").html('5折优惠');break;
			case 7:$("#couponMsg").html('4折优惠');break;
			case 8:$("#couponMsg").html('3折优惠');break;
			case 9:$("#couponMsg").html('2折优惠');break;
			case 10:$("#couponMsg").html('1折优惠');break;
			default:break;
		}
		var top1=parseInt($(".red-img").height()/3)-16;
		$('#closeImg').css('top',top1);
	})
	
	//展示中奖信息
}
function drawRouletteWheel() {    
  var canvas = document.getElementById("wheelcanvas");    
  if (canvas.getContext) {
	  //根据奖品个数计算圆周角度
	  var arc = Math.PI / (turnplate.restaraunts.length/2);
	  var ctx = canvas.getContext("2d");
	  //在给定矩形内清空一个矩形
	  ctx.clearRect(0,0,422,422);
	  //strokeStyle 属性设置或返回用于笔触的颜色、渐变或模式  
	  ctx.strokeStyle = "#FFBE04";
	  //font 属性设置或返回画布上文本内容的当前字体属性
	  ctx.font = '16px Microsoft YaHei';      
	  for(var i = 0; i < turnplate.restaraunts.length; i++) {       
		  var angle = turnplate.startAngle + i * arc;
		  ctx.fillStyle = turnplate.colors[i];
		  ctx.beginPath();
		  //arc(x,y,r,起始角,结束角,绘制方向) 方法创建弧/曲线（用于创建圆或部分圆）    
		  ctx.arc(211, 211, turnplate.outsideRadius, angle, angle + arc, false);    
		  ctx.arc(211, 211, turnplate.insideRadius, angle + arc, angle, true);
		  ctx.stroke();  
		  ctx.fill();
		  //锁画布(为了保存之前的画布状态)
		  ctx.save();   
		  //----绘制奖品开始----
		  ctx.fillStyle = "#E5302F";
		  var text = turnplate.restaraunts[i];
		  var line_height = 17;
		  //translate方法重新映射画布上的 (0,0) 位置
		  ctx.translate(211 + Math.cos(angle + arc / 2) * turnplate.textRadius, 211 + Math.sin(angle + arc / 2) * turnplate.textRadius);
		  //rotate方法旋转当前的绘图
		  ctx.rotate(angle + arc / 2 + Math.PI / 2);
		  /** 下面代码根据奖品类型、奖品名称长度渲染不同效果，如字体、颜色、图片效果。(具体根据实际情况改变) **/
		  if(text.indexOf("M")>0){//流量包
			  var texts = text.split("M");
			  for(var j = 0; j<texts.length; j++){
				  ctx.font = j == 0?'bold 20px Microsoft YaHei':'16px Microsoft YaHei';
				  if(j == 0){
					  ctx.fillText(texts[j]+"M", -ctx.measureText(texts[j]+"M").width / 2, j * line_height);
				  }else{
					  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
				  }
			  }
		  }else if(text.indexOf("M") == -1 && text.length>6){//奖品名称长度超过一定范围 
			  text = text.substring(0,6)+"||"+text.substring(6);
			  var texts = text.split("||");
			  for(var j = 0; j<texts.length; j++){
				  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
			  }
		  }else{
			  //在画布上绘制填色的文本。文本的默认颜色是黑色
			  //measureText()方法返回包含一个对象，该对象包含以像素计的指定字体宽度
			  ctx.fillText(text, -ctx.measureText(text).width / 2, 0);
		  }
		 if(text.indexOf("谢谢参与")>=0){
			  var img= document.getElementById("sorry-img");
			  img.onload=function(){  
				  ctx.drawImage(img,-15,10);      
			  };  
			  ctx.drawImage(img,-15,10);  
		  }
		  //把当前画布返回（调整）到上一个save()状态之前 
		  ctx.restore();
		  //----绘制奖品结束----
	  }     
  } 
}
</script>
<script type="text/javascript">
	var locat = "<%=basePath%>"; 
	var titles="大东江美食优惠活动";
	var appId='${appId}';
	var shareUrl='${shareUrl}';
   var imgUrl =locat+"uploadFiles/uploadImgs/ddj.jpg";
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
									title: titles, // 分享标题
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
									title: titles, // 分享标题
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
									title: titles, // 分享标题
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
									title: titles, // 分享标题
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
</head>
<body style="overflow-x:hidden;">
		<!-- 微信分享 -->
	<div class="wxshare"></div>
	<!-- 中奖弹框 -->
	<div id="priceDiologue" >
		<div>
			<div id="sharBtn" class="sharBtn">立即分享</div>
			<img src="<%=basePath%>static/weixin/images/close.png" id="closeImg" class="closeImg"> 
			<img  class="red-img">
			<a href="">
				<div id="awardText" class="awardText">
					<div class="awardTop">获得</div>
					<div id="couponMsg">优惠券</div>	
				</div>
			</a>
		</div>
		
	</div>
	<!-- 大转盘 -->
    <img src="<%=basePath%>static/weixin/images/1.png" id="shan-img" style="display:none;" />
    <img src="<%=basePath%>static/weixin/images/2.png" id="sorry-img" style="display:none;" />
	<div class="banner">
		<div class="turnplate" style="background-image:url(<%=basePath%>static/weixin/images/turnplate-bg.png);background-size:100% 100%;">
			<canvas class="item" id="wheelcanvas" width="422px" height="422px"></canvas>
			<img class="pointer" src="<%=basePath%>static/weixin/images/turnplate-pointer.png"/>
		</div>
	</div>
	<!-- 中奖信息 -->
	<div class="content">
    <div class="boxcontent boxyellow" id="discount" style="display: none;">
      <div class="box">
        <div class="title-red"><span>恭喜你中奖了</span></div>
        <div class="Detail">
          <p>奖品：<span class="red" id="discountMsg">一等奖</span></p>
          <p class="red">本次抽奖可立即进行消费抵扣!</p>
        </div>
      </div>
    </div>
	<div class="boxcontent boxyellow" id="coupons" style="display: none;">
	  <div class="box">
	    <div class="title-orange"><span>恭喜你中奖了</span></div>
	    <div class="Detail">
	      <p>奖品：<span class="red" id="couponsMsg">十等奖</span></p>
	      <p id="numbers"></p>
	    </div>
	  </div>
    </div>
    <div class="boxcontent boxyellow" >
      <div class="box">
        <div class="title-orange"><span>抽奖次数</span></div>
        <div class="Detail" id="lotteryNum">
          <p>您当前可抽奖次数：<span class="red">${pd.LOTTERYNUM}</span>次</p>
        </div>
      </div>
    </div>
    <div class="boxcontent boxyellow">
      <div class="box">
        <div class="title-green"><span>奖项设置：</span></div>
        <div class="Detail">
          <p>奖项1：<span class="red">消费享1折优惠 </span></p>
          <p>奖项2：<span class="red">消费享2折优惠 </span></p>
          <p>奖项3：<span class="red">消费享3折优惠</span> </p>
          <p>奖项4：<span class="red">消费享4折优惠</span> </p>
          <p>奖项5：<span class="red">消费享5折优惠 </span></p>
          <p>奖项6：<span class="red">消费享6折优惠 </span></p>
          <p>奖项7：<span class="red">消费享7折优惠</span> </p>
          <p>奖项8：<span class="red">消费享8折优惠</span> </p>
          <p>奖项9：<span class="red">消费享9折优惠</span> </p>
          <p>奖项10：<span class="red">价值20元优惠券 </span></p>
        </div>
      </div>
    </div>
    <div class="boxcontent boxyellow">
      <div class="box">
        <div class="title-green">活动说明：</div>
        <div class="Detail">
          <p>本次活动每消费一次可以获得<span class="red">1</span>次抽奖机会！ </p>
          <p> 我们的中奖率高达<span class="red">100%</span>！ </p>
          <p> 本活动最终解释权归<span class="red">广东省河源市大东江食品科技有限公司</span>所有。</p>
        </div>
      </div>
    </div>
    <c:choose>
   <c:when test="${not empty varList}">
    <div class="boxcontent boxyellow" >
      <div class="box">
        <div class="title-orange"><span>中奖记录</span></div>
        <div class="Detail">
       
        <div class="scroll">
        	<ul>
        	 <c:forEach items="${varList}" var="var" varStatus="vs">
        		<li>
        			<div>
        				<div class="floatName">${var.weChatName}</div>
        				<div class="floatTime"><fmt:formatDate value="${var.lotteryDate}" pattern="yyyy-MM-dd HH:mm:ss"  /></div>
        			</div>
        			<div class=" clearfix">获得:<span class="red floatTime">${var.lotteryContent}</span></div>
        		</li>
        		</c:forEach>
        	</ul>
        </div>
        
        </div>
      </div>
    </div>
   </c:when>
   </c:choose>
  </div>
</body>

</html>