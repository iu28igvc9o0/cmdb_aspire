package com.aspire.mirror.alert.server.vo.monthReport;

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
public class AlertCloudVmVo {

    /**
     *采集时间
     */
    private String col_time;
    /**
     * CPU使用率（%）：2位小数
     */
    private Float cpu_avg_util_percent;
    //内存使用率（%）：2位小数
    private Float mem_avg_util_percent;
    /**
     * 已用内存（G）
     */
    private Float mem_used_size;
    /**
     *总内存（G）
     */
    private Float mem_total_size;
    /**
     *磁盘读速率（KBS）
     */
    private Float disk_read_bytes_rate;
    /**
     *磁盘写速率（KBS）
     */
    private Float disk_write_bytes_rate;
    /**
     *硬盘读出IOPS（KBS）
     */
    private Float disk_read_requests_rate;
    /**
     * 硬盘写入IOPS（KBS）
     */
    private Float disk_write_requests_rate;
    /**
     * 网络端口接收速率（KBS
     */
    private Float network_incoming_bytes_rate;
    /**
     * 网络端口发送速率（KBS）
     */
    private Float network_outgoing_bytes_rate;
    
    /**
     * 磁盘空间使用百分比（%）：2位小数
     */
    private Float disk_all_percent;
    
    /**
     * 磁盘空间总大小（G）
     */
    private Float disk_all_total;
    
    /**
     * 磁盘空间已使用大小（G）
     */
    private Float disk_all_used;
    
    /**
     * 关联资源ID
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