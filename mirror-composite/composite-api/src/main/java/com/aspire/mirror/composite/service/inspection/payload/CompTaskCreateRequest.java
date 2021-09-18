package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * 任务创建对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompTaskCreateRequest.java
 * 类描述:    任务创建对象
 * 创建人:    JinSu
 * 创建时间:  2018/8/10 15:56
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompTaskCreateRequest implements Serializable {
    private static final long serialVersionUID = 6049536515624398882L;
    /** 任务名 */
    @NotBlank
    private String name;

    /** 巡检结果接收人 */
    private String receivers;

    /** 任务类型 1-手动 2-自动 */
    @NotBlank
    private String type;

    /** 时间周期,只有任务类型为自动，才能选择 MIN-分钟 MON-月 WEEK-周 DAY-日 DEFINE-自定义 */
    private String cycle;

    /** 执行时间，需根据周期类型来动态展示。具体参考已有的调度。如任务的执行时间功能。手动任务，是日期选择框 */
    @JsonProperty("exec_time")
    private String execTime;

    /** 状态:ON-启动    OFF-禁用 */
    private String status;

    /**
     * 设备信息集合，逗号分隔
     */
    @JsonProperty("object_list")
    private List<CompTaskObjectCreateReuqest> objectList;
}
