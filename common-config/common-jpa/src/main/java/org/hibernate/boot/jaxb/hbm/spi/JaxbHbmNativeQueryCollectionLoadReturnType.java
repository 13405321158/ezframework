//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.05.24 at 12:01:01 PM UTC 
//


package org.hibernate.boot.jaxb.hbm.spi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.hibernate.LockMode;


/**
 * <p>Java class for NativeQueryCollectionLoadReturnType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NativeQueryCollectionLoadReturnType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element name="return-property" type="{http://www.hibernate.org/xsd/orm/hbm}NativeQueryPropertyReturnType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="alias" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="lock-mode" type="{http://www.hibernate.org/xsd/orm/hbm}LockModeEnum" default="read" /&gt;
 *       &lt;attribute name="role" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NativeQueryCollectionLoadReturnType", namespace = "http://www.hibernate.org/xsd/orm/hbm", propOrder = {
    "returnProperty"
})
public class JaxbHbmNativeQueryCollectionLoadReturnType implements Serializable, NativeQueryNonScalarRootReturn
{

    @XmlElement(name = "return-property", namespace = "http://www.hibernate.org/xsd/orm/hbm")
    protected List<JaxbHbmNativeQueryPropertyReturnType> returnProperty;
    @XmlAttribute(name = "alias", required = true)
    protected String alias;
    @XmlAttribute(name = "lock-mode")
    @XmlJavaTypeAdapter(Adapter8 .class)
    protected LockMode lockMode;
    @XmlAttribute(name = "role", required = true)
    protected String role;

    /**
     * Gets the value of the returnProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the returnProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReturnProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JaxbHbmNativeQueryPropertyReturnType }
     * 
     * 
     */
    public List<JaxbHbmNativeQueryPropertyReturnType> getReturnProperty() {
        if (returnProperty == null) {
            returnProperty = new ArrayList<JaxbHbmNativeQueryPropertyReturnType>();
        }
        return this.returnProperty;
    }

    /**
     * Gets the value of the alias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the value of the alias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlias(String value) {
        this.alias = value;
    }

    /**
     * Gets the value of the lockMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public LockMode getLockMode() {
        if (lockMode == null) {
            return new Adapter8().unmarshal("read");
        } else {
            return lockMode;
        }
    }

    /**
     * Sets the value of the lockMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLockMode(LockMode value) {
        this.lockMode = value;
    }

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole(String value) {
        this.role = value;
    }

}
