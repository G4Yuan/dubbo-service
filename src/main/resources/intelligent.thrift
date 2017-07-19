namespace java com.qktz.service.thrift.intelligent
# 类型定义
typedef i32 int
typedef i64 long


# 异常定义
exception BusinessException {
    1: required int code;
    2: required string msg;
}

# 用户信息
struct UserInfo{
	1:string uid,		     # 用户的uid
	2:string password,       # 用户密码
	3:string username,       # 用户账号
	4:string createdAt,      # 创建时间
	5:string updatedAt,      # 更新时间
	6:string mobilePhoneNumber,  # 手机号码
	7:string nickname,       # 昵称
	8:string cid,            # 个推ID
	9:string desc,           # 备注
	10:string bindMobilePhoneNumber,  # 绑定手机号码
	11:string authData,      # 第三方等人绑定信息
	12:string headImageUrl   # 头像地址
}

#A股风向标
struct ChinaStocksVane{
    1:string trading_day,                    #交易日
    2:int qk_index_level,                    #钱坤指数等级
    3:string qk_index_text,                  #钱坤指数
    4:string qk_index_level_text,            #钱坤指数等级（表情）
    5:int qk_index_free_level,               #钱坤指数等级（免费）
    6:string qk_index_free_text,             #钱坤指数（免费）
    7:string qk_index_free_level_text,       #钱坤指数等级（免费表情）
    8:string qk50_text,                      #钱坤50指数
    9:string qkAD_text,                      #涨跌指标
    10:string emotion_index_text,            #情绪指数
    11:string emotion_index_free_text,       #情绪指数（免费）
    12:string main_capital_unscramble,       #主力资金解读
    13:string main_capital_free_unscramble,  #主力资金解读（免费）
    14:string emotion_data,                  #json对象
    15:string emotion_free_data              #json对象（免费）
    16:int buy_status                        #购买状态
    17:string creator,                       #创建人
    18:string create_time,                   #创建时间
    19:string modify_time,                   #修改时间
    20:int csvid                             #对象ID              
}

# 分页组件 (CSVane)
struct CSVanePager{
	1:int pageNow,				# 当前页
	2:int pageSize,				# 页面大小
	3:int recordSize,       	# 记录数
	4:int pageNum,				# 页数
	5:list<ChinaStocksVane> records,	# 数据列表
	6:int hasNext,				# 是否有下一页 0 否 1是
	7:int hasPrev				# 是否有前一页 0否  1是
}
# 股票信息
struct StockInfo{
	1:string stock_code,		# 股票代码
	2:string property,			# 股票属性
	3:string stock_name,		# 股票名称
	4:string recommend_reason	# 推荐理由
}
# 公告淘股
struct AmoySharePoolInfo{
	1:int id,							# 记录id
	2:list<StockInfo> stock_today,		# 今日股票池 
	3:list<StockInfo> stock_notice,		# 公告股票池
	4:int creator,						# 创建人
	5:string create_time,				# 创建时间 yyyy-MM-dd HH:mm:ss
	6:int modifier,						# 修改人
	7:string modify_time				# 修改时间
}

# 分页组件
struct AmoyPager{
	1:int pageNow,				# 当前页
	2:int pageSize,				# 页面大小
	3:int recordSize,       	# 记录数
	4:int pageNum,				# 页数
	5:list<AmoySharePoolInfo> records,	# 数据列表
	6:int hasNext,				# 是否有下一页 0 否 1是
	7:int hasPrev				# 是否有前一页 0否  1是
}

# 股票信息
struct MasterStockInfo{
	1:string stock_code,		# 股票代码
	2:string stock_property,	# 股票属性
	3:string stock_name,		# 股票名称
	4:string recommend_reason,	# 推荐理由
	5:double buy_price,			# 购买价
	7:double advice_buy_min,	# 建议购买最低价
	8:double advice_buy_max,	# 建议购买最高价
	9:double stop_profit_min,	# 止盈最低价
	10:double stop_profit_max,	# 止盈利最高价
	11:double stop_loss_min,	# 止损最低价
	12:double stop_loss_max,	# 止损最高价
	13:double current_yield,	# 当前最高收益
	14:double current_price,	# 最新价
	15:double current_zdf,		# 涨跌幅
	16:string strategy,			# 策略类型
	17:int level,				# 投资等级
	18:string oper_time,		# 调出调入时间
	19:long oper_timestamp		# 调出时间戳
}
# 操作详情
struct OperationInfo{
	1:long operator,	# 操作人
	2:string operator_name,	# 操作人姓名
	3:string operation,		# 操作
	4:string operator_time,	# 操作时间 yyyy-MM-dd HH:mm:ss
	5:string remark			# 备注
}
# 高手股票池查询条件
struct MasterQueryCondition{
	1:string name,		# 股票池名称
	2:int status		# 状态
}
# 高手股票池
struct BaseMasterStockPool{
	1:long id,		# 高手股票池id
	2:string name,	# 股票池名称
	3:string summary,		# 摘要
	4:list<string> labels,	# 标签
	5:int status,			# 状态  1 编辑待审核  2 编辑审核拒绝 3 风控待审核 4 风控审核拒绝 5 待发布 6 正常 7 禁用
	6:list<MasterStockInfo> stocks,		# 股票池
	7:list<OperationInfo> operations,	# 操作记录
	8:long creator,			# 创建人
	9:string create_time,	# yyyy-MM-dd HH:mm:ss
	10:long modifier,		# 修改人
	11:string modify_time,	# yyyy-MM-dd HH:mm:ss
	12:string strategy_remark,	# 操作策略描述
	13:list<MasterStockInfo> history_stocks,		#历史调出股票池
	14:string stock_name,		# 领涨股股票名称
	15:string stock_code,		# 领涨股股票代码
	16:double total_yield,		# 领涨股总收益
	17:long count,				# 影响股数
	18:long oper_timestamp		# 调出时间戳
}
# 分页组件
struct MasterStockPoolPager{
	1:int pageNow,				# 当前页
	2:int pageSize,				# 页面大小
	3:int recordSize,       	# 记录数
	4:int pageNum,				# 页数
	5:list<BaseMasterStockPool> records,	# 数据列表
	6:int hasNext,				# 是否有下一页 0 否 1是
	7:int hasPrev				# 是否有前一页 0否  1是
}

# 获取获取高手股票池列表
struct StockShortInfo{
	1:string stock_pool_name,	# 股票池名称
	2:string strategy,			#　策略
	3:string stock_name,		#　股票名称
	4:string stock_code,		#　 股票代码
	5:double total_yield,		#　 总收益
	6:long count,				#　 影响股数
	7:string strategy_remark,	# 策略描述 
	8:string least_modify_time,	# 最新更新时间 yyyy-MM-dd HH:mm:ss
	9:list<string> labels,		# 标签
	10:long id,					# 股票池id
	11:long oper_timestamp		# 调出时间戳
}
# 定义智能产品中心
# 组合信息
struct PortfolioInfo{
	1:string portfolio_title,                     //组合标题
	2:string portfolio_summary,                   //组合简介
	3:string portfolio_labels,                    //组合标签
	4:int risk_level,                             //风险评级 
	5:string valid_start_date,                    //组合开始日期
	6:double init_value,                          //初始资金
	7:string portfolio_categories,                //组合类别
	8:int portfolio_status,                       //组合状态 
	9:string user_object_id,                     //投顾信息_obejct_ID
	10:string account_id,                         //资金ID
	11:string user_name,                          //投顾人姓名
	12:string user_logo,                          //投顾人logo
	13:string user_summary,                       //投顾简介
	14:string qualificationName,                  //资格证书名称
	15:string qualificationNum,                   //资格证书
	16:int pfl_id,                                //数据库portfolio对象ID
	17:string createdAt,                          //创建时间
	18:string updatedAt                           //更新时间
}

# 分页组件 (Portfolio)
struct PortfolioPager{
	1:int pageNow,				# 当前页
	2:int pageSize,				# 页面大小
	3:int recordSize,       	# 记录数
	4:int pageNum,				# 页数
	5:list<PortfolioInfo> records,	# 数据列表
	6:int hasNext,				# 是否有下一页 0 否 1是
	7:int hasPrev				# 是否有前一页 0否  1是
}

# 分页组件 (App)
struct AppPager{
	1:int pageNow,				# 当前页
	2:int pageSize,				# 页面大小
	3:int recordSize,       	# 记录数
	4:int pageNum,				# 页数
	5:list<string> records,	# 数据列表
	6:int hasNext,				# 是否有下一页 0 否 1是
	7:int hasPrev				# 是否有前一页 0否  1是
}

# 定义用户中心
service IntelligentService{
	
	#主力资金查询
	string findMainCapital(1:string reqId,2:int buy_status)throws (1:BusinessException ex);
	
	#新增A股风向标对象
	bool insertCSVane(1:string reqId,2:ChinaStocksVane csVane)throws (1:BusinessException ex);
	string findCSVane(1:string reqId,2:int buy_status)throws (1:BusinessException ex);
	string findCSVaneByID(1:string reqId,2:int csvane_id)throws (1:BusinessException ex);
	bool updateCSVane(1:string reqId,2:ChinaStocksVane csVane)throws (1:BusinessException ex);
	CSVanePager findCSVanePage(1:string reqId,2:int pageNow,3:int pageSize)throws (1:BusinessException ex);
	
	#公告淘股
	int saveAndUpdateAmoySharePool(1:string reqId,2:long userId, 3:AmoySharePoolInfo info)throws (1:BusinessException ex);
	# 根据id获取公告淘股数据
	AmoySharePoolInfo findAmoySharePoolById(1:string reqId,2:long recordId)throws (1:BusinessException ex);
	# 分页获取公告池列表
	AmoyPager findByPager(1:string reqId,2:int pageNow, 3:int pageSize)throws (1:BusinessException ex);
	# 获取当期公告池列表数据
	AmoySharePoolInfo findCurrentAmoySharePool(1:string reqId)throws (1:BusinessException ex);
	
	# 添加和编辑高手股票池
	int saveAndEditMasterStockPool(1:string reqId, 2:long userId, 3:BaseMasterStockPool info) throws (1:BusinessException ex);
	# 修改股票池的状态
	int operatorMasterStockPool(1:string reqId, 2:long userId, 3:long masterId, 4:int status, 5:string content) throws (1:BusinessException ex);
	MasterStockPoolPager findMasterStockPoolPager(1:string reqId, 2:MasterQueryCondition condition, 3:int pageNow, 4:int pageSize) throws (1:BusinessException ex);
	# 高手股票池详情
	BaseMasterStockPool findStockPoolBossDetail(1:string reqId, 2:long stockPoolId)throws (1:BusinessException ex);
	# 高手股票池详情
	BaseMasterStockPool findMasterStockPoolDetail(1:string reqId, 2:long stockPoolId)throws (1:BusinessException ex);
	# 获取股票池列表
	list<StockShortInfo> findStockPoolList(1:string reqId)throws (1:BusinessException ex);
	
    #组合新建
    void addPortfolio(1:string reqId,2:PortfolioInfo portfolioInfo)throws (1:BusinessException ex);
    #组合查询列表
    PortfolioPager findPortfolioPage(1:string reqId,2:int pageNow,3:int pageSize)throws (1:BusinessException ex);
    #根据名称查询组合列表
    PortfolioPager findPortfolioByTitle(1:string reqId,2:string name,3:int pageNow,4:int pageSize)throws (1:BusinessException ex);
    #根据时间查询组合列表
    PortfolioPager findPortfolioByTime(1:string reqId,2:string createdAt,3:int pageNow,4:int pageSize)throws (1:BusinessException ex);
    #根据状态查询组合列表
    PortfolioPager findPortfolioByStatus(1:string reqId,2:int status,3:int pageNow,4:int pageSize)throws (1:BusinessException ex);
    #根据类别查询组合列表
    PortfolioPager findPortfolioByCategories(1:string reqId,2:string categories,3:int pageNow,4:int pageSize)throws (1:BusinessException ex);
    #根据资金ID查询组合
    PortfolioInfo findPortfolioByAccountID(1:string reqId,2:string account_id)throws (1:BusinessException ex);
    #根据组合ID查询
    PortfolioInfo findPortfolioByID(1:string reqId,2:int id)throws (1:BusinessException ex);
    #根据ID修改组合
    bool updatePortfolioByID(1:string reqId,2:PortfolioInfo portfolioInfo)throws (1:BusinessException ex);
    #根据adviser_object_id转让组合
    bool updatePortfolioByAdviser(1:string reqId,2:string user_object_id,3:int portfolio_id)throws (1:BusinessException ex);
    #app端组合列表
    AppPager findAppPagerSimple(1:string reqId,2:string isFree,3:int pageNow,4:int pageSize)throws (1:BusinessException ex);
    #app端详情
    string findAppPagerSpec(1:string reqId,2:string isFree,3:int portfolio_id)throws (1:BusinessException ex);
}
