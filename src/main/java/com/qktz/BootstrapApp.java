package com.qktz;


import org.apache.log4j.Logger;

import com.qktz.mgr.DbManager;
import com.qktz.mgr.ServerManager;
import com.qktz.service.thrift.intelligent.BusinessException;
/**
 * 启动类
 * @author zhangxiaofeng
 * @date 2016年5月4日17:07:31
 * @version 1.0
 */
public class BootstrapApp {
	private static Logger logger = Logger.getLogger(BootstrapApp.class);
	public static void main(String[] args) throws BusinessException {
		String ip = "";
		String port = "";
		if(args !=null){
			if(args.length ==2){
				ip = args[0];
				port = args[1];
			}
		}
		
		boolean flag = true;
		flag = DbManager.start();
		if(!flag) logger.info("数据库启动失败");
		logger.info("数据库启动成功");
		
		ServerManager.start(ip, port);
		
	}

}
