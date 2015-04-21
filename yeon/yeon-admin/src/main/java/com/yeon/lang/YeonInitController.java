package com.yeon.lang;

import com.yeon.lang.impl.MapClass;
import com.yeon.lang.remote.RemoteMapResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lang")
public class YeonInitController {

	@Autowired
	private RemoteMapResourceService remoteMapResourceService;

	@RequestMapping("init")
	public String init() {
		for (BuiltinClass builtinClass : BuiltinClass.values()) {
			if (remoteMapResourceService.get(builtinClass.getId()) == null) {
				MapClass typeClass = (MapClass) builtinClass.getTypeClass();
				remoteMapResourceService.add(typeClass);
			}
		}

		for (BuiltinType builtinType : BuiltinType.values()) {
			if (remoteMapResourceService.get(builtinType.getId()) == null) {
				remoteMapResourceService.add(builtinType.getTypeClass());
			}
		}

		return null;
	}
}
