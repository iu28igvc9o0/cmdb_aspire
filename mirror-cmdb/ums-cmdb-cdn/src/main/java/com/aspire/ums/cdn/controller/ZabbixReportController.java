/**
 *
 * 项目名： ums-cmdb-cdn 
 * <p/> 
 *
 * 文件名:  ZabbixReportController.java 
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
package com.aspire.ums.cdn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cdn.api.IZabbixReportService;
import com.aspire.ums.cdn.biz.ZabbixReportBiz;

/** 
 *
 * 项目名称: ums-cmdb-cdn 
 * <p/>
 * 
 * 类名: ZabbixReportController
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
@RestController
public class ZabbixReportController implements IZabbixReportService {
	@Autowired
	private ZabbixReportBiz reportBiz;

	/** 
	 * 方法重写 <br/>
	 * 功能描述： 
	 * <p>
	 * @return 
	 * @see com.aspire.ums.cdn.api.IZabbixReportService#fetchItemHistoryVals() 
	 */
	@Override
	public List<String> fetchItemHistoryVals() {
		return reportBiz.getItemHistoryVals();
	}

}
