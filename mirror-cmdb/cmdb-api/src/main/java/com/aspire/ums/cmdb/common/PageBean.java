
package com.aspire.ums.cmdb.common;



import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: Page
 * Author:   zhu.juwang
 * Date:     2019/3/20 9:22
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean<T> {
     
	/**
     * 总数
     */
    private int count;
    /**
     * 列表对象
     */
    private List<T> result;
     
}
