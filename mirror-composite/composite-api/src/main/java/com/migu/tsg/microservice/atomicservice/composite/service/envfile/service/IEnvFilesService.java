package com.migu.tsg.microservice.atomicservice.composite.service.envfile.service;

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
import net.sf.json.JSONArray;

/**
 * ClassName: EnvFilesService <br/>
 * date: 2017年7月26日 下午2:31:44 <br/>
 *
 * @author zhangqing
 * @version
 */
@RequestMapping("${version}/env-files")
@Api(value = "环境变量文件模块", description = "环境变量文件模块")
public interface IEnvFilesService {
    /**
     * ok
     */
    int RESPONSE_SUCCESS200 = 200;
    /**
     * No Content
     */
    int DELETE_RESPONSE_SUCCESS = 204;
    /**
     * created
     */
    int RESPONSE_SUCCESS201 = 201;

    /**
     *
    * getEnvFiles:获取环境变量一组记录到前台网页展现. <br/>
    *
    * 作者： zhangqing
    * @return
     */
    @GetMapping(value = "/{namespace}", produces = { "application/json" })
    @ApiOperation(value = "获取一组环境变量", notes = "获取一组环境变量", response = JSONArray.class, 
                  tags = { "composite env_file management service API" })
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200, message = "获取一组环境变量", 
                  response = JSONArray.class)})
    public String getEnvFileList(@PathVariable("namespace") String namespace,
                                 @RequestParam(value ="project_name",required = false)String projectName); 
                                 

    /**
     *
    * createEnvFiles:创建环境变量资源 <br/>
    *
    * 作者： zhangqing
    * @param rubicRequest
    * @return
     */
    @PostMapping(value = "/{namespace}", produces = { "application/json" })
    @ApiOperation(value = "创建环境变量", notes = "创建环境变量", response = String.class, 
                  tags = {"composite env_file management service API"})
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200, message = "创建成功", response = String.class)})
    public String createEnvFiles(@PathVariable("namespace") String namespace,
                                 @RequestParam(value ="project_name",required = false)String projectName,
                                 @RequestBody String rubicRequest);    

    /**
     * getSpecifiedEnvFile:获取指定环境变量. 作者： zhangqing
     * @param envfileName
     *            envfileName
     * @return EnvFilesResponse
     */
    @GetMapping(value = "/{namespace}/{envfileName}",produces = { "application/json" })
    @ApiOperation(value = "获取指定环境变量", notes = "获取指定环境变量", response = String.class, tags = {
                  "composite env_file management service API" })
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200,
                            message = "返回指定的环境变量", response = String.class)})
    public String getSpecifiedEnvFile(@PathVariable("namespace") String namespace,
                                          @PathVariable(value = "envfileName") String envfileName);

    /**
     * updateEnvFileByName:更新环境变量. 作者： zhangqing
     * @param envfileName
     *            envfileName
     * @param EnvFileResquest
     *            envRequest
     * @return EnvFilesResponse
     */
    @PutMapping(value = "/{namespace}/{envfileName}",produces = { "application/json" })
    @ApiOperation(value = "更新环境变量", notes = "更新环境变量", response = String.class, 
                  tags = { "composite env_file management service API"})
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200, message = "更新成功", response = String.class) })
    public String updateEnvFileByName(@PathVariable("namespace") String namespace,
                                      @PathVariable(value = "envfileName") String envfileName,
                                      @RequestBody String rubicRequest);
    
    /**
     * delEnvFileByName:删除环境变量. 作者： zhangqing
     * @param envfileName
     *            envfileName
     */
    @DeleteMapping(value = "/{namespace}/{envfileName}",produces = {"application/json" })
    @ApiOperation(value = "删除环境变量", notes = "删除环境变量", response = BaseResponse.class, 
                  tags = {"composite env_file management service API"})
    @ApiResponses(value = { @ApiResponse(code = DELETE_RESPONSE_SUCCESS, message = "删除成功", 
                            response = BaseResponse.class)})
    public BaseResponse delEnvFileByName(@PathVariable("namespace") String namespace,
                                 @PathVariable(value = "envfileName") String envfileName);
}
