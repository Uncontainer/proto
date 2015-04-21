package net.me2day.async.processor.commcast;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.me2day.async.dispatcher.EventProcessContext;
import net.me2day.async.dispatcher.ReprocessableException;
import net.me2day.async.dispatcher.processor.EventProcessor;
import net.me2day.async.processor.util.ReprocessingHttpInvoker;
import net.me2day.async.util.StrUtil;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nhncorp.nconfig.util.JSONHelper;

public abstract class AbstractCommcastProcessor implements EventProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractCommcastProcessor.class);

	private static final String COMMCAST_HOST = "alpha.int.commcast.naver.com";
	private static final String METHOD_URI = "http://%s/%s";
	private static final String SEED = "e5KEgOUFxTxuqQjLDgT2nC2XL";

	private ReprocessingHttpInvoker httpInvoker = new ReprocessingHttpInvoker(10, TimeUnit.MINUTES.toMillis(1));

	private static final String QA_ID_LIST[] = {
		"thedaz", "qame01", "qame02", "qa2tc25", "qa2tc26",
		"qa1tc01_com", "qa1tc02_com", "qa1tc03_com", "qa1tc04_com", "qa1tc05_com",
		"qa1tc06_com", "qa1tc07_com", "qa1tc08_com", "qa1tc09_com", "qa1tc10_com",
		"qa1tc11_com", "qa1tc12_com", "qa1tc13_com", "qa1tc14_com", "qa1tc15_com",
		"qa1tc16_com"
		};

	@Override
	public String getConfigurationTicket() {
		return "commcastProcessor";
	}

	@Override
	public boolean skipProcessing(EventProcessContext context) {
		return true;

		// String fireUserId = context.getEvent().getEventFireUserId();
		// String fireNaverUserId = getNaverIdByMe2dayUserId(fireUserId);
		//
		// if (StringUtils.isEmpty(fireNaverUserId)) {
		// LOG.info("[COMMCAST-delete] fireUserId(" + fireUserId + ") have NO naverId.");
		// return true;
		// }
		//
		// return !ArrayUtils.contains(QA_ID_LIST, fireNaverUserId);
	}

	@Override
	public void fail(EventProcessContext context) {
		LOG.error("[COMMCAST] Fail to process event. ({})", context.getEvent().getEventId());
	}

	@Override
	public void fail(List < Object > targets, EventProcessContext context) {
		LOG.error("[COMMCAST] Fail to process event. ({}: {})", context.getEvent().getEventId(), targets);
	}

	@Override
	public void process(EventProcessContext context) throws ReprocessableException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void process(List < Object > targets, EventProcessContext context) throws ReprocessableException {
		throw new UnsupportedOperationException();
	}

	protected void invoke(HttpMethod method) throws ReprocessableException {
		httpInvoker.executeMethod(method);
	}

	protected String invokeAndGetResponseBodyAsString(HttpMethod method) throws ReprocessableException {
		return httpInvoker.executeAndGetAsString(method);
	}

	protected Map < String, Object > sendByPutMethod(Object obj, List < Object > idList, int catId, String mode,
		String eventId)
		throws ReprocessableException {
		String body = CommcastMessageUtil.makeJsonMessage(obj, idList);

		String uri = String.format("v2/service/me2day/catid/%d/event/%s", catId, eventId);
		String seedUri = SEED + uri;

		String mkey = StrUtil.getMessageDigest(seedUri);

		NameValuePair param1 = new NameValuePair("format", "json");
		NameValuePair param2 = new NameValuePair("mode", mode);
		NameValuePair[] params = new NameValuePair[] {param1, param2};

		String putUri = String.format(METHOD_URI, COMMCAST_HOST, uri);
		PutMethod p = new PutMethod(putUri);

		p.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
		p.setQueryString(params);

		RequestEntity entity;
		try {
			entity = new StringRequestEntity(body, "application/json", "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			throw new RuntimeException();
		}

		p.setRequestEntity(entity);

		p.addRequestHeader("Host", COMMCAST_HOST);
		p.addRequestHeader("Content-Type", "application/json;charset=UTF-8");
		p.addRequestHeader("Authorization", "mkey=\"" + mkey + "\"");
		try {
			p.addRequestHeader("Content-Length", Integer.toString(body.getBytes("UTF-8").length));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		String resultText = invokeAndGetResponseBodyAsString(p);

		LOG.info("[COMMCAST] resultText : " + resultText);
		return JSONHelper.jsonToMap(resultText);
	}

	protected void sendByDeleteMethod(String userId, int catId, String mode, String eventId, long createdTime)
		throws ReprocessableException {
		String uri =
			String.format("v2/service/me2day/catid/%d/event/%s?userId=%s&createdTime=%d&targetId=commcast", catId,
				eventId, userId, createdTime);

		String deleteUri = String.format(METHOD_URI, COMMCAST_HOST, uri);
		DeleteMethod m = new DeleteMethod(deleteUri);

		m.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));

		m.addRequestHeader("Host", COMMCAST_HOST);
		m.addRequestHeader("Content-Type", "application/json;charset=UTF-8");

		m.addRequestHeader("Authorization",
			"mkey=\"" + StrUtil.getMessageDigest(SEED + StringUtils.substring(m.getPath(), 1)) + "\"");

		invokeAndGetResponseBodyAsString(m);

		// LOG.info("[COMMCAST-delete] resultText : " + resultText);
		// LOG.info("[COMMCAST-delete] deleteUri : " + deleteUri);
		// LOG.info("[COMMCAST-delete] m path : " + StringUtils.substring(m.getPath(), 1));
		// LOG.info("[COMMCAST-delete] m header : " + m.getRequestHeaders()[0]);
		// LOG.info("[COMMCAST-delete] m header : " + m.getRequestHeaders()[1]);
		// LOG.info("[COMMCAST-delete] m header : " + m.getRequestHeaders()[2]);
		// LOG.info("[COMMCAST-delete] m header : " + m.getRequestHeaders()[3]);
	}
}
