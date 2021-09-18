package com.aspire.ums.cmdb.v3.dictMapper.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbSyncFiledMapperEntity
 * Author:   hangfang
 * Date:     2020/9/9
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
public class CmdbSyncFiledMapperEntity {

    /**
     *ID
     */
    private String id;

    /**
     *第三方映射字段编码
     */
    private String otherFiledCode;

    /**
     *UMS映射字段编码
     */
    private String umsFiledCode;

    /**
     *映射归属类型(X86服务器、云主机等)
     */
    private String mapperType;

    /**
     * 字段类型（date）
     */
    private String filedType;

    /**
     *来源 苏研/华为等等
     */
    private String source;

    public CmdbSyncFiledMapperEntity(String mapperType, String source) {
        this.mapperType = mapperType;
        this.source = source;
    }

}
