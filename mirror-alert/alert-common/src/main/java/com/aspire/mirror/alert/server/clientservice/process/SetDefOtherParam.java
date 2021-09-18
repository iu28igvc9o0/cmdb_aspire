
package com.aspire.mirror.alert.server.clientservice.process;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setDefOtherParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setDefOtherParam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="defId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="json" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setDefOtherParam", propOrder = {
    "defId",
    "json"
})
public class SetDefOtherParam {

    protected String defId;
    protected String json;

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

    /**
     * Gets the value of the json property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJson() {
        return json;
    }

    /**
     * Sets the value of the json property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJson(String value) {
        this.json = value;
    }

}
