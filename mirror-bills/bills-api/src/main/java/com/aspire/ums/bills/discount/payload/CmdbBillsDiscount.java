package com.aspire.ums.bills.discount.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wangyihan
 * @date 2020-11-09 11:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbBillsDiscount {

    private String resId;

    private String id;

    private String resType;

    private Double discount;

    private Date insertTime;

    private String insertPerson;

    private Date updateTime;

    private String updatePerson;

    private String isDelete;



}
