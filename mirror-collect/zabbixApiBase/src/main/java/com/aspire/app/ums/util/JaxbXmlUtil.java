package com.aspire.app.ums.util;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/** 
 *
 * 项目名称: ums_cmdb_proxy 
 * <p/>
 *
 * 类功能描述: 基于JAXB的java对象与XML转换工具类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2015年12月8日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2015 卓望公司-版权所有 
 *
 */
public class JaxbXmlUtil {
	
	@SuppressWarnings("unchecked")
	public static <T> T parseXml2Object(File xmlFile, Class<T> clazz) throws Exception {
		JAXBContext ctx = JAXBContext.newInstance(clazz);
		Unmarshaller unMarshaller = ctx.createUnmarshaller();
		return (T) unMarshaller.unmarshal(xmlFile);
	}
	
	/** 
	 * 功能描述: 把JAXB注解对象影射到指定路径的XML文件中  
	 * <p>
	 * @param obj
	 * @param filePath
	 * @throws Exception
	 */
	public static void parseObj2Xml(Object obj, String filePath) throws Exception {
		File targetFile = new File(filePath);
		parseObj2Xml(obj, targetFile);
	}
	
	/** 
	 * 功能描述: 把JAXB注解对象影射到指定的XML文件  
	 * <p>
	 * @param obj
	 * @param outFile
	 * @throws Exception
	 */
	public static void parseObj2Xml(Object obj, File outFile) throws Exception {
		Class<?> clazz = obj.getClass();
		JAXBContext ctx = JAXBContext.newInstance(clazz);
		Marshaller marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(obj, outFile);
	}
	
	/** 
	 * 功能描述: 把对象映射成XML字符串  
	 * <p>
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String parseObj2Xml(Object obj) throws Exception {
		Class<?> clazz = obj.getClass();
		JAXBContext ctx = JAXBContext.newInstance(clazz);
		Marshaller marshaller = ctx.createMarshaller();
		
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		marshaller.marshal(obj, ostream);
		String xmlText = ostream.toString("UTF-8");
		
		ostream.close();
		return xmlText;
	}
}
