package com.aspire.ums.bills.price.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wangyihan
 * @date 2020-08-04 10:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbBillsPriceResponse {

    private String id;

    private String idcId;

    private String idcName;

    private String deviceTypeId;

    private String deviceTypeName;

    private Double price;

    private String unit;

    private Date insertTime;

    private String insertPerson;

    private Date updateTime;

    private String updatePerson;

}
