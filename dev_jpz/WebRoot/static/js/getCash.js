$(function(){
	$(".cashType li ").click(function(){
		var cashType=$(this).attr('data-type');
		$(this).addClass('current').siblings('li').removeClass('current');
		$("#cashType").val(cashType);
	})
	/*全部提现*/
	$("#allcash").click(function(){
		var value=parseFloat($("#usefulNum").text());
		if(value>0){
			$(".cashinput").val(value);
		}
	})
})