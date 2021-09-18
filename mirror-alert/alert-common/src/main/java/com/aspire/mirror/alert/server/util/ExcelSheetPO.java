package com.aspire.mirror.alert.server.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ExcelSheetPO {
    private String sheetName;
    private String title;
    private String[] headers;
    private List<List<Object>> dataList;
}
