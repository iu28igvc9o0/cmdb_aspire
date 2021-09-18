package com.migu.tsg.microservice.atomicservice.composite.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 键值对持有对象 Project Name:composite-service File Name:KeyValue.java 
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.common 
 * ClassName: KeyValue <br/>
 * date: 2017年8月27日 下午1:33:17 <br/>
 * 详细描述这个类的功能等
 * 
 * @author pengguihua
 * @version
 * @since JDK 1.6
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class KeyValue<K, V> {
    private K key;
    private V value;
}
