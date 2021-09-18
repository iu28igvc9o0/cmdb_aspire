package com.aspire.mirror.composite.payload.third;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TodayWarningMessageResponse implements Serializable {

    private String  name;

    private Integer great ;

    private Integer senior;

    private Integer mediumGrade;

    private Integer elementary ;

    private Integer  sum ;
}
