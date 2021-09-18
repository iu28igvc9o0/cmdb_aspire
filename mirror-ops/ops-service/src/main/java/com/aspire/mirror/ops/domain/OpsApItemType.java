/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsApItemType22.java 
 * <p/>
 *
 * 功能描述: 自愈指标类型 
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
package com.aspire.mirror.ops.domain;

import org.springframework.beans.BeanUtils;

import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemTypeDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsApItemType
 * <p/>
 *
 * 类功能描述: 自愈指标类型
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
@Setter
@Getter
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper=true)
public class OpsApItemType extends OpsApItemTypeDTO {
//	private List<OpsApItem>	apItemList;
//	
//	/** 
//	 * 功能描述: 获取子apItemList时, 填充相应的属性  
//	 * <p>
//	 * @return
//	 */
//	public List<OpsApItem> getApItemList() {
//		if (CollectionUtils.isEmpty(apItemList)) {
//			return apItemList;
//		}
//		apItemList.forEach(item -> {
//			item.setApItemTypeId(getId());
//			item.setSourceMark(getSourceMark());
//			item.setApItemType(getApItemType());
//			item.setApItemTypeName(getApItemTypeName());
//		});
//		return apItemList;
//	}
//	
//	public String getRecordMd5() {
//		Object[] referAttrs = {sourceMark, apItemType, apItemTypeName};
//		return Md5Util.getMD5String(StringUtils.join(referAttrs, "|"));
//	}
	
	public static OpsApItemType parse(OpsApItemTypeDTO apItemTypeDto) {
		OpsApItemType apItemType = new OpsApItemType();
		BeanUtils.copyProperties(apItemTypeDto, apItemType);
		return apItemType	;
	}
}
