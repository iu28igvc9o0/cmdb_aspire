package com.migu.tsg.microservice.atomicservice.common.exception;

import java.util.Arrays;
import java.util.List;

import com.migu.tsg.microservice.atomicservice.common.enums.ResultErrorEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.common.exception <br>
 * 类名称: BadRequestFieldException.java <br>
 * 类描述: 请求字段不合法的异常 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月29日下午8:41:08 <br>
 * 版本: v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BadRequestFieldException extends BaseException {

    private static final long serialVersionUID = 5516820019652198908L;

    /**
     * 无效的字段名
     */
    private String fieldName;

    /**
     * 无效原因的提示信息
     */
    private List<String> tipInfo;

    /**
     * 错误字段异常
     *   "fields": [
     *              {
     *                "field1": ["field1 invalid message1", "field1 invalid message2"],
     *                "field2": {
     *                  "child-field1": ["child-field1 invalid message"],
     *                  "child-field2": ["child-filed2 invalid message"]
     *                }
     *              }
     *            ]
     * @param resultErrorEnum resultErrorEnum
     * @param fieldName 字段名称
     * @param tipInfo 提示信息(为了和灵雀云接口一致,所以封装成数组) 例如：["field1 invalid message1", "field1 invalid message2"]
     */
    public BadRequestFieldException(final ResultErrorEnum resultErrorEnum, final String fieldName,
            final String[] tipInfo) {
        super(resultErrorEnum);
        this.fieldName = fieldName;
        this.tipInfo = Arrays.asList(tipInfo);
    }
    
}
