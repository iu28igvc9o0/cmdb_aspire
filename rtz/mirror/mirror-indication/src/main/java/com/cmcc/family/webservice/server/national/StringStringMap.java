package com.cmcc.family.webservice.server.national;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "StringStringMap",
        propOrder = {"entries"}
)
public class StringStringMap {
    protected List<StringString> entries;

    public StringStringMap() {
    }

    public List<StringString> getEntries() {
        if (this.entries == null) {
            this.entries = new ArrayList();
        }

        return this.entries;
    }
}