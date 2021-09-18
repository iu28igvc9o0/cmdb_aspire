package com.aspire.ums.bills.discount.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-04 10:30
 */
@Data
public class CmdbBillsDiscountDomain {

    private String id;

    private String name;

    private Double discount;

    private List<Map<String, String>> scopes;

    private Date insertTime;

    private String insertPerson;

    private Date updateTime;

    private String updatePerson;

}
