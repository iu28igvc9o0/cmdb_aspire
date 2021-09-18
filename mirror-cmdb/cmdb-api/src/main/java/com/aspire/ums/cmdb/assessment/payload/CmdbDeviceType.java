package com.aspire.ums.cmdb.assessment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-26 10:48:12
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbDeviceType {

    /**
     * 设备类型id
     */
    private String id;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 类型名称
     */
    private String type;
    /**
     * 类型名称
     */
    private String parent;
    /**
     * 低级
     */
    private List<CmdbDeviceType> child;


}