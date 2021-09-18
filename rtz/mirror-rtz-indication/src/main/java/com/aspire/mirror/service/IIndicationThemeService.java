package com.aspire.mirror.service;

import com.aspire.mirror.entity.IndicationThemeEntity;

import java.util.List;

public interface IIndicationThemeService {
    /**
     * 获取所有的主题
     * @return
     */
    List<IndicationThemeEntity> getAllTheme();
}
