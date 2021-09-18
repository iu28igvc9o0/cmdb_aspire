
package com.aspire.mirror.alert.server.clientservice.process;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getByBusinessKey complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getByBusinessKey">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="businessKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getByBusinessKey", propOrder = {
    "businessKey"
})
public class GetByBusinessKey {

    protected String businessKey;

    /**
     * Gets the value of the businessKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessKey() {
        return businessKey;
    }

    /**
     * Sets the value of the businessKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessKey(String value) {
        this.businessKey = value;
    }

}
