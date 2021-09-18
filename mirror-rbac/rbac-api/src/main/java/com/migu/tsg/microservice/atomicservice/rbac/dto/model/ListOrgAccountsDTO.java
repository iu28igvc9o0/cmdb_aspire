package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: ListOrgAccountsDTO.java <br>
 * 类描述: 查询成员列表DTO对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月28日上午11:34:12 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListOrgAccountsDTO {

    /**
     * 成员集合总数
     */
    private int count;

    /**
     * 页容量
     */
    @JsonProperty("page_size")
    private int pageSize;

    /**
     * 当前页数
     */
    @JsonProperty("num_pages")
    private int numPages;

    /**
     * 下一页数
     */
    private Integer next;

    /**
     * 上一页数
     */
    private Integer previous;

    /**
     * 成员集合
     */
    private List<AccountDTO> results;
}
