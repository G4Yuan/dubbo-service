package com.qktz.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;




public class HttpRequestUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);    //日志记录
    private static RequestConfig requestConfig;  
 
    /** 
     * 执行一个http/https post请求，返回请求响应的文本数据 
     *  
     * @param url       请求的URL地址 
     * @param params    请求的查询参数,可以为null 
     * @param charset   字符集 
     * @param pretty    是否美化 
     * @return          返回请求响应的文本数据 
     * @throws UnsupportedEncodingException 
     */  
    public static String doPost(String url, Map<String, String> params, Header[] headers, String charset, boolean pretty) throws UnsupportedEncodingException {  
            StringBuffer response = new StringBuffer();  
            HttpClient client = new HttpClient();  
            if(url.startsWith("https")){  
                //https请求  
                Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);  
                Protocol.registerProtocol("https", myhttps);  
            }  
            PostMethod method = new PostMethod(url);  
         // 如果有header，则添加header
    		if(headers != null && headers.length >0){
    			for(Header header:headers){
    				method.addRequestHeader(header);
    			}
    		}
            //设置参数的字符集  
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,charset);  
            //设置post数据  
            if (params != null) {  
//                //HttpMethodParams p = new HttpMethodParams();      
//                for (Map.Entry<String, String> entry : params.entrySet()) {  
//                    //p.setParameter(entry.getKey(), entry.getValue());     
//                    method.setParameter(entry.getKey(), entry.getValue());  
//                }  
//                //method.setParams(p);  
            	String josObject = JSON.toJSONString(params);
            	RequestEntity reEntity = new StringRequestEntity(josObject,"text/xml","UTF-8");
            	method.setRequestEntity(reEntity);
            }  
            try {  
                    client.executeMethod(method);  
                    if (method.getStatusCode() == HttpStatus.SC_OK) {  
                        BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));  
                        String line;  
                        while ((line = reader.readLine()) != null) {  
                            if (pretty)  
                                response.append(line).append(System.getProperty("line.separator"));  
                            else  
                                response.append(line);   
                        }  
                        reader.close();  
                    }  
            } catch (IOException e) {  
                logger.error("执行Post请求" + url + "时，发生异常！", e);  
            } finally {  
                method.releaseConnection();  
            }  
            return response.toString();  
    } 
    /** 
     * 执行一个http/https post请求，返回请求响应的文本数据 
     *  
     * @param url       请求的URL地址 
     * @param params    请求的查询参数,可以为null 
     * @param charset   字符集 
     * @param pretty    是否美化 
     * @return          返回请求响应的文本数据 
     * @throws UnsupportedEncodingException 
     */  
    public static String doPost(String url, JSON json, Header[] headers, String charset, boolean pretty) throws UnsupportedEncodingException {  
            StringBuffer response = new StringBuffer();  
            HttpClient client = new HttpClient();  
            if(url.startsWith("https")){  
                //https请求  
                Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);  
                Protocol.registerProtocol("https", myhttps);  
            }  
            PostMethod method = new PostMethod(url);  
         // 如果有header，则添加header
    		if(headers != null && headers.length >0){
    			for(Header header:headers){
    				method.addRequestHeader(header);
    			}
    		}
            //设置参数的字符集  
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,charset);  
            //设置post数据  
            if (json != null) {  
            	String jsonobject = JSON.toJSONString(json);
            	RequestEntity reEntity = new StringRequestEntity(jsonobject,"text/xml","UTF-8");
            	method.setRequestEntity(reEntity);
            }  
            try {  
                    client.executeMethod(method);  
                    if (method.getStatusCode() == HttpStatus.SC_OK) {  
                        BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));  
                        String line;  
                        while ((line = reader.readLine()) != null) {  
                            if (pretty)  
                                response.append(line).append(System.getProperty("line.separator"));  
                            else  
                                response.append(line);   
                        }  
                        reader.close();  
                    }  
            } catch (IOException e) {  
                logger.error("执行Post请求" + url + "时，发生异常！", e);  
            } finally {  
                method.releaseConnection();  
            }  
            return response.toString();  
    } 
    
    /** 
     * 执行一个http/https post请求，返回请求响应的文本数据 
     *  
     * @param url       请求的URL地址 
     * @param params    请求的查询参数,可以为null 
     * @param charset   字符集 
     * @param pretty    是否美化 
     * @return          返回请求响应的文本数据 
     * @throws UnsupportedEncodingException 
     */  
    public static String doPut(String url, Map<String, String> params, Header[] headers, String charset, boolean pretty) throws UnsupportedEncodingException {  
            StringBuffer response = new StringBuffer();  
            HttpClient client = new HttpClient();  
            if(url.startsWith("https")){  
                //https请求  
                Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);  
                Protocol.registerProtocol("https", myhttps);  
            }  
            PutMethod method = new PutMethod(url);  
         // 如果有header，则添加header
    		if(headers != null && headers.length >0){
    			for(Header header:headers){
    				method.addRequestHeader(header);
    			}
    		}
            //设置参数的字符集  
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,charset);  
            //设置post数据  
            if (params != null) {  
//                //HttpMethodParams p = new HttpMethodParams();      
//                for (Map.Entry<String, String> entry : params.entrySet()) {  
//                    //p.setParameter(entry.getKey(), entry.getValue());     
//                    method.setParameter(entry.getKey(), entry.getValue());  
//                }  
//                //method.setParams(p);  
            	String josObject = JSON.toJSONString(params);
            	RequestEntity reEntity = new StringRequestEntity(josObject,"text/xml","UTF-8");
            	method.setRequestEntity(reEntity);
            }  
            try {  
                    client.executeMethod(method);  
                    if (method.getStatusCode() == HttpStatus.SC_OK) {  
                        BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));  
                        String line;  
                        while ((line = reader.readLine()) != null) {  
                            if (pretty)  
                                response.append(line).append(System.getProperty("line.separator"));  
                            else  
                                response.append(line);   
                        }  
                        reader.close();  
                    }  
            } catch (IOException e) {  
                logger.error("执行Post请求" + url + "时，发生异常！", e);  
            } finally {  
                method.releaseConnection();  
            }  
            return response.toString();  
    } 
    
    /** 
     * 发送 POST 请求（HTTP），JSON形式 
     * @param apiUrl 
     * @param json json对象 
     * @return 
     */  
    public static String doPostJSON(String apiUrl, Object json) {  
        CloseableHttpClient httpClient = HttpClients.createDefault();  
        String httpStr = null;  
        HttpPost httpPost = new HttpPost(apiUrl);  
        CloseableHttpResponse response = null;  
  
        try {  
            httpPost.setConfig(requestConfig);  
            StringEntity stringEntity = new StringEntity(json.toString(),"UTF-8");//解决中文乱码问题  
            stringEntity.setContentEncoding("UTF-8");  
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);  
            HttpEntity entity = response.getEntity();  
            System.out.println(response.getStatusLine().getStatusCode());  
            httpStr = EntityUtils.toString(entity, "UTF-8");  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (response != null) {  
                try {  
                    EntityUtils.consume(response.getEntity());  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return httpStr;  
    } 
}
