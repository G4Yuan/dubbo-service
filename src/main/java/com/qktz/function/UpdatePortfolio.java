package com.qktz.function;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.Db;
import com.qktz.consts.Constants;
import com.qktz.consts.DataQueryMC;
import com.qktz.mgr.DbManager;
import com.qktz.model.Adviser;
import com.qktz.model.CurrentStocks;
import com.qktz.model.HistoryStocks;
import com.qktz.model.HistoryYield;
import com.qktz.model.LatestOperation;
import com.qktz.model.OperationDetail;
import com.qktz.model.Portfolio;
import com.qktz.model.PortfolioCreator;
import com.qktz.model.PortfolioDeal;
import com.qktz.model.PortfolioDealDynamics;
import com.qktz.model.PortfolioIndustry;
import com.qktz.model.PortfolioYield;
import com.qktz.service.client.ClientService;
import com.qktz.service.thrift.intelligent.BusinessException;
import com.qktz.utils.CollectionUtils;
import com.qktz.utils.HttpRequestUtils;
import com.qktz.utils.tools.ZKTools;

/**
 * @ClassName: UpdatePortfolio
 * @Description: 组合数据更新
 * @author yuanwei
 * @date 2017年6月29日 上午9:50:44
 * @version V1.0
 */
public class UpdatePortfolio implements Constants {

	// 日志组件
	private Logger logger = LoggerFactory.getLogger(UpdatePortfolio.class);
	private Gson gson = new Gson();
	// zookeeper
	Map<String, String> zkMap = ZKTools.getPrivateConfData("portfolio");
	// clientService
	ClientService service = new ClientService();
	// CollectionUtils
	CollectionUtils cUtils = new CollectionUtils();

	/**
	 * @param marketcode
	 * @param stockcode
	 * @Description: 得到股票类型
	 */
	public String getStockCategory(String reqId, String marketCode,
			String stockCode) {
		logger.info("#{}#{}#reqId={},marketcode={},stockcode={}#{}#", "股票种类",
				reqId, marketCode, stockCode, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		if (marketCode == null || stockCode == null)
			try {
				logger.info("#marketCode={},stockCode={}", marketCode,
						stockCode);
				;
				throw new BusinessException(10002, "参数不合法");
			} catch (BusinessException e) {
				logger.error("System Error:{}", e);
			}
		String stockCategory = null;
		if (marketCode.equals("SH")) {
			stockCategory = "上证";
		} else {
			stockCategory = "深证";
		}
		if ((marketCode.equals("SH") && stockCode.equals("2"))
				|| (marketCode.equals("SZ") && stockCode.equals("9"))) {
			stockCategory += "B股";
		} else {
			stockCategory += "A股";
		}

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));

		return stockCategory;
	}

	/**
	 * 
	 * @Title: getInitializationAdviser
	 * @Description: 投顾人初始数据
	 * @param reqId
	 */
	public Map<String, Adviser> getInitializationAdviser(String reqId) {
		logger.info("#{}#{}#reqId={}#{}#", "投顾人初始数据", reqId,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		List<Adviser> advisers = Adviser.dao
				.find("select * from portfolio_adviser");
		if (advisers == null)
			return null;
		Map<String, Adviser> map = new HashMap<String, Adviser>();
		for (Adviser adviser : advisers) {
			map.put(adviser.getUser_object_id(), adviser);
		}

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return map;
	}

	/**
	 * 
	 * @Title: getInitializationPortfolio
	 * @Description: 组合初始数据
	 * @param reqId
	 */
	public List<String> getInitializationPortfolio(String reqId) {
		logger.info("#{}#{}#reqId={}#{}#", "组合初始数据", reqId,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		List<Portfolio> portfolios = Portfolio.dao
				.find("select * from portfolio where portfolio_status=6");
		if (portfolios == null)
			return null;
		List<String> list = new ArrayList<String>();
		for (Portfolio portfolio : portfolios) {
			if (portfolio.getPortfolio_categories() != null) {
				list.add(portfolio.getAccount_id());
			}
		}

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return list;
	}

	/**
	 * @throws ParseException
	 * @throws BusinessException
	 * @Description: 获取组合的Map(资金ID、标题)
	 */
	public Map<String, String> getAccountMapping(String reqId)
			throws ParseException, BusinessException {
		logger.info("#{}#reqId={}#{}#", "获取组合资金ID以及title", reqId,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		// 获取当前时间
		Date date_now = sDate_time.parse(sDate_time.format(new Date()));

		// 获取有效组合 portfolio_status-->6
		List<Portfolio> portfolios = Portfolio.dao
				.find("select * from portfolio where account_id IS NOT NULL and portfolio_title IS NOT NULL and portfolio_status =6");

		Map<String, String> map = new HashMap<String, String>();
		if (portfolios == null || portfolios.size() <= 0) {
			logger.error("Portfolio No Data!");
			throw new BusinessException(10004, "数据表portfolio没有数据");
		} else {
			for (Portfolio portfolio : portfolios) {
				logger.info("#{}#accountId={},portfolio_title={}", "参数：",
						portfolio.getAccount_id(),
						portfolio.getPortfolio_title());
				Date valid_start_date = sDate_time.parse(sDate_time
						.format(portfolio.getValid_start_date()));
				if (valid_start_date == null) {
					logger.warn("valid_start_date is null");
					continue;
				}
				long second = date_now.getTime() - valid_start_date.getTime();
				if (second > 28800) {
					map.put(portfolio.getAccount_id(),
							portfolio.getPortfolio_title());
				}
			}
		}
		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));

		return map;
	}

	/**
	 * @param marketcode
	 * @param stockcode
	 * @throws BusinessException
	 * @Description: 获取组合流水
	 */
	public List<JSONObject> getPortfolioDeal(String reqId, String account,
			Date start_date, Date end_date) throws BusinessException {
		logger.info("#{}#reqId={},account={},start_date={},end_date={}#{}#",
				"获取组合流水", reqId, account, start_date, end_date,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		int page_index = 1;
		JSONObject resultSet = null;
		List<JSONObject> results = new ArrayList<JSONObject>();

		while (true) {
			try {
				resultSet = service.run(
						zkMap.get("URL"),
						"Query_StockTransList",
						new Object[] { account, "", sDate.format(start_date),
								sDate.format(end_date), PAGE_SIZE, page_index,
								zkMap.get("COORDINATES"),
								zkMap.get("ENCRYPTIONCHAR") });
				logger.info("#{}#resultSet={}#", "返回参数", gson.toJson(resultSet));
			} catch (Exception e) {
				logger.error("System Error:{}", e);
			}
			if (resultSet == null) {
				logger.error("The Request Returned Data Is Null!");
				throw new BusinessException(10002, "请求返回数据为空");
			}
			// 取得code
			int code = resultSet.getIntValue("Code");
			if (code == 0) {
				String DataObj = resultSet.getString("DataObj");
				JSONArray reArray = JSONArray.parseArray(DataObj);
				if (reArray == null || reArray.size() <= 0) {
					break;
				}
				for (Object object : reArray) {
					results.add((JSONObject) object);
				}
				page_index += 1;
			} else if (code == -100) {
				logger.error("{} get empty deals", account);

				long endTime = System.currentTimeMillis();
				logger.info("#cost_time:{}ms#", (endTime - startTime));
				return results;
			} else {
				logger.error("Error, update portfolio deal, {} {}", account,
						"Code is -1000, can not use 0 be the denominator");

				long endTime = System.currentTimeMillis();
				logger.info("#cost_time:{}ms#", (endTime - startTime));
				return results;
			}
		}

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return results;
	}

	/**
	 * @param reqId
	 * @param account
	 * @Description: 获取流水表的最后更新时间
	 */
	public Date getLastUpdateDate(String reqId, String account)
			throws ParseException {
		logger.info("#{}#reqId={},account={}#{}#", "获取流水表的最后更新时间", reqId,
				account, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		// 格式化时间
		Date resultDate = null;
		try {
			PortfolioDeal portfolioDeal = PortfolioDeal.dao
					.findFirst(
							"select * from portfolio_deal where accountId= ? order by deal_datetime DESC limit 1",
							account);
			logger.info("portfolioDeal:{}", gson.toJson(portfolioDeal));
			if (portfolioDeal != null) {
				Date deal_datetime = portfolioDeal.getDeal_datetime();
				if (deal_datetime != null) {
					resultDate = sDate_time.parse(sDate_time
							.format(deal_datetime));
				}else {
					resultDate = sDate_time.parse(DEFAULT_DATE);
				}
			}
		} catch (Exception e) {
			logger.error("System Error:{}",e);
		}
		resultDate = sDate_time.parse(DEFAULT_DATE);
		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return resultDate;
	}

	/**
	 * @throws BusinessException
	 * @throws AVException
	 * @Description:通过资金ID 得到组合
	 */
	public Portfolio getPortfolioByAccount(String reqId, String account)
			throws BusinessException {
		logger.info("#{}#reqId={},account={}#{}#", "通过资金ID得到组合", reqId,
				account, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();
		Portfolio portfolio = Portfolio.dao
				.findFirst(
						"select * from portfolio where account_id= ? AND portfolio_status=6",
						account);
		if (portfolio == null)
			throw new BusinessException(10004, "根据Account未得到portfolio表的数据");

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return portfolio;
	}

	/**
	 * @param portfolio
	 * @param acount
	 * @param start_date
	 * @param end_date
	 * @Description:获取成功率
	 */
	@SuppressWarnings("all")
	public double getSuccessRatio(String reqId, Portfolio portfolio,
			String account, Date start_date, Date end_date) throws Exception {
		logger.info(
				"#{}#reqId={},portfolio={},account={}start_date={},end_date={}#{}#",
				"获取成功率", reqId, gson.toJson(portfolio), account, start_date,
				end_date, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		Date startDate = sDate_zero.parse(sDate_time.format(start_date));
		Date endDate = sDate_zero.parse(sDate_time.format(end_date));

		JSONObject resultSet = null;
		try {
			resultSet = service.run(
					zkMap.get("URL"),
					"Query_DataSetList",
					new Object[] { account, 5,
							cUtils.getXMLGregorianCalendar(startDate),
							cUtils.getXMLGregorianCalendar(endDate), 10,
							zkMap.get("COORDINATES"),
							zkMap.get("ENCRYPTIONCHAR") });
		} catch (Exception e) {
			logger.error("System Error:{}", e);
		}
		JSONArray jsonArray = JSONArray.parseArray(resultSet
				.getString("DataObj"));
		double success_ratio = 0;
		if (jsonArray == null || jsonArray.size() <= 0) {
			success_ratio = 0;
		} else {
			success_ratio = jsonArray.getJSONObject(0).getDouble("SuccessRate") * 100.0;
		}

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return success_ratio;
	}

	/**
	 * @param portfolio
	 * @param account
	 * @param start_date
	 * @param end_date
	 * @Description:获取胜率
	 */
	@SuppressWarnings("all")
	public double getWinRatio(String reqId, Portfolio portfolio,
			String account, Date start_date, Date end_date) throws Exception {
		logger.info(
				"#{}#reqId={},portfolio={},account={}start_date={},end_date={}#{}#",
				"获取胜率", reqId, gson.toJson(portfolio), account, start_date,
				end_date, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		JSONObject resultSet = null;
		try {
			resultSet = service.run(
					zkMap.get("URL"),
					"Query_DataSetList",
					new Object[] { account, 4,
							cUtils.getXMLGregorianCalendar(start_date),
							cUtils.getXMLGregorianCalendar(end_date), 10,
							zkMap.get("COORDINATES"),
							zkMap.get("ENCRYPTIONCHAR") });
		} catch (Exception e) {
			logger.error("System Error:{}", e);
		}
		JSONArray jsonArray = JSONArray.parseArray(resultSet
				.getString("DataObj"));
		double win_ratio = 0;
		if (jsonArray == null || jsonArray.size() <= 0) {
			win_ratio = 0;
		} else {
			win_ratio = jsonArray.getJSONObject(0).getDouble("Odds") * 100.0;
		}

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return win_ratio;
	}

	/**
	 * @param portfolio
	 * @param account
	 * @param start_date
	 * @param end_date
	 * @Description:得到当前仓位
	 */
	@SuppressWarnings("all")
	public double getCurrentPosition(String reqId, Portfolio portfolio,
			String account, Date start_date, Date end_date) throws Exception {
		logger.info(
				"#{}#reqId={},portfolio={},account={}start_date={},end_date={}#{}#",
				"得到当前仓位", reqId, gson.toJson(portfolio), account, start_date,
				end_date, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		JSONObject resultSet = null;
		try {
			resultSet = service.run(
					zkMap.get("URL"),
					"Query_DataSetList",
					new Object[] { account, 6,
							cUtils.getXMLGregorianCalendar(start_date),
							cUtils.getXMLGregorianCalendar(end_date), 10,
							zkMap.get("COORDINATES"),
							zkMap.get("ENCRYPTIONCHAR") });
		} catch (Exception e) {
			logger.error("System Error:{}", e);
		}

		if (resultSet.getIntValue("Code") != 0) {
			logger.error("Error, get {} current position failed: {}", account,
					resultSet.getString("Message"));
			return 0;
		}
		JSONArray position_list = JSONArray.parseArray(resultSet
				.getString("DataObj"));
		if (position_list == null || position_list.size() <= 0) {
			logger.error("Current_Position Is Null!");
		}
		double current_position = 0;

		for (Object position : position_list) {
			JSONObject poJSON = (JSONObject) position;
			if (poJSON.getString("Tradedate").equals(
					sDate_zero.format(end_date))) {
				current_position = poJSON.getDouble("Positions") * 100.0;
			}
		}

		logger.info("current_position is {}", current_position);

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return current_position;
	}

	/**
	 * 获取最大回撤
	 * 
	 * @param portfolio
	 * @param account
	 * @param start_date
	 * @param end_date
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public Map<String, Double> getDrawdown(String reqId, Portfolio portfolio,
			String account, Date start_date, Date end_date) throws Exception {
		logger.info(
				"#{}#reqId={},portfolio={},account={}start_date={},end_date={}#{}#",
				"获取最大回撤", reqId, gson.toJson(portfolio), account, start_date,
				end_date, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		JSONObject resultSet = null;
		try {
			resultSet = service.run(
					zkMap.get("URL"),
					"Query_DataSetList",
					new Object[] { account, 7,
							cUtils.getXMLGregorianCalendar(start_date),
							cUtils.getXMLGregorianCalendar(end_date), 10,
							zkMap.get("COORDINATES"),
							zkMap.get("ENCRYPTIONCHAR") });
		} catch (Exception e) {
			logger.error("System Error:{}", e);
		}
		int code = resultSet.getInteger("Code");
		JSONArray reArray = null;
		if (code == 0) {
			String DataObj = resultSet.getString("DataObj");
			reArray = JSONArray.parseArray(DataObj);
			if (reArray == null || reArray.size() <= 0) {
				logger.info("Error, {} get all yields failed because empty",
						account);
			}

		} else if (code == -100) {
			logger.error("{}  get drawdown empty", account);

			long endTime = System.currentTimeMillis();
			logger.info("#cost_time:{}ms#", (endTime - startTime));
			return null;
		} else {
			logger.error("Error, get {} current position failed: {}", account,
					resultSet.getString("Message"));
			long endTime = System.currentTimeMillis();
			logger.info("#cost_time:{}ms#", (endTime - startTime));
			return null;
		}
		Map<String, Double> map = new HashMap<String, Double>();
		// 得到DataObj中的maxDown
		double max_drawdown = reArray.getJSONObject(0).getDouble("maxdown") * 100.0;
		map.put("max_drawdown", Math.abs(max_drawdown));

		// 得到start_date数据,前一个月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(end_date);
		calendar.add(Calendar.MONTH, -1);
		start_date = calendar.getTime();
		JSONObject resultSet_mouth = null;
		try {
			resultSet_mouth = service.run(
					zkMap.get("URL"),
					"Query_DataSetList",
					new Object[] { account, 7,
							cUtils.getXMLGregorianCalendar(start_date),
							cUtils.getXMLGregorianCalendar(end_date), 10,
							zkMap.get("COORDINATES"),
							zkMap.get("ENCRYPTIONCHAR") });
		} catch (Exception e) {
			logger.error("System Error:{}", e);
		}

		int code_month = resultSet_mouth.getInteger("Code");
		JSONArray reArray_month = null;
		if (code == 0) {
			String DataObj = resultSet_mouth.getString("DataObj");
			reArray_month = JSONArray.parseArray(DataObj);
			if (reArray_month == null || reArray_month.size() <= 0) {
				logger.info("Error, {} get all yields failed because empty",
						account);
			}

		} else if (code == -100) {
			logger.error("{}  get drawdown empty", account);

			long endTime = System.currentTimeMillis();
			logger.info("#cost_time:{}ms#", (endTime - startTime));
			return null;
		} else {
			logger.error("Error, get {} drawdown failed: {}", account,
					resultSet.getString("Message"));
			long endTime = System.currentTimeMillis();
			logger.info("#cost_time:{}ms#", (endTime - startTime));
			return null;
		}

		double month_max_drawdown = reArray_month.getJSONObject(0).getDouble(
				"maxdown") * 100.0;
		// 得到DataObj中的maxDown
		map.put("month_max_drawdown", Math.abs(month_max_drawdown));

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return map;
	}
	
	/**
	 * @throws BusinessException
	 * @Description:获取最高收益组合
	 */
	public PortfolioYield getPortfolioMaxYield(String reqId, String user_object_id)
			throws BusinessException {
		logger.info("#{}#reqId={},user_object_id={}#{}#", "获取最高收益组合", reqId,
				user_object_id, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		List<Portfolio> porList = Portfolio.dao.find("select * from portfolio where user_object_id= ?",user_object_id);
        Map<Integer, Double> map = null;
		if (porList != null && porList.size()>0) {
			map = new HashMap<Integer, Double>();
			for (Portfolio pfl : porList) {
				PortfolioYield yield = PortfolioYield.dao.findFirst("select * from portfolio_yield where portfolio_id= ?",pfl.getId());
				map.put(yield.getId(), yield.getTotal_yield());
			}
		}
		if (map == null) return null;
		
		int yield_id = 0;
		double max_yield = 0;
		for (Map.Entry<Integer, Double> pflMap : map.entrySet()) {
			if (pflMap.getValue()>max_yield) {
				max_yield = pflMap.getValue();
				yield_id = pflMap.getKey();
			}
		}
		
		//得到最大收益组合
		PortfolioYield yield = null;
		if (yield_id>0) {
			yield = PortfolioYield.dao.findById(yield_id);
		}
		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return yield;
	}

	/**
	 * @Description: 是否需要发送message
	 * @param account
	 * @throws BusinessException
	 */
	public boolean needSendMessage(String reqId, String account)
			throws BusinessException {
		logger.info("#{}#reqId={},account={}#{}#", "是否需要发送message", reqId,
				account, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		Portfolio portfolio = getPortfolioByAccount(reqId, account);
		String categories = portfolio.getPortfolio_categories();
		if (StringUtils.isNotBlank(NEED_SEND_MESSAGE_CATEGORY)
				&& StringUtils.isNotBlank(categories)) {

			long endTime = System.currentTimeMillis();
			logger.info("#cost_time:{}ms#", (endTime - startTime));
			return true;
		} else {

			long endTime = System.currentTimeMillis();
			logger.info("#cost_time:{}ms#", (endTime - startTime));
			return false;
		}
	}

	/**
	 * @Description: 更新历史个股
	 * @param reqId
	 * @param portfolio_id
	 * @param stockJSON
	 * @param BusinessException
	 */
	@SuppressWarnings("all")
	public void updateHistoryStocks(String reqId,
			Map<String, HistoryStocks> map, JSONArray stockArray,int portfolio_id)
			throws BusinessException {
		logger.info("#{}#reqId={},map={},stock={},portfolio_id={}#{}#", "更新历史个股", reqId,
				gson.toJson(map), gson.toJson(stockArray),portfolio_id,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		List<HistoryStocks> hStocks = new ArrayList<HistoryStocks>();
		List<HistoryStocks> hStocks_u = new ArrayList<HistoryStocks>();
		if (map == null || map.size()<=0) {
			for (Object object : stockArray) {
				JSONObject stockJSON = (JSONObject) object;
				HistoryStocks historyStock = new HistoryStocks();
				if (!stockJSON.getString("TransStyle").equals("卖")) {
					historyStock.setBuy_price(stockJSON.getDouble("Price"));
					historyStock.setSell_price(null);
					historyStock.setPrice(stockJSON.getDouble("Price"));
					historyStock.setMarket_code(stockJSON.getString("Market"));
					historyStock
							.setStock_code(stockJSON.getString("StockCode"));
					historyStock.setStock_name(stockJSON
							.getString("StockShortName"));
					JSONObject json_now = new JSONObject();
					json_now.put("DaiMa", stockJSON.getString("Market")
							+ stockJSON.getString("StockCode"));
					json_now.put("MingCheng",
							stockJSON.getString("StockShortName"));
					historyStock.setStock_info(json_now.toJSONString());

					historyStock.setYield(stockJSON.getDouble("syl"));
					historyStock.setOperation_datetime(stockJSON
							.getDate("cjdatetime"));
					historyStock.setPortfolio_id(portfolio_id);
					hStocks.add(historyStock);
				} else if (!stockJSON.getString("TransStyle").equals("买")) {
					historyStock.setSell_price(stockJSON.getDouble("Price"));
					historyStock.setPrice(stockJSON.getDouble("Price"));
					historyStock.setMarket_code(stockJSON.getString("Market"));
					historyStock
							.setStock_code(stockJSON.getString("StockCode"));
					historyStock.setStock_name(stockJSON
							.getString("StockShortName"));
					JSONObject json_now = new JSONObject();
					json_now.put("DaiMa", stockJSON.getString("Market")
							+ stockJSON.getString("StockCode"));
					json_now.put("MingCheng",
							stockJSON.getString("StockShortName"));
					historyStock.setStock_info(json_now.toJSONString());

					historyStock.setYield(stockJSON.getDouble("syl"));
					historyStock.setOperation_datetime(stockJSON
							.getDate("cjdatetime"));
					historyStock.setPortfolio_id(portfolio_id);
					hStocks.add(historyStock);
				} else {
					logger.warn("This deal type is not buy or sell");
				}
			}
		} else {
			for (Object object : stockArray) {
				JSONObject stockJSON = (JSONObject) object;
				if (map.containsKey(stockJSON.getString("StockCode"))) {
					HistoryStocks historyStock = map.get(stockJSON
							.getString("StockCode"));
					if (!stockJSON.getString("TransStyle").equals("卖")) {
						historyStock.setBuy_price(stockJSON.getDouble("Price"));
						historyStock.setSell_price(null);
						historyStock.setPrice(stockJSON.getDouble("Price"));
						historyStock.setMarket_code(stockJSON
								.getString("Market"));
						historyStock.setStock_code(stockJSON
								.getString("StockCode"));
						historyStock.setStock_name(stockJSON
								.getString("StockShortName"));
						JSONObject json_now = new JSONObject();
						json_now.put("DaiMa", stockJSON.getString("Market")
								+ stockJSON.getString("StockCode"));
						json_now.put("MingCheng",
								stockJSON.getString("StockShortName"));
						historyStock.setStock_info(json_now.toJSONString());

						historyStock.setYield(stockJSON.getDouble("syl"));
						historyStock.setOperation_datetime(stockJSON
								.getDate("cjdatetime"));
						hStocks_u.add(historyStock);
					} else if (!stockJSON.getString("TransStyle").equals("买")) {
						historyStock
								.setSell_price(stockJSON.getDouble("Price"));
						historyStock.setPrice(stockJSON.getDouble("Price"));
						historyStock.setMarket_code(stockJSON
								.getString("Market"));
						historyStock.setStock_code(stockJSON
								.getString("StockCode"));
						historyStock.setStock_name(stockJSON
								.getString("StockShortName"));
						JSONObject json_now = new JSONObject();
						json_now.put("DaiMa", stockJSON.getString("Market")
								+ stockJSON.getString("StockCode"));
						json_now.put("MingCheng",
								stockJSON.getString("StockShortName"));
						historyStock.setStock_info(json_now.toJSONString());

						historyStock.setYield(stockJSON.getDouble("syl"));
						historyStock.setOperation_datetime(stockJSON
								.getDate("cjdatetime"));
						hStocks_u.add(historyStock);
					} else {
						logger.warn("This deal type is not buy or sell");
					}
				} else {
					HistoryStocks historyStock = new HistoryStocks();
					if (!stockJSON.getString("TransStyle").equals("卖")) {
						historyStock.setBuy_price(stockJSON.getDouble("Price"));
						historyStock.setSell_price(null);
						historyStock.setPrice(stockJSON.getDouble("Price"));
						historyStock.setMarket_code(stockJSON
								.getString("Market"));
						historyStock.setStock_code(stockJSON
								.getString("StockCode"));
						historyStock.setStock_name(stockJSON
								.getString("StockShortName"));
						JSONObject json_now = new JSONObject();
						json_now.put("DaiMa", stockJSON.getString("Market")
								+ stockJSON.getString("StockCode"));
						json_now.put("MingCheng",
								stockJSON.getString("StockShortName"));
						historyStock.setStock_info(json_now.toJSONString());

						historyStock.setYield(stockJSON.getDouble("syl"));
						historyStock.setOperation_datetime(stockJSON
								.getDate("cjdatetime"));
						historyStock.setPortfolio_id(portfolio_id);
						hStocks.add(historyStock);
					} else if (!stockJSON.getString("TransStyle").equals("买")) {
						historyStock
								.setSell_price(stockJSON.getDouble("Price"));
						historyStock.setPrice(stockJSON.getDouble("Price"));
						historyStock.setMarket_code(stockJSON
								.getString("Market"));
						historyStock.setStock_code(stockJSON
								.getString("StockCode"));
						historyStock.setStock_name(stockJSON
								.getString("StockShortName"));
						JSONObject json_now = new JSONObject();
						json_now.put("DaiMa", stockJSON.getString("Market")
								+ stockJSON.getString("StockCode"));
						json_now.put("MingCheng",
								stockJSON.getString("StockShortName"));
						historyStock.setStock_info(json_now.toJSONString());

						historyStock.setYield(stockJSON.getDouble("syl"));
						historyStock.setOperation_datetime(stockJSON
								.getDate("cjdatetime"));
						historyStock.setPortfolio_id(portfolio_id);
						hStocks.add(historyStock);
					} else {
						logger.warn("This deal type is not buy or sell");
					}
				}
			}
		}

		//批量更新
		if (hStocks_u != null && hStocks_u.size()>0) {
			Db.batchUpdate(hStocks_u, 1000);
		}
		//批量保存
		Db.batchSave(hStocks, 1000);
		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
	}

	/**
	 * @Description: 更新组合流水
	 * @throws BusinessException
	 * @throws ParseException
	 */
	public PortfolioDeal updatePortfolioDeal(String reqId, String account,
			JSONObject dealJSON,Portfolio portfolio) throws BusinessException, ParseException {
		logger.info("#{}#reqId={},account={},dealJSON={},portfolio={}#{}#", "更新组合流水", reqId,
				account, gson.toJson(dealJSON),portfolio, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		if (dealJSON == null || account == null)
			return null;
		// 创建PortfolioDeal对象
		PortfolioDeal portfolioDeal = new PortfolioDeal();

		portfolioDeal.setAccountId(account);
		portfolioDeal.setMarket_code(dealJSON.getString("Market"));
		portfolioDeal.setRecorder_id(dealJSON.getInteger("transRecordid"));
		portfolioDeal.setStock_code(dealJSON.getString("StockCode"));
		portfolioDeal.setStock_name(dealJSON.getString("StockShortName"));
		portfolioDeal.setPrice(dealJSON.getDouble("Price"));
		portfolioDeal.setQuantity(dealJSON.getInteger("Volume"));
		portfolioDeal.setTurnover(dealJSON.getDouble("cjje"));
		portfolioDeal.setYield(dealJSON.getDouble("syl"));
		if (dealJSON.get("TransStyle").equals("卖")) {
			portfolioDeal.setWay("Sell");
		} else if (dealJSON.get("TransStyle").equals("分红")) {
			portfolioDeal.setWay("Dividend");
		} else if (dealJSON.get("TransStyle").equals("送股")) {
			portfolioDeal.setWay("PayingStock");
		} else {
			portfolioDeal.setWay("Buy");
		}
		portfolioDeal.setDeal_datetime(dealJSON.getDate("cjdatetime"));
		portfolioDeal.setCreatedAt(sDate_time.parse(sDate_time.format(new Date())));
		portfolioDeal.setUpdatedAt(sDate_time.parse(sDate_time.format(new Date())));
        portfolioDeal.setPortfolio_id(portfolio.getId());
		
		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return portfolioDeal;
	}

	/**
	 * @Description: 更新组合流水、组合历史个股
	 * @throws BusinessException
	 * @throws ParseException
	 */
	public boolean updateDealAndHistory(String reqId, String account)
			throws BusinessException, ParseException {
		logger.info("#{}#reqId={},account={}#{}#", "更新组合流水、组合历史个股", reqId,
				account, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		boolean need_send_msessage = false;
		Portfolio portfolio = getPortfolioByAccount(reqId, account);
		Date start_date = getLastUpdateDate(reqId, account);
		Date end_date = sDate_time.parse(sDate_time.format(new Date()));

		// 将结果的recorder_id作为Key,id作为Value
		Map<Integer, Integer> portfolioDealMapping = new HashMap<Integer, Integer>();
		try {
			String stringDate = sDate_zero.format(start_date);
			List<PortfolioDeal> portfoliodeals = PortfolioDeal.dao
					.find("select * from portfolio_deal where accountId= ? and deal_datetime>= ?",
							account, stringDate);
			if (portfoliodeals == null || portfoliodeals.size() <= 0)
				throw new BusinessException(10004, "未取到portfoliodeal对象");
			for (PortfolioDeal portfolioDeal : portfoliodeals) {
				portfolioDealMapping.put(portfolioDeal.getRecorder_id(),
						portfolioDeal.getId());
			}
		} catch (Exception e) {
			logger.error("Error,update portfolio deal,{}", e);
		}

		List<JSONObject> latest_deals = getPortfolioDeal(reqId, account,
				start_date, end_date);
		if (latest_deals.size() == 0)
			return need_send_msessage;

		//历史股票数据
		Map<String, HistoryStocks> map = new HashMap<String, HistoryStocks>();
		List<HistoryStocks> historyStocks = HistoryStocks.dao.find(
				"select * from history_stocks where portfolio_id= ?",
				portfolio.getId());
		if (historyStocks != null && historyStocks.size()>0) {
			for (HistoryStocks hStocks : historyStocks) {
				map.put(hStocks.getStock_code(), hStocks);
			}			
		}		
		// 创建PortfolioDeal对象
		PortfolioDeal portfolioDeal = null;
		List<PortfolioDeal> portfolioDeals = new ArrayList<PortfolioDeal>();
		JSONArray jsonArray = new JSONArray();
		for (int i = latest_deals.size() - 1; i >= 0; i--) {
			try {
				int recorder_id = latest_deals.get(i).getIntValue(
						"transRecordid");
				if (!portfolioDealMapping.containsKey(recorder_id)) {
					need_send_msessage = true;
					portfolioDeal = updatePortfolioDeal(reqId, account,
							latest_deals.get(i),portfolio);
					portfolioDeals.add(portfolioDeal);
                    //传递历史参数
					jsonArray.add(latest_deals.get(i));
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 更新历史个股
		updateHistoryStocks(reqId, map,
				jsonArray,portfolio.getId());
		
		// 批量更新组合流水
		Db.batchSave(portfolioDeals, 1000);

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return need_send_msessage;
	}

	/**
	 * @Description: 更新当前的股票
	 * @param reqId
	 * @param account
	 * @param portfolio
	 */
	@SuppressWarnings("all")
	public String updateCurrentStocks(String reqId, String account,
			Portfolio portfolio) {
		logger.info("#{}#reqId={},account={},portfolio={}#{}#", "更新当前的股票",
				reqId, account, gson.toJson(portfolio),
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		JSONObject resultSet = null;
		try {
			// 转成Json对象
			resultSet = service.run(
					zkMap.get("URL"),
					"P_W_Sel_MyCg_1",
					new Object[] { account, zkMap.get("COORDINATES"),
							zkMap.get("ENCRYPTIONCHAR") });
		} catch (Exception e) {
			logger.error("System Error:{}", e);
		}

		JSONArray reArray = null;
		if (resultSet == null)
			return null;
		int code = resultSet.getIntValue("Code");
		if (code == 0) {
			String DataObj = resultSet.getString("DataObj");
			reArray = JSONArray.parseArray(DataObj);
			if (reArray == null || reArray.size() <= 0) {
				logger.error("DataObj Is Null !");
			}

		} else if (code == -100) {
			logger.error("{} Current Stock is empty", account);

			long endTime = System.currentTimeMillis();
			logger.info("#cost_time:{}ms#", (endTime - startTime));
			return null;
		} else {
			logger.error("Error, {} current stock failed, {}", account,
					resultSet.getString("Message"));

			long endTime = System.currentTimeMillis();
			logger.info("#cost_time:{}ms#", (endTime - startTime));
			return null;
		}

		List<CurrentStocks> list = new ArrayList<CurrentStocks>();
		List<String> stocksname = new ArrayList<String>();
		// 处理当前股票
		for (Object object : reArray) {
			JSONObject current_stocks = (JSONObject) object;
			if (current_stocks.getIntValue("currentVolume") == 0)
				continue;
			CurrentStocks currentStocks = new CurrentStocks();
			currentStocks.setCost_price(current_stocks.getDouble("cbj"));
			currentStocks.setLast_price(current_stocks.getDouble("Price"));
			currentStocks
					.setMarket_code(current_stocks.getString("marketcode"));
			currentStocks.setStock_code(current_stocks.getString("stockcode"));
			currentStocks.setStock_name(current_stocks
					.getString("stockshortname"));
			currentStocks.setPosition(current_stocks
					.getIntValue("currentVolume"));
			currentStocks.setQuantity(current_stocks
					.getIntValue("currentVolume"));
			JSONObject json = new JSONObject();
			json.put("DaiMa", current_stocks.getString("marketcode")
					+ current_stocks.getString("stockcode"));
			json.put("MingChen", current_stocks.getString("stockshortname"));
			json.put("ShuXing", current_stocks.getString("marketName")
					+ current_stocks.getString("stockTypeName"));
			currentStocks.setStock_info(json.toJSONString());
			double yield = (current_stocks.getDouble("Price") - current_stocks
					.getDouble("cbj"))
					* 100.0
					/ current_stocks.getDouble("cbj");
			currentStocks.setYield(yield);
			currentStocks.setPortfolio_id(portfolio.getId());
			// 添加list
			list.add(currentStocks);
			stocksname.add(current_stocks.getString("stockshortname"));
		}

		// 批量操作
		List<CurrentStocks> currentStocks = CurrentStocks.dao.find("select * from current_stocks where portfolio_id= ?",portfolio.getId());
		if (currentStocks!=null && currentStocks.size()>0) {
			Db.update("delete from current_stocks where portfolio_id= ?",
					portfolio.getId());			
		}
		Db.batchSave(list, 1000);
		String join = "";
		for (int i = 0; i < stocksname.size(); i++) {
			if (i != stocksname.size() - 1) {
				join = join + stocksname.get(i) + "&";
			} else {
				join = join + stocksname.get(i);
			}
		}

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return join;
	}

	/**
	 * @Description: 更新组合的收益
	 * @param reqId
	 * @param account
	 * @param portfolio
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public PortfolioYield updatePortfolioYields(String reqId, String account,
			Portfolio portfolio,Date last_update_day,Date last_trading_day) throws Exception {
		logger.info(
				"#{}#reqId={},account={},portfolio={},last_update_day={},last_trading_day={}#{}#",
				"更新组合的收益", reqId, account, gson.toJson(portfolio),
				last_update_day, last_update_day, System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		if (portfolio == null) {
			logger.error("Parameter Error:portfolio is null!");
			return null;
		}
		JSONObject resultSet = null;
		try {
			resultSet = service.run(
					zkMap.get("URL_V2"),
					"P_W_SEL_DATA_TG",
					new Object[] { account, 1, zkMap.get("COORDINATES"),
							zkMap.get("ENCRYPTIONCHAR") });
		} catch (Exception e) {
			logger.error("System Error:{}", e);
		}

		JSONArray reArray = null;
		if (resultSet == null)
			return null;
		int code = resultSet.getIntValue("Code");
		if (code == 0) {
			String DataObj = resultSet.getString("DataObj");
			reArray = JSONArray.parseArray(DataObj);
			if (reArray == null || reArray.size() <= 0) {
				logger.info("Error, {} get all yields failed because empty",
						account);
			}

		} else if (code == -100) {
			logger.error("{} get all yields failed because empty", account);

			long endTime = System.currentTimeMillis();
			logger.info("#cost_time:{}ms#", (endTime - startTime));
			return null;
		} else {
			logger.error("Error, get {} all yield failed, {}", account,
					resultSet.getString("Message"));
			throw new BusinessException(10002, "Response Error");
		}

		JSONObject yields = reArray.getJSONObject(reArray.size() - 1);
		Double Nan = null;
		// success_ratio
		Double success_ratio = getSuccessRatio(reqId, portfolio, account,
				last_update_day, last_trading_day);
		// win_ratio
		Double win_ratio = getWinRatio(reqId, portfolio, account,
				last_update_day, last_trading_day);
		// drawdown
		Map<String, Double> map = getDrawdown(reqId, portfolio, account,
				last_update_day, last_trading_day);

		// 创建对象
		PortfolioYield portfolioYield = new PortfolioYield();
		if (!yields.getString("Ror_D").equals(""))
			portfolioYield.setToday_yield(new BigDecimal(Double
					.parseDouble(yields.getString("Ror_D"))).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue());
		else
			portfolioYield.setToday_yield(Nan);

		if (!yields.getString("Ror_W").equals(""))
			portfolioYield.setWeek_yield(new BigDecimal(Double
					.parseDouble(yields.getString("Ror_W"))).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue());
		else
			portfolioYield.setWeek_yield(Nan);

		if (!yields.getString("Ror_M").equals(""))
			portfolioYield.setMonth_yield(new BigDecimal(Double
					.parseDouble(yields.getString("Ror_M"))).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue());
		else
			portfolioYield.setMonth_yield(Nan);

		if (!yields.getString("Ror").equals(""))
			portfolioYield.setTotal_yield(new BigDecimal(Double
					.parseDouble(yields.getString("Ror"))).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue());
		else
			portfolioYield.setToday_yield(Nan);

		if (!yields.getString("Ror_Y").equals(""))
			portfolioYield.setAnnualized_yield(new BigDecimal(Double
					.parseDouble(yields.getString("Ror_Y"))).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue());
		else
			portfolioYield.setAnnualized_yield(Nan);
		portfolioYield.setTotal_value(yields.getDouble("Capital"));
		portfolioYield.setCash(yields.getDouble("ResidualCapital"));

		if (!yields.getString("Positions").equals(""))
			portfolioYield.setCurrent_position(new BigDecimal(Double
					.parseDouble(yields.getString("Positions"))).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue());
		else
			portfolioYield.setCurrent_position(Nan);

		if (!yields.getString("syl300").equals(""))
			portfolioYield.setSZ399300_yield(new BigDecimal(Double
					.parseDouble(yields.getString("syl300"))).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue());
		else
			portfolioYield.setSZ399300_yield(Nan);

		portfolioYield.setSuccess_ratio(success_ratio);
		portfolioYield.setWin_ratio(win_ratio);
		portfolioYield.setMax_drawdown(map.get("max_drawdown"));
		portfolioYield.setMonth_max_drawdown(map.get("month_max_drawdown"));
		portfolioYield.setPortfolio_id(portfolio.getId());

		List<PortfolioYield> pYields = PortfolioYield.dao.find("select * from portfolio_yield where portfolio_id= ?",portfolio.getId());
		if (pYields != null || pYields.size()>0) {
			Db.update("delete from portfolio_yield where portfolio_id= ?",portfolio.getId());
		}
		// 保存
		portfolioYield.save();
		logger.info("portfilioYield对象是：{}", portfolioYield);
		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return portfolioYield;
	}

	/**
	 * @Description: 更新组合的行业
	 * @param reqId
	 * @param portfolio
	 * @param account
	 * @throws BusinessException
	 */
	@SuppressWarnings("all")
	public void updateSwIndustryV2(String reqId, Portfolio portfolio,
			String account) throws BusinessException {
		logger.info("#{}#reqId={},account={},portfolio={}#{}#", "更新组合的行业",
				reqId, account, gson.toJson(portfolio),
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		// 总资产
		PortfolioYield portfolioYield = PortfolioYield.dao.findFirst(
				"select * from portfolio_yield where portfolio_id= ? limit 1",
				portfolio.getId());
		if (portfolioYield == null)
			throw new BusinessException(10004, "未取得总资产数据");
		double total_value = portfolioYield.getTotal_value();

		JSONArray industries = null;
		JSONObject resultSet = null;
		try {
			// 得到jsonobject,解析
			resultSet = service.run(
					zkMap.get("URL_V2"),
					"P_W_SEL_DATA_TG",
					new Object[] { account, 2, zkMap.get("COORDINATES"),
							zkMap.get("ENCRYPTIONCHAR") });
		} catch (Exception e) {
			logger.error("System Error:{}", e);
		}
		if (resultSet == null)
			return;
		if (resultSet.getIntValue("Code") == -100) {
			logger.error("{} SW_Industry is empty", account);
			industries = null;
			return;
		} else {
			industries = JSONArray.parseArray(resultSet.getString("DataObj"));
		}
		Map<String, JSONArray> portfolio_industries = new HashMap<String, JSONArray>();
		for (Object object : industries) {
			JSONObject jsonObject = (JSONObject) (object);
			if (!portfolio_industries.containsKey(jsonObject
					.getString("Section_Name"))) {
				portfolio_industries.put(jsonObject.getString("Section_Name"),
						new JSONArray());
			}
			JSONArray jsonA = portfolio_industries.get(jsonObject
					.getString("Section_Name"));
			JSONObject json = new JSONObject();
			json.put("stock_code", jsonObject.getString("StockCode"));
			json.put("market_code", jsonObject.getString("Market_Code"));
			json.put("stock_name", jsonObject.getString("StockShortName"));
			json.put("stock_value", jsonObject.getDouble("ScockValue"));
			double weight = (jsonObject.getDouble("ScockValue") * 100.0 / total_value);
			json.put("weight", new BigDecimal(weight).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			jsonA.add(json);
		}
		logger.info("portfolio_industries is:{}", portfolio_industries);

		List<PortfolioIndustry> list = new ArrayList<PortfolioIndustry>();
		for (Map.Entry<String, JSONArray> entry : portfolio_industries
				.entrySet()) {
			PortfolioIndustry portfolioIndustry = new PortfolioIndustry();
			portfolioIndustry.setIndustry(entry.getKey());
			double industry_weight = 0;
			for (Object object : entry.getValue()) {
				JSONObject jsonA = (JSONObject) object;
				double weightA = jsonA.getDouble("stock_value") * 100.0
						/ total_value;
				industry_weight += weightA;
			}
			portfolioIndustry.setWeight(industry_weight);
			portfolioIndustry.setStocks(entry.getValue().toJSONString());
			portfolioIndustry.setW_from_v2("industry_v2");
			portfolioIndustry.setPortfolio_id(portfolio.getId());
			list.add(portfolioIndustry);
		}

		double weight_P = 0;
		for (PortfolioIndustry portfolioIndustry : list) {
			weight_P += portfolioIndustry.getWeight();
		}

		PortfolioIndustry portfolioIndustry = new PortfolioIndustry();
		portfolioIndustry.setIndustry("现金");
		portfolioIndustry.setWeight(weight_P);
		portfolioIndustry.setW_from_v2("industry_v2");
		portfolioIndustry.setPortfolio_id(portfolio.getId());
		list.add(portfolioIndustry);

		List<PortfolioIndustry> pinIndustries = PortfolioIndustry.dao.find("select * from portfolio_industry where portfolio_id= ? and w_from_v2= ?",portfolio.getId(),"industry_v2");
		if (pinIndustries != null || pinIndustries.size()>0) {
			Db.update("delete from portfolio_industry where portfolio_id= ? and w_from_v2= ?",portfolio.getId(),"industry_v2");
		}
		// 批量操作
		Db.batchSave(list, 1000);

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
	}

	/**
	 * @Description: 更新组合的行业
	 * @param portfolio
	 * @param account
	 * @param start_date
	 * @param end_date
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public void updateSwIndustry(String reqId, Portfolio portfolio,
			String account, Date start_date, Date end_date) throws Exception {
		logger.info("#{}#reqId={},account={},portfolio={}#{}#", "更新组合的行业",
				reqId, account, gson.toJson(portfolio),
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		Date start_datetime = sDate_time.parse(sDate_time.format(start_date));
		Date end_datetime = sDate_time.parse(sDate_time.format(end_date));
		JSONObject resultSet = null;
		try {
			resultSet = service.run(
					zkMap.get("URL"),
					"Query_DataSetList",
					new Object[] { account, 9,
							cUtils.getXMLGregorianCalendar(start_date),
							cUtils.getXMLGregorianCalendar(end_date), 10,
							zkMap.get("COORDINATES"),
							zkMap.get("ENCRYPTIONCHAR") });
		} catch (Exception e) {
			logger.error("System Error:{}", e);
		}
		if (resultSet == null)
			throw new BusinessException(10002, "请求返回值为空");
		JSONArray industries = resultSet.getJSONArray("dataObj");
		if (industries == null)
			return;

		List<PortfolioIndustry> list = new ArrayList<PortfolioIndustry>();
		for (Object object : industries) {
			JSONObject i_json = (JSONObject) object;
			PortfolioIndustry portfolioIndustry = new PortfolioIndustry();
			portfolioIndustry.setIndustry(i_json.getString("section_name"));
			portfolioIndustry.setStocks(i_json.getString("stocks"));
			portfolioIndustry.setWeight(i_json.getDouble("SetionValueRatio"));
			portfolioIndustry.setW_from_v2("industry");
			portfolioIndustry.setPortfolio_id(portfolio.getId());
			list.add(portfolioIndustry);
		}
		
		List<PortfolioIndustry> pinIndustries = PortfolioIndustry.dao.find("select * from portfolio_industry where portfolio_id= ? and w_from_v2= ?",portfolio.getId(),"industry");
		if (pinIndustries != null || pinIndustries.size()>0) {
			Db.update("delete from portfolio_industry where portfolio_id= ? and w_from_v2= ?",portfolio.getId(),"industry");
		}
		// 批量操作
		Db.batchSave(list, 1000);

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));

	}

	/**
	 * @Description: 更新操作细节
	 * @param portfolio
	 * @param account
	 * @throws ParseException
	 * @throws BusinessException
	 */
	public void updateOperationDetail(String reqId, Portfolio portfolio,
			String account) throws ParseException, BusinessException {
		logger.info("#{}#reqId={},account={},portfolio={}#{}#", "更新操作细节",
				reqId, account, gson.toJson(portfolio),
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		Date last_operation_trading_day = null;
		List<PortfolioDeal> pLists = null;
		try {
			List<OperationDetail> oDetails = OperationDetail.dao
					.find("SELECT * FROM portfolio_operation_detail WHERE portfolio_id= ? order by trading_day",
							portfolio.getId());
			if (oDetails != null && oDetails.size() > 0) {
				Date trading_day = oDetails.get(0).getTrading_day();
				last_operation_trading_day = sDate_max.parse(sDate_time
						.format(trading_day));
			}else {
				pLists = PortfolioDeal.dao.find("select * from portfolio_deal where accountId= ? order by deal_datetime desc",account);
				if (pLists == null || pLists.size()<=0) {
					last_operation_trading_day = sDate_zero.parse(DEFAULT_DATE);
				}else {
					Date tra_time = pLists.get(0).getDeal_datetime();
					last_operation_trading_day = sDate_zero.parse(sDate_zero.format(tra_time));
				}
			}
		} catch (Exception e) {
			logger.error("System Error:{}",e);
		}
		List<PortfolioDeal> list = PortfolioDeal.dao
				.find("select * from portfolio_deal where accountId= ? and deal_datetime> ? order by deal_datetime asc",
						account, last_operation_trading_day);
		if (list == null || list.size() <= 0) {
			throw new BusinessException(10004, "PortfolioDeal表中未找到数据！");
		}
		Map<String, JSONArray> operation_detail_map = new HashMap<String, JSONArray>();
		for (PortfolioDeal portfolioDeal : list) {
			try {
				String trading_day_str = sDate_zero.format(portfolioDeal.getDeal_datetime());
				if (!operation_detail_map.containsKey(trading_day_str)) {
					operation_detail_map.put(trading_day_str, new JSONArray());
				}

				JSONArray jsArray = operation_detail_map.get(trading_day_str);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("market_code", portfolioDeal.getMarket_code());
				jsonObject.put("stock_code", portfolioDeal.getStock_code());
				jsonObject.put("stock_name", portfolioDeal.getStock_name());
				jsonObject.put("operation_tag", portfolioDeal.getWay());
				jsonObject.put("operation_price", portfolioDeal.getPrice());
				jsonObject.put("operation_quantity",
						portfolioDeal.getQuantity());
				jsonObject.put("operation_date",
						portfolioDeal.getDeal_datetime());

				jsArray.add(jsonObject);
			} catch (Exception e) {
				logger.error("System Error:{}", e);
			}
		}

		Calendar calendar = Calendar.getInstance();
		List<OperationDetail> opList = new ArrayList<OperationDetail>();
		for (Map.Entry<String, JSONArray> map : operation_detail_map.entrySet()) {
			OperationDetail operationDetail = new OperationDetail();
			Date date = sDate_time.parse(map.getKey());
			calendar.setTime(date);
			calendar.add(Calendar.HOUR_OF_DAY, 16);
			operationDetail.setTrading_day(calendar.getTime());
			operationDetail.setData(map.getValue().toJSONString());
			operationDetail.setPortfolio_id(portfolio.getId());
			opList.add(operationDetail);
		}

		if (opList.size()>0) {
			// 删除
			Db.update(
					"delete from portfolio_operation_detail where portfolio_id= ?",
					portfolio.getId());
		}
		// 批量储存
		Db.batchSave(opList, 1000);

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
	}

	/**
	 * @Description: 更新历史收益
	 * @param reqId
	 * @param portfolio
	 * @param account
	 * @param start_date
	 * @param end_date
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public int updateHistoryYield(String reqId, Portfolio portfolio,
			String account, Date start_date, Date end_date) throws Exception {
		logger.info("#{}#reqId={},account={},portfolio={}#{}#", "更新历史收益",
				reqId, account, gson.toJson(portfolio), start_date, end_date,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		Date start_datetime = sDate.parse("2016-01-01");
		Date end_datetime = sDate_time.parse(sDate_time.format(new Date()));
		JSONObject resultSet = null;
		try {
			resultSet = service.run(
					zkMap.get("URL"),
					"Query_DataSetList",
					new Object[] { account, 10,
							cUtils.getXMLGregorianCalendar(start_datetime),
							cUtils.getXMLGregorianCalendar(end_datetime), 10,
							zkMap.get("COORDINATES"),
							zkMap.get("ENCRYPTIONCHAR") });
		} catch (Exception e) {
			logger.error("System Error:{}", e);
		}
		if (resultSet == null)
			throw new BusinessException(10002, "请求返回数据为空");
		if (resultSet.getIntValue("Code") != 0
				&& resultSet.getIntValue("Code") != -100) {
			logger.error("Error, Get {} portfolio history yield failed: {}",
					account, resultSet.getString("Message"));

			long endTime = System.currentTimeMillis();
			logger.info("#cost_time:{}ms#", (endTime - startTime));
			return 0;
		}
		JSONArray yield_list = JSONArray.parseArray(resultSet
				.getString("DataObj"));

		List<HistoryYield> history_yield = new ArrayList<HistoryYield>();
		Date enDate = sDate_zero.parse(sDate_zero.format(end_date));
		HistoryYield historyYield = HistoryYield.dao
				.findFirst(
						"select * from history_yield where portfolio_id= ? order by trading_day desc limit 1",
						portfolio.getId());
		if (historyYield == null) {
			logger.info("historyYield is null,not have Data");
			for (Object yield_info : yield_list) {
				JSONObject jsonObject = (JSONObject) yield_info;
				if (sDate_time.parse(jsonObject.getString("Tradedate")).before(
						enDate)) {
					HistoryYield hYield = new HistoryYield();
					hYield.setTrading_day(sDate_time.parse(jsonObject
							.getString("Tradedate")));
					hYield.setYield(new BigDecimal(
							jsonObject.getDouble("syl") * 100.0).setScale(2,
							BigDecimal.ROUND_HALF_UP).doubleValue());
					hYield.setSZ399300_yield(new BigDecimal(jsonObject
							.getDouble("syl300") * 100.0).setScale(2,
							BigDecimal.ROUND_HALF_UP).doubleValue());
					hYield.setPortfolio_id(portfolio.getId());
					history_yield.add(hYield);
				}

			}
		} else {
			for (Object yield_info : yield_list) {
				JSONObject jsonObject = (JSONObject) yield_info;
				if (sDate_time.parse(jsonObject.getString("Tradedate")).before(
						enDate)
						&& sDate_time.parse(jsonObject.getString("Tradedate"))
								.after(historyYield.getTrading_day())) {

					HistoryYield hYield = new HistoryYield();
					hYield.setTrading_day(sDate_time.parse(jsonObject
							.getString("Tradedate")));
					hYield.setYield(new BigDecimal(
							jsonObject.getDouble("syl") * 100.0).setScale(2,
							BigDecimal.ROUND_HALF_UP).doubleValue());
					hYield.setSZ399300_yield(new BigDecimal(jsonObject
							.getDouble("syl300") * 100.0).setScale(2,
							BigDecimal.ROUND_HALF_UP).doubleValue());
					hYield.setPortfolio_id(portfolio.getId());
					history_yield.add(hYield);
				}

			}
		}
		// 最新的收益
		PortfolioYield pYield = PortfolioYield.dao.findFirst(
				"select * from portfolio_yield where portfolio_id= ?",
				portfolio.getId());
		logger.info("#portfolio_id={},pYield={}", portfolio.getId(),
				gson.toJson(pYield));
		if (pYield == null)
			return 0;
		HistoryYield hYield = new HistoryYield();
		hYield.setTrading_day(sDate_zero.parse(sDate_zero.format(new Date())));
		hYield.setYield(pYield.getToday_yield());
		hYield.setSZ399300_yield(pYield.getSZ399300_yield());
		hYield.setPortfolio_id(portfolio.getId());
		
		history_yield.add(hYield);

		if (historyYield != null) {
			//删除
			Db.update("delete from history_yield where id= ?",historyYield.getId());			
		}
		// 批量处理
		Db.batchSave(history_yield, 1000);

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return history_yield.size();
	}

	/**
	 * @Description: 更新最新操作
	 * @param reqId
	 * @param portfolio
	 * @param account
	 * @throws BusinessException
	 */
	public void updateLatestOperation(String reqId, Portfolio portfolio,
			String account) throws BusinessException {
		logger.info("#{}#reqId={},account={},portfolio={}#{}#", "更新最新操作",
				reqId, account, gson.toJson(portfolio),
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		PortfolioDeal portfolioDeal = PortfolioDeal.dao
				.findFirst(
						"select * from portfolio_deal where accountId= ? order by deal_datetime desc limit 1",
						account);
		logger.info("#portfolioDeal={}", gson.toJson(portfolioDeal));
		if (portfolioDeal == null)
			throw new BusinessException(10004, "portfolioDeal表中未取得相应数据");

		LatestOperation latest_operation = new LatestOperation();
		latest_operation
				.setOperation_datetime(portfolioDeal.getDeal_datetime());
		latest_operation.setStock_name(portfolioDeal.getStock_name());
		latest_operation.setStock_code(portfolioDeal.getStock_code());
		latest_operation.setMarket_code(portfolioDeal.getMarket_code());
		latest_operation.setOperation_quantity(portfolioDeal.getQuantity());
		latest_operation.setOperation_price(portfolioDeal.getPrice());
		latest_operation.setOperation_tag(portfolioDeal.getWay());
		latest_operation.setPortfolio_id(portfolio.getId());
        
		List<LatestOperation> las = LatestOperation.dao.find("select * from latest_operation where portfolio_id= ?",portfolio.getId());
		if(las.size()>0 || las != null){
			Db.update("delete from latest_operation where portfolio_id= ?",portfolio.getId());
		}
		latest_operation.save();

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
	}

	/**
	 * @Description: 发送信息
	 * @param reqId
	 * @param account
	 */
	public String sendMessage(String reqId, String account) throws Exception {
		logger.info("#{}#reqId={},account={}#{}#", "发送信息", reqId, account,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		Portfolio portfolio = getPortfolioByAccount(reqId, account);
		LatestOperation latestOperation = LatestOperation.dao.findFirst(
				"select * from latest_operation where portfolio_id= ?",
				portfolio.getId());
		if (latestOperation == null)
			throw new BusinessException(10004, "latestOperation表中未取到相应数据");

		// 总收益
		PortfolioYield portfolioYield = PortfolioYield.dao.findFirst(
				"select * from portfolio_yield where portfolio_id= ? limit 1",
				portfolio.getId());
		if (portfolioYield == null)
			throw new BusinessException(10004, "portfolioYield未取得总资产数据");
		double total_yield = portfolioYield.getToday_yield();

		JSONObject latest_operation = new JSONObject();
		JSONObject body = new JSONObject();
		JSONObject body_last = new JSONObject();
		// latest_operation
		latest_operation.put("market_code", latestOperation.getMarket_code());
		latest_operation.put("stock_code", latestOperation.getStock_code());
		latest_operation.put("stock_name", latestOperation.getStock_name());
		latest_operation.put("operation_price",
				latestOperation.getOperation_price());
		latest_operation.put("operation_quantity",
				latestOperation.getOperation_quantity());
		latest_operation.put("operation_tag",
				latestOperation.getOperation_tag());
		latest_operation.put("operation_datetime",
				latestOperation.getOperation_datetime());
		// body
		body.put("title", portfolio.getPortfolio_title());
		body.put("account_id", portfolio.getAccount_id());
		body.put("total_yield", total_yield);
		body.put("latest_operation", latest_operation);
		// body_last
		body_last.put("type", 2);
		body_last.put("msg_id", "master.group");
		body_last.put("body", body);

		logger.info("JSON Body ：{}", body_last);
		Header header = new Header("channel","qtg");
		Header header1 = new Header("content-type","application/json");
		String json = HttpRequestUtils.doPost(zkMap.get("MSG_URL"),
				body_last,new Header[]{header,header1},"UTF-8",false);
		logger.info("返回结果json={}", gson.toJson(json));
        System.out.println(json);
		JSONObject jsonObject = JSONObject.parseObject(json);
		String status_code = jsonObject.getString("status_code");

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
		return status_code;
	}

	/**
	 * @Description: 更新组合动态交易
	 * @param reqId
	 * @param account
	 * @throws BusinessException
	 * @throws ParseException 
	 */
	public void updateDynamicDeal(String reqId, String account)
			throws BusinessException, ParseException {
		logger.info("#{}#reqId={},account={}#{}#", "更新组合动态交易", reqId, account,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		List<String> portfolioAccounts = getInitializationPortfolio(reqId);
		// 判断资金ID
		if (!portfolioAccounts.contains(account))
			return;
		Portfolio portfolio = getPortfolioByAccount(reqId, account);
		PortfolioCreator creator = PortfolioCreator.dao.findFirst("select * from portfolio_creator where user_object_id= ?",portfolio.getUser_object_id());
		// 收益表
		PortfolioYield portfolioYield = PortfolioYield.dao.findFirst(
				"select * from portfolio_yield where portfolio_id= ?",
				portfolio.getId());

		PortfolioDealDynamics pDynamics = PortfolioDealDynamics.dao
				.findFirst(
						"select * from portfolio_deal_dynamics where accountID= ? order by recorder_id desc limit 1",
						account);
		int recorder_id = 0;
		if (pDynamics == null) {
			recorder_id = 0;
		} else {
			recorder_id = pDynamics.getRecorder_id();
		}
		logger.info("recorder_id is :{}", recorder_id);
		// 组合流水
		List<PortfolioDeal> list = PortfolioDeal.dao
				.find("select * from portfolio_deal where accountId= ? and recorder_id> ?",
						account, recorder_id);
		List<PortfolioDealDynamics> portfolioDealDynamics = new ArrayList<PortfolioDealDynamics>();
		for (PortfolioDeal portfolioDeal : list) {
			PortfolioDealDynamics portfolio_deal_dynamic = new PortfolioDealDynamics();
			portfolio_deal_dynamic.setAccountId(account);
			portfolio_deal_dynamic.setUser_object_id(portfolio
					.getUser_object_id());
			portfolio_deal_dynamic.setOwner_name(creator.getUser_name());
			portfolio_deal_dynamic.setOwner_avatar(creator.getUser_logo());
			portfolio_deal_dynamic.setTotal_yield(portfolioYield
					.getToday_yield());
			portfolio_deal_dynamic.setOperation_datetime(portfolioDeal
					.getDeal_datetime());
			portfolio_deal_dynamic.setStock_name(portfolioDeal.getStock_name());
			portfolio_deal_dynamic.setStock_code(portfolioDeal.getStock_code());
			portfolio_deal_dynamic.setMarket_code(portfolioDeal
					.getMarket_code());
			portfolio_deal_dynamic.setOperation_quantity(portfolioDeal
					.getQuantity());
			portfolio_deal_dynamic.setOperation_price(portfolioDeal.getPrice());
			portfolio_deal_dynamic.setOperation_tag(portfolioDeal.getWay());
			portfolio_deal_dynamic.setRecorder_id(portfolioDeal
					.getRecorder_id());
			portfolio_deal_dynamic.setCreatedAt(sDate_time.parse(sDate_time.format(new Date())));
			portfolio_deal_dynamic.setUpdatedAt(sDate_time.parse(sDate_time.format(new Date())));
			portfolioDealDynamics.add(portfolio_deal_dynamic);
		}
		// 批量操作
		Db.batchSave(portfolioDealDynamics, 1000);

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
	}
	
   /**
	* @Title: 更新portfolio更新时间
	* @param @param reqId
	* @param @param portfolio
	* @param @throws ParseException    入参
	 */
	public void updatePortfolioTime(String reqId, Portfolio portfolio) throws ParseException{
		logger.info("#{}#reqId={},portfolio={}#{}#", "更新组合时间", reqId, portfolio,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();
		
		if (portfolio == null) return;
		
		Date updatedAt = sDate_time.parse(sDate_time.format(new Date()));
		portfolio.setUpdatedAt(updatedAt);
		portfolio.update(); 
		
		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
	}

	/**
	 * @Description: 更新组合值
	 * @param reqId
	 * @param account
	 * @throws Exception
	 */
	public void updatePortfolioValue(String reqId, String account)
			throws Exception {
		logger.info("#{}#reqId={},account={}#{}#", "更新组合值", reqId, account,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		Portfolio portfolio = getPortfolioByAccount(reqId, account);
		Date last_update_datetime = sDate_second.parse(LAST_UPDATE_DATETIME);
		Date last_trading_day = sDate.parse(sDate.format(new Date()));
		Date last_update_day = sDate.parse(sDate_second
				.format(last_update_datetime));

		// 更新组合收益、胜率、成功率、回撤
		updatePortfolioYields(reqId, account, portfolio,last_update_day,last_trading_day);
		// 更新行业
		updateSwIndustryV2(reqId, portfolio, account);
		updateSwIndustry(reqId, portfolio, account, last_update_day,
				last_trading_day);
		// 更新最新股票
		updateCurrentStocks(reqId, account, portfolio);
		// 更新最新操作
		updateLatestOperation(reqId, portfolio, account);
		// 更新操作明细
		updateOperationDetail(reqId, portfolio, account);
		// 更新历史收益
		updateHistoryYield(reqId, portfolio, account, last_update_day,
				last_trading_day);
        //更新组合时间
		updatePortfolioTime(reqId, portfolio);
		
		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
	}

	/**
	 * @Description: 更新组合
	 * @param reqId
	 * @param account
	 */
	public void updatePortfolio(String reqId, String account) {
		logger.info("#{}#reqId={},account={}#{}#", "更新组合", reqId, account,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		try {
			Portfolio portfolio = getPortfolioByAccount(reqId, account);
			logger.info("Info: {} {} {} ", account,
					portfolio.getValid_start_date(), sDate.format(new Date()));

			boolean has_new_operation = updateDealAndHistory(reqId, account);
			//更新组合
			updatePortfolioValue(reqId, account);
			//更新动态流水
			updateDynamicDeal(reqId, account);
            //更新投顾人信息
			updateAdviser(reqId, account);
			
			if (has_new_operation && needSendMessage(reqId, account)) {
				sendMessage(reqId, account);
			}
		} catch (Exception e) {
			logger.error("System Error:{}", e);
		} finally {
			logger.info("{} sync done ", account);
		}

		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
	}

	/**
	 * @Description: 更新所有组合
	 * @param reqId
	 * @throws ParseException
	 * @throws BusinessException
	 */
	public void updateAllPortfolio(final String reqId) throws ParseException,
			BusinessException {
		logger.info("#{}#reqId={}#{}#", "更新所有组合", reqId,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		final Map<String, String> account_mapping = getAccountMapping(reqId);
		ScheduledExecutorService scheduledThreadPool = Executors
				.newScheduledThreadPool(Integer.valueOf(zkMap
						.get("THREAD_POOL_SIZE")));
		try {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					for (Map.Entry<String, String> entry : account_mapping
							.entrySet()) {
						String accountID = entry.getKey();
						updatePortfolio(reqId, accountID);
					}
				}
			};
			scheduledThreadPool.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
			Thread.sleep(5000);
		} catch (Exception e) {
			logger.info("Error!");
		} finally {
			scheduledThreadPool.shutdown();
		}

//		if (is_error) {
//			String subject = "[" + sDate.format(new Date())
//					+ "] Portfolio Information Failed ";
//			List<String> to = new ArrayList<String>();
//			to.add("zhoujiaji@qktz.com.cn");
//			to.add("zhangneng@qktz.com.cn");
//			// 发送信息
//		}
		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
	}

	/**
	* @Title: updateAdviser 
	* @Description: 更新投顾人信息
	* @param reqId
	* @param account
	* @param BusinessException
	* @param AVException
	* @param ParseException  
	*/
	public void updateAdviser(String reqId,String account) throws BusinessException, AVException, ParseException{
		logger.info("#{}#reqId={},account={}#{}#", "更新投顾信息", reqId, account,
				System.currentTimeMillis());
		long startTime = System.currentTimeMillis();
		
		if (account == null) {
			throw new BusinessException(10003,"非法参数");
		}
		Portfolio portfolio = Portfolio.dao.findFirst("select * from portfolio where account_id= ?",account);
		Adviser adviser = null;
		Adviser newAdviser = null;
		PortfolioYield yield = null;
		if (portfolio != null) {
			DataQueryMC mc = new DataQueryMC();
			PortfolioCreator creator = mc.getBossUser(portfolio.getUser_object_id());
			if (creator.getRoleType().equals("investor")) {
				yield = getPortfolioMaxYield(reqId, creator.getUser_object_id());
				adviser = Adviser.dao.findFirst("select * from portfolio_adviser where user_object_id= ?",creator.getUser_object_id());
				if (adviser == null) {
					newAdviser = new Adviser();
					newAdviser.setUser_object_id(creator.getUser_object_id());
					newAdviser.setName(creator.getUser_name());
					newAdviser.setLogo(creator.getUser_logo());
					newAdviser.setSummary(creator.getUser_summary());
					newAdviser.setQualificationName(creator.getQualificationName());
					newAdviser.setQualificationNum(creator.getQualificationNum());
					if (yield != null) {
						newAdviser.setWeek_yield(yield.getWeek_yield());
						newAdviser.setMonth_yield(yield.getMonth_yield());
						newAdviser.setTotal_yield(yield.getTotal_yield());
						newAdviser.setYear_yield(yield.getAnnualized_yield());
						newAdviser.setSuccess_ratio(yield.getSuccess_ratio());
						newAdviser.setMax_drawdown(yield.getMax_drawdown());
						newAdviser.setPosition(yield.getCurrent_position());
						newAdviser.setCreatedAt(sDate_time.parse(sDate_time.format(new Date())));
						newAdviser.setUpdatedAt(sDate_time.parse(sDate_time.format(new Date())));
					} 
				}else {
					if (yield != null) {
						adviser.setWeek_yield(yield.getWeek_yield());
						adviser.setMonth_yield(yield.getMonth_yield());
						adviser.setTotal_yield(yield.getTotal_yield());
						adviser.setYear_yield(yield.getAnnualized_yield());
						adviser.setSuccess_ratio(yield.getSuccess_ratio());
						adviser.setMax_drawdown(yield.getMax_drawdown());
						adviser.setPosition(yield.getCurrent_position());
						adviser.setUpdatedAt(sDate_time.parse(sDate_time.format(new Date())));
					} 
				}
			}
		}
		
		//保存或修改
		if (newAdviser != null) {
			newAdviser.save();
		}
		if (adviser != null) {
			adviser.update();
		}
		
		long endTime = System.currentTimeMillis();
		logger.info("#cost_time:{}ms#", (endTime - startTime));
	}
	public static void main(String[] args) throws ParseException,
			BusinessException {
		DbManager.start();
		UpdatePortfolio u = new UpdatePortfolio();
		Date last_update_datetime = sDate_second.parse(LAST_UPDATE_DATETIME);
		Date last_trading_day = sDate.parse(sDate.format(new Date()));
		Date last_update_day = sDate.parse(sDate_second
				.format(last_update_datetime));
		 try {
//			 u.updatePortfolio("", "Gsnb002148");
			 u.updateAllPortfolio("");
//			 u.getPortfolioDeal("", "Gsnb002465", last_update_day, last_trading_day);
//			u.updateDynamicDeal("", "Gsnb002203");
		 } catch (Exception e) {
			e.printStackTrace();
		}
	}
}
