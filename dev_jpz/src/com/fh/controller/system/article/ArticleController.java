package com.fh.controller.system.article;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.information.images.ImagesManager;
import com.fh.service.system.article.ArticleManager;
import com.fh.service.system.articleaudit.ArticleauditManager;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.product.ProductManager;
import com.fh.service.system.states.StatesManager;
import com.fh.service.system.types.TypesManager;
import com.fh.util.AppUtil;
import com.fh.util.Constants;
import com.fh.util.Convert;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.gongzhong.GroupMessage;
import com.fh.wechat.gongzhong.TemplateMessage;
import com.fh.wechat.gongzhong.vo.message.group.MessageMateria;
import com.fh.wechat.gongzhong.vo.message.template.Template;

/**
 * 说明：软文管理 创建人：Lukas 18923798379 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value = "/article")
public class ArticleController extends BaseController {

	String menuUrl = "article/list.do"; // 菜单地址(权限用)
	@Resource(name = "articleService")
	private ArticleManager articleService;
	@Resource(name = "statesService")
	private StatesManager statesService;
	@Resource(name = "typesService")
	private TypesManager typesService;
	@Resource(name = "productService")
	private ProductManager productService;
	@Resource(name = "articleauditService")
	private ArticleauditManager articleauditService;
	@Resource(name = "imagesService")
	private ImagesManager imagesService;
	@Resource(name = "memberService")
	private MemberManager memberService;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "新增Article");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
			return null;
		} // 校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if (Tools.isEmpty(pd.getString("SHOW_COVER_PIC"))) {
			pd.put("SHOW_COVER_PIC", 0);
		}
		String ARTICLEID = this.get32UUID();
		pd.put("ARTICLEID", ARTICLEID); // 主键
		pd.put("CREATEDATE", new Date());
		pd.put("CONTENT", pd.get("editorValue") + "");
		pd.put("CONTENT_SOURCE_URL", getRequertUrl() + "news/index.do?id="
				+ ARTICLEID);
		pd.put("ARTICLEURL", getRequertUrl() + "news/index.do?id=" + ARTICLEID);
		pd.put("EDITDATE", new Date());
		articleService.save(pd);
		mv.addObject("msg", "新增成功");
		mv.addObject("url", "article/list.do");
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 删除
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	public void delete(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除Article");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
			return;
		} // 校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		articleService.delete(pd);
		out.write("success");
		out.close();
	}

	/**
	 * 修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView edit() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "修改Article");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			return null;
		} // 校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if (Tools.isEmpty(pd.getString("SHOW_COVER_PIC"))) {
			pd.put("SHOW_COVER_PIC", 0);
		}
		pd.put("CONTENT", pd.get("editorValue") + "");
		pd.put("EDITDATE", new Date());
		pd.put("CONTENT_SOURCE_URL",
				getRequertUrl() + "news/index.do?id=" + pd.get("ARTICLEID")
						+ "");
		pd.put("ARTICLEURL",
				getRequertUrl() + "news/index.do?id=" + pd.get("ARTICLEID")
						+ "");
		articleService.edit(pd);
		mv.addObject("msg", "编辑成功");
		mv.addObject("url", "article/list.do");
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 去软文审核进度页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditAudit")
	public ModelAndView goEditAudit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		List<PageData> stateList = new ArrayList<PageData>();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = articleService.findById(pd); // 根据ID读取
		pd.put("stateType", "4");
		stateList = statesService.listAll(pd);
		mv.addObject("stateList", stateList);
		mv.setViewName("system/article/articleaudit_edit");
		mv.addObject("msg", "editAudit");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 软文审核进度记录
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editAudit")
	public ModelAndView editAudit() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "审核editAudit");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			return null;
		} // 校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String DETAILS = pd.get("DETAILS") + "";
		String articleID = pd.getString("ARTICLEID");
		if(Tools.isEmpty(DETAILS)){
			DETAILS=Jurisdiction.getUsername()+"操作审核状态为："+pd.get("STATENAME") + "";
		}else{
			DETAILS=Jurisdiction.getUsername()+"操作审核状态为："+pd.get("STATENAME") + ",备注："+DETAILS;
		}
		pd.put("DETAILS", DETAILS);
		pd.put("CREATEDATE", new Date());
		pd.put("OPERATIONNAME", Jurisdiction.getUsername());
		PageData p = new PageData();
		p.put("ARTICLEID", articleID);
		p.put("ARTICLESTATEID", pd.getString("ARTICLESTATEID"));
		p.put("EDITDATE", new Date());
		articleService.editState(p,pd);
		pd = new PageData();
		pd.put("ARTICLEID", articleID);
		pd = articleService.findByViewId(pd);
		if(pd!=null){
			if(!getRequertUrl().contains("localhost") && !getRequertUrl().contains("ddj.java.1yg.tv")){
				List<Template> templates = new ArrayList<Template>();
				String openId = pd.get("openId") + "";
				if (Tools.notEmpty(openId)) {
					String content = pd.get("weChatName") + ",您的作品已得到管理审核。点击查看详情！";
					templates = TemplateMessage.setTemplate(templates, "#173177",
							"first", content);
					templates = TemplateMessage.setTemplate(templates, "#173177",
							"keyword1", pd.get("title") + "");
					templates = TemplateMessage.setTemplate(templates, "#173177",
							"keyword2", pd.get("stateName") + "");
					templates = TemplateMessage.setTemplate(templates, "#173177",
							"keyword3", DETAILS);
					templates = TemplateMessage.setTemplate(templates, "#173177",
							"keyword4",
							Convert.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss", "1900-01-01"));
					templates = TemplateMessage.setTemplate(templates, "#173177",
							"remark", "非常感谢您投递该作品。");
					String url = getRequertUrl() + "myArticle/goDetails?id="
							+ articleID;
					TemplateMessage.sendTemplateMessage(openId,
							Constants.PRODUCT_ARTICLE_TEMPLATE_ID, url,
							Constants.PRODUCT_RESERVE_TOPCOLOR, templates);
					mv.addObject("msg", "success");
				}
			}
			
		}
		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Page page) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "列表Article");
		// if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		// //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if (Tools.notEmpty(pd.getString("keywords"))) {
			pd.put("keywords", pd.getString("keywords"));
		}
		if (!Tools.isEmpty(pd.get("lastStart") + "")) {
			pd.put("lastStart", pd.get("lastStart") + "");
		}
		if (!Tools.isEmpty(pd.get("lastEnd") + "")) {
			pd.put("lastEnd", pd.get("lastEnd") + "");
		}
		pd.put("articleTypeID",
				Convert.strToInt(pd.get("articleTypeID") + "", -1));
		pd.put("articleStateID",
				Convert.strToInt(pd.get("articleStateID") + "", -1));
		page.setPd(pd);
		List<PageData> varList = articleService.list(page); // 列出Article列表
		List<PageData> typeList = new ArrayList<PageData>();
		List<PageData> stateList = new ArrayList<PageData>();
		pd.put("typeClass", "4");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		pd.put("stateType", "4");
		stateList = statesService.listAll(pd);
		mv.addObject("stateList", stateList);
		mv.setViewName("system/article/article_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX", Jurisdiction.getHC()); // 按钮权限
		return mv;
	}

	/**
	 * 去新增页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAdd")
	public ModelAndView goAdd() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> typeList = new ArrayList<PageData>();
		List<PageData> stateList = new ArrayList<PageData>();
		pd.put("typeClass", "4");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		pd.put("stateType", "4");
		stateList = statesService.listAll(pd);
		mv.addObject("stateList", stateList);
		pd.put("proState", 4);
		List<PageData> varOList = productService.listAll(pd);
		mv.addObject("varOList", varOList);
		mv.setViewName("system/article/article_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 加载产品列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findProList")
	public ModelAndView findProList(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if (Tools.notEmpty(pd.getString("keywords"))) {
			pd.put("keywords", pd.getString("keywords"));
		}

		if (!Tools.isEmpty(pd.get("lastStart") + "")) {
			pd.put("lastStart", pd.get("lastStart") + "");
		}
		if (!Tools.isEmpty(pd.get("lastEnd") + "")) {
			pd.put("lastEnd", pd.get("lastEnd") + "");
		}
		pd.put("proState", Convert.strToInt("15", 15));
		pd.put("proType", Convert.strToInt(pd.get("proType") + "", -1));
		page.setShowCount(5);
		page.setPd(pd);
		List<PageData> varList = productService.list(page); // 列出Product列表
		List<PageData> proTypeList = new ArrayList<PageData>();
		pd.put("typeClass", "2");
		proTypeList = typesService.listAll(pd);
		mv.addObject("proTypeList", proTypeList);
		mv.addObject("varList", varList);
		mv.setViewName("system/article/product_list");
		mv.addObject("pd", pd);
		mv.addObject("QX", Jurisdiction.getHC()); // 按钮权限
		return mv;
	}

	/**
	 * 去修改页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	public ModelAndView goEdit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = articleService.findById(pd); // 根据ID读取
		List<PageData> typeList = new ArrayList<PageData>();
		List<PageData> stateList = new ArrayList<PageData>();
		List<PageData> imgList = new ArrayList<PageData>();
		pd.put("typeClass", "4");
		typeList = typesService.listAll(pd);
		mv.addObject("typeList", typeList);
		pd.put("stateType", "4");
		stateList = statesService.listAll(pd);
		mv.addObject("stateList", stateList);
		PageData p = new PageData();
		p.put("masterID", pd.get("ARTICLEID"));
		imgList = imagesService.listAll(p);
		mv.addObject("imgList", imgList);
		mv.setViewName("system/article/article_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 批量删除
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "批量删除Article");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
			return null;
		} // 校验权限
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (null != DATA_IDS && !"".equals(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			articleService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		} else {
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 导出到excel
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel")
	public ModelAndView exportExcel() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "导出Article到excel");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
			return null;
		}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("软文类型"); // 3
		titles.add("关联产品"); // 4
		titles.add("标题"); // 5
		titles.add("作者名称"); // 9
		titles.add("描述"); // 10
		titles.add("内容"); // 11
		titles.add("备注"); // 12
		titles.add("是否显示封面"); // 13
		titles.add("原文地址"); // 14
		titles.add("软文微信标识"); // 15
		titles.add("浏览量"); // 16
		titles.add("点赞量"); // 17
		titles.add("分享量"); // 18
		titles.add("状态"); // 19
		titles.add("推送次数"); // 20
		titles.add("排序"); // 21
		titles.add("软文地址"); // 22
		titles.add("创建时间"); // 23
		titles.add("编辑时间"); // 24
		dataMap.put("titles", titles);
		List<PageData> varOList = articleService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("ID").toString()); // 1
			vpd.put("var2", varOList.get(i).getString("ARTICLEID")); // 2
			vpd.put("var3", varOList.get(i).get("ARTICLETYPEID").toString()); // 3
			vpd.put("var4", varOList.get(i).getString("PRODUCTID")); // 4
			vpd.put("var5", varOList.get(i).getString("TITLE")); // 5
			vpd.put("var6", varOList.get(i).getString("THUMB_MEDIA_ID")); // 6
			vpd.put("var7", varOList.get(i).getString("ARTICLEIMG")); // 7
			vpd.put("var8", varOList.get(i).getString("AUTHORID")); // 8
			vpd.put("var9", varOList.get(i).getString("AUTHOR")); // 9
			vpd.put("var10", varOList.get(i).getString("DIGEST")); // 10
			vpd.put("var11", varOList.get(i).getString("CONTENT")); // 11
			vpd.put("var12", varOList.get(i).getString("ARTICLEREMARK")); // 12
			vpd.put("var13", varOList.get(i).get("SHOW_COVER_PIC").toString()); // 13
			vpd.put("var14", varOList.get(i).getString("CONTENT_SOURCE_URL")); // 14
			vpd.put("var15", varOList.get(i).getString("MEDIA_ID")); // 15
			vpd.put("var16", varOList.get(i).get("ARTICLEPV").toString()); // 16
			vpd.put("var17", varOList.get(i).get("ARTICLELIKE").toString()); // 17
			vpd.put("var18", varOList.get(i).get("SHARNUMBER").toString()); // 18
			vpd.put("var19", varOList.get(i).get("ARTICLESTATEID").toString()); // 19
			vpd.put("var20", varOList.get(i).get("PUSHNUMBER").toString()); // 20
			vpd.put("var21", varOList.get(i).get("SORT").toString()); // 21
			vpd.put("var22", varOList.get(i).getString("ARTICLEURL")); // 22
			vpd.put("var23", varOList.get(i).getString("CREATEDATE")); // 23
			vpd.put("var24", varOList.get(i).getString("EDITDATE")); // 24
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	/**
	 * 单图文推送
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pushArticle")
	@ResponseBody
	public Object pushArticle() {
		logBefore(logger, Jurisdiction.getUsername() + "推送pushArticle");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			return null;
		} // 校验权限
		List<MessageMateria> messageMaterias = new ArrayList<MessageMateria>();
		MessageMateria m = new MessageMateria();
		Map<String, Object> map = new HashMap<String, Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			// 查询当前图文
			pd = articleService.findById(pd);
			String MEDIA_ID = pd.getString("MEDIA_ID"); // 当前软文微信图文标识
			String MEDIA_ID_CREATED_AT = pd.getString("MEDIA_ID_CREATED_AT");// 当前软文上传微信图文库返回时间戳
			String THUMB_MEDIA_ID = pd.getString("THUMB_MEDIA_ID"); // 当前软文封面微信标识
			String THUMB_CREATED_AT = pd.getString("THUMB_CREATED_AT");// 当前软文封面上传微信素材库返回时间戳
			m.setTitle(pd.getString("TITLE"));// 设置图文标题
			m.setAuthor(pd.getString("AUTHOR"));// 设置图文作者
			m.setDigest(pd.getString("DIGEST"));// 设置图文信息描述
			m.setContent(pd.getString("CONTENT"));// 设置图文详情
			m.setContent_source_url(pd.getString("CONTENT_SOURCE_URL"));// 设置图文信息详细页面阅读原文地址
			/**
			 * 功能描述:该处将软文封面图片上传到微信服务器采用的是临时图片素材，所以多次进行推送时需要进行校验，校验临时素材是否过期（3天有效期
			 * ），避免重复调用上传临时图片素材接口逻辑
			 */
			if (Tools.isEmpty(THUMB_MEDIA_ID)
					|| Tools.isEmpty(THUMB_CREATED_AT)
					|| DateUtil.getDaySub(THUMB_CREATED_AT) > 3) {
				// 图文封面上传情况
				String filePath = getPath() + "/uploadFiles/uploadImgs/"
						+ pd.getString("ARTICLEIMG");
				logger.info("图片物理路径：" + filePath);
				String res = GongZhongService.uploadMedia("image", filePath);// 调用微信上传图片并返回图片标识
				JSONObject j = JSONObject.fromObject(res);
				if (j.get("media_id") == null) {
					logger.info("上传图文图片素材到微信服务器异常，错误代码(errcode):"
							+ j.get("errcode"));
					logger.info("上传图文图片素材到微信服务器异常，错误消息(errmsg):"
							+ j.get("errmsg"));
					map.put("type", -1);
					map.put("msg", "上传图文图片失败");
					return AppUtil.returnObject(pd, map);
				}
				m.setThumb_media_id(j.get("media_id") + "");// 设置图文微信标识
				pd.put("THUMB_MEDIA_ID", m.getThumb_media_id());// 更新当前图文封面标识
				pd.put("THUMB_CREATED_AT", j.get("created_at").toString());// 更新当前软文封面上传微信素材库返回时间戳
			} else {
				m.setThumb_media_id(THUMB_MEDIA_ID);// 设置图文微信标识
			}
			/**
			 * 上传当前图文返回的微信标识
			 */
			String media_id = "";
			messageMaterias.add(m);// 封装图文内容
			// 验证当前软文是否上传微信服务器
			/**
			 * 功能描述:该处将软文上传到微信服务器采用的是临时图文素材，所以多次进行推送时需要进行校验，校验临时素材是否过期（3天有效期），
			 * 避免重复调用上传临时图文素材接口逻辑
			 */
			if (Tools.isEmpty(MEDIA_ID) || Tools.isEmpty(MEDIA_ID_CREATED_AT)
					|| DateUtil.getDaySub(MEDIA_ID_CREATED_AT) > 3) {
				// 调用微信接口上传图文并返回图文标识
				String resultStr = GroupMessage.uploadNews(messageMaterias);
				JSONObject j = JSONObject.fromObject(resultStr);
				if (j.get("media_id") == null) {
					logger.info("上传单图文到微信服务器异常，错误代码(errcode):"
							+ j.get("errcode"));
					logger.info("上传单图文到微信服务器异常，错误消息(errmsg):" + j.get("errmsg"));
					map.put("type", -1);
					map.put("msg", "上传图文失败");
					return AppUtil.returnObject(pd, map);
				}
				media_id = j.get("media_id").toString();// 微信接口上传图文并返回图文标识
				pd.put("MEDIA_ID", media_id);// 更新当前软文的微信标识
				pd.put("MEDIA_ID_CREATED_AT", j.get("created_at").toString());// 更新当前软文上传标识返回时间戳
			} else {
				media_id = MEDIA_ID;
			}
			pd.put("PUSHNUMBER",
					Convert.strToInt(pd.get("PUSHNUMBER") + "", 0) + 1);// 更新推送次数
			articleService.editPush(pd);// 更新当前软文
			pd = new PageData();
			List<PageData> openList = new ArrayList<PageData>();
			pd.put("subscribe", 1);
			openList = memberService.listGroupId(pd);// 查询当前微信用户分组
			if (openList.size() > 0) {
				for (int i = 0; i < openList.size(); i++) {
					String groupId = "";
					if (openList.get(i).get("groupId") != null) {
						// 调用微信图文群发接口
						/**
						 * 描述：调用图文群发接口，如果关注用户数量太多（最多10000），那么则调用按关注用户分组进行发送，
						 * 假设没有分组用户则需要用队列进行调用
						 */
						groupId = openList.get(i).get("groupId") + "";
						String resultJosin = GroupMessage.sendNewsMessage(
								groupId, media_id);// 调用按分组推送图文
						JSONObject jsoin = JSONObject.fromObject(resultJosin);
						if (jsoin.get("errcode") == null
								|| !jsoin.get("errcode").toString().equals("0")) {
							logger.info("推送单图文异常，错误代码(errcode):"
									+ jsoin.get("errcode"));
							logger.info("推送单图文异常，错误消息(errmsg):"
									+ jsoin.get("errmsg"));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.info("推送单图文运行异常:" + e.getMessage());
			map.put("type", -1);
			map.put("msg", "推送失败");
			e.printStackTrace();
			return AppUtil.returnObject(pd, map);
		}
		map.put("type", 1);
		map.put("msg", "推送成功");
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 多图文推送
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pushMPArticle")
	@ResponseBody
	public Object pushMPArticle() {
		logBefore(logger, Jurisdiction.getUsername() + "多图文推送pushArticle");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			return null;
		} // 校验权限
		List<MessageMateria> messageMaterias = new ArrayList<MessageMateria>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<PageData> pdList = new ArrayList<PageData>();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String DATA_IDS = pd.getString("DATA_IDS");
			if (null == DATA_IDS && "".equals(DATA_IDS)) {
				map.put("type", -1);
				map.put("msg", "请选择需要推送的软文!");
				return AppUtil.returnObject(pd, map);
			}
			pdList = articleService.listByIds(pd);
			if (pdList.size() <= 0 || pdList.size() > 8) {
				map.put("type", -1);
				map.put("msg", "最多推送8篇软文!");
				return AppUtil.returnObject(pd, map);
			}
			for (int i = 0; i < pdList.size(); i++) {
				PageData vpd = new PageData();
				MessageMateria m = new MessageMateria();// 多图文实体
				vpd.put("ARTICLEID", pdList.get(i).get("ARTICLEID") + "");// 32位对外标识
				vpd.put("THUMB_MEDIA_ID", pdList.get(i).get("THUMB_MEDIA_ID")
						+ "");// 软文中封面图片上传到微信服务器标识
				vpd.put("THUMB_CREATED_AT",
						pdList.get(i).get("THUMB_CREATED_AT") + "");// 软文中封面图片上传到微信服务器返回时间戳
				vpd.put("MEDIA_ID", pdList.get(i).get("MEDIA_ID") + "");// 软文上传到微信服务器标识
				vpd.put("MEDIA_ID_CREATED_AT",
						pdList.get(i).get("MEDIA_ID_CREATED_AT") + "");// 软文上传到微信服务器返回时间戳
				String THUMB_MEDIA_ID = pdList.get(i).get("THUMB_MEDIA_ID")
						+ ""; // 当前软文封面微信标识
				String THUMB_CREATED_AT = pdList.get(i).get("THUMB_CREATED_AT")
						+ "";// 当前软文封面上传微信素材库返回时间戳
				m.setTitle(pdList.get(i).get("TITLE") + "");// 设置图文标题
				m.setAuthor(pdList.get(i).get("AUTHOR") + "");// 设置图文作者
				m.setDigest(pdList.get(i).get("DIGEST") + "");// 设置图文信息描述
				m.setContent(pdList.get(i).get("CONTENT") + "");// 设置图文详情
				m.setContent_source_url(pdList.get(i).get("CONTENT_SOURCE_URL")
						+ "");// 设置图文信息详细页面阅读原文地址
				/**
				 * 功能描述:该处将软文封面图片上传到微信服务器采用的是临时图片素材，所以多次进行推送时需要进行校验，校验临时素材是否过期（3
				 * 天有效期），避免重复调用上传临时图片素材接口逻辑
				 */
				if (Tools.isEmpty(THUMB_MEDIA_ID)
						|| Tools.isEmpty(THUMB_CREATED_AT)
						|| DateUtil.getDaySub(THUMB_CREATED_AT) > 3) {
					// 图文封面上传情况
					String filePath = getPath() + "/uploadFiles/uploadImgs/"
							+ pdList.get(i).get("ARTICLEIMG") + "";
					logger.info("图片物理路径：" + filePath);
					String res = GongZhongService
							.uploadMedia("image", filePath);// 调用微信上传图片并返回图片标识
					JSONObject j = JSONObject.fromObject(res);
					if (j.get("media_id") == null) {
						logger.info("上传多文图片素材到微信服务器异常，错误代码(errcode):"
								+ j.get("errcode"));
						logger.info("上传多文图片素材到微信服务器异常，错误消息(errmsg):"
								+ j.get("errmsg"));
						map.put("type", -1);
						map.put("msg", "上传多图文图片失败");
						return AppUtil.returnObject(pd, map);
					}
					m.setThumb_media_id(j.get("media_id") + "");// 设置图文微信标识
					vpd.put("THUMB_MEDIA_ID", m.getThumb_media_id());// 更新当前图文封面标识
					vpd.put("THUMB_CREATED_AT", j.get("created_at") + "");// 更新当前软文封面上传微信素材库返回时间戳
				} else {
					m.setThumb_media_id(THUMB_MEDIA_ID);// 设置图文微信标识
				}
				messageMaterias.add(m);// 封装图文内容
				vpd.put("PUSHNUMBER",
						Convert.strToInt(pdList.get(i).get("PUSHNUMBER")
								.toString(), 0) + 1);// 更新推送次数
				articleService.editPush(vpd);// 更新当前软文
			}
			/**
			 * 上传当前图文返回的微信标识
			 */
			String media_id = "";
			// 调用微信接口上传图文并返回图文标识
			String resultStr = GroupMessage.uploadNews(messageMaterias);
			JSONObject j = JSONObject.fromObject(resultStr);
			if (j.get("media_id") == null) {
				logger.info("上传多图文到微信服务器异常，错误代码(errcode):" + j.get("errcode"));
				logger.info("上传多图文到微信服务器异常，错误消息(errmsg):" + j.get("errmsg"));
				map.put("type", -1);
				map.put("msg", "上传多图文失败");
				return AppUtil.returnObject(pd, map);
			}
			media_id = j.get("media_id").toString();// 微信接口上传图文并返回图文标识
			pd = new PageData();
			List<PageData> openList = new ArrayList<PageData>();
			pd.put("subscribe", 1);
			openList = memberService.listGroupId(pd);// 查询当前微信用户分组
			if (openList.size() > 0) {
				for (int i = 0; i < openList.size(); i++) {
					String groupId = "";
					if (openList.get(i).get("groupId") != null) {
						// 调用微信图文群发接口
						/**
						 * 描述：调用图文群发接口，如果关注用户数量太多（最多10000），那么则调用按关注用户分组进行发送，
						 * 假设没有分组用户则需要用队列进行调用
						 */
						groupId = openList.get(i).get("groupId") + "";
						String resultJosin = GroupMessage.sendNewsMessage(
								groupId, media_id);// 调用按分组推送图文
						JSONObject jsoin = JSONObject.fromObject(resultJosin);
						if (jsoin.get("errcode") == null
								|| !jsoin.get("errcode").toString().equals("0")) {
							logger.info("推送单图文异常，错误代码(errcode):"
									+ jsoin.get("errcode"));
							logger.info("推送单图文异常，错误消息(errmsg):"
									+ jsoin.get("errmsg"));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.info("推送多图文运行异常:" + e.getMessage());
			map.put("type", -1);
			map.put("msg", "推送失败");
			e.printStackTrace();
			return AppUtil.returnObject(pd, map);
		}
		map.put("type", 1);
		map.put("msg", "推送成功");
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 去软文推送预览页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goPreView")
	public ModelAndView goPreView() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/article/article_PreView");
		mv.addObject("msg", "preViewArticle");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 软文推送预览
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/preViewArticle")
	@ResponseBody
	public Object preViewArticle() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "预览图文推送preViewArticle");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			return null;
		} // 校验权限
		List<MessageMateria> messageMaterias = new ArrayList<MessageMateria>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<PageData> pdList = new ArrayList<PageData>();
		PageData pd = new PageData();
		pd = this.getPageData();
		if (pd.get("OPENID") == null) {
			map.put("type", -1);
			map.put("msg", "请输入需要推送预览软文的OPENID!");
			return AppUtil.returnObject(pd, map);
		}
		String openId = pd.get("OPENID").toString().trim();
		if (Tools.isEmpty(openId)) {
			map.put("type", -1);
			map.put("msg", "请输入需要推送预览软文的OPENID!");
			return AppUtil.returnObject(pd, map);
		}
		try {
			String DATA_IDS = pd.getString("DATA_IDS");
			if (null == DATA_IDS && "".equals(DATA_IDS)) {
				map.put("type", -1);
				map.put("msg", "请选择需要推送的软文!");
				return AppUtil.returnObject(pd, map);
			}
			pdList = articleService.listByIds(pd);
			if (pdList.size() <= 0 || pdList.size() > 8) {
				map.put("type", -1);
				map.put("msg", "最多推送8篇软文!");
				return AppUtil.returnObject(pd, map);
			}
			for (int i = 0; i < pdList.size(); i++) {
				PageData vpd = new PageData();
				MessageMateria m = new MessageMateria();// 多图文实体
				vpd.put("ARTICLEID", pdList.get(i).get("ARTICLEID") + "");// 32位对外标识
				vpd.put("THUMB_MEDIA_ID", pdList.get(i).get("THUMB_MEDIA_ID")
						+ "");// 软文中封面图片上传到微信服务器标识
				vpd.put("THUMB_CREATED_AT",
						pdList.get(i).get("THUMB_CREATED_AT") + "");// 软文中封面图片上传到微信服务器返回时间戳
				vpd.put("MEDIA_ID", pdList.get(i).get("MEDIA_ID") + "");// 软文上传到微信服务器标识
				vpd.put("MEDIA_ID_CREATED_AT",
						pdList.get(i).get("MEDIA_ID_CREATED_AT") + "");// 软文上传到微信服务器返回时间戳
				String THUMB_MEDIA_ID = pdList.get(i).get("THUMB_MEDIA_ID")
						+ ""; // 当前软文封面微信标识
				String THUMB_CREATED_AT = pdList.get(i).get("THUMB_CREATED_AT")
						+ "";// 当前软文封面上传微信素材库返回时间戳
				m.setTitle(pdList.get(i).get("TITLE") + "");// 设置图文标题
				m.setAuthor(pdList.get(i).get("AUTHOR") + "");// 设置图文作者
				m.setDigest(pdList.get(i).get("DIGEST") + "");// 设置图文信息描述
				m.setContent(pdList.get(i).get("CONTENT") + "");// 设置图文详情
				m.setContent_source_url(pdList.get(i).get("CONTENT_SOURCE_URL")
						+ "");// 设置图文信息详细页面阅读原文地址
				/**
				 * 功能描述:该处将软文封面图片上传到微信服务器采用的是临时图片素材，所以多次进行推送时需要进行校验，校验临时素材是否过期（3
				 * 天有效期），避免重复调用上传临时图片素材接口逻辑
				 */
				if (Tools.isEmpty(THUMB_MEDIA_ID)
						|| Tools.isEmpty(THUMB_CREATED_AT)
						|| DateUtil.getDaySub(THUMB_CREATED_AT) > 3) {
					// 图文封面上传情况
					String filePath = getPath() + "/uploadFiles/uploadImgs/"
							+ pdList.get(i).get("ARTICLEIMG") + "";
					logger.info("图片物理路径：" + filePath);
					String res = GongZhongService
							.uploadMedia("image", filePath);// 调用微信上传图片并返回图片标识
					JSONObject j = JSONObject.fromObject(res);
					if (j.get("media_id") == null) {
						logger.info("上传预览图文图片素材到微信服务器异常，错误代码(errcode):"
								+ j.get("errcode"));
						logger.info("上传预览图文图片素材到微信服务器异常，错误消息(errmsg):"
								+ j.get("errmsg"));
						map.put("type", -1);
						map.put("msg", "上传预览图文图片失败");
						return AppUtil.returnObject(pd, map);
					}
					m.setThumb_media_id(j.get("media_id") + "");// 设置图文微信标识
					vpd.put("THUMB_MEDIA_ID", m.getThumb_media_id());// 更新当前图文封面标识
					vpd.put("THUMB_CREATED_AT", j.get("created_at") + "");// 更新当前软文封面上传微信素材库返回时间戳
				} else {
					m.setThumb_media_id(THUMB_MEDIA_ID);// 设置图文微信标识
				}
				messageMaterias.add(m);// 封装图文内容
				vpd.put("PUSHNUMBER",
						Convert.strToInt(pdList.get(i).get("PUSHNUMBER")
								.toString(), 0) + 1);// 更新推送次数
				articleService.editPush(vpd);// 更新当前软文
			}
			/**
			 * 上传当前图文返回的微信标识
			 */
			String media_id = "";
			// 调用微信接口上传图文并返回图文标识
			String resultStr = GroupMessage.uploadNews(messageMaterias);
			JSONObject j = JSONObject.fromObject(resultStr);
			if (j.get("media_id") == null) {
				logger.info("上传预览图文到微信服务器异常，错误代码(errcode):" + j.get("errcode"));
				logger.info("上传预览图文到微信服务器异常，错误消息(errmsg):" + j.get("errmsg"));
				map.put("type", -1);
				map.put("msg", "上传预览图文失败");
				return AppUtil.returnObject(pd, map);
			}
			media_id = j.get("media_id").toString();// 微信接口上传图文并返回图文标识
			// 调用微信图文预览接口
			String resultJosin = GroupMessage.sendNewsPreViewMessage(openId,
					media_id);
			JSONObject jsoin = JSONObject.fromObject(resultJosin);
			if (jsoin.get("errcode") == null
					|| !jsoin.get("errcode").toString().equals("0")) {
				logger.info("推送预览图文异常，错误代码(errcode):" + jsoin.get("errcode"));
				logger.info("推送预览图文异常，错误消息(errmsg):" + jsoin.get("errmsg"));
				map.put("type", -1);
				map.put("msg", jsoin.get("errmsg"));
				return AppUtil.returnObject(pd, map);
			}
		} catch (Exception e) {
			logger.info("推送预览图文运行异常:" + e.getMessage());
			map.put("type", -1);
			map.put("msg", "预览推送失败");
			e.printStackTrace();
			return AppUtil.returnObject(pd, map);
		}
		map.put("type", 1);
		map.put("msg", "预览推送成功");
		return AppUtil.returnObject(pd, map);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,
				true));
	}
}
