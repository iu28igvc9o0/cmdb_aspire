package com.aspire.ums.cmdb.automate.payload.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fanwenhui
 * @date 2020-08-24 10:42
 * @description
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BaseAutomateEntity implements Serializable {

    private static final long serialVersionUID = 7990500333030057649L;
    // 主键ID
    private String id;
    // 主机资源实例ID
    private String assetInstanceId;
    // 自动化实例ID
    private String instanceId;
    // 所属资源池
    private String resource;
    // 创建人
    private String insertPerson;
    // 更新人
    private String updatePerson;
    // 模型ID
    private String moduleId;
    // 删除标识:0-未删除，1-已删除
    private String isDelete;
    // 创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    // 修改时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public void setIsDelete(String isDelete) {
        this.isDelete = StringUtils.isEmpty(isDelete) ? "0" : isDelete;
    }
}
