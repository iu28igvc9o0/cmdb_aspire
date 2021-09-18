package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ipCollect;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpCollectRequest;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpCollectResponse;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpCollectResult;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVipCollectEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVipCollectRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceCreateRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceDeleteRequest;
import com.aspire.ums.cmdb.ipCollect.payload.vmware.InstanceUpdateRequest;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/20 16:04
 */
@FeignClient(value = "CMDB")
public interface CmdbIpCollectClient {

    /**
     * 接收新增推送接口
     *
     * @param instanceCreateRequest
     * @return
     */
    @RequestMapping(value = "/cmdb/ipCollect/instance/create", method = RequestMethod.POST)
    ResultVo create(@RequestBody InstanceCreateRequest instanceCreateRequest);

    /**
     * 接收修改推送接口
     *
     * @param instanceUpdateRequest
     * @return
     */
    @RequestMapping(value = "/cmdb/ipCollect/instance/update", method = RequestMethod.POST)
    ResultVo update(@RequestBody InstanceUpdateRequest instanceUpdateRequest);

    /**
     * 接收删除推送接口
     *
     * @param instanceDeleteRequest
     * @return
     */
    @RequestMapping(value = "/cmdb/ipCollect/instance/delete", method = RequestMethod.POST)
    ResultVo delete(@RequestBody InstanceDeleteRequest instanceDeleteRequest);


    //=========================存活IP接口=============================

    /**
     * 查询存活IP列表接口
     *
     * @param cmdbIpCollectRequest
     * @return
     */
    @RequestMapping(value = "/cmdb/ipCollect/survival/findList", method = RequestMethod.POST)
    ResultVo<List<CmdbIpCollectResponse>> findListS(@RequestBody CmdbIpCollectRequest cmdbIpCollectRequest);

    /**
     * 查询存活IP列表及头栏统计接口
     *
     * @param cmdbIpCollectRequest
     * @return
     */
    @RequestMapping(value = "/cmdb/ipCollect/survival/findPage", method = RequestMethod.POST)
    CmdbIpCollectResult findPageS(@RequestBody CmdbIpCollectRequest cmdbIpCollectRequest);

    /**
     * 存活IP资源池下拉框接口
     *
     * @return
     */
    @RequestMapping(value = "/cmdb/ipCollect/survival/getResource", method = RequestMethod.GET)
    ResultVo getResourceS();

    /**
     * 存活IP来源下拉框接口
     *
     * @return
     */
    @RequestMapping(value = "/cmdb/ipCollect/survival/getSource", method = RequestMethod.GET)
    ResultVo getSourceS();

    //=========================虚拟IP接口=============================

    /**
     * 查询虚拟IP列表接口
     *
     * @param cmdbVipCollectRequest
     * @return
     */
    @RequestMapping(value = "/cmdb/ipCollect/fictitious/findList", method = RequestMethod.POST)
    ResultVo<List<CmdbVipCollectEntity>> findListF(@RequestBody CmdbVipCollectRequest cmdbVipCollectRequest);

    /**
     * 查询虚拟IP列表接口
     *
     * @param cmdbVipCollectRequest
     * @return
     */
    @RequestMapping(value = "/cmdb/ipCollect/fictitious/findPage", method = RequestMethod.POST)
    Result<CmdbVipCollectEntity> findPageF(@RequestBody CmdbVipCollectRequest cmdbVipCollectRequest);

    /**
     * 虚拟IP资源池下拉框接口
     *
     * @return
     */
    @RequestMapping(value = "/cmdb/ipCollect/fictitious/getResource", method = RequestMethod.GET)
    ResultVo getResourceF();

    /**
     * 虚拟IP使用类型下拉框接口
     *
     * @return
     */
    @RequestMapping(value = "/cmdb/ipCollect/fictitious/getUserType", method = RequestMethod.GET)
    ResultVo getUserTypeF();
}
