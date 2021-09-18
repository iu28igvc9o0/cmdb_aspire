package com.aspire.ums.cmdb.serverProject.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbCabinetBill {

    /**
     * 资产ID
     */
    private String id;

    /**
     * 删除标识
     */
    private Integer isDelete;

    /**
     * 1月计费
     */
    private String billMonthTotal1;

    /**
     * 1月单价
     */
    private String billMonthUnit1;

    /**
     * 2月计费
     */
    private String billMonthTotal2;

    /**
     * 2月单价
     */
    private String billMonthUnit2;

    /**
     * 3月计费
     */
    private String billMonthTotal3;

    /**
     * 3月单价
     */
    private String billMonthUnit3;

    /**
     * 4月计费
     */
    private String billMonthTotal4;

    /**
     * 4月单价
     */
    private String billMonthUnit4;

    /**
     * 5月计费
     */
    private String billMonthTotal5;

    /**
     * 5月单价
     */
    private String billMonthUnit5;

    /**
     * 6月计费
     */
    private String billMonthTotal6;

    /**
     * 6月单价
     */
    private String billMonthUnit6;

    /**
     * 7月计费
     */
    private String billMonthTotal7;

    /**
     * 7月单价
     */
    private String billMonthUnit7;

    /**
     * 8月计费
     */
    private String billMonthTotal8;

    /**
     * 8月单价
     */
    private String billMonthUnit8;

    /**
     * 9月计费
     */
    private String billMonthTotal9;

    /**
     * 9月单价
     */
    private String billMonthUnit9;

    /**
     * 10月计费
     */
    private String billMonthTotal10;

    /**
     * 10月单价
     */
    private String billMonthUnit10;

    /**
     * 11月计费
     */
    private String billMonthTotal11;

    /**
     * 11月单价
     */
    private String billMonthUnit11;

    /**
     * 12月计费
     */
    private String billMonthTotal12;

    /**
     * 12月单价
     */
    private String billMonthUnit12;

    /**
     * 机柜结算年份
     */
    private String serverBillYear;
}
