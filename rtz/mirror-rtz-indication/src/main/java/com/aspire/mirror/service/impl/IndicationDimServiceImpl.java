package com.aspire.mirror.service.impl;

import com.aspire.mirror.dao.IIndicationDimDAO;
import com.aspire.mirror.entity.IndicationDimEntity;
import com.aspire.mirror.service.IIndicationDimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndicationDimServiceImpl implements IIndicationDimService {

    @Autowired
    private IIndicationDimDAO indicationDimDAO;

    /**
     * 获取所有的指标项
     * @return
     */
    public List<IndicationDimEntity> getAllDim() {
        return indicationDimDAO.getAllDim();
    }

    @Override
    public List<IndicationDimEntity> getDimListByThemeCode(String themeCode) {
        return indicationDimDAO.getDimListByThemeCode(themeCode);
    }

    /**
     * 批量新增/更新
     * @param dimList
     */
    public void batchInsert(List<IndicationDimEntity> dimList) {
        indicationDimDAO.batchInsert(dimList);
    }

    @Override
    public void deleteDim(IndicationDimEntity dimEntity) {
        indicationDimDAO.deleteDim(dimEntity);
    }
}
