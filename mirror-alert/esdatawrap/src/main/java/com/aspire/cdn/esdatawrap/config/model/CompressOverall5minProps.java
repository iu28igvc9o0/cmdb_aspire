package com.aspire.cdn.esdatawrap.config.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="local.config.es-run-action.compress-overall5min")
public class CompressOverall5minProps {
	private Duration	compressSpan;
	private String		provinces;
	
	public List<String> getProvinceList() {
		if (StringUtils.isBlank(provinces)) {
			return new ArrayList<String>();
		}
		return Arrays.stream(provinces.split(",")).filter(prov -> StringUtils.isNoneBlank(prov)).collect(Collectors.toList());
	}
}
