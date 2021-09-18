/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  IndexPageRunTimeSpanStatistic.java 
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
 * 类名: IndexPageRunTimeSpanStatistic
 * <p/>
 *
 * 类功能描述: 最近30天任务执行耗时
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
public class IndexPageRunTimeSpanStatistic {
	private int inOneMinuteCount;
	private int amongOne2ThreeMinutesCount;
	private int amongThree2FiveMinutesCount;
	private int amongFive2TenMinutesCount;
	private int amongTen2ThirtyMinutesCount;
	private int moreThanThirtyMinutesCount;
	
	public int stepInOneMinuteCount() {
		return (inOneMinuteCount++);
	}
	public int stepAmongOne2ThreeMinutesCount() {
		return (amongOne2ThreeMinutesCount++);
	}
	public int stepAmongThree2FiveMinutesCount() {
		return (amongThree2FiveMinutesCount++);
	}
	public int stepAmongFive2TenMinutesCount() {
		return (amongFive2TenMinutesCount++);
	}
	public int stepAmongTen2ThirtyMinutesCount() {
		return (amongTen2ThirtyMinutesCount++);
	}
	public int stepMoreThanThirtyMinutesCount() {
		return (moreThanThirtyMinutesCount++);
	}
}
