package com.aspire.mirror.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NamedParameter {

	private String key;
	
	private String value;
	
}
