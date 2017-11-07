package com.fh.controller.app.appuser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.controller.base.BaseController;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.createqr.CreateQrManager;
import com.fh.util.AppUtil;
import com.fh.util.PageData;
import com.fh.util.Tools;
import com.fh.util.security.Encrypt;
import com.fh.wechat.gongzhong.GongZhongService;
import com.fh.wechat.pay.TenpayUtil;


/**@author lukas 414024003@qq.com
 *  暂未使用
  * 会员-接口类 
  * 相关参数协议：
  * 00	请求失败
  * 01	请求成功
  * 02	返回空值
  * 03	请求协议参数不完整    
  * 04  用户名或密码错误
  * 05  FKEY验证失败
 */
@Controller
@RequestMapping(value="/appuser")
public class IntAppuserController extends BaseController {
    
	@Resource(name="appuserService")
	private AppuserManager appuserService;
	
	@Resource(name="createqrService")
	private CreateQrManager createqrService;
	
	/**根据用户名获取会员信息
	 * @return
	 */
	@RequestMapping(value="/getAppuserByUm")
	@ResponseBody
	public Object getAppuserByUsernmae(){
		logBefore(logger, "根据用户名获取会员信息");
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = "00";
		try{
			if(Tools.checkKey("USERNAME", pd.getString("FKEY"))){	//检验请求key值是否合法
				if(AppUtil.checkParam("getAppuserByUsernmae", pd)){	//检查参数
					pd = appuserService.findByUsername(pd);
					map.put("pd", pd);
					result = (null == pd) ?  "02" :  "01";
				}else {
					result = "03";
				}
			}else{
				result = "05";
			}
		}catch (Exception e){
			logger.error(e.toString(), e);
		}finally{
			map.put("result", result);
			logAfter(logger);
		}
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**生成二维码
	 * @return
	 */
	@RequestMapping(value="/createTempQrcode")
	@ResponseBody
	public Object createTempQrcode(){
		Map<String,Object> map = new HashMap<String,Object>();
		String ticket ="";
		String IMGPATH = getPath() + "/uploadFiles/uploadImgs/";// 物理路径
		String imgUrl = "";// 图片路径
		try{
			//TODO 
			//1.增加二维码记录 分别记录作者微信标识，分享者微信标识（如果有的前提下）和当前文章的标识，这里的二维码为零时二维码
			
			PageData qPd = this.getPageData();
			logger.info("文章ARTICLEID:"+qPd.getString("ARTICLEID"));
			logger.info("分享者SHARERID:"+qPd.getString("SHARERID"));
			qPd.put("ARTICLEID", qPd.getString("ARTICLEID"));
			qPd.put("SHARERID", qPd.getString("SHARERID"));
			qPd.put("CREATEDATE", new Date());
			createqrService.save(qPd);
			
			//2.得到二维码记录自增标识，作为生成二维码scene_id（scene_id为32位非0整型）的参数值，该值用户识别二维码被扫描关注时的来源
			int scene_id = qPd.getInt("id");
			logger.info("scene_id:"+scene_id);
			//3.调用产生二维码接口，得到返回的ticket（ticket为微信二维码标识）
			//4.通过ticket调用下载二维码接口，并且将二维码保存地址以及ticket保存到当前的二维码记录中（回写当前二维码记录中的保存在本地的二维码地址字段(最好带上【/uploadFiles/uploadImgs/】目录，但是不能带域名)和微信二维码标识字段）
			//5.将保存在本地服务器的二维码地址在文章详情关注中显示。
			
			String imagePath = TenpayUtil.getCurrTime()+TenpayUtil.buildRandom(4);
			ticket=GongZhongService.createTempQrcode("action_info", scene_id);
			
			IMGPATH=GongZhongService.getQrcodeByTicket(IMGPATH, ticket,imagePath);
			
			logger.info("返回物理路径----------------------" + IMGPATH);
			imgUrl = IMGPATH.substring(IMGPATH.lastIndexOf("/") + 1,IMGPATH.length());
			logger.info("实际保存图片路径----------------------" + imgUrl);
			
			//查询
			qPd = createqrService.findById(qPd);
			qPd.put("CREATE_QR_ID", Encrypt.MD5ByKey(scene_id+""));
			qPd.put("TICKET", ticket);
			qPd.put("QRURL", "/uploadFiles/uploadImgs/"+imgUrl);
			//编辑保存
			createqrService.edit(qPd);
			
			map.put("imgUrl", imgUrl);
		}catch (Exception e){
			logger.error(e.toString(), e);
		}finally{
		}
		return AppUtil.returnObject(new PageData(), map);
	}

	
	@RequestMapping(value="/testService")
	public void testService()throws Exception{
		createqrService.handerAuthorMoneyAndUserBalance("28");
	}
}
	
 