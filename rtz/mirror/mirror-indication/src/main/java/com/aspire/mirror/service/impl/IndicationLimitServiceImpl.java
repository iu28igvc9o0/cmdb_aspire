package com.aspire.mirror.service.impl;

import com.aspire.mirror.bean.IndicationLimitEntity;
import com.aspire.mirror.dao.IIndicationLimitDao;
import com.aspire.mirror.service.IIndicationLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/29
 * 软探针异常指标监控系统
 * com.aspire.mirror.service.impl.IndicationLimitServiceImpl
 */
@Service
public class IndicationLimitServiceImpl implements IIndicationLimitService {
    @Autowired
    private IIndicationLimitDao limitDao;
    @Override
    public IndicationLimitEntity getIndicationLimitByLimitId(Integer indicationLimitId) {
        return limitDao.getIndicationLimitByLimitId(indicationLimitId);
    }

    @Override
    public List<IndicationLimitEntity> getIndicationLimitByIndicationId(Integer indicationId) {
        return limitDao.getIndicationLimitByIndicationId(indicationId);
    }

    @Override
    public List<IndicationLimitEntity> getAll() {
        return limitDao.getAll();
    }

    @Override
	@Transactional(rollbackFor=Exception.class)
	public int saveMerits(List<IndicationLimitEntity> merits) {
		
		limitDao.deleteGateWayAll(merits);
		limitDao.saveGateWayMerit(merits);
		return 0;
	}

    @Override
    public IndicationLimitEntity getIndicationByIndicationIdAndProvinceCode(Integer indicationId, String provinceCode) {
        return limitDao.getIndicationByIndicationIdAndProvinceCode(indicationId, provinceCode);
    }
}
