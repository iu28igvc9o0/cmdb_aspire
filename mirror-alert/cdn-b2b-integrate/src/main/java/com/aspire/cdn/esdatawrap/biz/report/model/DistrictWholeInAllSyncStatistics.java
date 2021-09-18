package com.aspire.cdn.esdatawrap.biz.report.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: DistrictWholeInAllSyncStatistics
 * <p/>
 *
 * 类功能描述: 各区域、汇总数据返回对象(各区域和汇总使用相同的时间间隔), 返回格式如下: <br/>
 * <pre>
 * {
 *   "timelist": ["00:00", "01:00", "02:00", "03:00" ],
 *   "district_datalist": [
 *	    {"district_name": "湖南", "datalist": [34, 23, 56, 52]},
 *	    {"district_name": "广东", "datalist": [34, 23, 56, 52]}
 *   ],
 *   "whole_inall_datalist": [3345, 234, 3241, 3241]
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
public class DistrictWholeInAllSyncStatistics {
	
	@JsonIgnore
	private List<Long>			    timeNumList				= new ArrayList<>();	// 时间数值点
	
	@JsonProperty("timelist")
	private List<String>			timeList				= new ArrayList<>();	// 时间点label

	@JsonProperty("district_datalist")
	private List<DistrictValueItem>	districtDataList		= new ArrayList<>();	// 各区域在时间点的数据

	@JsonProperty("whole_inall_datalist")
	private List<? super Number>	wholeInallDatalist		= new ArrayList<>();	// 各区域汇总的数据
	
	
	public DistrictWholeInAllSyncStatistics(List<Pair<Long, String>> intervalLabelList) {
		intervalLabelList.forEach(pair -> {
			timeNumList.add(pair.getLeft());
			timeList.add(pair.getRight());
			wholeInallDatalist.add(0d);
		});
	}
	
	public void addDistrictTimeVal(String districtName, Long itemTime, Number itemVal) {
		DistrictValueItem matchDistrict = new DistrictValueItem(districtName);
		if (!districtDataList.contains(matchDistrict)) {
			initPopupDistrictDataList(districtName);
		}
		DistrictValueItem provValItem = districtDataList.get(districtDataList.indexOf(matchDistrict));
		int timeIdx = timeNumList.indexOf(itemTime);
		if (timeIdx >= 0) {
			provValItem.setDataVal(timeIdx, itemVal);
			Double prevVal = Double.valueOf(String.valueOf(wholeInallDatalist.get(timeIdx)));
			double refreshVal = prevVal + itemVal.doubleValue();
			wholeInallDatalist.set(timeIdx, refreshVal);
		}
	}
	
	private void initPopupDistrictDataList(String districtName) {
		DistrictValueItem districtData = new DistrictValueItem(districtName);
		List<Number> dataList = new ArrayList<>();
		for (int i = 0; i < timeList.size(); i++) {
			dataList.add(0d);
		}
		districtData.setDataList(dataList);
		districtDataList.add(districtData);
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
}
