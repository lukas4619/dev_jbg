package com.fh.controller.system.member;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.util.AppUtil;
import com.fh.util.Constants;
import com.fh.util.Convert;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.StringUtil;
import com.fh.util.Tools;
import com.fh.util.security.Encrypt;
import com.fh.wechat.gongzhong.TemplateMessage;
import com.fh.wechat.gongzhong.vo.message.template.Template;
import com.fh.service.system.member.MemberManager;

/** 
 * 说明：微信用户Controller
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-16
 */
@Controller
@RequestMapping(value="/member")
public class MemberController extends BaseController {
	
	String menuUrl = "member/list.do"; //菜单地址(权限用)
	@Resource(name="memberService")
	private MemberManager memberService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Member");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MEMBERID", this.get32UUID());	//主键
		memberService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Member");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		memberService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Member");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//memberService.edit(pd);
		pd.put("EDITDATE", new Date());
		memberService.editSoftAuthor(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	//跳转到修改密码的页面
	@RequestMapping(value="goModifyPass")
	public ModelAndView goModifyPass() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"goModifyPass");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.addObject("pd", pd);
		mv.setViewName("system/member/modifyPassword");
		return mv;
	}
	
	//修改用户密码
	@RequestMapping(value="modifyPass")
	public ModelAndView modifyPass() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"goModifyPass");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//Encrypt.MD5ByKey(KEYDATA[1]);	//登录过来的密码
		String password = pd.getString("passWord");
		pd.put("password", Encrypt.MD5ByKey(password));
		memberService.editPassWord(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
		}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Member");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		String keyType = pd.getString("keyType");				//关键词检索类型
		if(null != keyType && !"".equals(keyType)){
			pd.put("keyType", keyType);
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
		}
		
		String subTime = pd.getString("SubTime");				//关注时间检索
		if(null !=subTime && !"".equals(subTime)){
			pd.put("subTime", subTime.trim()+" 00:00:00");
		}
		
		String subEndTime = pd.getString("subEndTime");			//关注结束时间
		if(null !=subEndTime && !"".equals(subEndTime)){
			pd.put("subEndTime", subEndTime.trim()+" 23:59:59");
		}
		
		//查询列表的用户类型;
		String memberType = pd.getString("memberType");
		pd.put("memberType", memberType);//1作者;2普通用户
		page.setPd(pd);
		List<PageData>	varList = memberService.list(page);	//列出Member列表
		mv.setViewName("system/member/member_list");//作者列表页面
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/member/member_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = memberService.findById(pd);	//根据ID读取
		mv.setViewName("system/member/member_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	
	
	/**
	 * 去增加抽奖次数页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditLotterNum")
	public ModelAndView goEditLotterNum() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/member/member_lottery_edit");
		mv.addObject("msg", "editLotterNum");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 增加抽奖次数
	 * 
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/editLotterNum")
//	public ModelAndView editLotterNum() throws Exception {
//		logBefore(logger, Jurisdiction.getUsername() + "审核editAudit");
//		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
//			return null;
//		} // 校验权限
//		ModelAndView mv = this.getModelAndView();
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		memberService.editByLotteryNum(pd);
//		mv.addObject("msg", "success");
//		mv.setViewName("save_result");
//		return mv;
//	}
	
	
		/**增加抽奖次数
		 * @param
		 * @throws Exception
		 */
		@RequestMapping(value="/editLotterNum")
		@ResponseBody
		public Object editLotterNum() throws Exception{
			logBefore(logger, Jurisdiction.getUsername()+"增加抽奖次数");
			if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
			PageData pd = new PageData();		
			Map<String,Object> map = new HashMap<String,Object>();
			pd = this.getPageData();
			memberService.editByLotteryNum(pd);
			map.put("type", 1);
			map.put("msg", "成功");
			return AppUtil.returnObject(pd, map);
		}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Member");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			memberService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出Member到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("用户id");	//1
		titles.add("用户类型");	//2
		titles.add("OPENID");	//3
		titles.add("微信昵称");	//4
		titles.add("性别");	//5
		titles.add("省份");	//8
		titles.add("头像");	//10
		titles.add("关注时间");	//11
		titles.add("上次登录ip");	//20
		titles.add("最近登录时间");	//21
		titles.add("历史收益");	//22
		titles.add("可结算金额");	//23
		titles.add("软文作者浏览量收益比例");	//25
		titles.add("软文作者点赞收益比例");	//26
		titles.add("软文作者获取公众号关注收益比例");	//27
		titles.add("软文作者预订消费成功收益比例");	//28
		dataMap.put("titles", titles);
		pd.put("memberType", "");//用户类型为作者的
		List<PageData> varOList = memberService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("MEMBERID"));	    //1
			int memberType = Convert.strToInt(varOList.get(i).get("MEMBERTYPE")+"", 1);
			vpd.put("var2", memberType==1?"作者":"普通用户");	//2
			vpd.put("var3", varOList.get(i).getString("OPENID"));	    //3
			vpd.put("var4", varOList.get(i).getString("WECHATNAME"));	    //4
			int sex = Convert.strToInt(varOList.get(i).get("SEX")+"", 1);
			vpd.put("var5", sex==1?"男":(sex==2?"女":""));	//5
			vpd.put("var6", varOList.get(i).getString("PROVINCE"));	    //8
			vpd.put("var7", varOList.get(i).getString("HEADIMGURL"));	    //10
			System.out.println(varOList.get(i).get("SUBSCRIBETIME")+"");
			vpd.put("var8", varOList.get(i).get("SUBSCRIBETIME")+"");	    //11
			vpd.put("var9", varOList.get(i).getString("LASTIP"));	    //20
			vpd.put("var10", varOList.get(i).get("LASTDATE")+"");	    //21
			vpd.put("var11", varOList.get(i).get("REVENUEMONEY").toString());	//22
			vpd.put("var12", varOList.get(i).get("BALANCEMONEY").toString());	//23
			vpd.put("var13", varOList.get(i).get("REVENUEPV").toString());	//25
			vpd.put("var14", varOList.get(i).get("REVENUELIKE").toString());	//26
			vpd.put("var15", varOList.get(i).get("REVENUESUB").toString());	//27
			vpd.put("var16", varOList.get(i).get("REVENUECON").toString());	//28
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
