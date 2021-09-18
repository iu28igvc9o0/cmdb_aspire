package com.aspire.mirror.alert.api.dto.third;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TodayWarningMessageDTO {

    private String  name;

    private Integer great ;

    private Integer senior;

    private Integer mediumGrade;

    private Integer elementary ;

    private Integer  sum ;
}
