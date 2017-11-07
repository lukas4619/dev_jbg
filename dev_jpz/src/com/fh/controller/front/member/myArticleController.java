package com.fh.controller.front.member;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
import com.fh.service.information.images.ImagesManager;
import com.fh.service.system.article.ArticleManager;
import com.fh.service.system.articleaudit.ArticleauditManager;
import com.fh.service.system.member.MemberManager;
import com.fh.service.system.memberlog.MemberLogManager;
import com.fh.service.system.notelog.NoteLogManager;
import com.fh.service.system.revenue.RevenueManager;
import com.fh.service.system.states.StatesManager;
import com.fh.service.system.types.TypesManager;
import com.fh.sms.TaoBaoDaYu;
import com.fh.util.AppUtil;
import com.fh.util.Constants;
import com.fh.util.Convert;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import com.fh.util.PublicUtil;
import com.fh.util.Tools;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.gongzhong.TemplateMessage;
import com.fh.wechat.gongzhong.vo.message.template.Template;
import com.fh.wechat.pay.Sha1Util;

/**
 * 说明：我的软文记录 创建人：Lukas 18923798379 创建时间：2016-08-18
 */
@Controller
@RequestMapping(value = "/myArticle")
public class myArticleController extends BaseController {

	@Resource(name = "articleService")
	private ArticleManager articleService;
	@Resource(name = "imagesService")
	private ImagesManager imagesService;
	@Resource(name = "articleauditService")
	private ArticleauditManager articleauditService;
	@Resource(name = "statesService")
	private StatesManager statesService;
	@Resource(name = "typesService")
	private TypesManager typesService;

	/**
	 * 去发布软文
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/articleAdd")
	public ModelAndView articleAdd() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		String appId = GongZhongService.appId;
		String timestamp = Sha1Util.getTimeStamp();
		String nonceStr = Sha1Util.getNonceStr();
		String signature = createSignature(timestamp, nonceStr,
				getRequertNowUrl());
		pd.put("signature", signature);
		pd.put("appId", appId);
		pd.put("timestamp", timestamp);
		pd.put("nonceStr", nonceStr);
		mv.setViewName("front/article/myarticle_add");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 提交软文
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public Object save() throws Exception {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		String ARTICLEID = get32UUID();// 32位标识
		pd.put("ARTICLEID", ARTICLEID);
		pd.put("ARTICLETYPEID", 13);// 软文类型，默认
		String TITLE = "";
		if (Tools.isEmpty(pd.get("TITLE") + "")) {
			map.put("type", -1);
			map.put("msg", "请输入标题！");
			return AppUtil.returnObject(pd, map);
		}
		TITLE = pd.get("TITLE") + "";
		String AUTHORID = "";// 作者标识
		if (Tools.isEmpty(getMemberId()) || getMemberType() != 1) {
			map.put("type", -1);
			map.put("msg", "网络繁忙，请稍后再试！");
			return AppUtil.returnObject(pd, map);
		}
		AUTHORID = getMemberId();
		pd.put("AUTHORID", AUTHORID);// 作者标识
		String AUTHOR = getMemberName();// 作者名称
		pd.put("AUTHOR", AUTHOR);// 作者名称
		if (Tools.isEmpty(pd.get("DIGEST") + "")) {
			map.put("type", -1);
			map.put("msg", "请输入描述！");
			return AppUtil.returnObject(pd, map);
		}
		if (Tools.isEmpty(pd.get("CONTENT") + "")) {
			map.put("type", -1);
			map.put("msg", "请输入正文！");
			return AppUtil.returnObject(pd, map);
		}
		if (Tools.isEmpty(pd.get("imglist") + "")) {
			map.put("type", -1);
			map.put("msg", "请上传图片！");
			return AppUtil.returnObject(pd, map);
		}
		String imglist[] = pd.getString("imglist").split(",");
		if (imglist.length > 4) {
			map.put("type", -1);
			map.put("msg", "最多上传4张图片！");
			return AppUtil.returnObject(pd, map);
		}
		List<PageData> lp = new ArrayList<PageData>();// 图片数据集合
		try {
			for (int i = 0; i < imglist.length; i++) {
				PageData ip = new PageData();
				String mediaId = imglist[i];
				String IMGPATH = getPath() + "/uploadFiles/uploadImgs/";// 物理路径
				String imgUrl = "";// 图片路径
				logger.info("mediaId----------------------" + mediaId);
				logger.info("IMGPATH物理路径----------------------" + IMGPATH);
				logger.info("imgUrl图片路径----------------------" + imgUrl);
				logger.info("--------开始调用下载接口--------------");
				IMGPATH = GongZhongService.downloadMedia(mediaId, IMGPATH);
				if(Tools.isEmpty(IMGPATH)){
					continue;
				}
				logger.info("--------结束调用下载接口--------------");
				logger.info("返回物理路径----------------------" + IMGPATH);
				imgUrl = IMGPATH.substring(IMGPATH.lastIndexOf("/") + 1,IMGPATH.length());
				
				logger.info("实际保存图片路径----------------------" + imgUrl);
				if (i == 0) {
					pd.put("THUMB_MEDIA_ID", mediaId);// 封面图片上传微信标识
					pd.put("ARTICLEIMG", imgUrl);// 封面图片虚拟路径
					pd.put("THUMB_CREATED_AT", "");// 封面图片上传微信时间戳
				}
				ip.put("IMGID", get32UUID());
				ip.put("IMGTYPE", 10);
				ip.put("IMGTITLE", TITLE);
				ip.put("IMGDESCRIPTION", "");
				ip.put("IMGPATH", imgUrl);
				ip.put("IMGGROUPNAME", "");
				ip.put("MASTERID", ARTICLEID);
				ip.put("MEDIA_ID", mediaId);
				ip.put("ISUPLOAD", 1);
				ip.put("IMGSORT", 0);
				ip.put("IMGREMARK", "");
				ip.put("CREATEDATE", new Date());
				lp.add(ip);
			}
			if(lp==null || lp.size()<=0){
				map.put("type", -1);
				map.put("msg", "图片上传异常！");
				return AppUtil.returnObject(pd, map);
			}
			if(lp.size()>4){
				map.put("type", -1);
				map.put("msg", "最多上传4张图片！");
				return AppUtil.returnObject(pd, map);
			}

			pd.put("SHOW_COVER_PIC", 1);// 是否显示封面，默认显示
			pd.put("CONTENT_SOURCE_URL", getRequertUrl() + "news/index.do?id="
					+ ARTICLEID);// 软文详情地址
			pd.put("ARTICLEPV", 0);
			pd.put("ARTICLELIKE", 0);
			pd.put("SHARNUMBER", 0);
			pd.put("ARTICLESTATEID", 17);// 默认软文状态
			pd.put("PUSHNUMBER", 0);
			pd.put("SORT", 0);
			pd.put("ARTICLEURL", getRequertUrl() + "news/index.do?id="
					+ ARTICLEID);
			pd.put("CREATEDATE", new Date());
			pd.put("REVENUEPV", 0.00);
			pd.put("REVENUELIKE", 0.00);
			pd.put("REVENUESUB", 0.00);
			pd.put("REVENUECON", 0.00);
			pd.put("REVENUECONM", 0.00);
			pd.put("REVENUESUBM", 0.00);
			pd.put("REVENUEPVM", 0.00);
			PageData ad = new PageData();// 进度数据
			ad.put("DETAILS", "审核中");
			ad.put("CREATEDATE", new Date());
			ad.put("OPERATIONNAME", getMemberName());
			ad.put("ARTICLEID", ARTICLEID);
			articleService.authorInsert(pd, ad, lp);
		} catch (Exception e) {
			logger.info("发布软文发生异常：" + e.toString());
			e.printStackTrace();
			map.put("type", -1);
			map.put("msg", "网络繁忙请稍后再试！");
		}
		// List<Template> templates = new ArrayList<Template>();
		// templates = TemplateMessage.setTemplate(templates, "#173177",
		// "first",getWeChatName()+",您已成功预订套餐："+PRONAME+"，请尽快付款。");
		// templates = TemplateMessage.setTemplate(templates, "#173177",
		// "productType",PROCLASSNAME);
		// templates = TemplateMessage.setTemplate(templates, "#173177",
		// "name",PRONAME);
		// templates = TemplateMessage.setTemplate(templates, "#173177",
		// "number","1");
		// templates = TemplateMessage.setTemplate(templates, "#173177",
		// "expDate",Convert.dateToStr(VALIDITYDATE, "yyyy-MM-dd",
		// "1900-01-01"));
		// String url
		// =getRequertUrl()+"myReservation/goDetails?id="+RESERVATIONID;
		// TemplateMessage.sendTemplateMessage(getWeChatOpenId(),
		// Constants.PRODUCT_RESERVE_TEMPLATE_ID, url,
		// Constants.PRODUCT_RESERVE_TOPCOLOR, templates);
		map.put("type", 1);
		map.put("msg", "成功！");
		map.put("data", ARTICLEID);
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 去发布提交完成页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/articleAddFinish")
	public ModelAndView articleAddFinish() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String ARTICLEID = "";
		if (!Tools.isEmpty(pd.get("id") + "")) {
			ARTICLEID = pd.getString("id");
		}
		pd.put("ARTICLEID", ARTICLEID);
		pd = articleService.findById(pd);
		mv.addObject("pd", pd);
		mv.setViewName("front/article/save_result");
		return mv;
	}

	/**
	 * 首页
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("front/article/myarticle_list");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 软文分页
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/listPage")
	@ResponseBody
	public Object listPage() throws Exception {
		Page page = new Page();
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		List<PageData> varList = new ArrayList<PageData>();
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
		if (Tools.isEmpty(getMemberId())) {
			map.put("type", 1);
			map.put("msg", "成功！");
			map.put("data", varList);
			return AppUtil.returnObject(pd, map);
		}
		pd.put("authorId", getMemberId());
		pd.put("articleTypeID",
				Convert.strToInt(pd.get("articleTypeID") + "", -1));
		pd.put("articleStateID",
				Convert.strToInt(pd.get("articleStateID") + "", -1));
		int CurrentPage = 1;// 当前页
		if (Tools.notEmpty(pd.get("CurrentPage") + "")) {
			CurrentPage = Convert.strToInt(pd.get("CurrentPage") + "",
					CurrentPage);
		}
		page.setCurrentPage(CurrentPage);// 设置当前页
		int ShowCount = 10;
		if (Tools.notEmpty(pd.get("ShowCount") + "")) {
			ShowCount = Convert.strToInt(pd.get("ShowCount") + "", ShowCount);
		}
		page.setShowCount(ShowCount);
		page.setPd(pd);
		varList = articleService.list(page); // 查询当前用户所有预订记录
		map.put("type", 1);
		map.put("msg", "成功！");
		map.put("data", varList);
		map.put("totalResult", page.getTotalResult());// 总条数
		map.put("totalPage", page.getTotalPage());// 总页数
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 去详情页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goDetails")
	public ModelAndView goDetails() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String ARTICLEID = "";
		if (!Tools.isEmpty(pd.get("id") + "")) {
			ARTICLEID = pd.getString("id");
		}
		pd.put("ARTICLEID", ARTICLEID);
		pd = articleService.findByViewId(pd); // 根据ID读取
		PageData p = new PageData();
		p.put("articleID", ARTICLEID);
		List<PageData> varList = articleauditService.listAll(p);
		p = new PageData();
		p.put("masterID", ARTICLEID);
		List<PageData> imgList = imagesService.listAll(p);
		mv.setViewName("front/article/myarticle_detail");
		mv.addObject("pd", pd);
		mv.addObject("varList", varList);
		mv.addObject("imgList", imgList);
		return mv;
	}

	/**
	 * 获取签名 用于页面wx.config注入
	 * 
	 * @param timestamp
	 *            Sha1Util.getTimeStamp()
	 * @param nonce_str
	 *            Sha1Util.getNonceStr()
	 * @param url
	 *            当前网页的URL不包含#及其后面部分(参数需要带上,必须是完整的URL)
	 * @return "" 表示获取签名出现异常
	 * @throws Exception
	 */
	public String createSignature(String timestamp, String nonce_str, String url)
			throws Exception {
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		String jsapi_ticket = GongZhongService.getTicket();
		if (Tools.isEmpty(jsapi_ticket)) {
			logger.info("获取jsapi_ticket异常");
			return "";
		}
		if (Tools.isEmpty(nonce_str)) {
			logger.info("nonce_str参数不能为空");
			return "";
		}
		signParams.put("nonce_str", nonce_str);
		signParams.put("jsapi_ticket", jsapi_ticket);
		if (Tools.isEmpty(timestamp)) {
			logger.info("timestamp参数不能为空");
			return "";
		}
		signParams.put("timestamp", timestamp);
		if (Tools.isEmpty(url)) {
			logger.info("url参数不能为空");
			return "";
		}
		signParams.put("url", url);
		String signature = "";
		try {
			signature = Sha1Util.getSignature(signParams);
		} catch (IOException e) {
			logger.info("wx.config签名生成异常：" + e.getMessage());
			e.printStackTrace();
		}
		if (signature.equals("false")) {
			return "";
		}
		return signature;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,
				true));
	}
}
