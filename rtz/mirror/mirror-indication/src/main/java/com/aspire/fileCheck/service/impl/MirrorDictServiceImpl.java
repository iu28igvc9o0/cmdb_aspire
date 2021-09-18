package com.aspire.fileCheck.service.impl;

import com.aspire.fileCheck.dao.IMirrorDictDAO;
import com.aspire.fileCheck.entity.MirrorDictEntity;
import com.aspire.fileCheck.service.IMirrorDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MirrorDictServiceImpl implements IMirrorDictService {

    @Autowired
    private IMirrorDictDAO mirrorDictDAO;

    @Override
    public List<MirrorDictEntity> getMirrorDictByDictName(String dictName) {
        return mirrorDictDAO.getMirrorDictByDictName(dictName);
    }
}
