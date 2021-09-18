/**
 *
 * 项目名： ums-cmdb-cdn 
 * <p/> 
 *
 * 文件名:  LinkPercentReportData.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.ums.cdn.model;

import lombok.Data;

/** 
 *
 * 项目名称: ums-cmdb-cdn 
 * <p/>
 * 
 * 类名: LinkPercentReportData
 * <p/>
 *
 * 类功能描述: 连通率报表实体
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
public class LinkPercentReportData {
	private String province;
	private Float sshLinkPercent;				// SSH 连通率
	private Float agentLinkPercent;				// Agent 连通率
	private Float sshLinkPercentExcludeFH;		// SSH 连通率(不含烽火)
	private Float agentLinkPercentExcludeFH;	// Agent 连通率(不含烽火)
	private String hwOms;
	private String hwJieru;
	private String hwSsh;
	private String hwAgent;
	private Float sshLinkPercentHW;				// SSH 连通率(华为)
	private Float agentLinkPercentHW;			// Agent 连通率(华为)
	private String zxOms;
	private String zxJieru;
	private String zxSsh;
	private String zxAgent; 
	private Float sshLinkPercentZX;				// SSH 连通率(中兴)
	private Float agentLinkPercentZX;			// Agent 连通率(中兴)
	private String fhOms;
	private String fhJieru;
	private String fhSsh;
	private String fhAgent; 
	private Float sshLinkPercentFH;				// SSH 连通率(烽火)
	private Float agentLinkPercentFH;			// Agent 连通率(烽火)
	private String hyOms;
	private String hyJieru;
	private String hySsh;
	private String hyAgent; 
	private Float sshLinkPercentHY;				// SSH 连通率(杭研)
	private Float agentLinkPercentHY;			// Agent 连通率(杭研)
}
