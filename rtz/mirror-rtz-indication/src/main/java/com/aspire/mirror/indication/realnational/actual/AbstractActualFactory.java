package com.aspire.mirror.indication.realnational.actual;

import com.aspire.mirror.indication.AbstractFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractActualFactory<T> extends AbstractFactory<T> {

    @Override
    /**
     * 调用wsdl接口. <此为通用方法> 如果刚方法调用规则不满足子类, 子类可重写该方法. 请勿直接修改此方法.
     */
    public void callWsdl() {
        super.callWsdl();
    }
}
