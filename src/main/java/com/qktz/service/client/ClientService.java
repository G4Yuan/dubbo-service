package com.qktz.service.client;

import java.net.URL;

import org.codehaus.xfire.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

/**
 * @ClassName: ClientService
 * @Description: 客户端调用
 * @author yuanwei
 * @date 2017年6月29日 下午1:25:23
 * @version V1.0
 */
public class ClientService {

	private static Logger logger = LoggerFactory.getLogger(ClientService.class);

	public ClientService() {
	}

	public JSONObject run(String url, String operation, Object object[]) {

		JSONObject json = null;
		Client client = null;
		try {
			client = new Client(new URL(url));
			client.setTimeout(60);
			Object[] results = client.invoke(operation, object);
			if (results != null) {
				String jsonstr = (String) results[0];
				// 解析
				json = JSONObject.parseObject(jsonstr);
				logger.info(">>>>>>>>同步成功, {}", json.toString());
			} else {
				logger.info(">>>>>>>>同步失败, ret={}", new Gson().toJson(results));
			}
		} catch (Exception e) {
			logger.error("系统出现异常", e);
		} finally {
			client.close();
		}
		return json;
	}
}
