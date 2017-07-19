package com.qktz.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qktz.mgr.DbManager;
import com.qktz.model.Account;
import com.qktz.consts.DataQueryMC;
import com.qktz.consts.MasterStockPoolStatus;
import com.qktz.function.AppPagerQuery;
import com.qktz.model.ChinaStockVaneModel;
import com.qktz.model.CurrentStocks;
import com.qktz.model.HistoryYield;
import com.qktz.model.LargeCapitalData;
import com.qktz.model.PortfolioDeal;
import com.qktz.model.PortfolioIndustry;
import com.qktz.model.PortfolioYield;
import com.qktz.service.thrift.intelligent.AmoyPager;
import com.qktz.service.thrift.intelligent.AmoySharePoolInfo;
import com.qktz.service.thrift.intelligent.BaseMasterStockPool;
import com.qktz.model.Portfolio;
import com.qktz.model.PortfolioCreator;
import com.qktz.service.thrift.intelligent.AppPager;
import com.qktz.service.thrift.intelligent.BusinessException;
import com.qktz.service.thrift.intelligent.CSVanePager;
import com.qktz.service.thrift.intelligent.ChinaStocksVane;
import com.qktz.service.thrift.intelligent.IntelligentService;
import com.qktz.service.thrift.intelligent.MasterQueryCondition;
import com.qktz.service.thrift.intelligent.MasterStockInfo;
import com.qktz.service.thrift.intelligent.MasterStockPoolPager;
import com.qktz.service.thrift.intelligent.StockShortInfo;
import com.qktz.service.thrift.intelligent.PortfolioInfo;
import com.qktz.service.thrift.intelligent.PortfolioPager;
import com.qktz.utils.CommonUtils;
import com.qktz.utils.tools.ActiveMqTools;
import com.qktz.utils.tools.ZKTools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.Db;

/**
 * 智能机器人
 * <p>
 * 机器人随机建数、随机交易
 * 
 * @author yuanwei
 * @date 二〇一七年六月二十日 16:41:45
 */
public class IntelligentServiceImpl implements IntelligentService.Iface {
	/**
	 * 日志组件
	 */
	private Logger logger = LoggerFactory.getLogger(IntelligentServiceImpl.class);
	private Gson gson = new Gson();
	// zookeeper
	Map<String, String> zkMap = ZKTools.getPrivateConfData("portfolio");
	//mc
	DataQueryMC mc = new DataQueryMC();
	//CommonUtils
	CommonUtils commonUtils = new CommonUtils();

	/**
	 * 新增ChinaStocksVane对象
	 * 
	 * @throws BusinessException
	 * 
	 */
	public boolean insertCSVane(String reqId, ChinaStocksVane csVane) throws BusinessException {
		logger.info("#{}#{}#caVane={}#{}#", "新增A股风向标数据", reqId, gson.toJson(csVane), System.currentTimeMillis());
		long startTime = System.currentTimeMillis();
		if (csVane == null)
			throw new BusinessException(99998, "非法参数");

		String fields_con = CommonUtils.genCode();
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		ChinaStockVaneModel csVaneModel_H = new ChinaStockVaneModel();
		ChinaStockVaneModel csVaneModel_N = new ChinaStockVaneModel();
		// buy_status为1
		try {
			csVaneModel_H.setTrading_day(sFormat.parse(csVane.getTrading_day()));
			csVaneModel_H.setCreate_time(sdFormat.parse(date_now));
			csVaneModel_H.setModify_time(sdFormat.parse(date_now));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		csVaneModel_H.setQk_index_level(csVane.getQk_index_level());
		csVaneModel_H.setQk_index_text(csVane.getQk_index_text());
		csVaneModel_H.setQk_index_level_text(csVane.getQk_index_level_text());
		csVaneModel_H.setQk50_text(csVane.getQk50_text());
		csVaneModel_H.setQkAD_text(csVane.getQkAD_text());
		csVaneModel_H.setEmotion_index_text(csVane.getEmotion_index_text());
		csVaneModel_H.setMain_capital_unscramble(csVane.getMain_capital_unscramble());
		csVaneModel_H.setCreator(csVane.getCreator());
		csVaneModel_H.setBuy_status(1);
		csVaneModel_H.setEmotion_data(csVane.getEmotion_data());
		csVaneModel_H.setFields_con(fields_con);

		// buy_status为0
		try {
			csVaneModel_N.setTrading_day(sFormat.parse(csVane.getTrading_day()));
			csVaneModel_N.setCreate_time(sdFormat.parse(date_now));
			csVaneModel_N.setModify_time(sdFormat.parse(date_now));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		csVaneModel_N.setQk_index_level(csVane.getQk_index_free_level());
		csVaneModel_N.setQk_index_text(csVane.getQk_index_free_text());
		csVaneModel_N.setQk_index_level_text(csVane.getQk_index_free_level_text());
		csVaneModel_N.setQk50_text(csVane.getQk50_text());
		csVaneModel_N.setQkAD_text(csVane.getQkAD_text());
		csVaneModel_N.setEmotion_index_text(csVane.getEmotion_index_free_text());
		csVaneModel_N.setMain_capital_unscramble(csVane.getMain_capital_free_unscramble());
		csVaneModel_N.setCreator(csVane.getCreator());
		csVaneModel_N.setBuy_status(0);
		csVaneModel_N.setEmotion_data(csVane.getEmotion_free_data());
		csVaneModel_N.setFields_con(fields_con);

		csVaneModel_H.save();
		csVaneModel_N.save();
		long endTime = System.currentTimeMillis();
		logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
		return true;
	}

	/**
	 * 查询CSVane数据
	 * 
	 * @param buy_status
	 * @return
	 * @throws BusinessException
	 */
	public String findCSVane(String reqId, int buy_status) throws BusinessException {
		logger.info("#{}#{}#buy_status={}#{}#", "查询A股风向标数据", reqId, buy_status, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();
		if (buy_status < 0)
			throw new BusinessException(99998, "非法参数");

		// 查询对象
		ChinaStockVaneModel csVaneModel = ChinaStockVaneModel.dao.findFirst(
				"select * from china_stocks_vane where buy_status= ? order by create_time desc limit 1", buy_status);
		if (csVaneModel == null)
			throw new BusinessException(99998, "找不到CSVane数据");

		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
		JSONObject csvJSON = new JSONObject();
		csvJSON.put("trading_day", sFormat.format(csVaneModel.getTrading_day()));
		csvJSON.put("qk_index_level", csVaneModel.getQk_index_level());
		csvJSON.put("qk_index_text", csVaneModel.getQk_index_text());
		csvJSON.put("qk_index_level_text", csVaneModel.getQk_index_level_text());
		csvJSON.put("qk50_text", csVaneModel.getQk50_text());
		csvJSON.put("qkAD_text", csVaneModel.getQkAD_text());
		csvJSON.put("emotion_index_text", csVaneModel.getEmotion_index_text());
		csvJSON.put("main_capital_unscramble", csVaneModel.getMain_capital_unscramble());
		csvJSON.put("creator", csVaneModel.getCreator());
		csvJSON.put("buy_status", csVaneModel.getBuy_status());
		csvJSON.put("emotion_data", JSONObject.parseObject(csVaneModel.getEmotion_data()));
		csvJSON.put("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(csVaneModel.getCreate_time()));
		csvJSON.put("modify_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(csVaneModel.getModify_time()));
		csvJSON.put("csvid", csVaneModel.getId());
		String jsoString = JSON.toJSONString(csvJSON);
		long endTime = System.currentTimeMillis();
		logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
		return jsoString;
	}

	/**
	 * 查询CSVane数据
	 * 
	 * @param buy_status
	 * @return
	 * @throws BusinessException
	 */
	public String findCSVaneByID(String reqId, int ID) throws BusinessException {
		logger.info("#{}#{}#ID={}#{}#", "查询A股风向标数据(ID)", reqId, ID, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();
		if (ID < 0)
			throw new BusinessException(99998, "非法参数");

		// 查询对象
		ChinaStockVaneModel csVaneModel = ChinaStockVaneModel.dao.findById(ID);
		ChinaStockVaneModel csVaneModel_other = ChinaStockVaneModel.dao.findFirst(
				"select * from china_stocks_vane where fields_con= ? and id != ?", csVaneModel.getFields_con(),
				csVaneModel.getId());
		if (csVaneModel == null || csVaneModel_other == null)
			throw new BusinessException(99998, "找不到CSVane数据");

		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
		JSONObject csvJSON = new JSONObject();
		csvJSON.put("trading_day", sFormat.format(csVaneModel.getTrading_day()));
		csvJSON.put("qk_index_level", csVaneModel.getQk_index_level());
		csvJSON.put("qk_index_text", csVaneModel.getQk_index_text());
		csvJSON.put("qk_index_level_text", csVaneModel.getQk_index_level_text());
		csvJSON.put("qk_index_free_level", csVaneModel_other.getQk_index_level());
		csvJSON.put("qk_index_free_text", csVaneModel_other.getQk_index_text());
		csvJSON.put("qk_index_free_level_text", csVaneModel_other.getQk_index_level_text());
		csvJSON.put("qk50_text", csVaneModel.getQk50_text());
		csvJSON.put("qkAD_text", csVaneModel.getQkAD_text());
		csvJSON.put("emotion_index_text", csVaneModel.getEmotion_index_text());
		csvJSON.put("emotion_index_free_text", csVaneModel_other.getEmotion_index_text());
		csvJSON.put("main_capital_unscramble", csVaneModel.getMain_capital_unscramble());
		csvJSON.put("main_capital_free_unscramble", csVaneModel_other.getMain_capital_unscramble());
		csvJSON.put("creator", csVaneModel.getCreator());
		// csvJSON.put("buy_status",csVaneModel.getBuy_status());
		csvJSON.put("emotion_data", JSONObject.parseObject(csVaneModel.getEmotion_data()));
		csvJSON.put("emotion_free_data", JSONObject.parseObject(csVaneModel_other.getEmotion_data()));
		// csvJSON.put("create_time", new
		// SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss").format(csVaneModel.getCreate_time()));
		// csvJSON.put("modify_time", new
		// SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss").format(csVaneModel.getModify_time()));
		csvJSON.put("csvid", csVaneModel.getId());
		String jsoString = JSON.toJSONString(csvJSON);
		long endTime = System.currentTimeMillis();
		logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
		return jsoString;
	}

	/**
	 * 更新CSVane数据
	 * 
	 * @param buy_status
	 * @param csVane
	 * @return
	 * @throws BusinessException
	 */
	public boolean updateCSVane(String reqId, ChinaStocksVane csVane) throws BusinessException {
		logger.info("#{}#{}#csVane={}#{}#", "修改A股风向标数据", reqId, gson.toJson(csVane), System.currentTimeMillis());
		long startTime = System.currentTimeMillis();
		if (csVane == null)
			throw new BusinessException(99998, "非法参数");

		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ChinaStockVaneModel cStockVaneModel = ChinaStockVaneModel.dao.findById(csVane.getCsvid());
		ChinaStockVaneModel csVaneModel_H = null;
		ChinaStockVaneModel csVaneModel_N = null;
		if (cStockVaneModel.getBuy_status() == 1) {
			csVaneModel_H = cStockVaneModel;
			csVaneModel_N = ChinaStockVaneModel.dao.findFirst(
					"select * from china_stocks_vane where fields_con= ? and buy_status= 0",
					csVaneModel_H.getFields_con());
		}
		if (cStockVaneModel.getBuy_status() == 0) {
			csVaneModel_N = cStockVaneModel;
			csVaneModel_H = ChinaStockVaneModel.dao.findFirst(
					"select * from china_stocks_vane where fields_con= ? and buy_status= 1",
					csVaneModel_N.getFields_con());
		}

		if (csVaneModel_H == null || csVaneModel_N == null)
			throw new BusinessException(99998, "未找到最新对象信息");
		// buy_status为1
		try {
			csVaneModel_H.setTrading_day(sFormat.parse(csVane.getTrading_day()));
			csVaneModel_H
					.setModify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdFormat.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		csVaneModel_H.setQk_index_level(csVane.getQk_index_level());
		csVaneModel_H.setQk_index_text(csVane.getQk_index_text());
		csVaneModel_H.setQk_index_level_text(csVane.getQk_index_level_text());
		csVaneModel_H.setQk50_text(csVane.getQk50_text());
		csVaneModel_H.setQkAD_text(csVane.getQkAD_text());
		csVaneModel_H.setEmotion_index_text(csVane.getEmotion_index_text());
		csVaneModel_H.setMain_capital_unscramble(csVane.getMain_capital_unscramble());
		csVaneModel_H.setCreator(csVane.getCreator());
		csVaneModel_H.setBuy_status(1);
		csVaneModel_H.setEmotion_data(csVane.getEmotion_data());
		csVaneModel_H.setCreate_time(csVaneModel_H.getCreate_time());

		// buy_status为0
		try {
			csVaneModel_N.setTrading_day(sFormat.parse(csVane.getTrading_day()));
			csVaneModel_N
					.setModify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdFormat.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		csVaneModel_N.setQk_index_level(csVane.getQk_index_free_level());
		csVaneModel_N.setQk_index_text(csVane.getQk_index_free_text());
		csVaneModel_N.setQk_index_level_text(csVane.getQk_index_free_level_text());
		csVaneModel_N.setQk50_text(csVane.getQk50_text());
		csVaneModel_N.setQkAD_text(csVane.getQkAD_text());
		csVaneModel_N.setEmotion_index_text(csVane.getEmotion_index_free_text());
		csVaneModel_N.setMain_capital_unscramble(csVane.getMain_capital_free_unscramble());
		csVaneModel_N.setCreator(csVane.getCreator());
		csVaneModel_N.setBuy_status(0);
		csVaneModel_N.setEmotion_data(csVane.getEmotion_free_data());
		csVaneModel_N.setCreate_time(csVaneModel_N.getCreate_time());

		csVaneModel_H.update();
		csVaneModel_N.update();

		long endTime = System.currentTimeMillis();
		logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
		return true;
	}

	/**
	 * 获取Vane Page
	 * 
	 * @param pageNow
	 *            当前页号
	 * @param pageSize
	 *            每页个数
	 * @return 当前页List
	 * @throws BusinessException
	 */
	@Override
	public CSVanePager findCSVanePage(String reqId, int pageNow, int pageSize) throws BusinessException {
		logger.info("#{}#{}#pageNow={},pageSize={}#{}#", "查询A股风向标(列表)", reqId, pageNow, pageSize,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();
		CSVanePager pager = new CSVanePager();

		if (pageNow <= 0 || pageSize <= 0)
			throw new BusinessException(99998, "非法参数");
		int pageNum = 0;
		// 记录总数
		Long pageRecords = Db.queryLong("select count(*) from china_stocks_vane");

		if (pageSize != 0) {
			// 分页查询
			int recordSize = Integer.parseInt(pageRecords + "");
			pageNum = recordSize > 0 ? (recordSize % pageSize == 0 ? recordSize / pageSize : recordSize / pageSize + 1)
					: 0;
			int offset = (pageNow - 1) * pageSize;

			pager.setHasNext(pageNow >= pageNum ? 0 : 1);
			pager.setHasPrev(pageNow <= 1 ? 0 : 1);
			pager.setPageNow(pageNow);
			pager.setPageNum(pageNum);
			pager.setRecordSize(Integer.parseInt(pageRecords + ""));
			pager.setPageSize(pageSize);
			pager.setRecords(ChinaStockVaneModel.dao.findCSVane(offset, pageSize));

		} else {
			// 全量查询
			pager.setHasNext(0);
			pager.setHasPrev(0);
			pager.setPageNow(1);
			pager.setPageNum(1);
			pager.setRecordSize(Integer.parseInt(pageRecords + ""));
			pager.setPageSize(pageSize);
			pager.setRecords(ChinaStockVaneModel.dao.findCSVane(0, 0));
		}
		long endTime = System.currentTimeMillis();
		logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
		return pager;

	}

	/**
	 * 查询主力资金数据
	 * 
	 * @param buy_status
	 * @return
	 * @throws BusinessException
	 */
	public String findMainCapital(String reqId, int buy_status) throws BusinessException {
		logger.info("#{}#{}#buy_status={}#{}#", "查询主力资金", reqId, buy_status, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

		Map<String, String> map = new HashMap<String, String>();
		map.put("SH000002", "A股指数");
		map.put("SH000001", "上证指数");
		map.put("SZ399107", "深圳A指");
		map.put("SZ399006", "创业板指");
		map.put("SZ399005", "中小板指");

		// 最后传入
		// Map<String, JSON> newMap = new LinkedHashMap<String, JSON>();
		JSONObject jsonResult = new JSONObject();

		try {

			LargeCapitalData lcData = LargeCapitalData.dao
					.findFirst("select * from large_capital_data order by trading_day desc limit 1");
			LargeCapitalData lcData_YTDB = LargeCapitalData.dao
					.findFirst("select * from large_capital_data order by trading_day desc limit 1,1");
			Map<String, String> blocktopMap = null;
			if (lcData == null)
				throw new BusinessException(99998, "没有匹配到最新的主力数据");

			Date trading_day = lcData.getDate("trading_day");
			for (Map.Entry<String, String> entry : map.entrySet()) {
				blocktopMap = new HashMap<String, String>();
				blocktopMap.put("trading_day", sdFormat.format(trading_day));
				blocktopMap.put("marketname", entry.getValue());
				blocktopMap.put("shares_index", lcData.getBigDecimal(entry.getKey() + "_index").toString());
				blocktopMap.put("shares_index_ytdb", lcData_YTDB.getBigDecimal(entry.getKey() + "_index").toString());
				if (buy_status == 1) {
					blocktopMap.put("position_ratio", lcData.getDouble(entry.getKey()).toString());
				}
				jsonResult.put(entry.getKey(), JSON.parseObject(JSON.toJSONString(blocktopMap)));
				// newMap.put(entry.getKey(),
				// JSON.parseObject(JSON.toJSONString(blocktopMap)));
			}

			// 趋势数据
			List<LargeCapitalData> lcDataList = null;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(trading_day);
			Date trading_day_oneM = null;
			Date trading_day_twoM = null;
			if (buy_status == 1) {
				calendar.add(Calendar.MONTH, -1);
				trading_day_oneM = calendar.getTime();
				lcDataList = LargeCapitalData.dao.find(
						"select * from large_capital_data where trading_day between ? and ? order by trading_day asc",
						sdFormat.format(trading_day_oneM), sdFormat.format(trading_day));
			}
			if (buy_status == 0) {
				calendar.add(Calendar.MONTH, -1);
				trading_day_oneM = calendar.getTime();
				calendar.add(Calendar.MONTH, -1);
				trading_day_twoM = calendar.getTime();
				lcDataList = LargeCapitalData.dao.find(
						"select * from large_capital_data where trading_day between ? and ? order by trading_day asc",
						sdFormat.format(trading_day_twoM), sdFormat.format(trading_day_oneM));
			}

			if (lcDataList == null || lcDataList.size() < 1)
				throw new BusinessException(99998, "没有主力资金数据");

			Map<String, String> blockmidMap = null;
			List<JSONObject> midJSON = null;
			// JSONArray midArray = new JSONArray();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				midJSON = new ArrayList<JSONObject>();
				for (LargeCapitalData lcDatas : lcDataList) {
					blockmidMap = new HashMap<String, String>();
					blockmidMap.put("trading_day", lcDatas.getDate("trading_day").toString());
					blockmidMap.put("marketname", entry.getValue());
					blockmidMap.put("shares_index", lcDatas.getBigDecimal(entry.getKey() + "_index").toString());
					blockmidMap.put("position_ratio", lcDatas.getDouble(entry.getKey()).toString());

					midJSON.add(JSONObject.parseObject(JSON.toJSONString(blockmidMap)));
				}
				// midArray.add(midJSON);
				// newMap.put(entry.getKey()+"_index",
				// JSON.parseObject(JSON.toJSONString(midJSON)));
				jsonResult.put(entry.getKey() + "_index", midJSON);
			}
		} catch (Exception e) {
			logger.error("System Error:{},json={}", e, jsonResult);
			throw new BusinessException(10002, "返回参数异常");
		}
		String jsoString = JSON.toJSONString(jsonResult);
		long endTime = System.currentTimeMillis();
		logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
		return jsoString;
	}

	  /**
		* @param reqId
		* @param portfolioInfo
		* @Description: 新建组合 
		 */
		@SuppressWarnings("all")
		@Override
		 public void addPortfolio(String reqId,PortfolioInfo portfolioInfo) throws BusinessException {
		    	logger.info("#{}#{}#caVane={}#{}#", "新建组合数据", reqId, gson.toJson(portfolioInfo), System.currentTimeMillis());
				long startTime = System.currentTimeMillis();
				
				SimpleDateFormat sDate_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//获取资金ID
				List<Account> accounts = Account.dao.find("select * from portfolio_accountid where isUsed= ?",1);
				if (accounts == null || accounts.size() == 0) {
					logger.warn("#{}#accounts={}","具体",accounts);
				    throw new BusinessException(10002,"可用资金ID为空");
				}
				Random random = new Random();
				String account_id = accounts.get(random.nextInt(accounts.size()-1)).getAccountId();
					
				//创建组合
				Portfolio portfolio = new Portfolio();
				portfolio.setPortfolio_title(portfolioInfo.getPortfolio_title());
				portfolio.setPortfolio_summary(portfolioInfo.getPortfolio_summary());
				portfolio.setPortfolio_labels(portfolioInfo.getPortfolio_labels());
				portfolio.setRisk_level(portfolioInfo.getRisk_level());
				try {
					portfolio.setValid_start_date(sDate_time.parse(portfolioInfo.getValid_start_date()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				portfolio.setInit_value(Double.valueOf(zkMap.get("INIT_VALUE")));
				portfolio.setPortfolio_categories(portfolioInfo.getPortfolio_categories());
				portfolio.setPortfolio_status(Integer.valueOf(zkMap.get("STATUS")));
				portfolio.setUser_object_id(portfolioInfo.getUser_object_id());
				portfolio.setAccount_id(account_id);
				try {
					portfolio.setCreatedAt(sDate_time.parse(sDate_time.format(new Date())));
					portfolio.setUpdatedAt(sDate_time.parse(sDate_time.format(new Date())));
				} catch (ParseException e1) {
					logger.warn("System Error:{}",e1);
				}
				
				//是否创建创建人
				PortfolioCreator creator = null;
				if (StringUtils.isNotBlank(portfolioInfo.getUser_object_id())) {
					List<PortfolioCreator> creators = PortfolioCreator.dao.find("select * from portfolio_creator where user_object_id= ?",portfolioInfo.getUser_object_id());
				if (creators == null || creators.size() <= 0) {
					creator = new PortfolioCreator();
					creator.setUser_object_id(portfolioInfo.getUser_object_id());
						creator.setUser_name(mc.getBossUser(portfolioInfo.getUser_object_id()).getUser_name());
						creator.setUser_logo(mc.getBossUser(portfolioInfo.getUser_object_id()).getUser_logo());
						creator.setUser_summary(mc.getBossUser(portfolioInfo.getUser_object_id()).getUser_summary());
						creator.setQualificationName(mc.getBossUser(portfolioInfo.getUser_object_id()).getQualificationName());
						creator.setQualificationNum(mc.getBossUser(portfolioInfo.getUser_object_id()).getQualificationNum());
					    creator.setRoleType(mc.getBossUser(portfolioInfo.getUser_object_id()).getRoleType());
					try {
						creator.setCreatedAt(sDate_time.parse(sDate_time.format(new Date())));
						creator.setUpdatedAt(sDate_time.parse(sDate_time.format(new Date())));
					} catch (ParseException e) {
						logger.warn("System Error:{}",e);
					}
				}else {
					logger.info("The Creator Already Exists:name={}",creators.get(0).getUser_name());
				}
				}
				
				//保存
				boolean return_p = portfolio.save();
				Account account = null;
				if (creator!=null) {
					creator.save();
				}
				//修改资金ID使用状态
				if(return_p==true){
					account = Account.dao.findFirst("select * from portfolio_accountid where accountId= ?",account_id);
					account.set("isUsed", 2);
					account.update();
				}
				
				long endTime = System.currentTimeMillis();
				logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
			}
		 
		 /**
		 * @param reqId
		 * @param pageNow
		 * @param pageSize
		 * @Description: 查询组合列表 
		  */
		 @Override
		 public PortfolioPager findPortfolioPage(String reqId, int pageNow, int pageSize)
					throws BusinessException {
				logger.info("#{}#{}#pageNow={},pageSize={}#{}#", "查询组合信息(列表)", reqId, pageNow,pageSize, System.currentTimeMillis());
				long startTime = System.currentTimeMillis();
				PortfolioPager pager = new PortfolioPager();

				if (pageNow <= 0 || pageSize <= 0)
					throw new BusinessException(99998, "非法参数");
				int pageNum = 0;
				// 记录总数
				Long pageRecords = Db
						.queryLong("select count(*) from portfolio");

				if (pageSize != 0) {
					// 分页查询
					int recordSize = Integer.parseInt(pageRecords + "");
					pageNum = recordSize > 0 ? (recordSize % pageSize == 0 ? recordSize
							/ pageSize : recordSize / pageSize + 1) : 0;
					int offset = (pageNow - 1) * pageSize;

					pager.setHasNext(pageNow >= pageNum ? 0 : 1);
					pager.setHasPrev(pageNow <= 1 ? 0 : 1);
					pager.setPageNow(pageNow);
					pager.setPageNum(pageNum);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(Portfolio.dao.findPortfolio(offset,
							pageSize));

				} else {
					// 全量查询
					pager.setHasNext(0);
					pager.setHasPrev(0);
					pager.setPageNow(1);
					pager.setPageNum(1);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(Portfolio.dao.findPortfolio(0, 0));
				}
				long endTime = System.currentTimeMillis();
				logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
				return pager;
			} 
		 
		 /**
		  * @param reqId
		  * @param name
		  * @param pageNow
		  * @param pageSize
		  * @Description: 根据title查询组合列表
		  */
		 @Override
		 public PortfolioPager findPortfolioByTitle(String reqId,String title,int pageNow,int pageSize)throws BusinessException{
			 logger.info("#{}#{}#title={},pageNow={},pageSize={}#{}#", "根据name查询组合信息(列表)", reqId, title,pageNow,pageSize, System.currentTimeMillis());
				long startTime = System.currentTimeMillis();
				PortfolioPager pager = new PortfolioPager();

				if (pageNow <= 0 || pageSize <= 0)
					throw new BusinessException(99998, "非法参数");
				int pageNum = 0;
				String portfolio_title = "%" +title+ "%";
				// 记录总数
				Long pageRecords = Db
						.queryLong("select count(*) from portfolio where portfolio_title like ?",portfolio_title);

				if (pageSize != 0) {
					// 分页查询
					int recordSize = Integer.parseInt(pageRecords + "");
					pageNum = recordSize > 0 ? (recordSize % pageSize == 0 ? recordSize
							/ pageSize : recordSize / pageSize + 1) : 0;
					int offset = (pageNow - 1) * pageSize;

					pager.setHasNext(pageNow >= pageNum ? 0 : 1);
					pager.setHasPrev(pageNow <= 1 ? 0 : 1);
					pager.setPageNow(pageNow);
					pager.setPageNum(pageNum);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(Portfolio.dao.findPortfolioByTitle(portfolio_title, offset, pageSize));

				} else {
					// 全量查询
					pager.setHasNext(0);
					pager.setHasPrev(0);
					pager.setPageNow(1);
					pager.setPageNum(1);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(Portfolio.dao.findPortfolioByTitle(portfolio_title, 0, 0));
				}
				long endTime = System.currentTimeMillis();
				logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
				return pager;
			 
		 }

		 /**
		  * @param reqId
		  * @param createdAt
		  * @param pageNow
		  * @param pageSize
		  * @Description: 根据创建时间查询组合列表 
		  */
		@Override
		public PortfolioPager findPortfolioByTime(String reqId, String createdAt,
				int pageNow, int pageSize) throws BusinessException {
			 logger.info("#{}#{}#createdAt={},pageNow={},pageSize={}#{}#", "根据创建时间查询组合信息(列表)", reqId, createdAt,pageNow,pageSize, System.currentTimeMillis());
				long startTime = System.currentTimeMillis();
				PortfolioPager pager = new PortfolioPager();

				if (pageNow <= 0 || pageSize <= 0)
					throw new BusinessException(99998, "非法参数");
				int pageNum = 0;
				String created_time = "%" +createdAt+ "%";
				// 记录总数
				Long pageRecords = Db
						.queryLong("select count(*) from portfolio where createdAt like ?",created_time);

				if (pageSize != 0) {
					// 分页查询
					int recordSize = Integer.parseInt(pageRecords + "");
					pageNum = recordSize > 0 ? (recordSize % pageSize == 0 ? recordSize
							/ pageSize : recordSize / pageSize + 1) : 0;
					int offset = (pageNow - 1) * pageSize;

					pager.setHasNext(pageNow >= pageNum ? 0 : 1);
					pager.setHasPrev(pageNow <= 1 ? 0 : 1);
					pager.setPageNow(pageNow);
					pager.setPageNum(pageNum);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(Portfolio.dao.findPortfolioByTime(created_time, offset, pageSize));

				} else {
					// 全量查询
					pager.setHasNext(0);
					pager.setHasPrev(0);
					pager.setPageNow(1);
					pager.setPageNum(1);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(Portfolio.dao.findPortfolioByTime(created_time, 0, 0));
				}
				long endTime = System.currentTimeMillis();
				logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
				return pager;
		}

		/**
		 * @param reqId
		 * @param status
		 * @param pageNow
		 * @param pageSize
		 * @Description: 根据状态查询组合列表 
		 */
		@Override
		public PortfolioPager findPortfolioByStatus(String reqId, int status,
				int pageNow, int pageSize) throws BusinessException {
			 logger.info("#{}#{}#status={},pageNow={},pageSize={}#{}#", "根据状态查询组合信息(列表)", reqId, status,pageNow,pageSize, System.currentTimeMillis());
				long startTime = System.currentTimeMillis();
				if (status<0) {
					throw new BusinessException(10002,"参数不合法");
				}
				if (pageNow <= 0 || pageSize <= 0)
					throw new BusinessException(99998, "非法参数");
				PortfolioPager pager = new PortfolioPager();
				int pageNum = 0;
				// 记录总数
				Long pageRecords = Db
						.queryLong("select count(*) from portfolio where portfolio_status= ?",status);

				if (pageSize != 0) {
					// 分页查询
					int recordSize = Integer.parseInt(pageRecords + "");
					pageNum = recordSize > 0 ? (recordSize % pageSize == 0 ? recordSize
							/ pageSize : recordSize / pageSize + 1) : 0;
					int offset = (pageNow - 1) * pageSize;

					pager.setHasNext(pageNow >= pageNum ? 0 : 1);
					pager.setHasPrev(pageNow <= 1 ? 0 : 1);
					pager.setPageNow(pageNow);
					pager.setPageNum(pageNum);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(Portfolio.dao.findPortfolioByStatus(status, offset, pageSize));

				} else {
					// 全量查询
					pager.setHasNext(0);
					pager.setHasPrev(0);
					pager.setPageNow(1);
					pager.setPageNum(1);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(Portfolio.dao.findPortfolioByStatus(status, 0, 0));
				}
				long endTime = System.currentTimeMillis();
				logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
				return pager;
		}
		
		 /**
		  * @param reqId
		  * @param name
		  * @param pageNow
		  * @param pageSize
		  * @Description: 根据categories查询组合列表
		  */
		 @Override
		 public PortfolioPager findPortfolioByCategories(String reqId,String categories,int pageNow,int pageSize)throws BusinessException{
			 logger.info("#{}#{}#categories={},pageNow={},pageSize={}#{}#", "根据name查询组合信息(列表)", reqId, categories,pageNow,pageSize, System.currentTimeMillis());
				long startTime = System.currentTimeMillis();
				PortfolioPager pager = new PortfolioPager();

				if (pageNow <= 0 || pageSize <= 0)
					throw new BusinessException(99998, "非法参数");
				int pageNum = 0;
				String portfolio_categories = "%" +categories+ "%";
				// 记录总数
				Long pageRecords = Db
						.queryLong("select count(*) from portfolio where portfolio_categories like ?",portfolio_categories);

				if (pageSize != 0) {
					// 分页查询
					int recordSize = Integer.parseInt(pageRecords + "");
					pageNum = recordSize > 0 ? (recordSize % pageSize == 0 ? recordSize
							/ pageSize : recordSize / pageSize + 1) : 0;
					int offset = (pageNow - 1) * pageSize;

					pager.setHasNext(pageNow >= pageNum ? 0 : 1);
					pager.setHasPrev(pageNow <= 1 ? 0 : 1);
					pager.setPageNow(pageNow);
					pager.setPageNum(pageNum);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(Portfolio.dao.findPortfolioByCategories(portfolio_categories, offset, pageSize));

				} else {
					// 全量查询
					pager.setHasNext(0);
					pager.setHasPrev(0);
					pager.setPageNow(1);
					pager.setPageNum(1);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(Portfolio.dao.findPortfolioByCategories(portfolio_categories, 0, 0));
				}
				long endTime = System.currentTimeMillis();
				logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
				return pager;
			 
		 }

		/**
		 * @param reqId
		 * @param account_id
		 * @Description: 根据资金ID查询组合
		 */
		@Override
		public PortfolioInfo findPortfolioByAccountID(String reqId,
				String account_id) throws BusinessException {
			 logger.info("#{}#{}#account_id={}#{}#", "根据资金ID查询组合", reqId, account_id, System.currentTimeMillis());
			 long startTime = System.currentTimeMillis();
			
			//格式化时间
			SimpleDateFormat sDate_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Portfolio portfolio = Portfolio.dao.findFirst("select * from portfolio where account_id= ?",account_id);
			if (portfolio == null) {
				throw new BusinessException(10002,"未找到对应组合");
			}
			PortfolioCreator creator = PortfolioCreator.dao.findFirst("select * from portfolio_creator where user_object_id= ?",portfolio.getUser_object_id());
			PortfolioInfo portfolioInfo = new PortfolioInfo();
			portfolioInfo.setPfl_id(portfolio.getId());
			portfolioInfo.setPortfolio_title(portfolio.getPortfolio_title());
			portfolioInfo.setPortfolio_summary(portfolio.getPortfolio_summary());
			portfolioInfo.setPortfolio_labels(portfolio.getPortfolio_labels());
			portfolioInfo.setRisk_level(portfolio.getRisk_level());
			portfolioInfo.setValid_start_date(sDate_time.format(portfolio.getValid_start_date()));
			portfolioInfo.setInit_value(portfolio.getInit_value());
			portfolioInfo.setPortfolio_categories(portfolio.getPortfolio_categories());
			portfolioInfo.setPortfolio_status(portfolio.getPortfolio_status());
			portfolioInfo.setAccount_id(portfolio.getAccount_id());
			portfolioInfo.setUser_object_id(portfolio.getUser_object_id());
			portfolioInfo.setCreatedAt(sDate_time.format(portfolio.getCreatedAt()));
			portfolioInfo.setUpdatedAt(sDate_time.format(portfolio.getUpdatedAt()));
			if (creator != null) {
				portfolioInfo.setUser_name(creator.getUser_name());
				portfolioInfo.setUser_logo(creator.getUser_logo());
				portfolioInfo.setUser_summary(creator.getUser_summary());
				portfolioInfo.setQualificationName(creator.getQualificationName());
				portfolioInfo.setQualificationNum(creator.getQualificationNum());
			}
			
			long endTime = System.currentTimeMillis();
			logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
			return portfolioInfo;
		}

		/**
		 * @param reqId
		 * @param id
		 * @Description: 根据组合ID查询组合
		 */
		@Override
		public PortfolioInfo findPortfolioByID(String reqId, int id)
				throws BusinessException {
			 logger.info("#{}#{}#id={}#{}#", "根据组合ID查询组合", reqId, id, System.currentTimeMillis());
			 long startTime = System.currentTimeMillis();
			
			//格式化时间
			SimpleDateFormat sDate_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Portfolio portfolio = Portfolio.dao.findById(id);
			if (portfolio == null) {
				throw new BusinessException(10002,"未找到对应组合");
			}
			PortfolioCreator creator = PortfolioCreator.dao.findFirst("select * from portfolio_creator where user_object_id= ?",portfolio.getUser_object_id());
			PortfolioInfo portfolioInfo = new PortfolioInfo();
			portfolioInfo.setPfl_id(portfolio.getId());
			portfolioInfo.setPortfolio_title(portfolio.getPortfolio_title());
			portfolioInfo.setPortfolio_summary(portfolio.getPortfolio_summary());
			portfolioInfo.setPortfolio_labels(portfolio.getPortfolio_labels());
			portfolioInfo.setRisk_level(portfolio.getRisk_level());
			portfolioInfo.setValid_start_date(sDate_time.format(portfolio.getValid_start_date()));
			portfolioInfo.setInit_value(portfolio.getInit_value());
			portfolioInfo.setPortfolio_categories(portfolio.getPortfolio_categories());
			portfolioInfo.setPortfolio_status(portfolio.getPortfolio_status());
			portfolioInfo.setAccount_id(portfolio.getAccount_id());
			portfolioInfo.setUser_object_id(portfolio.getUser_object_id());
			portfolioInfo.setCreatedAt(sDate_time.format(portfolio.getCreatedAt()));
			portfolioInfo.setUpdatedAt(sDate_time.format(portfolio.getUpdatedAt()));
			if (creator != null) {
				portfolioInfo.setUser_name(creator.getUser_name());
				portfolioInfo.setUser_logo(creator.getUser_logo());
				portfolioInfo.setUser_summary(creator.getUser_summary());
				portfolioInfo.setQualificationName(creator.getQualificationName());
				portfolioInfo.setQualificationNum(creator.getQualificationNum());
			}
			
			long endTime = System.currentTimeMillis();
			logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
			return portfolioInfo;
		}


		 /**
		 * @param reqId
		 * @param portfolioInfo
		 * @Description: 根据组合ID修改组合
		 */ 
		@Override
		public boolean updatePortfolioByID(String reqId, PortfolioInfo portfolioInfo)
				throws BusinessException {
			 logger.info("#{}#{}#portfolio={}#{}#", "根据组合ID更新组合", reqId, gson.toJson(portfolioInfo), System.currentTimeMillis());
			 long startTime = System.currentTimeMillis();
			 //格式化时间
			 SimpleDateFormat sDate_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 
			 int portfolio_id = portfolioInfo.getPfl_id();
			 Portfolio portfolio = Portfolio.dao.findById(portfolio_id);
			 if (portfolio == null) {
				logger.warn("Portfolio is Null!");
				throw new BusinessException(10002,"未找到组合对象");
			}
			 portfolio.setPortfolio_summary(portfolioInfo.getPortfolio_summary());
			 portfolio.setPortfolio_labels(portfolioInfo.getPortfolio_labels());
			 portfolio.setRisk_level(portfolioInfo.getRisk_level());
			 try {
				portfolio.setValid_start_date(sDate_time.parse(portfolioInfo.getValid_start_date()));
				portfolio.setCreatedAt(sDate_time.parse(portfolioInfo.getCreatedAt()));
				portfolio.setUpdatedAt(sDate_time.parse(sDate_time.format(new Date())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			 portfolio.setInit_value(portfolioInfo.getInit_value());
			 portfolio.setPortfolio_categories(portfolioInfo.getPortfolio_categories());
			 portfolio.setPortfolio_status(portfolioInfo.getPortfolio_status());
			 portfolio.setAccount_id(portfolioInfo.getAccount_id());
			 portfolio.setUser_object_id(portfolioInfo.getUser_object_id());
			 
			 PortfolioCreator creator = PortfolioCreator.dao.findFirst("select * from portfolio_creator where user_object_id= ?",portfolioInfo.getUser_object_id());
			 PortfolioCreator creator_insert = null;
			 if (creator == null) {
					PortfolioCreator creator_p = mc.getBossUser(portfolioInfo.getUser_object_id());
				    if (creator_p != null) {
						creator_insert = new PortfolioCreator();
						creator_insert.setUser_object_id(portfolioInfo.getUser_object_id());
						creator_insert.setUser_name(creator_p.getUser_name());
						creator_insert.setUser_logo(creator_p.getUser_logo());
						creator_insert.setUser_summary(creator_p.getUser_summary());
						creator_insert.setQualificationName(creator_p.getQualificationName());
						creator_insert.setQualificationNum(creator_p.getQualificationNum());
					    creator_insert.setRoleType(creator_p.getRoleType());
				    }
			}
			 //更新
			boolean return_p =  portfolio.update();
			
			//新增
			if (creator_insert != null) {
				creator_insert.save();
			}
			long endTime = System.currentTimeMillis();
			logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
			return return_p;
		}

		 /**
		 * @param reqId
		 * @param portfolioInfo
         * @param adviser_object_id
		 * @Description: 根据组合ID转让组合
		 */ 
		@Override
	public boolean updatePortfolioByAdviser(String reqId,
			String user_object_id, int portfolio_id) throws BusinessException {
		logger.info("#{}#{}#user_object_id={},portfolio_id={}#{}#",
				"根据组合ID转让组合", reqId, user_object_id, portfolio_id,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();
		// 格式化时间
		SimpleDateFormat sDate_time = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		logger.info("portfolio:{}", portfolio_id);
		if (portfolio_id < 0)
			throw new BusinessException(10002, "未取到portfolio对象的ID");
		Portfolio portfolio = Portfolio.dao.findById(portfolio_id);
		boolean result = false;
		if (portfolio != null) {
			// 更新
			portfolio.set("user_object_id", user_object_id);
			try {
				portfolio.set("updatedAt",
						sDate_time.parse(sDate_time.format(new Date())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			result = portfolio.update();
		} else {
			logger.warn("Portfolio Is Null,No Change Were Made To The Object!");
		}

		PortfolioCreator creator = PortfolioCreator.dao.findFirst(
				"select * from portfolio_creator where user_object_id= ?",
				user_object_id);
		PortfolioCreator creator_insert = null;
		if (creator == null) {
				PortfolioCreator creator_p = mc.getBossUser(user_object_id);
				if (creator_p != null) {
					creator_insert = new PortfolioCreator();
					creator_insert.setUser_object_id(user_object_id);
					creator_insert.setUser_name(creator_p.getUser_name());
					creator_insert.setUser_logo(creator_p.getUser_logo());
					creator_insert.setUser_summary(creator_p.getUser_summary());
					creator_insert.setQualificationName(creator_p
							.getQualificationName());
					creator_insert.setQualificationNum(creator_p
							.getQualificationNum());
					creator_insert.setRoleType(creator_p.getRoleType());
				}
		}

		// 新增
		if (creator_insert != null) {
			creator_insert.save();
		}
		long endTime = System.currentTimeMillis();
		logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
		return result;
	}

		 /**
		  * @param reqId
		  * @param isFree
		  * @param pageNow
		  * @param pageSize
		  * @Description: app端组合列表展示
		  */
		@Override
		 public AppPager findAppPagerSimple(String reqId,String isFree,int pageNow,int pageSize)throws BusinessException{
			 logger.info("#{}#{}#isFree={},pageNow={},pageSize={}#{}#", "app端组合列表展示", reqId, isFree,pageNow,pageSize, System.currentTimeMillis());
				long startTime = System.currentTimeMillis();
				AppPager pager = new AppPager();
				if (pageNow <= 0 || pageSize <= 0)
					throw new BusinessException(99998, "非法参数");
				int pageNum = 0;
				String categories = "公司";
				String portfolio_categories = "%" +categories+ "%";
				// 记录总数
				Long pageRecords = Db
						.queryLong("select count(*) from portfolio where portfolio_categories like ? and portfolio_status=6",portfolio_categories);

				AppPagerQuery aPagerQuery = new AppPagerQuery();
				if (pageSize != 0) {
					// 分页查询
					int recordSize = Integer.parseInt(pageRecords + "");
					pageNum = recordSize > 0 ? (recordSize % pageSize == 0 ? recordSize
							/ pageSize : recordSize / pageSize + 1) : 0;
					int offset = (pageNow - 1) * pageSize;

					pager.setHasNext(pageNow >= pageNum ? 0 : 1);
					pager.setHasPrev(pageNow <= 1 ? 0 : 1);
					pager.setPageNow(pageNow);
					pager.setPageNum(pageNum);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(aPagerQuery.getPagerSimple(portfolio_categories, offset, pageSize));

				} else {
					// 全量查询
					pager.setHasNext(0);
					pager.setHasPrev(0);
					pager.setPageNow(1);
					pager.setPageNum(1);
					pager.setRecordSize(Integer.parseInt(pageRecords + ""));
					pager.setPageSize(pageSize);
					pager.setRecords(aPagerQuery.getPagerSimple(portfolio_categories, 0, 0));
				}
				long endTime = System.currentTimeMillis();
				logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
				return pager;
			 
		 }
		 
		 /**
		  * @param reqId
		  * @param pageNow
		  * @param pageSize
		  * @Description: app端组合详情展示
		  */
		 @SuppressWarnings("all")
		 @Override
		public String findAppPagerSpec(String reqId,String isFree,int portfolio_id)throws BusinessException{
			 logger.info("#{}#{}#isFree={},portfolio_id={}#{}#", "app端组合详情展示", reqId, isFree,portfolio_id, System.currentTimeMillis());
				long startTime = System.currentTimeMillis();
				
				if (portfolio_id<0) {
					throw new BusinessException(10004,"非法参数");
				}
				
				//格式化时间
				SimpleDateFormat sDate_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Portfolio portfolio = Portfolio.dao.findById(portfolio_id);
				JSONObject allJSON = null;
				if (portfolio != null) {
					allJSON = new JSONObject();
					//组合信息
					JSONObject portfolioJSON = new JSONObject();
					portfolioJSON.put("title", portfolio.getPortfolio_title());
					portfolioJSON.put("portfolio_summary", portfolio.getPortfolio_summary());
					int day = 0;
					try {
						 day = commonUtils.daysOfTwo(portfolio.getCreatedAt(), sDate_time.parse(sDate_time.format(new Date())));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					portfolioJSON.put("working_day", day);
					portfolioJSON.put("createdAt", portfolio.getCreatedAt());
					allJSON.put("portfolio", portfolioJSON);
					//个人信息
					JSONObject creatorJSON = null;
					PortfolioCreator creator = PortfolioCreator.dao.findFirst("select * from portfolio_creator where user_object_id= ?",portfolio.getUser_object_id());
					if (creator != null) {
						creatorJSON = new JSONObject();
						creatorJSON.put("user_name", creator.getUser_name());
						creatorJSON.put("user_object_id", creator.getUser_object_id());
						creatorJSON.put("user_logo", creator.getUser_logo());
						creatorJSON.put("user_summary", creator.getUser_summary());
						creatorJSON.put("qualificationName", creator.getQualificationName());
						creatorJSON.put("qualificationNum", creator.getQualificationNum());
					}
					allJSON.put("creator", creatorJSON);
					 //收益信息
					JSONObject yieldJSON = null;
					PortfolioYield yield = PortfolioYield.dao.findFirst("select * from portfolio_yield where portfolio_id= ?",portfolio.getId());
					if (yield != null) {
						yieldJSON = new JSONObject();
						yieldJSON.put("total_yield", yield.getTotal_yield());
						yieldJSON.put("today_yield", yield.getToday_yield());
						yieldJSON.put("week_yield", yield.getWeek_yield());
						yieldJSON.put("month_yield", yield.getMonth_yield());
						yieldJSON.put("SZ399300_yield", yield.getSZ399300_yield());
						yieldJSON.put("success_ratio", yield.getSuccess_ratio());
						yieldJSON.put("win_ratio", yield.getWin_ratio());
						yieldJSON.put("max_drawdown", yield.getMax_drawdown());
						yieldJSON.put("month_max_drawdown", yield.getMonth_max_drawdown());
					}
					allJSON.put("yield", yieldJSON);
					 //相关个股
					JSONArray stocksArray = new JSONArray();
					List<CurrentStocks> stocks = CurrentStocks.dao.find("select * from current_stocks where portfolio_id= ?",portfolio.getId());
					if (stocks != null && stocks.size()>0) {
						for (CurrentStocks current_stock : stocks) {
							JSONObject stocksJSON = new JSONObject();
							stocksJSON.put("stock_name", current_stock.getStock_name());
							stocksJSON.put("stock_code", current_stock.getStock_code());
							stocksJSON.put("market_code", current_stock.getMarket_code());
							stocksJSON.put("cost_price", current_stock.getCost_price());
							stocksJSON.put("last_price", current_stock.getLast_price());
							stocksJSON.put("position", current_stock.getPosition());
							stocksJSON.put("quantity", current_stock.getQuantity());
							stocksJSON.put("yield", current_stock.getYield());
						    stocksArray.add(stocksJSON);
						} 
					}
					allJSON.put("current_stocks", stocksArray);
					//收益走势
					JSONArray historyArray = new JSONArray();
					List<HistoryYield> historyYields = HistoryYield.dao.find("select * from history_yield where portfolio_id= ?",portfolio.getId());
					if (historyYields != null && historyYields.size()>0) {
						for (HistoryYield historyYield : historyYields) {
							JSONObject historyJSON = new JSONObject();
							historyJSON.put("trading_day", sDate_time.format(historyYield.getTrading_day()));
							historyJSON.put("yield", historyYield.getYield());
							historyJSON.put("SZ399300_yield", historyYield.getSZ399300_yield());
							historyArray.add(historyJSON);
						} 
					}
					allJSON.put("history_yield", historyArray);
					//股票配置
					JSONArray industryArray = new JSONArray();
					List<PortfolioIndustry> industries = PortfolioIndustry.dao.find("select * from portfolio_industry where portfolio_id= ? and w_from_v2= ?",portfolio.getId(),"industry_v2");
					if (industries != null && industries.size()>0) {
						for (PortfolioIndustry industry : industries) {
							JSONObject industryJSON = new JSONObject();
							industryJSON.put("industry", industry.getIndustry());
							industryJSON.put("stocks", JSONArray.parseArray(industry.getStocks()));
							industryJSON.put("weight", industry.getWeight());
							industryArray.add(industryJSON);
						} 
					}
					allJSON.put("industry", industryArray);
					//历史调仓
					JSONArray dealArray = new JSONArray();
					List<PortfolioDeal> deals = PortfolioDeal.dao.find("select * from portfolio_deal where portfolio_id= ? order by deal_datetime",portfolio.getId());
					if (deals != null && deals.size()>0) {
						for (PortfolioDeal deal : deals) {
							JSONObject dealJSON = new JSONObject();
							dealJSON.put("stock_name", deal.getStock_name());
							dealJSON.put("stock_code", deal.getStock_code());
							dealJSON.put("price", deal.getPrice());
							dealJSON.put("deal_datetime", sDate_time.format(deal.getDeal_datetime()));
							dealJSON.put("yield", deal.getYield());
							dealJSON.put("way", deal.getWay());
							dealJSON.put("market_code", deal.getMarket_code());
							dealJSON.put("quantity", deal.getQuantity());
							dealArray.add(dealJSON);
						} 
					}
					allJSON.put("portfolio_deal", dealArray);
				}
				String allString = allJSON.toJSONString();
				long endTime = System.currentTimeMillis();
				logger.info("#{}#cost_time:{}ms#", reqId, (endTime - startTime));
				return allString;
			 
		 }
    
		
		public static void main(String[] args) {
//			DbManager.start();
//			IntelligentServiceImpl impl = new IntelligentServiceImpl();
//			PortfolioInfo portfolioInfo = new PortfolioInfo();
//			portfolioInfo.setPortfolio_name("大牛组合");
//			portfolioInfo.setPortfolio_title("稳定增长");
//			portfolioInfo.setPortfolio_summary("summary");
//			portfolioInfo.setPortfolio_labels("[{\'label\': \'稳定\'},{\'label\': \'可控\'}]");
//			portfolioInfo.setRisk_level(2);
//			portfolioInfo.setValid_start_date("2017-07-04 14:23:32");
//			portfolioInfo.setPortfolio_categories("投顾个人");
//			portfolioInfo.setPortfolio_status(6);
//			portfolioInfo.setAdviser_object_id("5833e46954ca760008a35c98");
//			portfolioInfo.setAdviser_name("陈竹音");
//			portfolioInfo.setAdviser_logo("http://app.qkzhi.com/e17b971773eaf41df259.png");
//			portfolioInfo.setAdviser_summary("追求风险可控的稳定增长");
//			portfolioInfo.setQualificationName("四川省钱坤证券投资咨询有限公司");
//			portfolioInfo.setQualificationNum("A0660616040001");
//			try {
//				impl.addPortfolio("", portfolioInfo);
//			} catch (BusinessException e) {
//				e.printStackTrace();
//			}
//			DbManager.start();
//			IntelligentServiceImpl impl = new IntelligentServiceImpl();
//			try {
//				impl.findAppPagerSpec("", "", 1);
//			} catch (BusinessException e) {
//				e.printStackTrace();
//			}
		}

}
