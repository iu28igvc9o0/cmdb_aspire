package com.aspire.real_mirror.indication;

import com.aspire.AbstractIndicationFactory;
import com.aspire.common.EnvConfigProperties;
import com.aspire.common.FactoryUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AbstractRealMirrorIndicationFactory
 * Author:   zhu.juwang
 * Date:     2019/9/17 15:21
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public abstract class AbstractRealMirrorIndicationFactory<T> extends AbstractIndicationFactory<T> {
    /**
     * 计算时间
     */
    @Setter @Getter
    public String calcDate;
    /**
     * 开始时间
     */
    @Setter @Getter
    public String beginTime;
    /**
     * 结束时间
     */
    @Setter @Getter
    public String endTime;

    @Override
    public String initWsdl() {
        EnvConfigProperties envConfigProperties = FactoryUtils.getBean(EnvConfigProperties.class);
        return envConfigProperties.getRealNationalWeb().getWsdl() + getEndpointAddress();
    }

    /**
     * 获取wsdl endpoint address
     * @return
     */
    public abstract String getEndpointAddress();

    public void init() {
        super.setCalcDate(calcDate);
        super.init();
    }
}
