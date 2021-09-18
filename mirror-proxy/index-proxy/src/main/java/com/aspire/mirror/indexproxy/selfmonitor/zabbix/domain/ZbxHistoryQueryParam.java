package com.aspire.mirror.indexproxy.selfmonitor.zabbix.domain;

import lombok.Data;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: ZbxHistoryQueryParam
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年10月30日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public final class ZbxHistoryQueryParam {
	private Long	hostId;
	private Long	sequenceNo;
	private Boolean	limitOne;
	private String	sortWay	= "ASC";	// 默认升序
	
	private ZbxHistoryQueryParam() {}
	
	public static ZbxHistoryQueryParam bySequence(Long hostId, Long sequenceNo) {
		ZbxHistoryQueryParam param = new ZbxHistoryQueryParam();
		param.hostId = hostId;
		param.sequenceNo = sequenceNo;
		return param;
	}
	
	/** 
	 * 功能描述: 设备第一次查询时, 由于外部条件没有 sequenceNo, 所以查该设备最新的那条数据, 以获取到 sequenceNo
	 * <p>
	 * @param hostId
	 * @return
	 */
	public static ZbxHistoryQueryParam byFirstTime(Long hostId) {
		ZbxHistoryQueryParam param = new ZbxHistoryQueryParam();
		param.hostId = hostId;
		param.sortWay = "DESC";
		param.limitOne = true;
		return param;
	}
}
