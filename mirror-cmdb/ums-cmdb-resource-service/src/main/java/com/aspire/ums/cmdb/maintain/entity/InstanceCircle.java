/**
 * 
 */
package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;

/**
 * @author lupeng
 *
 */
public class InstanceCircle implements Serializable {
	
	private static final long serialVersionUID = 85651680018288231L;

	private String instanceId;
	
	private String circleId;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getCircleId() {
		return circleId;
	}

	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}
}
