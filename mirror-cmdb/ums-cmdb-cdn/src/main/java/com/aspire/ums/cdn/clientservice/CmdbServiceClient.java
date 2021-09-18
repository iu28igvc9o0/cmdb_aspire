/**
 *
 * 项目名： ums-cmdb-cdn 
 * <p/> 
 *
 * 文件名:  CmdbServiceClient.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.ums.cdn.clientservice;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cdn.clientservice.payload.CmdbCode;
import com.aspire.ums.cdn.clientservice.payload.CmdbInstance;
import com.aspire.ums.cdn.model.Module;

/** 
 *
 * 项目名称: ums-cmdb-cdn 
 * <p/>
 * 
 * 类名: CmdbServiceClient
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@FeignClient(value = "CMDB", url = "http://10.1.203.100:2222")
public interface CmdbServiceClient {
	
	/**
     * 查询模型
     */
    @RequestMapping(value = "/cmdb/module/getModuleSelective", method = RequestMethod.POST)
    List<Module> queryModuleByParams(@RequestBody Module queryParam);

	
    /** 
     * 功能描述: 查询模型下的码表列表  
     * <p>
     * @param moduleId
     * @return
     */
    @RequestMapping(value = "/cmdb/code/list/{moduleId}", method = RequestMethod.GET)
    List<CmdbCode> getCodeListByModuleId(@PathVariable("moduleId") String moduleId);
    
	/**
     * 新增CI
     */
    @RequestMapping(value = "/cmdb/instance/add", method = RequestMethod.POST)
    Map<String, Object> addInstance(@RequestBody Map<String, Object> instanceData);

    /**
     * 更新CI
     */
    @RequestMapping(value = "/cmdb/instance/update/{id}", method = RequestMethod.POST)
    Map<String, Object> updateInstance(@PathVariable("id") String id, @RequestBody Map<String, Object> instanceData);

    /**
     * 根据资源池及IP地址查询设备信息
     * @param idc 资源池
     * @param deviceIp 设备IP
     * @return
     */
    @RequestMapping(value = "/cmdb/instance/queryDeviceByRoomAndIP", method = RequestMethod.GET)
    CmdbInstance queryDeviceByRoomIdAndIP(@RequestParam(value = "idc", required = false) String idc,
                                                @RequestParam("deviceIp") String deviceIp);
}
