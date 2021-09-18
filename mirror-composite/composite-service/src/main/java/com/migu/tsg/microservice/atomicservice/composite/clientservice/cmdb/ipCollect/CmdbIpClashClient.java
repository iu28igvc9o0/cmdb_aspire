package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ipCollect;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashFindPageRequest;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashFindPageResponse;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashResult;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpClashUpdateRequest;

/**
 * 冲突IP
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/28 11:33
 */
@FeignClient(value = "CMDB")
public interface CmdbIpClashClient {

    /**
     * 查询列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/cmdb/ipClash/findPageList", method = RequestMethod.POST)
    ResultVo<List<CmdbIpClashFindPageResponse>> findPageList(@RequestBody CmdbIpClashFindPageRequest request);

    /**
     * 页面查询
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/cmdb/ipClash/findPageAndTotal", method = RequestMethod.POST)
    CmdbIpClashResult findPage(@RequestBody CmdbIpClashFindPageRequest request);

    /**
     * 修改处理状态
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/cmdb/ipClash/updateHandleStatus", method = RequestMethod.POST)
    ResultVo updateHandleStatus(@RequestBody CmdbIpClashUpdateRequest request);

    /**
     * 冲突IP推送接收处理
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/cmdb/ipClash/createIpClash", method = RequestMethod.POST)
    ResultVo createIpClash(@RequestBody Result<CmdbIpClashCreateRequest> request);
}
