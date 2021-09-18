package com.migu.tsg.microservice.atomicservice.ldap.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* 项目名称: ldap-api <br>
* 包: com.migu.tsg.microservice.atomicservice.ldap.dto <br>
* 类名称: ListPagenationResponse.java <br>
* 类描述: 获取命名空间中(根账号)的成员信息列表响应对象<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月8日上午10:45:54 <br>
* 版本: v1.0
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListPagenationResponse {

    /**
     * 传参或指定
     */
    @JsonProperty("current_page")
    private int currentPage;

    /**
     * 每页显示记录数
     */
    @JsonProperty("page_size")
    private int pageSize;

    /**
     * 总记录数
     */
    @JsonProperty("row_count")
    private int rowCount;

    /**
     * 总页数
     */
    @JsonProperty("page_count")
    private int pageCount;

    /**
     * 当前页面开始行
     */
    @JsonProperty("start_row")
    private int startRow;

    /**
     * 首页
     */
    private int first = 1;

    /**
     * 最后一页
     */
    private int last;

    /**
     * 下一页
     */
    private int next;

    /**
     * 上一页
     */
    private int prev;

    /**
     * 成员集合
     */
    private List<GetLdapUserResponse> results;
}
