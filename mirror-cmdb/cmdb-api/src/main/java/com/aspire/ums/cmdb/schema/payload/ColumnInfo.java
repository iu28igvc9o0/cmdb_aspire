package com.aspire.ums.cmdb.schema.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据库表字段属性model
 * <p>Project: ums-cmdb-service</p>
 *
 * @Description: 
 *
 * @author: pengfeng
 *
 * @Date: 2019-4-24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo implements Serializable{
	private static final long serialVersionUID = 1L;

    private String name;

    private String type;

    private Integer strLength;

    private Integer intLength;

    //todo 注意：浮点类型的整数位应该是 floatLength-1
    private Integer floatLength;

    private String isNullable;

    private String comment;
    
    @Override
    public String toString() {
        return "ColumnInfo{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", strLength=" + strLength +
                ", intLength=" + intLength +
                ", floatLength=" + floatLength +
                ", isNullable='" + isNullable + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnInfo that = (ColumnInfo) o;

        if (!name.equals(that.name)) return false;
        if (!type.equals(that.type)) return false;
        if (!strLength.equals(that.strLength)) return false;
        if (!intLength.equals(that.intLength)) return false;
        if (!floatLength.equals(that.floatLength)) return false;
        if (!isNullable.equals(that.isNullable)) return false;
        return comment.equals(that.comment);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + strLength.hashCode();
        result = 31 * result + intLength.hashCode();
        result = 31 * result + floatLength.hashCode();
        result = 31 * result + isNullable.hashCode();
        result = 31 * result + comment.hashCode();
        return result;
    }
}