package com.aspire.mirror.dao;

import com.aspire.mirror.entity.ProcessEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IProcessDAO {

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
    ProcessEntity getProcessByIndicationId(@Param("indicationId") Integer indicationId);

    /**
     * 更新处理类信息
     * @param processEntity
     */
    void updateProcess(ProcessEntity processEntity);
}
