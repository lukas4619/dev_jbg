$(function(){
	/*点赞事件*/
	var dzImg1="../static/weixin/images/dianzan-01-16-16.png";
	var dzImg2="../static/weixin/images/yidian-02-16-16.png";
	
	var islike = $("#islike").val();
	if(islike=='1'){
		$(".praise").removeClass('dz');
		$(".praise").attr("src",dzImg2);
	}
	
	$(".praise").click(function(){
		if($(this).hasClass('dz')){
			$(this).removeClass('dz');
			$(this).attr("src",dzImg2);
			$("#dzCount").text(parseInt($("#dzCount").text())+1);
		}
//		else{
//			$(this).addClass('dz');
//			$(this).attr("src",dzImg1);
//			$("#dzCount").text(parseInt($("#dzCount").text())-1);
//		}
	})
	/*提交预订信息*/
//	$("#asubmit").click(function(){
//		var flag=true;
//		var tel=$("input[name='tel']");
//		var name=$("input[name='name']");
//		var date=$("input[name='date']");
//		var time=$("input[name='time']");
//		if(checkMobile(tel.val())){
//			tel.next('.errormsg').text("*请输入正确的手机号码").show();
//			flag=false;
//		}else{
//			tel.next('.errormsg').text("").hide();
//		}
//		if(name.val().trim()==""){
//			name.next('.errormsg').text("*联系人不能为空").show();
//			flag=false;
//		}else{
//			name.next('.errormsg').text("").hide();
//		}
//		if(date.val().trim()==""){
//			date.next('.errormsg').text("*预定日期不能为空").show();
//			flag=false;
//		}else{
//			date.next('.errormsg').text("").hide();
//		}
//		if(time.val().trim()==""){
//			time.next('.errormsg').text("*预定时间不能为空").show();
//			flag=false;
//		}else{
//			time.next('.errormsg').text("").hide();
//		}
//		if(flag){
//			/*ajax*/
//			location.href="rewardsuccess.html";
//		}
//	})
	
})

//校验提交数据的格式
	function checkDataForm(){
		var flag=true;
		var tel=$("input[name='RESERVEDNUMBER']");
		var name=$("input[name='RESERVATIONNAME']");
		var date=$("input[name='date']");
		var time=$("input[name='time']");
		if(checkMobile(tel.val())){
			tel.next('.errormsg').text("*请输入正确的手机号码").show();
			flag=false;
		}else{
			tel.next('.errormsg').text("").hide();
		}
		if(name.val().trim()==""){
			name.next('.errormsg').text("*联系人不能为空").show();
			flag=false;
		}else{
			name.next('.errormsg').text("").hide();
		}
		if(date.val().trim()==""){
			date.next('.errormsg').text("*预定日期不能为空").show();
			flag=false;
		}else{
			date.next('.errormsg').text("").hide();
		}
		if($("div.time ul li.current").length!=1){
			$("div.time").next('.errormsg').text("*请选择预定时间").show();
			flag=false;
		}else{
			$("div.time").next().hide();
		}
		return flag;
	}
	