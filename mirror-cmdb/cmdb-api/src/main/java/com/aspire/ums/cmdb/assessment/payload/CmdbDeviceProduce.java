package com.aspire.ums.cmdb.assessment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-06-26 10:48:11
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbDeviceProduce {

    /**
     * 设备厂家id
     */
    private String id;
    /**
     * 设备厂家名称
     */
    private String name;
}