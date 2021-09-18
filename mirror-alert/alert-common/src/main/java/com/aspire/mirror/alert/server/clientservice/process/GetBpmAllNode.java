
package com.aspire.mirror.alert.server.clientservice.process;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getBpmAllNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getBpmAllNode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="defId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBpmAllNode", propOrder = {
    "defId"
})
public class GetBpmAllNode {

    protected String defId;

    /**
     * Gets the value of the defId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefId() {
        return defId;
    }

    /**
     * Sets the value of the defId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefId(String value) {
        this.defId = value;
    }

}
