package com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.discount;

import com.aspire.ums.bills.discount.payload.CmdbBillsDiscountRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-13 09:51
 */
@FeignClient("BILLS")
public interface CmdbBillsDiscountClient {

    @RequestMapping(value = "/v1/cmdb/bill/discount/loadDeptTreeData", method = RequestMethod.GET)
    List<Map<String, String>> getDeptTreeData();

    @RequestMapping(value = "/v1/cmdb/bill/discount/loadPoolTreeData", method = RequestMethod.GET)
    List<Map<String, String>> getPoolTreeData();

    @RequestMapping(value = "/v1/cmdb/bill/discount/loadResourceTreeData", method = RequestMethod.GET)
    List<Map<String, String>> getResourceTreeData();

    @RequestMapping(value = "/v1/cmdb/bill/discount/loadBusinessList", method = RequestMethod.GET)
    List<Map<String, String>> getBusinessList(@RequestParam(value = "dept1", required = false) String dept1,
                                              @RequestParam(value = "dept2", required = false) String dept2);

    @RequestMapping(value = "/v1/cmdb/bill/discount/list", method = RequestMethod.GET)
    List<Map<String, Object>> getDiscountList(@RequestParam("type") String type);

    @RequestMapping(value = "/v1/cmdb/bill/discount", method = RequestMethod.POST)
    Integer commitDiscount(@RequestBody CmdbBillsDiscountRequest discountRequest);

    @RequestMapping(value = "/v1/cmdb/bill/discount/query", method = RequestMethod.GET)
    Map<String, Object> queryDiscount(@RequestBody List<String> ids);

}
