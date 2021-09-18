package com.aspire.ums.cmdb.automate.payload;

import com.aspire.ums.cmdb.automate.payload.base.BaseAutomateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author fanwenhui
 * @date 2020-08-24 15:03
 * @description 自动化主机模型——CPU信息
 */
@Data
@ToString(callSuper = true)
public class AutomateHostCpuEntity extends BaseAutomateEntity implements Serializable {
    private static final long serialVersionUID = -852115557879559118L;
    // 型号
    private String brand;
    // 架构
    private String architecture;
    // 频率
    private String hz;
    // 物理颗粒数
    private String cpuPieces;
    // 总物理核心数
    private String physicalCores;
    // 逻辑CPU数
    private String logicalCores;
}
