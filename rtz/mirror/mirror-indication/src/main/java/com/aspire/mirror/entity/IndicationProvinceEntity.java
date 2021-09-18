package com.aspire.mirror.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/25
 * 软探针异常指标监控系统
 * com.aspire.mirror.entity.IndicationProvinceEntity
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IndicationProvinceEntity {

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 路径
     */
    private String provinceFileDir;

    /**
     * 服务器IP
     */
    private String provinceServerIp;

    /**
     * 服务器登录用户
     */
    private String provinceServerUser;

    /**
     * 服务器登录密码
     */
    private String provinceServerPwd;

    public IndicationProvinceEntity(String provinceCode) {
        this.provinceCode = provinceCode;
    }

}
