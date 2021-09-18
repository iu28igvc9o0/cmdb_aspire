package com.aspire.mirror.dao;

import com.aspire.mirror.entity.IndicationThemeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IIndicationThemeDAO {

    /**
     * 获取所有的主题
     * @return
     */
    List<IndicationThemeEntity> getAllTheme();
}
