package com.pulsarang.infra.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.pulsarang.core.util.CollectionJsonMapper;
import com.pulsarang.core.util.MapModel;
import com.pulsarang.core.util.MapModelReader;
import com.pulsarang.core.util.MapModelWriter;

public class ApiUtils {
	public static <T extends MapModel> void writeMapModels(HttpServletResponse response, List<T> list) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		MapModelWriter<T> jsonWriter = new MapModelWriter<T>(response.getWriter());
		jsonWriter.write(list);
		jsonWriter.close();
	}

	public static <T extends MapModel> void writeMapModel(HttpServletResponse response, T item) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		MapModelWriter<T> jsonWriter = new MapModelWriter<T>(response.getWriter());
		jsonWriter.write(item);
		jsonWriter.close();
	}

	public static void writeString(HttpServletResponse response, String value) throws IOException {
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(value);
		writer.close();
	}

	public static String writeMapModel(String path, MapModel model, int timeout) throws IOException {
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(2000);

		PostMethod method = new PostMethod(path);
		method.setParameter("jsonModel", URLEncoder.encode(CollectionJsonMapper.getInstance().toJson(model.getProperties()), "UTF8"));

		try {
			httpClient.executeMethod(method);
			InputStream is = method.getResponseBodyAsStream();
			return new BufferedReader(new InputStreamReader(is)).readLine();
		} finally {
			method.releaseConnection();
		}
	}

	public static <T extends MapModel> T readMapModel(HttpServletRequest request, Class<T> clazz) {
		String encodedJsonModel = request.getParameter("jsonModel");
		String json;
		try {
			json = URLDecoder.decode(encodedJsonModel, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		Map<String, Object> map = CollectionJsonMapper.getInstance().toMap(json);

		return MapModel.fromMap(map, clazz);
	}

	public static <T extends MapModel> T get(String remoteUrl, int timeout, Class<T> clazz) throws IOException {
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(2000);

		GetMethod method = new GetMethod(remoteUrl);
		try {
			int responseCode = httpClient.executeMethod(method);
			if (HttpStatus.SC_OK != responseCode) {
				throw new IOException("Unexpected response code.(" + responseCode + ")");
			}
			InputStream is = method.getResponseBodyAsStream();

			return new MapModelReader<T>(new InputStreamReader(is, "UTF-8"), clazz).get();
		} finally {
			method.releaseConnection();
		}
	}

	public static <T extends MapModel> List<T> list(String remoteUrl, int timeout, Class<T> clazz) throws IOException {
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);

		GetMethod method = new GetMethod(remoteUrl);
		try {
			int responseCode = httpClient.executeMethod(method);
			if (HttpStatus.SC_OK != responseCode) {
				throw new IOException("Unexpected response code.(" + responseCode + ")");
			}
			InputStream is = method.getResponseBodyAsStream();

			return new MapModelReader<T>(new InputStreamReader(is, "UTF-8"), clazz).list();
		} finally {
			method.releaseConnection();
		}
	}
}
