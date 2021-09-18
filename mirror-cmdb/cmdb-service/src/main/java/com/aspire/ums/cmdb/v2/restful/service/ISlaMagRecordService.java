package com.aspire.ums.cmdb.v2.restful.service;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICommonRestfulService
 * Author:   zhu.juwang
 * Date:     2020/3/13 11:21
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ISlaMagRecordService {
	/**
	 * 查询短信发送记录
	 * @param params
	 * @return
	 */
	  List<Map<String, Object>> queryMsgRecord(Map<String, Object> params);
	 /**
	  * 插入发送短信记录
	  * @param Object
	  */
	   void insert(Map<String, Object> Object) ;
	   
	   List<Map<String,Object>> listNoWorkingDays();
}
