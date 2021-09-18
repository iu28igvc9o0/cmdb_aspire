package com.aspire.mirror.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessEntity implements Cloneable{
    /**
     * 处理类ID
     */
    private Integer processId;
    /**
     * 指标ID
     */
    private Integer indicationId;
    /**
     * 处理类
     */
    private String processClass;
    /**
     * 处理状态
     */
    private String processStatus;
    /**
     * 最后处理时间
     */
    private Date processLastTime;

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
