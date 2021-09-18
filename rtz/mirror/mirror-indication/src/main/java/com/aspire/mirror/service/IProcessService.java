package com.aspire.mirror.service;

import com.aspire.mirror.entity.ProcessEntity;

import java.util.List;

public interface IProcessService {
    /**
     * 获取所有的指标处理类
     * @return
     */
    List<ProcessEntity> getAllProcess();

    /**
     * 根据指标ID, 查找指标对应的处理类
     * @param indicationId 指标ID
     * @return
     */
    ProcessEntity getProcessByIndicationId(Integer indicationId);

    /**
     * 更新处理类信息
     * @param processEntity
     */
    void updateProcess(ProcessEntity processEntity);
}
