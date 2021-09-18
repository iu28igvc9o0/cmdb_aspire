package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CmdbMaintenanceInfo implements java.io.Serializable{
	private static final long serialVersionUID = 300297746261108014L;

	private String id;
	private String mode;
	private String vender;//厂商
	private String contact;//联系人
	private String tel;//电话
	private String mail;//厂商

}
