package com.aspire.mirror.service.impl;

import com.aspire.mirror.dao.IIndicationProvinceDao;
import com.aspire.mirror.entity.IndicationProvinceEntity;
import com.aspire.mirror.service.IIndicationProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/25
 * 软探针异常指标监控系统
 * com.aspire.mirror.service.impl.IndicationProvinceServiceImpl
 */
@Service
public class IndicationProvinceServiceImpl implements IIndicationProvinceService {

    @Autowired
    private IIndicationProvinceDao provinceDao;

    @Override
    public IndicationProvinceEntity getIndicationProvinceByName(String provinceName) {
        return provinceDao.getIndicationProvinceByName(provinceName);
    }

    @Override
    public List<IndicationProvinceEntity> getAllProvince() {
        return provinceDao.getAllProvince();
    }
}
