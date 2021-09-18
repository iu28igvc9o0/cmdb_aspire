/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  PageListQueryResult.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月25日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: PageListQueryResult
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月25日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageListQueryResult<T> {
	private Integer totalCount;
	private List<T> dataList;
}
