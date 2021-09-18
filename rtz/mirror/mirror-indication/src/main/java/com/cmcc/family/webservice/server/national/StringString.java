package com.cmcc.family.webservice.server.national;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "StringString",
        propOrder = {"key", "value"}
)
public class StringString {
    @XmlElement(
            required = true
    )
    protected String key;
    @XmlElement(
            required = true
    )
    protected String value;

    public StringString() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String value) {
        this.key = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
