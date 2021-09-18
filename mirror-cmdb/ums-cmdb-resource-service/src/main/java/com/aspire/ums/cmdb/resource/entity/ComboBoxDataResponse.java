package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ComboBoxDataResponse implements Serializable {
    
    private List<ComboBox> names;

    private List<ComboBox> types;

    private List<ComboBox> pools;
}
