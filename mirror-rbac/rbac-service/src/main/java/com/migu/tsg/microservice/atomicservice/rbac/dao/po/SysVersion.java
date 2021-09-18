package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author menglinjie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysVersion {
    /**
     * 平台名称
     */
    private String name;
    /**
     *系统版本
     */
    private String systemVersion;
    /**
     *数据库版本
     */
    private String databaseVersion;
    /**
     *版本日期
     */
    private String versionDate;
    /**
     *版权信息
     */
    private String versionInfo;
    /**
     *首页logo图片
     */
    private String outLogo;
    /**
     *内页logo图片
     */
    private String inLogo;
    /**
     *logo图片
     */
    private String inTitleLogo;
    /**
     *登录logo图片
     */
    private String loginLogo;


}
