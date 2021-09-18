package com.aspire.fileCheck.service;

import com.aspire.fileCheck.entity.FileIndicationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IFileIndicationService
 * Author:   zhu.juwang
 * Date:     2019/11/13 11:12
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface IFileIndicationService {

    /**
     * 查询文件指标列表
     * @param catalog 分类
     * @return
     */
    List<FileIndicationEntity> getFileIndication(@Param("catalog") String catalog);

}
