package com.aspire.mirror.service;

import com.aspire.mirror.entity.IndicationAllItemEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/24
 * 软探针异常指标监控系统
 * com.aspire.mirror.service.IIndicationItemService
 */
public interface IIndicationItemService {
    /**
     * 根据指标ID, 获取指标所有的指标项.
     * @param indicationId
     * @return
     */
    List<IndicationAllItemEntity> getIndicationAllItemsByIndicationId(Integer indicationId);
}
