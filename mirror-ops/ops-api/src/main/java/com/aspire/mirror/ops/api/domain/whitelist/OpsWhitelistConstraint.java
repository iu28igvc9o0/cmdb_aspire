package com.aspire.mirror.ops.api.domain.whitelist;

import org.apache.commons.lang3.StringUtils;

import com.aspire.mirror.ops.api.domain.whitelist.WhitelistConst.WhitelistTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsWhitelistConstraint
 * <p/>
 *
 * 类功能描述: 白名单约束对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2021年3月5日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
 *
 */
@Data
@EqualsAndHashCode(of={"whitelistType", "whitelistId", "constraintType", "constraintValue"})
public class OpsWhitelistConstraint {

	@JsonProperty("whitelist_type")
	private WhitelistTypeEnum	whitelistType;

	@JsonProperty("whitelist_id")
	private String				whitelistId;

	@JsonProperty("constraint_type")
	private String				constraintType;

	@JsonProperty("constraint_value")
	private String				constraintValue;
	
	public boolean selfCheck() {
		return whitelistType != null && StringUtils.isNotBlank(whitelistId)
					&& StringUtils.isNotBlank(constraintType) && StringUtils.isNotBlank(constraintValue);
	}
}
