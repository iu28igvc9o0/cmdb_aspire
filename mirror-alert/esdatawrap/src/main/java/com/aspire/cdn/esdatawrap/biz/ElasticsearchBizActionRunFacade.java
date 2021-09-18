package com.aspire.cdn.esdatawrap.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: ElasticsearchBizActionRunFacade
 * <p/>
 *
 * 类功能描述: ES业务动作驱动入口
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月15日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
public final class ElasticsearchBizActionRunFacade implements ApplicationRunner {
	@Autowired(required=false)
	private List<IElasticSearchBizRunAction> esRunActionList;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (CollectionUtils.isEmpty(esRunActionList)) {
			log.warn("There is no IElasticSearchRunActions to run ....");
			return;
		}
		for (IElasticSearchBizRunAction runAction : esRunActionList) {
			runAction.doAction();
		}
	}
}
