package com.aspire.mirror.ops.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    ChildGroupQueryModel
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/9/29 10:15
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildGroupQueryModel {
    private List<String> groupIdList;
}
