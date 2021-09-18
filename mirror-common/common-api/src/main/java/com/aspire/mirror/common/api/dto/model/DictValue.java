package com.aspire.mirror.common.api.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.common.api.dto.model
 * 类名称:    DictValue.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/12/3 14:04
 * 版本:      v1.0
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DictValue {

    String name;

    Map<String, DictValue> child;
}
