package com.aspire.ums.cdn.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module implements Serializable{
	private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String code;
    private String parentId;
    private String iconUrl;
    private Integer sortIndex;
    private String disabled;
    private String builtin;
    private Date insertTime;
    private Date updateTime;
    private Integer isDelete;
    private Integer partitionId;
    private Integer collectCycle;
    private String collectUnit;
    private List<Module> childModules;

	@Override
	public String toString() {
		return "Module [id=" + id + ", name=" + name + ", code=" + code
				+ ", parentid=" + parentId + ", iconurl=" + iconUrl
				+ ", sortindex=" + sortIndex + ", disabled=" + disabled
				+ ", builtin=" + builtin + ", inserttime=" + insertTime
				+ ", updatetime=" + updateTime + ", isdelete=" + isDelete
				+ ", childModules=" + childModules
				+ "]";
	}
}