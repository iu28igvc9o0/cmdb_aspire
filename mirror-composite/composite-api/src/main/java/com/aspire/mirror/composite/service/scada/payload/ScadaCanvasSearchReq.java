package com.aspire.mirror.composite.service.scada.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.scada.payload
 * 类名称:    ScadaCanvasSearchReq.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/9/25 10:36
 * 版本:      v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScadaCanvasSearchReq {
    private String pod;

    private String idc;

    private String bizSystem;

    private String name;

    private Integer pictureType;

    private String projectName;
}
