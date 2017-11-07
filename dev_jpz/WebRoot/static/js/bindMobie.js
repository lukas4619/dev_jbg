$(function(){
	var validCode = true;
	$("#sendCode").click(function(){
		var newmobile=$(".newmobile");
		if(newmobile.val()==""){
			newmobile.siblings('.errormsg').text('*请输入手机号').show();
			return false;
		}else{
			newmobile.siblings('.errormsg').text('').hide();
			var code = $(this);
			code.addClass('greybtn');
			$(".bind").attr("disabled", false);
			var time = 150;
			if (validCode) {
				validCode = false;
				code.attr("disabled", true);
				var t = setInterval(function() {
					time--;
					code.html(time + "秒后获取");
					if (time == 0) {
						clearInterval(t);
						code.html("重新获取");
						validCode = true;
						code.attr("disabled", false);
					}
				}, 1000)
			}
		}
	})
})