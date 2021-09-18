package com.aspire.aiops.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * @author ：Vincent Hu
 * @date ：Created in 6/18/2019 13:46
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveAlarm implements Serializable {

    private String alertId;
    private String itemid;
    private String alertTime;

}
