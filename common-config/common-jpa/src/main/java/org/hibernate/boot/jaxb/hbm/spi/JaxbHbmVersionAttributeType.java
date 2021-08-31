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


/**
 * 
 *                 Optimistic locking attribute based on an incrementing value.
 *             
 * 
 * <p>Java class for VersionAttributeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VersionAttributeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hibernate.org/xsd/orm/hbm}BaseVersionAttributeType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="column" type="{http://www.hibernate.org/xsd/orm/hbm}ColumnType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="insert" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" default="integer" /&gt;
 *       &lt;attribute name="unsaved-value" type="{http://www.hibernate.org/xsd/orm/hbm}UnsavedValueVersionEnum" default="undefined" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VersionAttributeType", namespace = "http://www.hibernate.org/xsd/orm/hbm", propOrder = {
    "column"
})
public class JaxbHbmVersionAttributeType
    extends JaxbHbmBaseVersionAttributeType
    implements Serializable, SingularAttributeInfo, ToolingHintContainer
{

    @XmlElement(namespace = "http://www.hibernate.org/xsd/orm/hbm")
    protected List<JaxbHbmColumnType> column;
    @XmlAttribute(name = "insert")
    protected Boolean insert;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "unsaved-value")
    protected JaxbHbmUnsavedValueVersionEnum unsavedValue;

    /**
     * Gets the value of the column property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the column property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getColumn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JaxbHbmColumnType }
     * 
     * 
     */
    public List<JaxbHbmColumnType> getColumn() {
        if (column == null) {
            column = new ArrayList<JaxbHbmColumnType>();
        }
        return this.column;
    }

    /**
     * Gets the value of the insert property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInsert() {
        return insert;
    }

    /**
     * Sets the value of the insert property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInsert(Boolean value) {
        this.insert = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        if (type == null) {
            return "integer";
        } else {
            return type;
        }
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the unsavedValue property.
     * 
     * @return
     *     possible object is
     *     {@link JaxbHbmUnsavedValueVersionEnum }
     *     
     */
    public JaxbHbmUnsavedValueVersionEnum getUnsavedValue() {
        if (unsavedValue == null) {
            return JaxbHbmUnsavedValueVersionEnum.UNDEFINED;
        } else {
            return unsavedValue;
        }
    }

    /**
     * Sets the value of the unsavedValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link JaxbHbmUnsavedValueVersionEnum }
     *     
     */
    public void setUnsavedValue(JaxbHbmUnsavedValueVersionEnum value) {
        this.unsavedValue = value;
    }

}
