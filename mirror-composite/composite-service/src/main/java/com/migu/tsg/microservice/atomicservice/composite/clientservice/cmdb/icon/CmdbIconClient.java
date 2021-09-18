package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.icon;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.icon.payload.CmdbIcon;

@FeignClient(value = "CMDB")
public interface CmdbIconClient {
    /**
     * 获取图片
     */
    @RequestMapping(value = "/cmdb/icon/getIcons", method = RequestMethod.POST)
    Object getIcon(@RequestBody(required = false) CmdbIcon icon, @RequestParam(value = "pageNumber", required = true) Integer pageNumber,
                   @RequestParam(value = "pageSize", required = true) Integer pageSize);

    /**
     * 上传图片
     */
    @RequestMapping(value = "/cmdb/icon/uploadIcon", method = RequestMethod.POST)
    Map<String,Object> uploadIcon(HttpServletRequest request);
}
