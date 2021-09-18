package com.aspire.mirror.composite.payload.dashboard;

import lombok.Data;

/**
 * @author baiwp
 * @title: ItemIndexDto
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/7/2714:12
 */
@Data
public class ComItemIndexDto {
    private String moniObject;
    private String key;
    private String units;
    private String valueType;//0history3history_unit

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof ComItemIndexDto) {
            ComItemIndexDto vo = (ComItemIndexDto) obj;

            // 比较每个属性的值 一致时才返回true
            if (vo.key == null ? this.key == null : vo.key.equals(this.key)
                    && vo.units == null ? this.units == null : vo.units.equals(this.units)
                    && vo.valueType == null ? this.valueType == null : vo.valueType.equals(this.valueType))
                return true;
        }
        return false;
    }

    /**
     * 重写hashcode 方法，返回的hashCode不一样才再去比较每个属性的值
     */
    @Override
    public int hashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append(units);
        sb.append(valueType);
        return sb.toString().hashCode();
    }
}
