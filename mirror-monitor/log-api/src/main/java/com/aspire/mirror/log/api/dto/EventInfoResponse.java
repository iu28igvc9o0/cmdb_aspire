package com.aspire.mirror.log.api.dto;

import com.aspire.mirror.common.entity.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
* 类说明 ： 事件保存请求返回类
* 项目名称:  微服务
* 包:        com.migu.tsg.microservice.monitor.log.dto
* 类名称:    EventInfoResponse.java
* 类描述:    本类是一个文件操作工具类，包括了文件的几个基本操作方法，创	*            建文件、删除文件、文件重命名
* 创建人:    jiangfuyi
* 创建时间:  2017年7月27日
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventInfoResponse {
    // 默认返回成功
    private Result result = Result.success();
}
