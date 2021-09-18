package com.aspire.mirror.service.impl;

import com.aspire.mirror.dao.IIndicationItemDAO;
import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationItemEntity;
import com.aspire.mirror.service.IIndicationItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/24
 * 软探针异常指标监控系统
 * com.aspire.mirror.service.impl.IndicationItemServiceImpl
 */
@Service
public class IndicationItemServiceImpl implements IIndicationItemService {

    @Autowired
    private IIndicationItemDAO indicationItemDAO;

    @Override
    public List<IndicationAllItemEntity> getIndicationAllItemsByIndicationId(Integer indicationId) {
        return indicationItemDAO.getIndicationAllItemsByIndicationId(indicationId);
    }
}
