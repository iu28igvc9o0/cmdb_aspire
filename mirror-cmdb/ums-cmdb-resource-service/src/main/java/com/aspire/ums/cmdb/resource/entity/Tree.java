package com.aspire.ums.cmdb.resource.entity;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree implements Serializable {
	private String id;
	private String moduleName;
	private Map<String, Object> attributes = Maps.newHashMap();
	private String name;
	private Boolean checked;
	private Boolean isParent;
	private List<? extends Tree> children;
	private Boolean chkDisabled;
	private Boolean halfCheck;
	private String icon;
	private String iconClose;
	private String iconOpen;
	private String iconSkin;
	private Boolean isHidden;
	private Boolean nocheck;
	private Boolean open;
	private String target;
	private String url;
	private String level;
	private String groupUI;

	public Tree() {
	}

	public void addAttribute(String key, String value) {
		this.attributes = (this.attributes == null ? new HashMap() : this.attributes);
		this.attributes.put(key, value);
	}

	public void removeAttribute(String key) {
		if (this.attributes != null)
			this.attributes.remove(key);
	}

	public Tree(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getChecked() {
		return this.checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Boolean getIsParent() {
		return this.isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public List<? extends Tree> getChildren() {
		return this.children;
	}

	public void setChildren(List<? extends Tree> children) {
		this.children = children;
	}

	public Boolean getChkDisabled() {
		return this.chkDisabled;
	}

	public void setChkDisabled(Boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public Boolean getHalfCheck() {
		return this.halfCheck;
	}

	public void setHalfCheck(Boolean halfCheck) {
		this.halfCheck = halfCheck;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconClose() {
		return this.iconClose;
	}

	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}

	public String getIconOpen() {
		return this.iconOpen;
	}

	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}

	public String getIconSkin() {
		return this.iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}

	public Boolean getIsHidden() {
		return this.isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public Boolean getNocheck() {
		return this.nocheck;
	}

	public void setNocheck(Boolean nocheck) {
		this.nocheck = nocheck;
	}

	public Boolean getOpen() {
		return this.open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getGroupUI() {
		return this.groupUI;
	}

	public void setGroupUI(String groupUI) {
		this.groupUI = groupUI;
	}

	public <T extends Tree> void addChildren(T tree) {
		this.children = (this.children == null ? new ArrayList() : this.children);

		List l = this.children;
		l.add(tree);
	}
}