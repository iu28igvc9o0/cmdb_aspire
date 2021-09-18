package com.aspire.mirror.elasticsearch.server.enums;


/**
 * 公共静态变量
 * 
 * 
 */
public class Constants {
	public static final String OPT_TYPE_QUERY = "query";	//前台请求类型-查询
	
	public static final String OPT_TYPE_ADD = "add";	//前台请求类型-新增

	public static final String ORACLE_FIELD_TYPE_NUMBER = "NUMBER";	//oracle字段类型
	
	public static final String UNIT_MARKER_WEIDU = "weidu";	//单位标记（维度）
	
	public static final String UNIT_MARKER_DULIANG = "duliang";	//单位标记（度量）
	
	public static final String BASIC_LINE_TYPE_FRONT = "front"; //前

	public static final String BASIC_LINE_TYPE_AFTER = "after"; //后

	public static final String BASIC_LINE_TYPE_UP = "up"; //上

	public static final String BASIC_LINE_TYPE_DOWN = "down"; //下
	
	public static final String BASIC_LINE_TYPE_CONSTANT = "constant"; //常量

	public static final String TIME_UNIT_YEAR = "year"; 

	public static final String TIME_UNIT_MONTH = "month"; 

	public static final String TIME_UNIT_WEEK = "week"; 

	public static final String TIME_UNIT_DAY = "day"; 

	public static final String TIME_UNIT_HOUR = "hour"; 

	public static final String TIME_UNIT_MINUTE = "minute"; 

	public static final String TIME_UNIT_SECOND = "second"; 
	
	public static final String AGGREGATION_TYPE_SUM = "sum";
	
	public static final String AGGREGATION_TYPE_MAX = "max";
	
	public static final String AGGREGATION_TYPE_MIN = "min";
	
	public static final String AGGREGATION_TYPE_AVG = "avg";
	
	public static final String AGGREGATION_TYPE_COUNT = "count";
	
	public static final String AGGREGATION_TYPE_UNIQUECOUNT = "uniquecount";
	
	public static final String AGGREGATION_GROUP_INTERTIME_ALIAS = "dateagg";  //时间间隔别称 
	
	public static final String AGGREGATION_GROUP_XLINE_COLUMN_ALIAS = "group_xline";  //x轴分组字段别称  
	
	public static final String AGGREGATION_GROUP_COLUMN_ALIAS = "group_type";   //分组字段别称 
	
	public static final String ORACLE_DATASOURCE_NAME = "oracle";   //oracle数据源名称
	
	public static final String MYSQL_DATASOURCE_NAME = "mysql";   //mysql数据源名称
	
	public static final String TRANSFER_STATUS_BATCH = "B";   //批量同步
	
	public static final String TRANSFER_STATUS_SINGER = "S";   //单表同步
	
	public static final String TRANSFER_STATUS_NO = "N";   //空闲状态
	
	public static final String TRANSFER_STATUS_X = "X";   //不用同步
	
	public static final String BIZ_SYSTEM = "bizSystem";   //业务系统
	
	public static final String ROOM_ID = "roomId"; 
	
	public static final String IDC_TYPE = "idcType"; 
	
	public static final String DEVICE_CLASS = "deviceClass"; 
	
	public static final String DEVICE_TYPE = "deviceType";

	public static final String ITEM = "item";

	public static final String DATE_TIME = "datetime";

	public static final String CLOCK = "clock";

	public static final String RESOURCE_ID = "resourceId";

	public static final String PORT = "port";
	
	public static final String DEVICE_IP = "host";

	public static final String VALUE = "value";

	public static final String monitor_index_0 = "history-*";

	public static final String monitor_index_1 = "*:history_uint*-";

	public static final String monitor_index_3 = "history_uint-*"; 
	
	
	public static final int redisTimeOut = 6;
	
	public static final String redisKey = "indexPage";
	
	/*
	 * public static final String VM_CPU_ITEM =
	 * "cpu.usage_average,custom.cpu.avg5.pused"; public static final String
	 * VM_MEMORY_ITEM = "custom.memory.pused,mem.usage_average";
	 */
	public static final String VM_DISK_ITEM = "disk.usage_average,custom.disk.avg5.pused";
	/*
	 * public static final String PHY_CPU_ITEM =
	 * "custom.cpu.avg5.pused,cpu.capacity_usagepct_average"; public static final
	 * String PHY_MEMORY_ITEM = "custom.memory.pused,mem.host_usagePct";
	 */
	public static final String PHY_DISK_ITEM = "custom.disk.pused,disk.host_usagePct";
	public static final String CPU_ITEM = "cpu.usage_average,custom.cpu.avg5.pused,cpu.capacity_usagepct_average";
	public static final String MEMORY_ITEM = "custom.memory.pused,mem.usage_average,mem.host_usagePct";
	
	public static final String NODE_TYPE_SZJ = "宿主机"; 
	public static final String NODE_TYPE_MANEGERNODE = "管理节点";
	public static final String NODE_TYPE_JSJD = "计算节点";

	public static final String KPI_DIR_REDIS_KEY = "KPI:";

	public static final String REDIS_KEY_SAN_TOTAL = "SAN_TOTAL";
	public static final String REDIS_KEY_STORAGE_TREND = "STORAGE_TREND";
	public static final String REDIS_KEY_SAN_PUSED_M = "SAN_PUSED_MAX";
	public static final String REDIS_KEY_SAN_PUSED_A = "SAN_PUSED_AVG";
	
	public static final String NET_IN_ITEM = "net.if.in";
	
	public static final String NET_OUT_ITEM = "net.if.out";
	
	public static final String SERVER_PROCESS_LOAD = "system.cpu.load[all,avg5]";//服务器工作负载
	public static final String SERVER_DISK_USAGE = "vfs.fs.size[";//磁盘
	
	public static final String SERVER_DISK_SUFFIX_USAGE = ",pused]";//磁盘
	
	public static final String NET_IN_ITEM_NET = "ifHCInOctets";
	public static final String NET_OUT_ITEM_NET = "ifHCOutOctets";
	
	public static final String IF_IN_ERRORS = "ifInErrors";
	public static final String IF_OUT_ERRORS = "ifOutErrors";

	public static final String SYS_RUNTIME = "sys.osUptime_latest///,system.uptime";//云主机系统运行时间system.uptime x86服务器
	
	public static final String MEMORY_SIZE = "vm.memory.size[total]";//内存总量
	
	public static final String SYS_RUNTIME_NET = "sysUpTime";
	
	public static final String SYS_ICMPPINGSEC_NET = "icmppingsec";
	
	public static final String SYS_INTERFACE_NET = "ifOperStatus";
 
	
}
