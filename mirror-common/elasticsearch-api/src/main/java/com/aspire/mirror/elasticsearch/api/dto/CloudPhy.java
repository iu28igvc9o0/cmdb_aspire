package com.aspire.mirror.elasticsearch.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudPhy {

    /**
     *
     */
    private String col_time;
    /**
     *CPU使用率（%）：2位小数
     */
    private Float cpu_avg_util_percent_percent_avg;
    //内存使用率（%）：2位小数
    private Float mem_percent_usedwobufferscaches;
    /**
     * 总内存（G）
     */
    private Float mem_mem_total;
    /**
     *已用内存（G）
     */
    private Float mem_mem_used;
    /**
     *网络端口接收速率
     */
    private Float interface_rxrate;
    /**
     *网络端口发送速率
     */
    private Float interface_txrate;
    /**
     *关联资源ID
     */
    private String res_id;
    /**
     * 资源类型(服务器)
     */
    private String res_type;
    /**
     * 入库时间（云资源侧的入库时间）
     */
    private String create_time;
   
}