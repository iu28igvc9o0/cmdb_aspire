package com.aspire.ums.cmdb.demand.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class DataGrid implements Serializable {
    private static final long serialVersionUID = -7417937885426775074L;
    private int total = 0;
    private List rows;

}
