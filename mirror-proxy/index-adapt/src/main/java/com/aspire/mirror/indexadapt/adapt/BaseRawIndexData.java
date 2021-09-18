package com.aspire.mirror.indexadapt.adapt;

/**
 * 原始指标对象基类   <br/>
 * Project Name:index-proxy
 * File Name:BaseRawIndexData.java
 * Package Name:com.aspire.mirror.indexadapt.adapt
 * ClassName: BaseRawIndexData <br/>
 * date: 2018年8月7日 下午6:07:43 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
public abstract class BaseRawIndexData implements Comparable<BaseRawIndexData> {
    /**
     * 获取原始指标对象的序列号. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @return
     */
    public abstract Integer getIndexSeq();

    @Override
    public final int compareTo(BaseRawIndexData other) {
        return other.getIndexSeq().intValue() - getIndexSeq().intValue();
    }
}
