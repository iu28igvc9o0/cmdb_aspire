package com.aspire.ums.cmdb.automate.payload;

import com.alibaba.fastjson.annotation.JSONField;
import com.aspire.ums.cmdb.automate.payload.base.BaseAutomateEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author fanwenhui
 * @date 2020-08-24 15:08
 * @description 自动化主机模型——服务信息
 */
@Data
@ToString(callSuper = true)
public class AutomateHostServiceEntity extends BaseAutomateEntity implements Serializable {
    private static final long serialVersionUID = -6709680766364086485L;
    // 名称
    private String name;
    // 当前工作目录
    private String cwd;
    // 执行路径
    private String exe;
    // 监听地址
    private String listeningIp;
    // 监听端口
    private String listeningPort;
    // 进程名称
    private String pname;
    // 执行用户
    private String username;
}
