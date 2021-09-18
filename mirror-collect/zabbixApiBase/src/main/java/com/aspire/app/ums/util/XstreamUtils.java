package com.aspire.app.ums.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XstreamUtils {
    private static final Logger logger = LoggerFactory.getLogger(XstreamUtils.class);
    private static final String APP_CHARSET = "UTF-8";

    /**
     * 功能描述: 把XML文件转换成Java对象
     * <p>
     *
     * @param xmlFile
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T xml2Object(File xmlFile, Class<T> clazz) {
        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(clazz);
        return (T) xStream.fromXML(xmlFile);
    }

    @SuppressWarnings("unchecked")
    public static <T> T xml2Object(InputStream is, Class<T> clazz) {
        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(clazz);
        return (T) xStream.fromXML(is);
    }

    /**
     * 功能描述: 把对象转换成XML字符串
     * <p>
     *
     * @param targetObj
     * @return
     */
    public static String object2Xml(Object targetObj) {
        String charName = APP_CHARSET;
        XStream xstream = new XStream(new DomDriver(charName));
        xstream.processAnnotations(targetObj.getClass());
        return generateXmlMeta(charName) + xstream.toXML(targetObj);
    }

    private static String generateXmlMeta(String charset) {
        String lineSep = System.getProperty("line.separator");
        return "<?xml version=\"1.0\" encoding=\"" + charset + "\" standalone=\"yes\"?>" + lineSep;
    }

    /**
     * 功能描述: 把对象映射到指定XML文件中
     * <p>
     *
     * @param targetObj
     * @param xmlFilePath
     * @return
     */
    public static void object2Xml(Object targetObj, String xmlFilePath) {
        object2Xml(targetObj, new File(xmlFilePath));
    }

    /**
     * 功能描述: 把对象映射到指定XML文件中
     * <p>
     *
     * @param targetObj
     * @param xmlFile
     * @return
     */
    public static void object2Xml(Object targetObj, File xmlFile) {
        String charName = APP_CHARSET;
        XStream xstream = new XStream(new DomDriver(charName));
        xstream.processAnnotations(targetObj.getClass());

        FileOutputStream ostream = null;
        Writer writer = null;
        try {
            ostream = FileUtils.openOutputStream(xmlFile);
            writer = new OutputStreamWriter(ostream);

            String xmlMeta = generateXmlMeta(charName);
            writer.write(xmlMeta);
            xstream.toXML(targetObj, writer);

            writer.close();
        } catch (IOException e) {
            logger.error("", e);
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(ostream);
        }
    }

    /**
     * 功能描述: 把对象转换成JSON字符串
     * <p>
     *
     * @param object
     * @return
     */
    public static String object2Json(Object object) {
        XStream xStream = new XStream(new JsonHierarchicalStreamDriver());
        xStream.processAnnotations(object.getClass());
        xStream.setMode(XStream.NO_REFERENCES);
        return xStream.toXML(object);
    }
}
