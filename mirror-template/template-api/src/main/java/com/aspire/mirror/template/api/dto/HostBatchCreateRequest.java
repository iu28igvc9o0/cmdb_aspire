package com.aspire.mirror.template.api.dto;

import com.aspire.mirror.template.api.dto.vo.MirrorHostVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    HostBatchCreateRequest
 * 类描述:    自监控设备创建
 * 创建人:    JinSu
 * 创建时间:  2020/11/2 20:40
 * 版本:      v1.0
 */
@Data
public class HostBatchCreateRequest {
    @JsonProperty("host_list")
    private List<MirrorHostVO> hostList;
}
