package com.aspire.ums.cmdb.v3.dictMapper.payload;

import lombok.Data;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbDictMapperEntity
 * Author:   zhu.juwang
 * Date:     2019/10/15 11:01
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
public class CmdbDictMapperEntity {

    public CmdbDictMapperEntity() {

    }

    public CmdbDictMapperEntity(String mapperSource) {
        this.mapperSource = mapperSource;
    }
    public CmdbDictMapperEntity(String mapperDictType, String mapperSource) {
        this.mapperDictType = mapperDictType;
        this.mapperSource = mapperSource;
    }

    public CmdbDictMapperEntity(String mapperDictType, String mapperSource, String mapperDeviceType) {
        this.mapperDictType = mapperDictType;
        this.mapperSource = mapperSource;
        this.mapperDeviceType = mapperDeviceType;
    }

    /**
     * ID
     */
    private String id;

    /**
     * 字典类型
     */
    private String mapperDictType;

    /**
     * 第三方字典编码
     */
    private String mapperDictCode;

    /**
     * UMS字典编码
     */
    private String umsDictCode;

    /**
     * UMS字典值
     */
    private String umsDictName;

    /**
     * 设备类型
     */
    private String mapperDeviceType;

    /**
     * 第三方来源
     */
    private String mapperSource;
}
