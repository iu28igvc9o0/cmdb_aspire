package com.aspire.mirror.service.impl;

import com.aspire.mirror.dao.IIndicationThemeDAO;
import com.aspire.mirror.entity.IndicationThemeEntity;
import com.aspire.mirror.service.IIndicationThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndicationThemeServiceImpl implements IIndicationThemeService {

    @Autowired
    private IIndicationThemeDAO indicationThemeDAO;
    /**
     * 获取所有的主题
     * @return
     */
    public List<IndicationThemeEntity> getAllTheme() {
        return indicationThemeDAO.getAllTheme();
    }
}
