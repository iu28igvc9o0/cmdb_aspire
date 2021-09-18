package com.aspire.real_mirror.service;

import net.sf.json.JSONObject;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IRealIndicationDataService
 * Author:   zhu.juwang
 * Date:     2019/11/11 10:11
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface IRealIndicationDataService {
    /**
     * 获取指标列表数据
     * @param indicationOwner
     * @param indicationCatalog
     * @param indicationPosition
     * @param indicationFrequency
     * @param calcDate
     * @return
     */
    JSONObject getDayIndicationMailData(String indicationOwner, String indicationCatalog, String indicationPosition,
                                        String indicationFrequency, String calcDate);
}
