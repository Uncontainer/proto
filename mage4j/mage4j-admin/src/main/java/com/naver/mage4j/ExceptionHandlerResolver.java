package com.naver.mage4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.naver.mage4j.core.mage.core.RedirectAndExitException;

public class ExceptionHandlerResolver implements HandlerExceptionResolver, Ordered {
	private final Logger log = LoggerFactory.getLogger(ExceptionHandlerResolver.class);

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (ex instanceof RedirectAndExitException) {
			return new ModelAndView();
		}

		log.error("Fail to handle request.", ex);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("error:mselleradmin/500");

		return mav;
	}

	private static boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
			|| "JSONP".equalsIgnoreCase(request.getParameter("X-Requested-With"));
	}
}
