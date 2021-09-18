/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  YumSourceGroup.java 
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

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsYumFileGroup
 * <p/>
 *
 * 类功能描述: yum文件分组
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
@EqualsAndHashCode(callSuper=false)
public class OpsYumFileGroupQueryModel extends OpsYumFileGroup {
//	private String	groupNameLike;

//	@JsonProperty("page_size")
//	private Integer	pageSize;		// 如果为null， 则查询全部数据
//	@JsonProperty("page_no")
//	private Integer	pageNo;			// 从0开始
//
//	public Integer getStartIdx() {
//		if (pageSize == null) {
//			return null;
//		}
//		return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
//	}
}
