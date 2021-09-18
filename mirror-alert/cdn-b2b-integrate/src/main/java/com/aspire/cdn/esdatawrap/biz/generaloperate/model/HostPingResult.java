package com.aspire.cdn.esdatawrap.biz.generaloperate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostPingResult {
	private String	host;
	private boolean	pingFlag;
}
