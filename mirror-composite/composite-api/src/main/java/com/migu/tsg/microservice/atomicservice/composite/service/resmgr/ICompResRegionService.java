package com.migu.tsg.microservice.atomicservice.composite.service.resmgr;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.*;
import net.sf.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
* Composite层集群管理API接口
* Project Name:composite-api
* File Name:ICompResRegionService.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr
* ClassName: ICompResRegionService <br/>
* date: 2017年9月18日 下午7:57:56 <br/>
* Composite层集群管理API接口
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@RequestMapping(path = "/v1/regions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "Composite Resource management(Region)", description = "Composite Resource management(Region)")
public interface ICompResRegionService {
    
    /**
     * 获取集群列表.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "获取集群列表", notes = "获取集群列表",
            response = CompRegionDetailResponse.class, tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List region resources for the namespace",
                                        response = CompRegionDetailResponse.class)})
    @GetMapping(value = "/{namespace}", consumes = MediaType.ALL_VALUE)
    List<CompRegionDetailResponse> listRegions(@PathVariable("namespace") String namespace);
    
    
    /**
     * 获取支持的docker版本.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "获取支持的docker版本", notes = "获取支持的docker版本",
            response = DockerVersionResponse.class, tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Get supported docker version.",
                                        response = DockerVersionResponse.class)})
    @GetMapping(value = "/{namespace}/support-docker-version", consumes = MediaType.ALL_VALUE)
    DockerVersionResponse getSupportDockerVersion(@PathVariable("namespace") String namespace); 
   
    
    /**
    * 创建集群.<br/>
    *
    * 作者： pengguihua
    * @param namespace
    * @param req
    * @return
    */
    @ApiOperation(value = "创建集群", notes = "创建集群",
            response = CompRegionCreateResponse.class, tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Create region resources for the namespace",
                                        response = CompRegionCreateResponse.class)})
    @PostMapping(value = "/{namespace}/{zone_name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    CompRegionCreateResponse createRegion(@PathVariable("namespace") String namespace,
            @PathVariable("zone_name") String zone_name,
            @Valid@RequestBody CompRegionCreateRequest req);
    
    
    /**
     * 获取集群详情.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "获取集群详情", notes = "获取集群详情",
            response = CompRegionDetailResponse.class, tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get a region detail.",
                                        response = CompRegionDetailResponse.class)})
    @GetMapping(value = "/{namespace}/{region_name}", consumes = MediaType.ALL_VALUE)
    CompRegionDetailResponse retrieveRegionDetail(@PathVariable("namespace") String namespace,
                                                         @PathVariable("region_name") String regionName);
    
    /**
     * 获取集群组件状态.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "获取集群组件状态", notes = "获取集群组件状态",
            response = CompRegionDetailResponse.class, tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get region components status.",
                                        response = CompRegionDetailResponse.class)})
    @GetMapping(value = "/{namespace}/{region_name}/components", consumes = MediaType.ALL_VALUE)
    CompRegionComponentsResp getRegionComponentList(@PathVariable("namespace") String namespace,
                                                           @PathVariable("region_name") String regionName);
    
    /**
     * 修改集群.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "修改集群", notes = "修改集群", tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                               message = "Update a region. Switch a region between EMPTY and MESOS/KUBERNETES/SWARM.")})
    @PutMapping(value = "/{namespace}/{region_name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseResponse updateRegion(@PathVariable("namespace") String namespace,
                             @PathVariable("region_name") String regionName,
                             @RequestBody CompRegionUpdateRequest reqBody);
    
    /**
     * 删除空的集群.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "删除空的集群", notes = "删除空的集群",
                  tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Delete an EMPTY region.")})
    @DeleteMapping(value = "/{namespace}/{region_name}", consumes = MediaType.ALL_VALUE)
    BaseResponse removeRegion(@PathVariable("namespace") String namespace, 
                             @PathVariable("region_name") String regionName);
    
    
    /**
    * 获取集群stats数据.<br/>
    *
    * 作者： pengguihua
    * @param namespace
    * @param regionName
    * @return
    */
    @ApiOperation(value = "获取集群stats数据", notes = "获取集群stats数据",
        response = CompRegionStatsResponse.class, tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200,  message = "get stats of cluster.",
                                        response = CompRegionStatsResponse.class)})
    @GetMapping(value = "/{namespace}/{region_name}/stats", consumes = MediaType.ALL_VALUE)
    Object getRegionStatsData(@PathVariable("namespace") String namespace,
                                                      @PathVariable("region_name") String regionName);
    
    /**
     * 修改集群stats数据.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param regionName
     * @return
     */
     @ApiOperation(value = "修改集群stats数据", notes = "修改集群stats数据",
                   tags = {"Composite Resource management(Regions) service API"})
     @ApiResponses(value = {@ApiResponse(code = 200,  message = "update stats data of a region.")})
     @PutMapping(value = "/{namespace}/{region_name}/stats", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
     BaseResponse updateRegionStatsData(@PathVariable("namespace") String namespace, 
                                       @PathVariable("region_name") String regionName,
                                       @RequestBody CompRegionStatsUpdateRequest statsUpdateData);
     
     /**
      * 获取集群Node部署和卸载命令.<br/>
      *
      * 作者： pengguihua
      * @param namespace
      * @param regionName
      * @return
      */
      @ApiOperation(value = "获取集群节点类型部署和卸载命令", notes = "获取集群节点类型部署和卸载命令",
      response = CompRegionCommandsResponse.class, tags = {"Composite Resource management(Regions) service API"})
      @ApiResponses(value = {@ApiResponse(code = 200,  message = "get deploy and uninstall scripts for node.",
                                          response = CompRegionCommandsResponse.class)})
      @GetMapping(value = "/{namespace}/{region_name}/node-scripts", consumes = MediaType.ALL_VALUE)
      CompRegionCommandsResponse getRegionNodeScriptCommands(
              @PathVariable("namespace") String namespace, 
              @PathVariable("region_name") String regionName,
              @RequestParam("node_type") String nodeType);
      
      
      /**
       * 获取集群node列表.<br/>
       *
       * 作者： pengguihua
       * @param namespace
       * @param req
       * @return
       */
      @ApiOperation(value = "获取集群node列表", notes = "获取集群node列表",
              response = CompRegionNodeResponse.class, tags = {"Composite Resource management(Regions) service API"})
      @ApiResponses(value = {@ApiResponse(code = 200, message = "get node list of a region.",
                                          response = CompRegionNodeResponse.class)})
      @GetMapping(value = "/{namespace}/{region_name}/nodes", consumes = MediaType.ALL_VALUE)
      List<CompRegionNodeResponse> listRegionNodeList(
              @PathVariable("namespace") String namespace, @PathVariable("region_name") String regionName);
      

      /**
       * 获取集群node详情.<br/>
       *
       * 作者： pengguihua
       * @param namespace
       * @param req
       * @return
       */
      @ApiOperation(value = "获取集群node详情", notes = "获取集群node详情",
              response = CompRegionNodeResponse.class, tags = {"Composite Resource management(Regions) service API"})
      @ApiResponses(value = {@ApiResponse(code = 200, message = "Get a node details in a region.",
                                          response = CompRegionNodeResponse.class)})
      @GetMapping(value = "/{namespace}/{region_name}/nodes/{private_ip:.+}", consumes = MediaType.ALL_VALUE)
      CompRegionNodeResponse retrieveRegionNodeDetail(@PathVariable("namespace") String namespace, 
              @PathVariable("region_name") String regionName, 
              @Pattern(
                 regexp="^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])(\\.(\\d{1}|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3}$", 
                 message="the format of the private_ip parameter is invalid.")
              @PathVariable("private_ip") String privateIP);
      
      /**
       * 删除集群node.<br/>
       *
       * 作者： pengguihua
       * @param namespace
       * @param req
       * @return
       */
      @ApiOperation(value = "删除集群node", notes = "删除集群node",
                    tags = {"Composite Resource management(Regions) service API"})
      @ApiResponses(value = {@ApiResponse(code = 200, 
      message = "Delete an empty node. Right now support delete a slave node or an empty node."
              + " Can't delete a controller node.")})
      @DeleteMapping(value = "/{namespace}/{region_name}/nodes/{private_ip:.+}", consumes = MediaType.ALL_VALUE)
      BaseResponse removeRegionNode(@PathVariable("namespace") String namespace, 
              @PathVariable("region_name") String regionName, @PathVariable("private_ip") String privateIP);
      
      /**
       * 获取集群stats数据.<br/>
       *
       * 作者： pengguihua
       * @param namespace
       * @param regionName
       * @return
       */
       @ApiOperation(value = "获取集群stats数据", notes = "获取集群stats数据",
       response = CompRegionStatsResponse.class, tags = {"Composite Resource management(Regions) service API"})
       @ApiResponses(value = {@ApiResponse(code = 200,  message = "get stats of cluster.",
                                           response = CompRegionStatsResponse.class)})
       @GetMapping(value = "/{namespace}/{region_name}/nodes/{private_ip}/stats", consumes = MediaType.ALL_VALUE)
       Object getRegionNodeStatsData(@PathVariable("namespace") String namespace,
               @PathVariable("region_name") String regionName, 
               @Pattern(
               regexp="^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])(\\.(\\d{1}|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])){3}$", 
               message="the format of the private_ip parameter is invalid.")
               @PathVariable("private_ip") String privateIP,
               @RequestParam(name = "node_type", required = false, defaultValue = "JAKIRO_LEGACY") String nodeType, 
               @RequestParam(name = "mode", required = false, defaultValue = "current") String mode, 
               @RequestParam(name = "metrics_name") String metircsName,
               @RequestParam(name = "start_time") String startTime,
               @RequestParam(name = "end_time") String endTime,
               @RequestParam(name = "period") String period
               );
       
       /**
        * 更新集群节点label.<br/>
        *
        * 作者： pengguihua
        * @param namespace
        * @param regionName
        * @return
        */
        @ApiOperation(value = "更新集群节点label", notes = "更新集群节点label",
                      tags = {"Composite Resource management(Regions) service API"})
        @ApiResponses(value = {@ApiResponse(code = 204,  message = "Update labels of a node.")})
        @PutMapping(value = "/{namespace}/{region_name}/nodes/{private_ip}/labels", 
                    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
        BaseResponse updateRegionNodeLabels(@PathVariable("namespace") String namespace,
                                           @PathVariable("region_name") String regionName,
                                           @PathVariable("private_ip") String privateIP,
                                           @RequestBody Map<String, String> labels);
        
        /**
         * 获得集群内所有主机的标签.<br/>
         *
         * 作者： pengguihua
         * @param namespace
         * @param regionName
         * @return
         */
         @ApiOperation(value = "获得集群内所有主机的标签", notes = "获得集群内所有主机的标签", 
                       response = CompRegionNodeLabelResponse.class, 
                       tags = {"Composite Resource management(Regions) service API"})
         @ApiResponses(value = {@ApiResponse(code = 200,  message = "Get all labels for nodes in a region.",
                                             response = CompRegionNodeLabelResponse.class)})
         @GetMapping(value = "/{namespace}/{region_name}/labels", consumes = MediaType.ALL_VALUE)
         CompRegionNodeLabelResponse getRegionNodesLabels(
                 @PathVariable("namespace") String namespace, @PathVariable("region_name") String regionName);
         
         
         /**
          * 检查标签是否合法.<br/>
          *
          * 作者： pengguihua
          * @param namespace
          * @param regionName
          * @return
          */
          @ApiOperation(value = "检查标签是否合法", notes = "检查标签是否合法", 
                        tags = {"Composite Resource management(Regions) service API"})
          @ApiResponses(value = {@ApiResponse(code = 204,  
                                              message = "Check the given label name and value is valid.")})
          @GetMapping(value = "/{namespace}/{region_name}/labels/check", consumes = MediaType.ALL_VALUE)
          void checkRegionLabel(
                  @PathVariable("namespace") String namespace, @PathVariable("region_name") String regionName,
                  @RequestParam("key") String key, @RequestParam("value") String value);
          
          
          /**
           * 在集群节点上执行action操作.<br/>
           *
           * 作者： pengguihua
           * @param namespace
           * @param regionName
           * @return
           */
           @ApiOperation(value = "在集群节点上执行action操作", notes = "在集群节点上执行action操作",
                         tags = {"Composite Resource management(Regions) service API"})
           @ApiResponses(value = {@ApiResponse(code = 204,  message = "Do various actions on a node.")})
           @PutMapping(value = "/{namespace}/{region_name}/nodes/{private_ip}/actions", 
                       consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
           void execRegionAction(@PathVariable("namespace") String namespace,
                                        @PathVariable("region_name") String regionName,
                                        @PathVariable("private_ip") String privateIP,
                                        @RequestBody CompRegionActionRequest actionCmd);
           
           /**
            * 更新集群主机类型.<br/>
            *
            * 作者： pengguihua
            * @param namespace
            * @param regionName
            * @return
            */
            @ApiOperation(value = "更新集群主机类型", notes = "更新集群主机类型",
                          tags = {"Composite Resource management(Regions) service API"})
            @ApiResponses(value = {@ApiResponse(code = 204,  
                                    message = "Update the type of a node. Choices are ['mesos-slave', "
                                            + "'mesos-slave-attribute', 'empty', 'k8s-slave'].")})
            @PutMapping(value = "/{namespace}/{region_name}/nodes/{private_ip}/update_type", 
                        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
            BaseResponse updateRegionNodeType(@PathVariable("namespace") String namespace,
                                             @PathVariable("region_name") String regionName,
                                             @PathVariable("private_ip") String privateIP,
                                             @RequestBody CompRegionNodeUpdateTypeRequest nodeType);

    /**
     * 获取集群配置列表
     *
     * @param namespace
     * @param regionName
     * @return
     */
    @ApiOperation(value = "获取集群配置列表", notes = "获取集群配置列表",
            tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Get all config by region name.")})
    @GetMapping(value = "/{namespace}/{region_name}/config", consumes = MediaType.ALL_VALUE)
    List<CompRegionConfigDetail> getRegionConfigList(@PathVariable("namespace") String namespace,
            @PathVariable("region_name") String regionName);

    /**
     * 添加集群配置.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param regionName
     * @return
     */
    @ApiOperation(value = "添加集群配置", notes = "添加集群配置", tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "create region config.", response = CompRegionConfigDetail.class)})
    @PostMapping(value = "/{namespace}/{region_name}/config", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseResponse createRegionConfig(@PathVariable("namespace") String namespace,
            @PathVariable("region_name") String regionName, @RequestBody CompRegionConfigCreateRequests regionConfigs);

    /**
     * 修改集群配置
     *
     * @param namespace
     * @param regionName
     * @return
     */
    @ApiOperation(value = "修改集群配置", notes = "修改集群配置", tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "update region config.", response = CompRegionConfigDetail.class)})
    @PutMapping(value = "/{namespace}/{region_name}/config/{type}/{key}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    CompRegionConfigDetail putRegionConfig(@PathVariable("namespace") String namespace,
            @PathVariable("region_name") String regionName, @PathVariable("type") String type,
            @PathVariable("key") String key, @RequestBody CompRegionConfigUpdateRequest regionConfig);

    /**
     * 删除集群配置.
     *
     * @param namespace
     * @param regionName
     * @return
     */
    @ApiOperation(value = "删除集群配置", notes = "删除集群配置", tags = {"Composite Resource management(Regions) service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "delete region config.", response = BaseResponse.class)})
    @DeleteMapping(value = "/{namespace}/{region_name}/config/{type}/{key}", consumes = MediaType.ALL_VALUE)
    BaseResponse deleteRegionConfig(@PathVariable("namespace") String namespace,
            @PathVariable("region_name") String regionName, @PathVariable("type") String type,
            @PathVariable("key") String key);
    
    
    /**
     * halcyon获取主机部署资源列表.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param regionName
     * @return
     */
     @ApiOperation(value = "halcyon获取主机部署资源列表", notes = "halcyon获取主机部署资源列表",
                   tags = {"Composite Resource management(Regions) service API"})
     @ApiResponses(value = {@ApiResponse(code = 200, message = "Get halcyon resources success",
    		 response = CompHalcyonList.class)})
     @GetMapping(value = "/{namespace}/{region_name}/halcyon", 
                 consumes = MediaType.ALL_VALUE)
    CompHalcyonList getHalcyonResourceList(@PathVariable("namespace") String namespace,
            @PathVariable("region_name") String regionName);
     
     /**
      * halcyon更新某个主机部署资源
      * @param namespace
      * @param regionName
      * @param resourceUuid
      * @param compHalcyonResourcesResponse
      * @return
      */
     @ApiOperation(value = "halcyon更新某个主机部署资源", notes = "halcyon更新某个主机部署资源",
             tags = {"Composite Resource management(Regions) service API"})
     @ApiResponses(value = {@ApiResponse(code = 204, message = "Update halcyon resources success",
    		 response = BaseResponse.class)})
     @PutMapping(value = "/{namespace}/{region_name}/halcyon/{resource_uuid}", 
                   consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
     BaseResponse updateHalcyonResource(@PathVariable("namespace") String namespace,
             @PathVariable("region_name") String regionName,
             @PathVariable("resource_uuid") String resourceUuid,
             @RequestBody ComHalcyonUpRequest compHalcyon);

    /**
     * 根据集群名称和根账号获取集群配置
     * 作者： zhujuwang
     * @param regionId 集群名称
     * @param type 类型 固定为LYCAN
     * @return
     * 	{
     *		"endpoint": "http://管理域lycan nginx代理地址"
     *  }
     */
    @ApiOperation(value = "获取集群配置信息", notes = "获取集群配置信息", tags = {
            "Composite Resource management(Regions) service API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Get region meta_info.") })
    @GetMapping(value = "/{region_id}/metainfo/{type}", consumes = MediaType.ALL_VALUE)
    JSONObject getRegionMetaInfo(@PathVariable("region_id") String regionId, @PathVariable("type") String type);
}
