package com.aspire.fileCheck.service;

import com.aspire.fileCheck.entity.MirrorDictEntity;

import java.util.List;
import java.util.Map;

public interface IMirrorDictService {

    /**
     * 根据字典名称 获取字典列表
     * @param dictName
     * @return
     */
    List<MirrorDictEntity> getMirrorDictByDictName(String dictName);
}
