/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  IndexPageRecent30DaysRunTrend.java 
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

import org.apache.commons.lang.mutable.MutableInt;
//import org.apache.commons.lang3.mutable.MutableInt;

import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: IndexPageDayRunTrendItem
 * <p/>
 *
 * 类功能描述: 按天统计任务执行趋势统计项
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
@NoArgsConstructor
public class IndexPageDayRunTrendItem {
	private String				day;
	private final MutableInt successCount	= new MutableInt(0);
	private final MutableInt	failCount		= new MutableInt(0);
	private final MutableInt	totalCount		= new MutableInt(0);

	public IndexPageDayRunTrendItem(String day) {
		this.day = day;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!IndexPageDayRunTrendItem.class.isInstance(obj)) {
			return false;
		}
		if (this.day == null) {
			return super.equals(obj);
		}
		IndexPageDayRunTrendItem other = IndexPageDayRunTrendItem.class.cast(obj);
		return this.day.equals(other.getDay());
	}
	
	@Override
	public int hashCode() {
		return day == null ? super.hashCode() : day.hashCode();
	}
	
	@Data
	public static class DayStausCountItem {
		private String	theDay;
		private Integer	status;
		private Integer	statusCount;
	}
}
