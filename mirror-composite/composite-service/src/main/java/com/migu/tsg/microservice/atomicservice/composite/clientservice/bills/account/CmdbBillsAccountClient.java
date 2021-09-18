package com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.account;

import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountBalance;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthRecord;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsAccountClient
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-12 20:28
 **/
@FeignClient(value="BILLS" )
public interface CmdbBillsAccountClient {

    @RequestMapping(value = "/v1/cmdb/bill/record/payList",method = RequestMethod.GET)
    List<Map<String,Object>> getAccountPayRecord(@RequestParam("type") String type ,
                                                 @RequestParam("chargeTime") String chargeTime);
    @RequestMapping(value = "/v1/cmdb/bill/record/payList/detail",method = RequestMethod.GET)
    Map<String, Object> getAccountPayRecordDetail(@RequestParam("departmentId") String departmentId,
                                                  @RequestParam("chargeTime") String chargeTime );

    @RequestMapping(value = "/v1/cmdb/bill/record/payList",method = RequestMethod.PUT)
    Result updateAccountRecord(@RequestBody CmdbBillsMonthRecord monthRecord);

    @RequestMapping(value = "/v1/cmdb/bill/account/getByDept",method = RequestMethod.GET)
    CmdbBillsAccountBalance getAccountBydepartment(@RequestParam("department") String departmentId);

}
