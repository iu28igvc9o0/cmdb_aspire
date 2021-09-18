package com.aspire.mirror.collect.biz.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZbxTemplate {
    @JsonProperty("templateid")
    private String templateId;

    @JsonProperty
    private String name;

    @Override
    public boolean equals(Object obj) {
        if (!this.getClass().isInstance(obj)) {
            return false;
        }
        ZbxTemplate other = this.getClass().cast(obj);
        if (this.templateId == null) {
            return false;
        }
        return this.templateId.equals(other.templateId);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (this.templateId != null) {
            hash += 13 * this.templateId.hashCode();
        }
        return hash;
    }
}
