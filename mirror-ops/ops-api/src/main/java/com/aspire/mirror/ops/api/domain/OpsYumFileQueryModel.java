/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  YumSourceModel.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月22日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.List;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsYumFileQueryModel
 * <p/>
 *
 * 类功能描述: yum源文件
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
public class OpsYumFileQueryModel extends GeneralAuthConstraintsAware {
	private Integer	fileType;
	private String	name;
	private String	version;
	private String	nameLike;
	private String	yumFileGroupNameLike;
	// private String osDistributionNameLike;
	private Long 	yumFileGroupId;
	private List<Long>	yumFileGroupIdList;
	private String	createrLike;
	private String	uploadFilePathLike;

	@JsonProperty("page_size")
	private Integer	pageSize;				// 如果为null， 则查询全部数据
	@JsonProperty("page_no")
	private Integer	pageNo;					// 从0开始
	
	public Integer getStartIdx() {
		if (pageSize == null) {
			return null;
		}
		return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
	}
}
