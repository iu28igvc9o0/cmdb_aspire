package com.migu.tsg.microservice.atomicservice.composite.service.storage.payload;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: composite-api
 * 包: com.migu.tsg.microservice.atomicservice.composite.service.storage.payload
 * 类名称: VolumesDetailResponse.java
 * 类描述:
 * 创建人: ZhangRiYue
 * 创建时间: 2017年9月14日上午10:26:30
 * 版本: v1.0
 */


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Actions {
    private List<Map<String, String>> stats;

    private List<String> resourceList;

    private String name;

    private String backup_url;

    private String bricks;

}
