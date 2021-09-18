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
package com.aspire.mirror.ops.api.domain.autorepair;

import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.aspire.mirror.ops.api.domain.AbsPageQueryParamsAware;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsApItemTypeDTO
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
public class OpsApItemTypeDTO {
	public static final String 	MANAGE_TYPE_ALL	   = "A";
	public static final String 	MANAGE_TYPE_UPDATE = "U";
	public static final String 	MANAGE_TYPE_DELETE = "D";
	protected Long					id;
	protected String				sourceMark;
	protected String				apItemType;
	protected String				apItemTypeName;

	@JsonProperty("manage_type")
	protected String				manageType;		// A|U|D  A:全量同步  U:更新 D：删除

	protected List<OpsApItemDTO>	apItemList;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date					firstSyncTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date					lastUpdateTime;
	
	/** 
	 * 功能描述: 获取子apItemList时, 填充相应的属性  
	 * <p>
	 * @return
	 */
	public List<OpsApItemDTO> getApItemList() {
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
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class OpsAutoRepairItemTypeQueryModel extends AbsPageQueryParamsAware {
		private String apItemTypeNameLike;
	}
}
