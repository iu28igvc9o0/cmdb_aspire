/*
 * Created on 2016年11月28日
 * ModuleUtils.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of aspire Technologies Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2012 =======================================================
 */
package com.aspire.ums.cmdb.util;

import com.google.common.collect.Maps;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * Title: osa_web <br>
 * Description: <br>
 * Copyright: aspire Copyright (C) 2016 <br>
 * 
 * @author <a href="mailto:lijinwei@aspirecn.com">李谨威</a><br>
 * @e-mail: lijinwei@aspirecn.com<br>
 * @version 1.0 <br>
 * @creatdate 2016年11月28日 上午11:14:18 <br>
 * 
 */
public class ModuleUtils {
	public static final String SYS_MODULE_CONFIG =  "sys-module-config.xml";
	
	private static Map<String,String> module;
	
	public static Map<String,String> getModule(){
		if(module==null){
			module=initModule();
		}
		return module;
	}
	
	private static Map<String,String> initModule(){
		Map<String,String> result = Maps.newHashMap();
		URL url  = ModuleUtils.class.getClassLoader().getResource(ModuleUtils.SYS_MODULE_CONFIG);		
		File file = new File(url.getFile());  
        if (!file.exists()) {  
            System.out.println("  Error : Config file doesn't exist!");  
        }  
        try {
        	SAXReader reader = new SAXReader();  
            Document doc = reader.read(file);  
            Element data;  
            Iterator<?> iterator = doc.getRootElement().elementIterator("module");
            while(iterator.hasNext()){
            	data = (Element) iterator.next();
                String key = data.elementText("key").trim();
                String value = data.elementText("value").trim();
                result.put(key, value);
            }  
        }catch(Exception e){
        	e.printStackTrace();
        }
        return result;
	}
	
//	public static void main(String[] args) {
//		Map<String,String> module = ModuleUtils.getModule();
//		if(null!=module.get("maintain_mode_query")){
//			System.out.println(module.get("maintain_mode_query"));
//		}
//	}

}
