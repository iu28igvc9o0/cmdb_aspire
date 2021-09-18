package com.aspire.ums.cmdb.module.entity;

import java.io.Serializable;
import java.util.Date;
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
public class Icon implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private String id;

    private String iconurl;

    private Integer iconcategory;

    private Integer sortindex;

    private Date inserttime;

    private Integer isdelete;

    public Icon() {
		super();
	}

	public Icon(String id, String iconurl, Integer iconcategory,
			Integer sortindex, Date inserttime, Integer isdelete) {
		super();
		this.id = id;
		this.iconurl = iconurl;
		this.iconcategory = iconcategory;
		this.sortindex = sortindex;
		this.inserttime = inserttime;
		this.isdelete = isdelete;
	}
	public Icon(String id, String iconurl, Integer iconcategory,
			Integer sortindex, Integer isdelete) {
		super();
		this.id = id;
		this.iconurl = iconurl;
		this.iconcategory = iconcategory;
		this.sortindex = sortindex;
		this.isdelete = isdelete;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl == null ? null : iconurl.trim();
    }

    public Integer getIconcategory() {
        return iconcategory;
    }

    public void setIconcategory(Integer iconcategory) {
        this.iconcategory = iconcategory;
    }

    public Integer getSortindex() {
        return sortindex;
    }

    public void setSortindex(Integer sortindex) {
        this.sortindex = sortindex;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

	@Override
	public String toString() {
		return "Icon [id=" + id + ", iconurl=" + iconurl + ", iconcategory="
				+ iconcategory + ", sortindex=" + sortindex + ", inserttime="
				+ inserttime + ", isdelete=" + isdelete + "]";
	}
    
}