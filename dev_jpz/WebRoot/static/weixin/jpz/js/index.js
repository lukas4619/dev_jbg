//设置各块面板的初始宽高
$(".all_list").focus();
var width = $("body").width();
width = parseInt(width) * 0.73 - 1 + "px";
$(".all_list").css("width", width);
$(".bottom_nav").css("width", $("body").width());
$(".menus").css("height", $(window).height() * 0.9);
$(".menus").css("max-height", $(window).height() * 0.9);

$(".all_list").css("max-height", $(window).height() * 0.9);
$(".pa_bo").css("height", $(window).height() * 0.7)


//防止在手机端时菜单栏下拉与主屏下拉冲突
$("body").css({"height": "100%", "overflow": "hidden"});
$("html").css({"height": "100%", "overflow": "hidden"});

//商品栏各个栏目的ID

//判断是否在滚动中
var ff = 0;
var timeId, timeId2;

//判断当前所在的位置为合适的栏目加上样式
//timeId = window.setTimeout("skipHref()", 200);
function skipHref() {
  if (ff == 1) {
	  
  } else {
    $.each(type, function (i) {
      if ($("#" + type[i]).offset().top < $(window).height() * 0.1) {
        $(".menus li").removeClass("active");
        $(".menus a").removeClass("active");
        /*
        $("a[href=#" + type[i] + "]").parent().addClass("active");
        $("a[href=#" + type[i] + "]").addClass("active");
       
        var text = $("a[href=#" + type[i] + "]").text();
        $(".title").removeClass("active");
        $("p:contains(" + text + ")").addClass("active");
         */
        
        $("a[placeid=" + type[i] + "]").parent().addClass("active");
        $("a[placeid=" + type[i] + "]").addClass("active");
       
        var text = $("a[placeid=" + type[i] + "]").text();
        $(".title").removeClass("active");
        $("p:contains(" + text + ")").addClass("active");
      }

    });
    timeId = window.setTimeout("skipHref()", 200);
  }
  $(".all_list").focus();
}
/* 锚点跳转 */
var j = 0, posL = [], posO = {};
function aSkip(o) {
  clearTimeout(timeId);
  ff = 1;
  timeId2 = window.setTimeout("isStop()", 200);
  $(".menus li").removeClass("active");
  $(".menus a").removeClass("active");
  var text = $(o).text();
  $(".title").removeClass("active");
  $("p:contains(" + text + ")").addClass("active");
  $(o).addClass("active");
  $(o).parent().addClass("active");
  var placeid = $(o).attr('placeid');
  var shelvesid=$(o).attr('shelvesid');
  //平滑跳转
  if (j == 0) {
    $.each(type, function (i, item) {
	      /*
	      posO.id = "#" + type[i];
	      posO.top = $("#" + type[i]).offset().top + $(".all_list").scrollTop()-60;
	      */
	 posO.id =  type[i];
     posO.top = $("#a" + type[i]).offset().top + $("#list"+shelvesid).scrollTop()-60;
      posL[i] = posO;
      posO = {};
    });
    /*
    $.each(posL, function (i, item) {
      if ($(o).attr("href") == item.id) {
        $(".all_list").animate({scrollTop: item.top}, 700);
      }
    });
    */
    $.each(posL, function (i, item) {
        if (placeid == item.id) {
          $("#list"+shelvesid).animate({scrollTop: item.top}, 700);
        }
      });
    j++;
  } else {
    $("#list"+shelvesid).stop();
    /*
    $.each(posL, function (i, item) {
      if ($(o).attr("href") == item.id) {
        $(".all_list").animate({scrollTop: item.top}, 700);
      }
    });
    */
    $.each(posL, function (i, item) {
        if (placeid == item.id) {
          $("#list"+shelvesid).animate({scrollTop: item.top}, 700);
        }
      });
  }
  
};

//锚点连接后判断动画是否完成
function isStop() {
  //alert($(".all_list").is(":animated"))
  if ($(".all_list").is(":animated")) {
    ff = 1;
    timeId2 = window.setTimeout("isStop()", 200);
  } else {
    ff = 0;
    clearTimeout(timeId2);
  }
}
//左右联动
$(".all_list").on("scrollstop", function () {
  if (ff == 0) {
    console.log("?");
    timeId = window.setTimeout("skipHref()", 200);
  } else {
    clearTimeout(timeId);
  }
});

var type = [];
$(function(){
	
  $(".nav>ul>li").click(function(){
	  j=0;
	  var shelvesId = $(this).attr('data');
	  $(".menus").each(function(i,o){
		  if($(o).attr('shelvesId')==shelvesId){
			  type = [];
			  $(o).find('li').each(function(j,k){
				 type[j]= $(k).find('a').attr('placeid');
			  });
			  /*
			  var l  =$(o).find('li').length;
			  for(var i=0;i<l;i++){
				  type[i]= "l"+(i+1);
			  }*/
			  $(o).show();
			  $("#list"+shelvesId).show();
		  }else{
			  $(o).hide();
			  $("#list"+shelvesId).hide();
		  }
	  });
	  
	  $(".all_list").each(function(i,o){
		  if($(o).attr('shelvesId')==shelvesId){
			  $(o).show();
		  }else{
			  $(o).hide();
		  }
	  });
	  
	 
	  
	  
	 /*
    if($(this).html() == '货架'){
      $('.one1').css("display","block");
      $(".list1").css("display","block");
      $('.one2').css("display","none");
      $(".list2").css("display","none");
      type = ["l1", "l2", "l3","l4"];
    }else{
      $('.one2').css("display","block");
      $(".list2").css("display","block");
      $('.one1').css("display","none");
      $(".list1").css("display","none");
      type = ["l21","l22","l23","l24","l25"];
    }
    */
    $('.nav').find('ul li').removeClass('li-active');
    $(this).addClass('li-active');
  })
  firstload();
  var umenus = $("#main_plane").children("ul:first").children("li:first").find("a");
  aSkip(umenus);
})

function firstload(){
	var shelvesId = '';
	 $('.nav').find('ul li').each(function(i,o){
		 if($(o).attr('class')=='li-active'){
			 shelvesId = $(o).attr('data');
		 }
	 });
	 
	  $(".menus").each(function(i,o){
		  if($(o).attr('shelvesId')==shelvesId){
			  type = [];
			  $(o).find('li').each(function(j,k){
					 type[j]= $(k).find('a').attr('placeid');
			});
			  /*
			  var l  =$(o).find('li').length;
			  for(var i=0;i<l;i++){
				  type[i]= "l"+(i+1);
			  }
			  */
			  $(o).show();
			  $("#list"+shelvesId).show();
		  }else{
			  $(o).hide();
			  $("#list"+shelvesId).hide();
		  }
	  });
	  
	  $(".all_list").each(function(i,o){
		  if($(o).attr('shelvesId')==shelvesId){
			  $(o).show();
		  }else{
			  $(o).hide();
		  }
	  });
}
