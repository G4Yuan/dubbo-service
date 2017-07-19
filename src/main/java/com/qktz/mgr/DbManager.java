package com.qktz.mgr;

import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.qktz.model._MappingKit;
import com.qktz.utils.tools.ZKTools;

/**
 * @ClassName: DbManager
 * @Description: 数据库启动管理器
 * @author yuanwei
 * @date 2017年7月17日 下午3:32:42
 * @version V1.0
 */
public class DbManager {
	// 日志插件
	private static Logger logger = Logger.getLogger(DbManager.class);

	/**
	 * @Description: 启动
	 * @return boolean 返回类型
	 */
	public static boolean start() {

		// zk获取数据配置
		Map<String, String> dbcfg = ZKTools.getPrivateConfData("dbconfig");
		logger.info(String.format("数据库配置为：%s", new Gson().toJson(dbcfg)));

		// Druid配置
		DruidPlugin dp = new DruidPlugin(dbcfg.get("url"),
				dbcfg.get("username"), dbcfg.get("password"));
		// 启动插件
		dp.start();

		// 启动ActiveRecord
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		// 是否显示sql
		arp.setShowSql(true);
		// 数据库别名
		arp.setDialect(new MysqlDialect());

		// 数据库映射
		_MappingKit.mapping(arp);

		// 启动服务
		if (arp.start())
			return true;

		return false;
	}
}
