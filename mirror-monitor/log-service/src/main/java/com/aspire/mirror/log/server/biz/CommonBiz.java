package com.aspire.mirror.log.server.biz;

/**
 * @BelongsProject: mirror-monitor
 * @BelongsPackage: com.aspire.mirror.log.server.biz
 * @Author: baiwenping
 * @CreateTime: 2020-05-19 16:09
 * @Description: ${Description}
 */
public interface CommonBiz {

    /**
     * 生成跨域查询索引
     * @param indices
     * @return
     */
    String[] getClusterIndex (Object queryParam, String... indices);
}
