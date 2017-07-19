package com.qktz.mgr;

import org.apache.log4j.Logger;
import org.rpc.framework.constants.RpcFrameConstants;
import org.rpc.framework.exceptions.RpcFrameException;
import org.rpc.framework.mgr.RpcManager;
import org.rpc.framework.thrift.RpcProcessor;
import org.rpc.framework.thrift.impl.RpcServer;
import org.rpc.framework.thrift.processor.ThriftProcessor;
import org.rpc.framework.zkregister.IServiceRegister;
import org.rpc.framework.zkregister.impl.ServiceRegister;
import org.rpc.framework.zkregister.node.ZKServiceNode;

import com.google.gson.Gson;
import com.qktz.service.impl.IntelligentServiceImpl;
import com.qktz.service.thrift.intelligent.IntelligentService;
import com.qktz.utils.kit.PropsKit;
import com.qktz.utils.kit.StringKit;

/**
 * <p>服务管理
 * @author zhangxiaofeng
 * @date 2016年5月3日16:06:52
 * @version 1.0
 */
public class ServerManager {
	private static Logger logger = Logger.getLogger(ServerManager.class);
	private static Gson gson = new Gson();
	/**
	 * 服务启动类
	 * @return
	 */
	public static boolean start(String ip_param, String port_param){
		int result = 0;
		String zkxmlDir = PropsKit.getVal("zk.servers.cfg");
		logger.info(String.format("获取的配置信息为:%s", zkxmlDir));
		logger.info("开始初始化信息...");
		/**
		 * 初始化zk服务器
		 */
		result= RpcManager.getInstance().initialize(zkxmlDir);
		if(result != RpcFrameConstants.SERVER_STATUS_OK) return false;
		
		logger.info(String.format("初始化结果为-ok"));
		String ip = StringKit.isBlank(ip_param)?PropsKit.getVal("ip"):ip_param;
		String port = StringKit.isBlank(port_param)?PropsKit.getVal("port"):port_param;
		String serviceName = PropsKit.getVal("sname");
		String serviceType = PropsKit.getVal("type");
		if(StringKit.isBlank(serviceType)) serviceType ="qktz/rpc";
		// 注册的服务信息
		ZKServiceNode znode = new ZKServiceNode(serviceType, serviceName, ip, port);
		logger.info(String.format("获取的配置信息为:%s", gson.toJson(znode)));
		RpcProcessor processor = new ThriftProcessor(serviceName, new IntelligentService.Processor<IntelligentService.Iface>(new IntelligentServiceImpl()));
		RpcServer rs = RpcServer.getInstance();
		logger.info("开始初始化处理器...");
		/**
		 * 初始化处理器
		 */
		result = rs.getTip().initProcessor(processor);
		logger.info(String.format("处理器初始化结果为-ok"));
		if(result != RpcFrameConstants.SERVER_STATUS_OK) return false;
		
		IServiceRegister sr = new ServiceRegister();
		try {
			/**
			 * 创建服务
			 */
			result = rs.createServer(0, ip, Integer.parseInt(port));
			if(result != RpcFrameConstants.SERVER_STATUS_OK) return false;
			
			/**
			 * 注册服务
			 */
			result = sr.registService(znode);
			logger.info(String.format("服务注册结果为-ok"));
			if(result != RpcFrameConstants.SERVER_STATUS_OK) return false;
			/**
			 * 启动服务
			 */
			logger.info(String.format("服务启动成功-ok"));
			rs.startServer();
			
		} catch (NumberFormatException | RpcFrameException e) {
			logger.error("数据格式异常或者框架异常!!", e);
			return false;
		}
		return true;
	}
}
