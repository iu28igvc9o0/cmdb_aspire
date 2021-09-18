package com.aspire.cdn.esdatawrap.biz.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DropDownEntry {
	private String key;			// 下拉选项提交的值
	private String label;		// 下拉选项展示的name
}
