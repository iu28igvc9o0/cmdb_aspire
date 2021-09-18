package com.aspire.ums.cmdb.maintenance.payload;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Dscription: 维保厂商联系人
 * @Author: PJX
 * @Version: V1.0
 */
@Data
@NoArgsConstructor
public class Concat {

    private String id;

    private String produceId;

    private String name;

    private String phone;

    private String email;

    private String remark;

    private Date createTime;

    private List<ConfigDict> personTypes;
}
