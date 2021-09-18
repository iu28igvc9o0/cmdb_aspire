
package com.aspire.mirror.alert.server.clientservice.process;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for addSignUsers complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="addSignUsers">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="signUserIds" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taskId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addSignUsers", propOrder = {
        "signUserIds",
        "taskId"
})
public class AddSignUsers {

    protected String signUserIds;
    protected String taskId;

    /**
     * Gets the value of the signUserIds property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSignUserIds() {
        return signUserIds;
    }

    /**
     * Sets the value of the signUserIds property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSignUserIds(String value) {
        this.signUserIds = value;
    }

    /**
     * Gets the value of the taskId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * Sets the value of the taskId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTaskId(String value) {
        this.taskId = value;
    }

}
