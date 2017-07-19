package com.qktz.consts; 

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GlobalParm;
import com.qktz.model.PortfolioCreator;
import com.qktz.service.impl.IntelligentServiceImpl;
import com.qktz.service.thrift.intelligent.BusinessException;
import com.qktz.utils.tools.ZKTools;

/** 
* @ClassName: DataQueryMC 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (作者)  
* @date 2017年7月11日 下午3:23:26 
* @version V1.0 
*/
public class DataQueryMC {

	/**
	 * 日志组件
	 */
	private Logger logger = LoggerFactory.getLogger(IntelligentServiceImpl.class);
	
	//zookeeper
	Map<String, String> zkMap = ZKTools.getPrivateConfData("portfolio");
	
	
	public PortfolioCreator getBossUser(String user_object_id) throws BusinessException {
		if (zkMap == null || zkMap.size()<=0) {
			throw new BusinessException(10003,"未取到zk配置数据");
		}
		GlobalParm.initParam(true, zkMap.get("APP_ID"), zkMap.get("APP_KEY"),
				"", zkMap.get("URL_P"));
		 AVObject avObject = null;
		 PortfolioCreator creator = null;
		if (StringUtils.isNotBlank(user_object_id)) {
			AVQuery<AVObject> query = new AVQuery<AVObject>("Boss_User");
			query.whereEqualTo("user_object_id", user_object_id);
			List<AVObject> list = null;
			try {
				list = query.find();
			} catch (AVException e) {
				e.printStackTrace();
			}
			if (list == null || list.size()<=0) {
				logger.error("MC未找到该ID下的投顾人#user_object_id={}",user_object_id);
                return null;
			}
			avObject = list.get(0);
			creator = new PortfolioCreator();
			creator.setUser_name(avObject.getString("nickname"));
			creator.setUser_logo(avObject.getString("logo"));
			creator.setUser_summary(avObject.getString("summary"));
			creator.setQualificationName(avObject.getString("qualificationName"));
			creator.setQualificationNum(avObject.getString("qualificationNum"));
		    creator.setRoleType(avObject.getJSONObject("roleData").getString("roleType"));
		}	
		
		return creator;
	}
	
	public static void main(String[] args) throws AVException, BusinessException {
//		GlobalParm.initParam(true, "w4IN4CKw1h2dMXuV3y1hJqf1-gzGzoHsz",
//				"Vk8N5F6YcR4WIlwGJSI2AbT6", "FmIMLEHlCGnlzNXNWqlqnmyH","https://leancloud.cn");
//		AVQuery<AVObject> query = new AVQuery<AVObject>("TestObject");
//		List<AVObject> avObject = query.find();
//		System.out.println(avObject.get(0));
		
//		DataQueryMC mc = new DataQueryMC();
//		PortfolioCreator creator = mc.getBossUser("57e4d7e47db2a20063a5b137");
//		System.out.println(creator.getUser_logo());
	}
}
 