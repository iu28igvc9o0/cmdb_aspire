package com.aspire.mirror.alert.server.vo.third;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TodayWarningMessageVo {

    private String  name;

    private Integer great ;

    private Integer senior;

    private Integer mediumGrade;

    private Integer elementary ;

    private Integer  sum ;
}
