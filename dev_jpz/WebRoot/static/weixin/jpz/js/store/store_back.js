var byArry= new Array();
$(function(){
	     var subUrl =$("#subUrl").val();
	     $("#pay").click(function(){
		 if(parseInt($("#PLUCOUNT").val())<=0 || toDecimal($("#TOTALPRICE").html())<=0){
			 alert("请选择需要购买的商品");
			 return;
		 }
		 $("#sCar").val(byArry.join().replace(/,/g, ""));
		 $.ajax({
				type: "POST",
				url: subUrl+'frontStore/subOrder.do?tm='+new Date().getTime(),
		    	data: {sCar:$("#sCar").val(),STOREID:$("#STOREID").val(),TOTALPRICE:$("#TOTALPRICE").val(),PLUCOUNT:$("#PLUCOUNT").val()},
				dataType:'json',
				//beforeSend: validateData,
				cache: false,
				success: function(data){
					if(data.type==0){
						 $("#Form").submit();
					}else{
						alert(data.msg);
					}
				}
			});
	    
	 });
	 
	
	 $(".sub").click(function(){
		 var tempTotal = parseInt($("#PLUCOUNT").val());//总数量
		 var pluCode =$(this).attr("plucode");//商品编码
		 var pluCount=1;//购买数量
		 var shelvesId=$(this).attr("shelvesid");//货架标识
		 var placeId=$(this).attr("placeid");//架位标识
		 var inventorycount=parseInt($(this).attr("inventorycount"));//当前库存
		 var price=toDecimal($(this).attr("price"));//当前售价
		 var placepluid=$(this).attr("placepluid");//当前商品唯一标识
		 var tempNum= parseInt($("#sum"+placepluid).html());//当前已购买数量
		 var total = toDecimal($("#total").html());//当前已选商品金额
		 var shelvesCount =  parseInt($("#spanss"+placeId).html());//货架计数
		 if(tempNum<=0){
			 $(this).hide();
			 return;
		 }
		 $("#total").html(total-price);
		 $("#TOTALPRICE").val(total-price);
		 $("#sum"+placepluid).html((tempNum-pluCount)).show();
		 $("#PLUCOUNT").val(tempTotal-pluCount);
		 if((tempNum-pluCount)==0){
			 byArry[placepluid]="";
			 $(this).hide();
			 if(shelvesCount<=0 || (shelvesCount-pluCount)<=0){
				 $("#divss"+placeId).hide();
				 $("#spanss"+placeId).html(0);
			 }else{
				 $("#divss"+placeId).show();
				 $("#spanss"+placeId).html(shelvesCount-pluCount);
			 }
		 }else{
			 byArry[placepluid]=pluCode+"＠"+(tempNum-pluCount)+"＠"+shelvesId+"＠"+placeId+"＆";
			 if(shelvesCount<=0){
				 $("#divss"+placeId).hide();
				 $("#spanss"+placeId).html(0);
			 }else{
				 $("#divss"+placeId).show();
				 $("#spanss"+placeId).html(shelvesCount-pluCount);
			 }
			
		 }
		
	 });
	 
	$(".add").click(function(){
		var tempTotal = parseInt($("#PLUCOUNT").val());//总数量
		 var pluCode =$(this).attr("plucode");//商品编码
		 var pluCount=1;//购买数量
		 var shelvesId=$(this).attr("shelvesid");//货架标识
		 var placeId=$(this).attr("placeid");//架位标识
		 var inventorycount=parseInt($(this).attr("inventorycount"));//当前库存
		 var price=toDecimal($(this).attr("price"));//当前守家
		 var placepluid=$(this).attr("placepluid");//当前商品唯一标识
		 var tempNum= parseInt($("#sum"+placepluid).html());//当前已购买数量
		 var total = toDecimal($("#total").html());//当前已选商品金额
		 var shelvesCount =  parseInt($("#spanss"+placeId).html());//货架计数
		 if(inventorycount<tempNum+pluCount){
			 return;
		 }
		 $("#total").html(total+price);
		 $("#TOTALPRICE").val(total+price);
		 byArry[placepluid]=pluCode+"＠"+(tempNum+pluCount)+"＠"+shelvesId+"＠"+placeId+"＆";
		 $("#sum"+placepluid).html((tempNum+pluCount)).show();
		 $("#PLUCOUNT").val(tempTotal+pluCount);
		 if(inventorycount==(tempNum+pluCount)){
			 $(this).hide();
		 }
		 $("#divsub"+placepluid).show();
		 $("#divss"+placeId).show();
		 $("#spanss"+placeId).html(shelvesCount+pluCount);
		 
	 });
});

function toDecimal(x) { 
      var f = parseFloat(x); 
      if (isNaN(f)) { 
        return; 
      } 
      f = Math.round(x*100)/100; 
      return f; 
    }; 
