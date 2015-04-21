package com.pulsarang.mom.receiver;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.receiver.EventPushResult;
import com.pulsarang.infra.mom.receiver.EventReceiver;
import com.pulsarang.infra.util.ApiUtils;

/**
 * 
 * @author pulsarang
 */
@Controller
@RequestMapping("/receiver")
public class EventReceiverServlet {
	@Autowired
	private EventReceiver eventReceiver;

	@RequestMapping("push")
	public void push(@ModelAttribute Event event, HttpServletResponse response) throws IOException {
		EventPushResult pushResult = eventReceiver.push(event);
		
		ApiUtils.writeMapModel(response, pushResult);
	}
}
