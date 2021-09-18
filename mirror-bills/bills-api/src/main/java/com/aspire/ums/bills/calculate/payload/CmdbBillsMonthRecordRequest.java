package com.aspire.ums.bills.calculate.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsMonthRecordRequest
 * @description: 月账单请求类
 * @author: luowenbo
 * @create: 2020-08-03 19:53
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbBillsMonthRecordRequest {
    private String department;

    private String businessSystem;

    private String idc;

    private String pod;

    @JsonProperty("deviceTypes")
    private Map<String,String> deviceTypes;

    @JsonIgnore
    private String deviceType;

    @JsonIgnore
    private String needPay;
}
