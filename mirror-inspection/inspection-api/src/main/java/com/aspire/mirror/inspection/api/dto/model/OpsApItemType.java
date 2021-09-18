/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  OpsAutoRepairItemType.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月5日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.inspection.api.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsApItemType
 * <p/>
 *
 * 类功能描述: 故障自愈指标类型
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月5日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@EqualsAndHashCode(of= {"sourceMark", "apItemType"})
@JsonInclude(Include.NON_EMPTY)
public class OpsApItemType {
	protected Long					id;
	protected String				sourceMark;
	protected String				apItemType;
	protected String				apItemTypeName;

	@JsonProperty("manage_type")
	protected String				manageType;		// U|D U:更新 D：删除

	protected List<OpsApItem>	apItemList;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date					firstSyncTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date					lastUpdateTime;
	
	/** 
	 * 功能描述: 获取子apItemList时, 填充相应的属性  
	 * <p>
	 * @return
	 */
	public List<OpsApItem> getApItemList() {
		if (CollectionUtils.isEmpty(apItemList)) {
			return apItemList;
		}
		apItemList.forEach(item -> {
			item.setApItemTypeId(getId());
			item.setSourceMark(getSourceMark());
			item.setApItemType(getApItemType());
			item.setApItemTypeName(getApItemTypeName());
		});
		return apItemList;
	}
	
//	public String getRecordMd5() {
//		Object[] referAttrs = {sourceMark, apItemType, apItemTypeName};
//		return Md5Util.getMD5String(StringUtils.join(referAttrs, "|"));
//	}
//
//	@Data
//	@EqualsAndHashCode(callSuper=false)
//	public static class OpsAutoRepairItemTypeQueryModel extends AbsPageQueryParamsAware {
//		private String apItemTypeNameLike;
//	}
}
