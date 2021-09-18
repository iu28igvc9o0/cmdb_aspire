package com.aspire.ums.cmdb.automate.payload;

import com.aspire.ums.cmdb.automate.payload.base.BaseAutomateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author fanwenhui
 * @date 2020-08-24 15:06
 * @description 自动化主机模型——磁盘信息
 */
@Data
@ToString(callSuper = true)
public class AutomateHostDiskEntity extends BaseAutomateEntity implements Serializable {
    private static final long serialVersionUID = -2431798284672964672L;
    // 磁盘
    private String device;
    // 文件系统类型
    private String fstype;
    // 挂载点
    private String mountpoint;
    // 容量（KB）
    private String size;
    // 网络挂载
    private String provider;
}
