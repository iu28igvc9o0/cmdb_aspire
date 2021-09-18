package com.aspire.mirror.inspection.server.dao.po;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReportResultStatistic {
    private Integer deviceNum;
    private Integer itemNum;
    private Integer resultNum;
    private Integer normalNum;
    private Integer exceptionNum;
    private Integer noResultNum;
    private Integer artificialJudgmentNum;
}
