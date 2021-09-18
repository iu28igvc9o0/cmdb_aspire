package com.aspire.mirror.indexproxy.indexprocess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.indexproxy.domain.StandardIndex;

import lombok.extern.slf4j.Slf4j;

/**
* HTTP处理标准指标入口controller   <br/>
* Project Name:index-proxy
* File Name:HttpStandardIndexProcessController.java
* Package Name:com.aspire.mirror.indexproxy.indexprocess
* ClassName: HttpStandardIndexProcessController <br/>
* date: 2019年7月1日 下午7:24:21 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@RestController
public class HttpStandardIndexProcessController {
	@Autowired
	private List<IStandardIndexProcess> indexProcessorList;
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PostMapping(value = "/v1/indexProxy/standardIndex/process")
	public void processStandardIndex(@RequestBody List<StandardIndex> standardIdxList) {
		if (log.isInfoEnabled()) {
			log.info("Received standard index data list with size {}.", standardIdxList.size());
		}
		
		standardIdxList.stream().forEach(standardIdx -> {
			for (IStandardIndexProcess processor : indexProcessorList) {
				boolean flag = processor.processStandardIndex(standardIdx);
				if (!flag) {
					break;
				}
			}
		});
	}
}
