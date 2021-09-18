
package com.aspire.mirror.alert.server.clientservice.process;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getFinishTaskDetailUrl complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getFinishTaskDetailUrl">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="actInstId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nodeKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFinishTaskDetailUrl", propOrder = {
    "actInstId",
    "nodeKey"
})
public class GetFinishTaskDetailUrl {

    protected String actInstId;
    protected String nodeKey;

    /**
     * Gets the value of the actInstId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActInstId() {
        return actInstId;
    }

    /**
     * Sets the value of the actInstId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActInstId(String value) {
        this.actInstId = value;
    }

    /**
     * Gets the value of the nodeKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeKey() {
        return nodeKey;
    }

    /**
     * Sets the value of the nodeKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeKey(String value) {
        this.nodeKey = value;
    }

}
