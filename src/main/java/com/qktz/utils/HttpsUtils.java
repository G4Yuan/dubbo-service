package com.qktz.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
/**
 * http utils 工具
 * @author zhangxiaofeng
 * @date 二〇一六年十一月三十日 13:38:46
 */
public class HttpsUtils {

    private static Logger logger = Logger.getLogger(HttpsUtils.class);
    private static final String CHARSET = "UTF-8";
    private static enum HttpMethods {
		POST,
		GET
	}

	/**
	 * @param url
	 *            request address
	 * @param params
	 *            request parameter
	 * @throws UnsupportedEncodingException 
	 */
	public static String get(String url, Map<String, String> params) throws UnsupportedEncodingException {
		return get(url, params, CHARSET);
	}

	public static String get(String url, Map<String, String> params, String encode) throws UnsupportedEncodingException {
		return connect(url, params, HttpMethods.GET, encode, new Header[]{});
	}

	public static String get(String url, Map<String, String> params, Header[] headers) throws UnsupportedEncodingException {
		return connect(url, params, HttpMethods.GET, CHARSET, headers);
	}
	public static String post(String url, Map<String, String> params) throws UnsupportedEncodingException {
		return post(url, params, CHARSET);
	}
	
	public static String post(String url, Map<String, String> params, Header[] headers) throws UnsupportedEncodingException {
		return connect(url, params, HttpMethods.POST, CHARSET, headers);
	}
	
	public static String post(String url, Map<String, String> params, String encode) throws UnsupportedEncodingException {
		return connect(url, params, HttpMethods.POST, encode,new Header[]{});
	}

	public static String post(String url, Map<String, String> params, String encode, Cookie[] cookies) throws UnsupportedEncodingException {
		return connect(url, params, HttpMethods.POST, encode, cookies);
	}

	private static String connect(String url, Map<String, String> params, HttpMethods method, String encode,Cookie[] cookies) throws UnsupportedEncodingException {
		if(StringUtils.isBlank(url)) {
			logger.error("connect url is null");
			return null;
		}

		if(!url.startsWith("http://"))
			url = "http://" + url;
		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.useragent", "Mozilla/4.0; ca");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(1000 * 30);
		client.getHttpConnectionManager().getParams().setSoTimeout(1000 * 30);
		HttpMethod httpMethod;

		if (method.equals(HttpMethods.POST)) {
			httpMethod = getPostMethod(url, params);
		}else {
			httpMethod = getGetMethod(url, params, encode);
		}
		if(cookies!=null && cookies.length>0){
		}
		httpMethod.getParams().setContentCharset(encode);

		try {
			int statusCode = client.executeMethod(httpMethod);
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			return httpMethod.getResponseBodyAsString();
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		} finally {
			httpMethod.releaseConnection();
		}
	}

	private static String connect(String url, Map<String, String> params, HttpMethods method, String encode, Header[] headers) throws UnsupportedEncodingException {
		if(StringUtils.isBlank(url)) {
			logger.error("connect url is null");
			return null;
		}

		if(!url.startsWith("http://"))
			url = "http://" + url;
		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.useragent", "Mozilla/4.0; ca");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(1000 * 30);
		client.getHttpConnectionManager().getParams().setSoTimeout(1000 * 30);
		HttpMethod httpMethod;

		if (method.equals(HttpMethods.POST)) {
			httpMethod = getPostMethod(url, params);
			
		}else {
			httpMethod = getGetMethod(url, params, encode);
		}
		// 如果有header，则添加header
		if(headers != null && headers.length >0){
			for(Header header:headers){
				httpMethod.addRequestHeader(header);
			}
		}
		httpMethod.getParams().setContentCharset(encode);

		try {
			logger.info("connect url: " + httpMethod.getURI());
			int statusCode = client.executeMethod(httpMethod);
			logger.info("HTTP REQUEST STATUSCODE="+statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			return httpMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpMethod.releaseConnection();
		}

		return null;
	}

	private static NameValuePair[] getNameValuePairs(Map<String, String> params) {
		NameValuePair[] nameValueArray = new NameValuePair[params.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			nameValueArray[i++] = new NameValuePair(entry.getKey(), entry.getValue());
		}

		return nameValueArray;
	}

	private static HttpMethod getPostMethod(String url, Map<String, String> params) throws UnsupportedEncodingException {
		PostMethod post = new PostMethod(url);
		if(params==null) {
			return post;
		}

		post.addParameters(getNameValuePairs(params));
		post.setRequestEntity(new StringRequestEntity(new Gson().toJson(params), "applicaiton/json", CHARSET));
		return post;
	}

	private static HttpMethod getGetMethod(String url, Map<String, String> params, String encode) {
		if(params != null) {
			StringBuilder sb = new StringBuilder("?");
			for(Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append("=");
				try {
					sb.append(URLEncoder.encode(entry.getValue(), encode)).append("&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					sb.append(entry.getValue()).append("&");
				}
			}

			url += sb.substring(0, sb.length() - 1).replace("\n", "").replace("\r\n", "");
		}
		GetMethod get = new GetMethod(url);

		return get;
	}
	public static String HttpPost(String url, String json, Header[] headers) throws UnsupportedEncodingException {
		if(StringUtils.isBlank(url)) {
			logger.error("connect url is null");
			return null;
		}
		
		if(!url.startsWith("http://"))
			url = "http://" + url;
		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.useragent", "Mozilla/4.0; ca");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(1000 * 30);
		client.getHttpConnectionManager().getParams().setSoTimeout(1000 * 30);
		HttpMethod httpMethod;

		PostMethod post = new PostMethod(url);
		if(StringUtils.isNotBlank(json)){
			logger.error("params");
			post.setRequestEntity(new StringRequestEntity(json, "applicaiton/json", CHARSET));
		}

		httpMethod = post;
		
		// 如果有header，则添加header
		if(headers != null && headers.length >0){
			for(Header header:headers){
				httpMethod.addRequestHeader(header);
			}
		}
		httpMethod.getParams().setContentCharset(CHARSET);

		try {
			logger.info("connect url: " + httpMethod.getURI());
			int statusCode = client.executeMethod(httpMethod);
			logger.info("HTTP REQUEST STATUSCODE="+statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			return httpMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpMethod.releaseConnection();
		}

		return null;
	}
	/**
	 * 
	 * @param urlPath
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static Object postBody(String urlPath, String json, Map<String, String> headers) throws Exception {
		try {
			// Configure and open a connection to the site you will send the
			// request
			URL url = new URL(urlPath);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			// 设置doOutput属性为true表示将使用此urlConnection写入数据
			urlConnection.setDoOutput(true);
			Set<String> keys = headers.keySet();
			for(String item:keys){
				urlConnection.setRequestProperty(item, headers.get(item));
			}
			// 得到请求的输出流对象
			OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
			// 把数据写入请求的Body
			out.write(json);
			out.flush();
			out.close();

			// 从服务器读取响应
			InputStream inputStream = urlConnection.getInputStream();
			String encoding = urlConnection.getContentEncoding();
			String body = IOUtils.toString(inputStream, encoding);
			if (urlConnection.getResponseCode() == 200) {
				return JSONObject.parse(body);
			} else {
				throw new Exception(body);
			}
		} catch (IOException e) {
			logger.error("", e);
			throw e;
		}
	}
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		String uid = "57e4e3f12e958a00542b113b";
		String token = "eyJhbGciOiJIUzI1NiIsImV4cCI6MTQ5MDQ2NzQ5MCwiaWF0IjoxNDg2ODY3NDkwfQ.IjU3ZTRlM2YxMmU5NThhMDA1NDJiMTEzYiI.2dWxmlP7PlPiRmtU3xPminCNASVOo2OYWeB73euPXT4";
		String url = "http://api.qkzhi.com/1.1/users/me";
		Map params = new HashMap();
		params.put("session_token", token);
		
		
//		Map headers = new HashMap();
//		headers.put("Content-Type", "application/json");
		Header[] headers = new Header[]{new Header("Content-Type", "application/json")};
		String ret = post(url, params, headers);
		System.out.println(ret);
//		JSONObject ret;
//		try {
//			ret = (JSONObject) postBody(url,new Gson().toJson(params), headers);
//			System.out.println(ret.getString("objectId"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}
}
