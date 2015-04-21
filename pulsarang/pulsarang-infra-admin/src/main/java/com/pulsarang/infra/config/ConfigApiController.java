package com.pulsarang.infra.config;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pulsarang.infra.util.ApiUtils;

@Controller
@RequestMapping("/api/config")
public class ConfigApiController {
	@Autowired
	private ConfigService configService;

	@RequestMapping("get/{category}/{id}")
	public void get(HttpServletResponse response, @PathVariable("category") String category, @PathVariable("id") String id) throws IOException {
		ConfigEntityId configEntityId = new ConfigEntityId(category, id);
		Map<String, Object> config = configService.get(configEntityId);

		ApiUtils.writeMapModel(response, new Config(config));
	}
}
