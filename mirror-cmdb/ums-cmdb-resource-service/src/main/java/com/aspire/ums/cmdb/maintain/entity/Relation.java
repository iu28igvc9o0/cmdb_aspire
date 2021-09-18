package com.aspire.ums.cmdb.maintain.entity;

import java.io.Serializable;

public class Relation implements Serializable {
  
        
    private static final long serialVersionUID = 1L;
    private String id;
	private String name;
	private String builtin;

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


    public String getBuiltin() {
        return builtin;
    }


    public void setBuiltin(String builtin) {
        this.builtin = builtin;
    }


    @Override
    public String toString() {
        return "Relation [name=" + name + ", builtin=" + builtin + "]";
    }

}