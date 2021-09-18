package com.aspire.fileCheck.service.impl;

import com.aspire.fileCheck.dao.IFileIndicationDAO;
import com.aspire.fileCheck.entity.FileIndicationEntity;
import com.aspire.fileCheck.service.IFileIndicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: FileIndicationServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/11/13 11:21
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Service
public class FileIndicationServiceImpl implements IFileIndicationService {

    @Autowired
    private IFileIndicationDAO fileIndicationDAO;

    @Override
    public List<FileIndicationEntity> getFileIndication(String catalog) {
        return fileIndicationDAO.getFileIndication(catalog);
    }
}
