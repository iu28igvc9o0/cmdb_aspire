/**
 *
 * 项目名： ums-cmdb-cdn 
 * <p/> 
 *
 * 文件名:  CdnReportBiz.java 
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

import com.aspire.ums.cdn.dao.cdn.CdnDao;
import com.aspire.ums.cdn.model.LinkPercentReportData;

/** 
 *
 * 项目名称: ums-cmdb-cdn 
 * <p/>
 * 
 * 类名: CdnReportBiz
 * <p/>
 *
 * 类功能描述: CDB报表业务
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
@Transactional("transactionManager")
public class CdnReportBiz {
	@Autowired(required = false)
	private CdnDao cdnDao;
	
	public List<LinkPercentReportData> getLinkPercentReportData() {
		return cdnDao.getLinkPercentReportData();
	}
}
