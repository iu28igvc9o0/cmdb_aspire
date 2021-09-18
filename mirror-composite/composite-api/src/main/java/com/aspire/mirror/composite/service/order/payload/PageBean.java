package com.aspire.mirror.composite.service.order.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.order.payload
 * 类名称:    PageBean.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/9/2 13:43
 * 版本:      v1.0
 */
@Data
public class PageBean {
    @ApiModelProperty("页数")
    private Integer page;

    @ApiModelProperty("每页大小")
    private Integer pageSize;

    @ApiModelProperty("是否显示总数")
    private Boolean showTotal;
}
