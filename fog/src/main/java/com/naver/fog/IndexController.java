package com.naver.fog;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("")
public class IndexController {
	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletResponse response) throws IOException {
		return "index";
	}
}
