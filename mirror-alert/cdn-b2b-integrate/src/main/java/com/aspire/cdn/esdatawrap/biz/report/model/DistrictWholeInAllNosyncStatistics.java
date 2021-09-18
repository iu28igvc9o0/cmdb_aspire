package com.aspire.cdn.esdatawrap.biz.report.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: DistrictWholeInAllNosyncStatistics
 * <p/>
 *
 * 类功能描述: 各区域、汇总 报表返回对象(各区和汇总不使用相同时间点), 返回格式如下: <br/>
 * <pre>
 * {
 *   "timelist": ["00:00", "01:00", "02:00", "03:00" ],
 *   "district_datalist": [
 *	    {"distirct_name": "湖南", "datalist": [0.98, 0.98, 0.98, 0.98]},
 *	    {"distirct_name": "广东", "datalist": [0.98, 0.98, 0.98, 0.98]}
 *   ],
 *   "whole_inall_datalist": [0.96, 0.04]
 * }
 * </pre>
 *
 * @author	pengguihua
 *
 * @date	2020年7月20日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class DistrictWholeInAllNosyncStatistics {
	
	@JsonIgnore
	private List<Long>			    timeNumList				= new ArrayList<>();	// 时间数值点
	
	@JsonProperty("timelist")
	private List<String>			timeList				= new ArrayList<>();	// 时间点label

	@JsonProperty("district_datalist")
	private List<DistrictValueItem>	districtDataList		= new ArrayList<>();	// 各区域在时间点的数据

	@JsonProperty("whole_inall_datalist")
	private List<Object>			wholeInallDatalist		= new ArrayList<>();	// 各区域汇总的数据
	
	
	public DistrictWholeInAllNosyncStatistics(List<Pair<Long, String>> intervalLabelList) {
		intervalLabelList.forEach(pair -> {
			timeNumList.add(pair.getLeft());
			timeList.add(pair.getRight());
		});
	}
	
	public void addDistrictTimeVal(String districtName, Long itemTime, Number itemVal) {
		DistrictValueItem matchProv = new DistrictValueItem(districtName);
		if (!districtDataList.contains(matchProv)) {
			initPopupDistrictDataList(districtName);
		}
		DistrictValueItem provValItem = districtDataList.get(districtDataList.indexOf(matchProv));
		int timeIdx = timeNumList.indexOf(itemTime);
		if (timeIdx >= 0) {
			provValItem.setDataVal(timeIdx, itemVal);
		}
	}
	
	private void initPopupDistrictDataList(String districtName) {
		DistrictValueItem distData = new DistrictValueItem(districtName);
		List<Number> dataList = new ArrayList<>();
		for (int i = 0; i < timeList.size(); i++) {
			dataList.add(0d);
		}
		distData.setDataList(dataList);
		districtDataList.add(distData);
	}
	
	public void addWholeInallValueItem(Object ... itemArr) {
		wholeInallDatalist.addAll(Arrays.asList(itemArr));
	}
	
	@Data
	@NoArgsConstructor
	@EqualsAndHashCode(of={"districtName"})
	@JsonInclude(Include.NON_EMPTY)
	public static class DistrictValueItem {

		@JsonProperty("district_name")
		private String districtName;
		
		@JsonProperty("datalist")
		private List<? super Number> dataList = new ArrayList<>();
		
		public DistrictValueItem(String districtName) {
			this.districtName = districtName;
		}
		
		public void setDataVal(int idx, Number itemVal) {
			dataList.set(idx, itemVal);
		}
	}
	
	// 饼图值对象
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PieItemVal {
		private String name;
		private Object value;
		
		public static PieItemVal of(String name, Object value) {
			return new PieItemVal(name, value);
		}
	}
	
	// 柱状图值对象， 支持2个不同含义的值， 比如   5xx总数及百分比  
	@Data
	@NoArgsConstructor
	public static class BarItemVal {
		@JsonIgnore
		private Map<String, Object> barAttrs = new HashMap<>();
		
		@JsonAnySetter
		public void addAttr(String attrKey, Object attrVal) {
			barAttrs.put(attrKey, attrVal);
		}
		
		@JsonAnyGetter
		public Map<String, Object> getAllAttrs() {
			return barAttrs;
		}
		
		public <T> T getAttrVal(String attrKey, Class<T> clazz) {
			Object attrVal = barAttrs.get(attrKey);
			return attrVal == null ? null : clazz.cast(attrVal);
		}
	}
}
