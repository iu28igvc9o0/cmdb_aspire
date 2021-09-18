package com.migu.tsg.microservice.atomicservice.composite.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.constant
 * @Author: baiwenping
 * @CreateTime: 2020-02-21 16:30
 * @Description: ${Description}
 */
public class AlertConfigConstants {
    public static final String YES = "1";

    public static final String NO = "0";

    public static final String MESSAGE_TEMPLATE_ALERT_TEMPLATE = "alert_template";


    public static final String IDC_TRANSFER = "idcTransfer";

    public static final Map<String, String> TASK_TEMPLATE =
            Collections.unmodifiableMap(new HashMap<String, String>() {
                private static final long serialVersionUID = -6003388516488993113L;
                {
                    put("任务名称", "taskName");
                    put("操作类型", "taskType");
                    put("任务描述", "taskDescription");
                    put("资源池", "idcType");
                    put("计划执行开始时间", "taskStartTime");
                    put("计划执行结束时间", "taskEndTime");
                }
            });
}
