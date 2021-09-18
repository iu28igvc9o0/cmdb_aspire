package com.aspire.mirror.indication;

import com.aspire.AbstractIndicationFactory;
import com.aspire.common.EnvConfigProperties;
import com.aspire.common.FactoryUtils;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AbstractMirrorIndicationFactory
 * Author:   zhu.juwang
 * Date:     2019/9/17 15:18
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public abstract class AbstractMirrorIndicationFactory<T> extends AbstractIndicationFactory<T> {
    @Override
    public String initWsdl() {
        EnvConfigProperties envConfigProperties = FactoryUtils.getBean(EnvConfigProperties.class);
        return envConfigProperties.getNationalWeb().getWsdl() + getEndpointAddress();
    }

    /**
     * 获取wsdl endpoint address
     * @return
     */
    public abstract String getEndpointAddress();
}
