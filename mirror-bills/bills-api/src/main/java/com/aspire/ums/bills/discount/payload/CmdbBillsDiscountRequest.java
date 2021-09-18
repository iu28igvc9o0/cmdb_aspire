package com.aspire.ums.bills.discount.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wangyihan
 * @date 2020-08-04 10:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbBillsDiscountRequest {

    List<CmdbBillsDiscount> discountList;

}
