package com.naver.fog.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.naver.fog.ui.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.naver.fog.web.FogHandledException.HandleType;

public class ExceptionHandlerResolver implements HandlerExceptionResolver, Ordered {
	private final Logger log = LoggerFactory.getLogger(ExceptionHandlerResolver.class);

	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (ex instanceof FogHandledException) {
			ModelAndView mav = handleHandledProductServiceException(request, (FogHandledException)ex);
			if (mav != null) {
				return mav;
			}
		}

		log.error("Fail to handle request.", ex);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("error:mselleradmin/500");

		return mav;
	}

	private ModelAndView handleHandledProductServiceException(HttpServletRequest request, FogHandledException ex) {
		HandleType handleType;
		if (isAjaxRequest(request)) {
			handleType = HandleType.FAIL_MESSAGE_JSON;
		} else {
			handleType = ex.getHandleType();
		}

		ModelAndView mav;
		switch (handleType) {
			case ALERT_AND_BACK:
				mav = new ModelAndView();
				mav.setViewName("/common/alert_and_back");
				mav.getModelMap().addAttribute("__message", ex.getErrorMessage());
				break;
			case ALERT_AND_MOVE:
				mav = new ModelAndView();
				mav.setViewName("/common/alert_and_move");
				mav.getModelMap().addAttribute("__message", ex.getErrorMessage());
				mav.getModelMap().addAttribute("__move_url", getRedirectUrl(request, ex, "/"));
				break;
			case FAIL_MESSAGE_JSON:
				mav = new ModelAndView();
				Map<String, Object> data;
				if (ex.getRedirectUrl() != null) {
					data = new HashMap<String, Object>(1);
					data.put(JsonResponse.ATTR_REDIRECT_PAGE, getRedirectUrl(request, ex, null));
				} else {
					data = null;
				}

				String viewName = JsonResponse.fail(ex.getCode(), ex.getErrorMessage(), data, mav.getModelMap());
				mav.setViewName(viewName);
				break;
			case DELEGATE:
			default:
				mav = null;
		}

		if (mav != null) {
			Long userNo = ex.getUser() != null ? ex.getUser().getId() : null;
			log.info("Fail to process product service.(error:{} , target:{}, user:{})", new Object[] {ex.getCode(), ex.getTargetId(), userNo});

			return mav;
		}

		return null;
	}

	private String getRedirectUrl(HttpServletRequest request, FogHandledException ex, String defaultRedirectUrl) {
		String redirectUrl = ex.getRedirectUrl();
		if (redirectUrl == null) {
			redirectUrl = defaultRedirectUrl;
		}
		if (redirectUrl == null) {
			redirectUrl = request.getContextPath() + "/";
		} else {
			if (!redirectUrl.startsWith("http://") && !redirectUrl.startsWith(request.getContextPath())) {
				redirectUrl = request.getContextPath() + redirectUrl;
			}
		}

		return redirectUrl;
	}

	private static boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
			|| "JSONP".equalsIgnoreCase(request.getParameter("X-Requested-With"));
	}
}
