package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 公内网映射管理
 * 
 * @author cuizhijun
 *
 */
@Data
public class PublicNetAndIntranetIPDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6466396775606871835L;
	/**
	 * 主键
	 */
	@JsonProperty("ID")
	private String id;
	/**
	 * 防火墙ip
	 */
	@JsonProperty("FW_IP")
	private String fwIp;
	/**
	 * 外网ip
	 */
	@JsonProperty("OUTER_IP")
	private String outerIp;
	/**
	 * 外网端口
	 */
	@JsonProperty("OUTER_PORT")
	private String outerPort;
	/**
	 * 负载ip
	 */
	@JsonProperty("LB_IP")
	private String lbIp;
	/**
	 * 负载端口
	 */
	@JsonProperty("LB_PORT")
	private String lbPort;
	/**
	 * 内网ip
	 */
	@JsonProperty("INNER_IP")
	private String innerIp;
	/**
	 * 内网端口
	 */
	@JsonProperty("INNER_PORT")
	private String innerPort;
	/**
	 * 业务线
	 */
	@JsonProperty("BUSINESS_NAME")
	private String businessName;
}
