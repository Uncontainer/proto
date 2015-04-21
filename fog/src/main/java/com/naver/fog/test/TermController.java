package com.naver.fog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/term")
public class TermController {

	@RequestMapping("fetchTerm")
	public String fetchTerm(@RequestParam("term") String term) {
		return null;
	}
}
