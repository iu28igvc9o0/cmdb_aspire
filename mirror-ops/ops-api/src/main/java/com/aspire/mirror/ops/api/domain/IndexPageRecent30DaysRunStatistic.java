/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  IndexPageRecent30DaysRunStatistic.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月28日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import lombok.Data;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: IndexPageRecent30DaysRunStatistic
 * <p/>
 *
 * 类功能描述: 最近30天任务执行统计数
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月28日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
public class IndexPageRecent30DaysRunStatistic {
	private Integer totalCount;
	private Integer runCount;
	private Integer successCount;
	private Integer failCount;
	private Integer timeoutCount;
}
