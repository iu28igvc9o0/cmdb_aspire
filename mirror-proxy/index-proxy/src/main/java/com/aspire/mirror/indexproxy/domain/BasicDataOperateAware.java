package com.aspire.mirror.indexproxy.domain;

import lombok.Data;

/**
* 基础数据操作感应   <br/>
* Project Name:index-proxy
* File Name:BasicDataOperateAware.java
* Package Name:com.aspire.mirror.indexproxy.basicdatasync
* ClassName: BasicDataOperateAware <br/>
* date: 2018年11月29日 下午5:26:11 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
public abstract class BasicDataOperateAware extends PartitionGenerateRequest{
	protected Operate operateType;
	
	public static enum Operate {
		C, D, U;	// 增  删  改
	}
}
