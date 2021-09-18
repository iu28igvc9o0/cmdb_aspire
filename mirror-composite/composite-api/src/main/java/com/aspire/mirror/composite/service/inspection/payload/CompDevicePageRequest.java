package com.aspire.mirror.composite.service.inspection.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 设备列表请求
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompDevicePageRequest.java
 * 类描述:    设备列表请求
 * 创建人:    JinSu
 * 创建时间:  2018/8/15 15:42
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompDevicePageRequest implements Serializable {
//    @JsonProperty("module_id")
//    private String moduleId;
//
//    @JsonProperty("page_size")
//    @NotBlank
//    private Integer pageSize;
//
//    @JsonProperty("page_no")
//    @NotBlank
//    private Integer pageNumber;
//
//    @NotBlank
//    private String sort;
//
//    @NotBlank
//    private  String order;
//
//    /**
//     * ip地址
//     */
//    private String name;
//
//    /**
//     * 机房Id
//     */
//    private String roomId;
//
//    /**
//     * 设备分类Id
//     */
//    private String categoryId;
//
//    /**
//     * 厂商
//     */
//    private String manufactor;
//
//    /**
//     * 业务系统
//     */
//    private String bizSystem;
    private Integer pageNumber;
    private Integer pageSize;
    private String sort;
    private String order;
    private String moduleId;
    private String name;
    private String roomId;
    private List<String> instanceList;
    private String ip;
    private String templateId;
}
