package com.aspire.ums.cmdb.module.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 
 * <p>Project: ums-cmdb-service</p>
 *
 * @Description: 
 *
 * @author: mingjianyong
 *
 * @Date: 2017-6-28
 */
public class Module implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private String id;

    private String name;

    private String code;
    
    private String parentid;
    
    private String iconurl;

    private Integer sortindex;

    private String disabled;

    private String builtin;

    private Date inserttime;

    private Date updatetime;

    private Integer isdelete;
    

    
    private List<Module> childModules;
    
    public Module() {
		super();
	}

	public Module(String id, String name, String code, String parentid,
			String iconurl, Integer sortindex, String disabled, String builtin,
			Date inserttime, Date updatetime, Integer isdelete,
			 List<Module> childModules) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.parentid = parentid;
		this.iconurl = iconurl;
		this.sortindex = sortindex;
		this.disabled = disabled;
		this.builtin = builtin;
		this.inserttime = inserttime;
		this.updatetime = updatetime;
		this.isdelete = isdelete;

		this.childModules = childModules;
	}


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
    
	public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl == null ? null : iconurl.trim();
    }

    public Integer getSortindex() {
        return sortindex;
    }

    public void setSortindex(Integer sortindex) {
        this.sortindex = sortindex;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled == null ? null : disabled.trim();
    }

    public String getBuiltin() {
        return builtin;
    }

    public void setBuiltin(String builtin) {
        this.builtin = builtin == null ? null : builtin.trim();
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

	public List<Module> getChildModules() {
		return childModules;
	}

	public void setChildModules(List<Module> childModules) {
		this.childModules = childModules;
	}
	public String getParentId() {
		return parentid;
	}
	public void setParentId(String parentid) {
		this.parentid = parentid;
	}


	@Override
	public String toString() {
		return "Module [id=" + id + ", name=" + name + ", code=" + code
				+ ", parentid=" + parentid + ", iconurl=" + iconurl
				+ ", sortindex=" + sortindex + ", disabled=" + disabled
				+ ", builtin=" + builtin + ", inserttime=" + inserttime
				+ ", updatetime=" + updatetime + ", isdelete=" + isdelete
				+ ", childModules=" + childModules
				+ "]";
	}
	
	
}