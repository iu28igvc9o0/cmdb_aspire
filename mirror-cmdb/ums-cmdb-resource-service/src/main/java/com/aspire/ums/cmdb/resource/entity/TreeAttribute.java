package com.aspire.ums.cmdb.resource.entity;

import java.util.HashMap;
import java.util.Map;

public class TreeAttribute implements java.io.Serializable {

	private static final long serialVersionUID = -2325718905928947663L;
	
	private String id;
	private String type;
	private String managedByAnsible;
	private String resourcePool;
	
	private String department;//部门
	
	private String aBusiness;//一级
	
	private String twoBusiness;//二级
	
	private String model;//分类模型
	
	private String equipment;//设备
		
	private String modelName;
	
	
	public String getResourcePool() {
		return resourcePool;
	}

	public void setResourcePool(String resourcePool) {
		this.resourcePool = resourcePool;
	}

	public String getManagedByAnsible() {
		return managedByAnsible;
	}

	public void setManagedByAnsible(String managedByAnsible) {
		this.managedByAnsible = managedByAnsible;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}		
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getaBusiness() {
		return aBusiness;
	}

	public void setaBusiness(String aBusiness) {
		this.aBusiness = aBusiness;
	}

	public String getTwoBusiness() {
		return twoBusiness;
	}

	public void setTwoBusiness(String twoBusiness) {
		this.twoBusiness = twoBusiness;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
			
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Map<String, Object> toMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", type);
		map.put("managedByAnsible", managedByAnsible);
		
		map.put("department", department);
		map.put("aBusiness", aBusiness);
		map.put("twoBusiness", twoBusiness);
		map.put("model", model);
		map.put("equipment", equipment);
		map.put("resourcePool", resourcePool);
		map.put("modelName", modelName);
        return map;
	}	
}