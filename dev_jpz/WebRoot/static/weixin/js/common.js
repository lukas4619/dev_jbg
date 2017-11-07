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

//清除空格包含中间空格，以及清除所有的HTML标记
var delHtmlTag=function(str) {
            var str = str.replace(/<\/?[^>]*>/gim, ''); //去掉所有的html标记
            var result = str.replace(/(^\s+)|(\s+$)/g, ''); //去掉前后空格
            return result.replace(/\s/g, ''); //去除文章中间空格
}

//判断字符串是否为空 false 不为空 true  为空
var isNull = function (text) {
	if (text == null || text == undefined || text == "") {
		text = text + "";
		if (isNaN(text)) {
			return true;
		} else {
			if (delHtmlTag(text).length <= 0) {
				return true;
			}
		}
	}
	return false;
};