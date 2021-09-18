package com.aspire.ums.cmdb.v3.config.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: CmdbConfigRequest
 * @description: 类
 * @author: luowenbo
 * @create: 2020-06-25 15:15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbConfigRequest {

    private String configCode;

    private Integer pageNo;

    private Integer pageSize;
}
