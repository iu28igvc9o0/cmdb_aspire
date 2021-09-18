package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: ErrorsResponse.java <br>
 * 类描述: 错误响应对象<br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月29日上午10:55:24 <br>
 * 版本: v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorsResponse {

    /**
     * 错误响应结果
     * 例子：
     *      // 场景：用户没有权限操作资源。
     *      // 返回状态码：403
     *      {
     *        "errors": [
     *          {
     *            "source": "1007",
     *            "code": "permission_denied",
     *            "message": "Current user has no permission to perform the action"
     *          }
     *        ]
     *      }
     *      
     *      // 场景：用户 POST 了不正确的参数。
     *      // 返回状态码：400
     *      {
     *        "errors": [
     *          {
     *            "source": "1007",
     *            "code": "invalid_args",
     *            "message": "Invalid parameters were passed.",
     *            "fields": [
     *              {
     *                "field1": ["field1 invalid message1", "field1 invalid message2"],
     *                "field2": {
     *                  "child-field1": ["child-field1 invalid message"],
     *                  "child-field2": ["child-filed2 invalid message"]
     *                }
     *              }
     *            ]
     *          }
     *        ]
     *      }
     */
    private List<Map<String, Object>> errors;

}
