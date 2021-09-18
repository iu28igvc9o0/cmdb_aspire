package com.aspire.fileCheck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MirrorDictEntity {
	private Integer dictId ;
	private String dictName;
	private String dictValue ;
}
