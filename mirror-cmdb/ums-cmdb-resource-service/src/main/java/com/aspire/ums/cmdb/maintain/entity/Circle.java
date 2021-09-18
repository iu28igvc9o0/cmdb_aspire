package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;
import java.util.Date;

public class Circle implements Serializable {
  
        
    private static final long serialVersionUID = 1L;
    private String id;
	private String name;
	private String code;
    private String dec;
    private Integer type;
    private Integer isTop;
    private Date insertTime;
    private Date updateTime;
    private Integer isDelete;
	public Circle() {
		super();
	}
	
    public Circle(String name, String code, String dec, Integer type, Integer isTop, Date insertTime,
            Date updateTime, Integer isDelete) {
        super();
        this.name = name;
        this.code = code;
        this.dec = dec;
        this.type = type;
        this.isTop = isTop;
        this.insertTime = insertTime;
        this.updateTime = updateTime;
        this.isDelete = isDelete;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDec() {
        return dec;
    }
    public void setDec(String dec) {
        this.dec = dec;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getIsTop() {
        return isTop;
    }
    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }
    public Date getInsertTime() {
        return insertTime;
    }
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public Integer getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
    @Override
    public String toString() {
        return "Circle [id=" + id + ", name=" + name + ", code=" + code + ", dec=" + dec + ", type=" + type
                + ", isTop=" + isTop + ", insertTime=" + insertTime + ", updateTime=" + updateTime + ", isDelete="
                + isDelete + "]";
    }

}