package com.migu.tsg.microservice.atomicservice.composite.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**   
 * <p>
 * 扩展原来的KeyValue，添加一个值
 * </p>
 * @title ThreeValue.java
 * @package com.migu.tsg.microservice.atomicservice.composite.common 
 * @author 曾祥华
 * @version 0.1 2019年2月23日
 */
@NoArgsConstructor
@Setter
@Getter
public class ThreeValue<K,V,T> extends KeyValue<K,V> {
    /**
     * 数据资源约束
     * 
     * Map<String,String[]> 格式
     * 其中：
     * key：约束的维度，例如：区域area是一个维度，设备类型devicetype是一个维度
     * value:所属约束维度的值
     */
    private T dataConstraint;
    
    public ThreeValue(K k, V v, T t){
    	super(k,v);
    	this.dataConstraint=t;
    }
    
}
