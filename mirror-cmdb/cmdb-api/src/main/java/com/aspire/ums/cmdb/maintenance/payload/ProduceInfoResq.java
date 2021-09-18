package com.aspire.ums.cmdb.maintenance.payload;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * @Dscription: 维保厂商
 * @Author: PJX
 * @Version: V1.0
 */
@Data
@NoArgsConstructor
public class ProduceInfoResq {

    private String id;

    private String produce;

    private String logoUrl;

    private String type;

    private String remark;

    private int isdel;

    List<Concat> concats;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProduceInfoResq that = (ProduceInfoResq) o;
        return isdel == that.isdel &&
                Objects.equals(id, that.id) &&
                Objects.equals(produce, that.produce) &&
                Objects.equals(type, that.type) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(concats, that.concats);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, produce, type, remark, isdel, concats);
    }
}
