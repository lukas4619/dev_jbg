package com.fh.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fh.util.Logger;
/**
 * 
* 类名称：MyExceptionResolver.java
* 类描述： 
* @author lukas
* 作者单位： 
* 联系方式： 414024003@qq.com
* @version 1.0
 */
public class MyExceptionResolver implements HandlerExceptionResolver{

	protected Logger logger = Logger.getLogger(this.getClass());
	
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		// TODO Auto-generated method stub
		logger.info("==============异常开始=============");
		logger.info("异常内容为："+ex.toString());
		ex.printStackTrace();
		logger.info("==============异常结束=============");
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("exception", ex.toString().replaceAll("\n", "<br/>"));
		return mv;
	}

}
