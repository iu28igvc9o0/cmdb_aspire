package com.aspire.cdn.esdatawrap.config.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: ReIndexConfigProps
 * <p/>
 *
 * 类功能描述:  
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月12日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@ConfigurationProperties(prefix="local.config.es-run-action.reindex")
public class ReIndexConfigProps {
	private Boolean 			syncNodataAlert;							// 连续同步空数据时，是否触发告警
	private Integer				syncNodataMaxTryCount;						// 连续未同步到数据的重试次数, 达到此值会触发产生未同步到数据的告警
	private Duration			reidxSpanTime;								// 重建索引读取时长区间
	private Duration			runInterval;								// 定时任务间隔
	private String				sourceProvinces;				
	private List<String>		updateProvinces;							
	private Integer				singleReqBatchCount = 1500;					// 单个网络请求处理数据数量
	private final List<String>	provinceList		= new ArrayList<>();	// 省份
	
	@PostConstruct
	private void init() {
		if (StringUtils.isBlank(sourceProvinces)) {
			throw new RuntimeException("The 'local.config.reindex.source-provinces' config item is absent.");
		}
		String[] provinceArr = sourceProvinces.split(",");
		for (String province : provinceArr) {
			if (StringUtils.isBlank(province)) {
				continue;
			}
			provinceList.add(province.trim());
		}
	}
}
