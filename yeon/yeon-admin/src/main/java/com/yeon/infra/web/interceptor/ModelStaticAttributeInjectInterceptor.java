package com.yeon.infra.web.interceptor;

import com.yeon.infra.web.SerialService;
import com.yeon.infra.web.freemarker.ResourceDirective;
import com.yeon.infra.web.freemarker.ScriptDirective;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ModelStaticAttributeInjectInterceptor implements HandlerInterceptor, InitializingBean {
	@Autowired
	private ResourceDirective resourceDirective;

	@Autowired
	private ScriptDirective scriptDirective;

	private Map<String, Object> staticAttributes;

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO 공용으로 사용할 수 있도록 리팩토링
		staticAttributes = new HashMap<String, Object>();
		staticAttributes.put("resource", resourceDirective);
		staticAttributes.put("script", scriptDirective);
		staticAttributes.put("serial", SerialService.getInstance());
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView == null || StringUtils.startsWith(modelAndView.getViewName(), "redirect:")) {
			return;
		}

		ModelMap modelMap = modelAndView.getModelMap();
		modelMap.addAttribute("y", staticAttributes);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}
}
