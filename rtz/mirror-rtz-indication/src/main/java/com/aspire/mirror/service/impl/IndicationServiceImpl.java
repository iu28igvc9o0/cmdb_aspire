package com.aspire.mirror.service.impl;

import com.aspire.mirror.dao.IIndicationDAO;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.service.IIndicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IndicationServiceImpl implements IIndicationService {

    @Autowired
    private IIndicationDAO indicationDAO;

    @Override
    public List<IndicationEntity> getAllIndication() {
        return indicationDAO.getAllIndication();
    }

    @Override
    public IndicationEntity getIndicationByIndicationId(Integer indicationId) {
        return indicationDAO.getIndicationByIndicationId(indicationId);
    }

    @Override
    public List<IndicationEntity> filterList(Map<String, String> params) {
        return indicationDAO.filterList(params);
    }

}
