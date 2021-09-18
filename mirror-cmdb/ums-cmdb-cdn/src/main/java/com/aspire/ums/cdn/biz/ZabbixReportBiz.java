/**
 *
 * 项目名： ums-cmdb-cdn 
 * <p/> 
 *
 * 文件名:  ZabbixReportBiz.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.ums.cdn.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cdn.dao.zabbix.ZabbixDao;

/** 
 *
 * 项目名称: ums-cmdb-cdn 
 * <p/>
 * 
 * 类名: ZabbixReportBiz
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Service
@Transactional(transactionManager="zabbixDbTransactionManager")
public class ZabbixReportBiz {
	@Autowired(required=false)
	private ZabbixDao zabbixDao;
	
	public List<String> getItemHistoryVals() {
		return zabbixDao.listItemVal();
	}
}
