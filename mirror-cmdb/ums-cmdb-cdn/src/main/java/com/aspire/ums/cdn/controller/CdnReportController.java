/**
 *
 * 项目名： ums-cmdb-cdn 
 * <p/> 
 *
 * 文件名:  CdnReportController.java 
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cdn.api.ICdnReportService;
import com.aspire.ums.cdn.biz.CdnReportBiz;
import com.aspire.ums.cdn.model.LinkPercentReportData;

/** 
 *
 * 项目名称: ums-cmdb-cdn 
 * <p/>
 * 
 * 类名: CdnReportController
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
public class CdnReportController implements ICdnReportService {
	@Autowired
	private CdnReportBiz cdnReportBiz;

	/** 
	 * 方法重写 <br/>
	 * 功能描述： 
	 * <p>
	 * @return 
	 * @see com.aspire.ums.cdn.api.ICdnReportService#getLinkPercentReportData() 
	 */
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<LinkPercentReportData> getLinkPercentReportData() {
		return cdnReportBiz.getLinkPercentReportData();
	}
}
