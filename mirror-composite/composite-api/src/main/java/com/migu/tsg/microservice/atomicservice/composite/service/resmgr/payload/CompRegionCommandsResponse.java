package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.Data;


/**
* 集群节点部署及卸载脚本命令model
* Project Name:composite-api
* File Name:CompRegionCommandsResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CompRegionCommandsResponse <br/>
* date: 2017年9月21日 上午1:41:30 <br/>
* TODO 详细描述这个类的功能等
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
public class CompRegionCommandsResponse {
    private Commands commands;
    
    @Data
    public static class Commands {
        private String install;
        private String uninstall;
    }
}
