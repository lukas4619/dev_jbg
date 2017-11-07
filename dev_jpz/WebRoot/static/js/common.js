/*限制数字*/
function onlyNum() {
  	//非退格等功能键
    if(!(event.keyCode==46)&&!(event.keyCode==9)&&!(event.keyCode==8)&&!(event.keyCode==37)&&!(event.keyCode==39))
    {
   	 	//非数字
    	if((event.keyCode>=48&&event.keyCode<=57)==false&&(event.keyCode>=96&&event.keyCode<=105)==false)
    	{
    		//alert(123)
   			event.returnValue=false;
    	}
    }
}
var checkMobile=function(value){
  var mobileReg = /^1[3|4|5|8|7][0-9]{9}$/;
  if (mobileReg.test(value)) {
      return false;
  }
  return true;
}
/*只能输入最多两位小数的金额*/
function clearNoNum(obj){
  obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
  obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字而不是
  obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
  obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
  obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
}