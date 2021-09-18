package com.aspire.mirror.redis.api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: KeyValueRedisEntity
 * Author:   zhu.juwang
 * Date:     2019/12/17 20:21
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KeyValueRedisEntity implements Serializable {
    /**
     * redis key值
     */
    private String key;
    /**
     * redis value值
     */
    private Object value;
    /**
     * redis 失效时间
     */
    private Long expireTime;
    /**
     * redis 失效时间单位
     */
    private TimeUnit timeUnit;
}
