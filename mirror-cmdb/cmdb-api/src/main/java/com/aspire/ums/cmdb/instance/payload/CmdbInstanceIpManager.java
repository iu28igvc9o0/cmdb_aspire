package com.aspire.ums.cmdb.instance.payload;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-06-04 09:51:05
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbInstanceIpManager {
    /**
     * CI资产编号
     */
    private String instanceId;
    /**
     * 码表ID
     */
    private String codeId;
    /**
     * IP地址
     */
    private String ip;
    /**
     * IP地址类型
     */
    private String ipType;
}