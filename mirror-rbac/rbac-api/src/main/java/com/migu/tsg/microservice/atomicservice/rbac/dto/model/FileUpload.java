/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 项目名称: rbac-api <br>
* 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
* 类名称: FileUpload.java <br>
* 类描述: 公司LOGO图片DTO对象<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月23日上午10:53:04 <br>
* 版本: v1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUpload {

    /**
     * 公司LOGO图片Base64格式
     */
    @JsonProperty("logo_file")
    private String logoFile;
}
