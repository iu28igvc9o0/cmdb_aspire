/**
 * 
 */
package com.aspire.ums.cmdb.sync.entity;

import java.io.Serializable;

/**
 * @author lupeng
 *
 */
public class Dict implements Serializable {

	private static final long serialVersionUID = 1537181884085124799L;

	private String dictCode;

	private String code;

	private String name;

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		Dict dict = (Dict) o;
		if (this.dictCode.equals(dict.getDictCode()) && this.code.equals(dict.getCode())) {
			return true;
		}
		return false;
	}
	
	@Override
    public int hashCode() {
        int result = dictCode.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }

}
