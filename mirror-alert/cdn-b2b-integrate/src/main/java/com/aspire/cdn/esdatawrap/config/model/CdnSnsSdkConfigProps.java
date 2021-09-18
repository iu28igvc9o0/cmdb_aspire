package com.aspire.cdn.esdatawrap.config.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="cdnsnssdk")
public class CdnSnsSdkConfigProps {
	private String				url;
	private String				userName;
	private String				token;
	private Integer				intervalMinutes;
	private Double				generalErrorPercentTherehold;
	private Double				firstFrameDataTimeTherehold;
	private final List<String>	appNameList	= new ArrayList<>();
}
