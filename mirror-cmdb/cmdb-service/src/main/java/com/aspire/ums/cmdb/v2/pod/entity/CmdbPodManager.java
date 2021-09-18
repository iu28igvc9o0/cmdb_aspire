package com.aspire.ums.cmdb.v2.pod.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-06-17 17:29:54
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbPodManager {

    /**
     * ID
     */
    private String id;
    /**
     * POD编码
     */
    private String podCode;
    /**
     * POD名称
     */
    private String podName;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 是否删除
     */
    private String isDelete;
}