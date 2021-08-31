//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.05.24 at 12:01:01 PM UTC 
//


package org.hibernate.boot.jaxb.hbm.spi;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Polymorphic data requires a column holding a class discriminator value. This
 *                 value is not directly exposed to the application.
 *             
 * 
 * <p>Java class for EntityDiscriminatorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EntityDiscriminatorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://www.hibernate.org/xsd/orm/hbm}column-or-formula" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="column" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="force" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="formula" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="insert" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="length" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="not-null" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" default="string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntityDiscriminatorType", namespace = "http://www.hibernate.org/xsd/orm/hbm", propOrder = {
    "column",
    "formula"
})
public class JaxbHbmEntityDiscriminatorType
    implements Serializable
{

    @XmlElement(namespace = "http://www.hibernate.org/xsd/orm/hbm")
    protected JaxbHbmColumnType column;
    @XmlElement(namespace = "http://www.hibernate.org/xsd/orm/hbm")
    protected String formula;
    @XmlAttribute(name = "column")
    protected String columnAttribute;
    @XmlAttribute(name = "force")
    protected Boolean force;
    @XmlAttribute(name = "formula")
    protected String formulaAttribute;
    @XmlAttribute(name = "insert")
    protected Boolean insert;
    @XmlAttribute(name = "length")
    protected Integer length;
    @XmlAttribute(name = "not-null")
    protected Boolean notNull;
    @XmlAttribute(name = "type")
    protected String type;

    /**
     * Gets the value of the column property.
     * 
     * @return
     *     possible object is
     *     {@link JaxbHbmColumnType }
     *     
     */
    public JaxbHbmColumnType getColumn() {
        return column;
    }

    /**
     * Sets the value of the column property.
     * 
     * @param value
     *     allowed object is
     *     {@link JaxbHbmColumnType }
     *     
     */
    public void setColumn(JaxbHbmColumnType value) {
        this.column = value;
    }

    /**
     * Gets the value of the formula property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormula() {
        return formula;
    }

    /**
     * Sets the value of the formula property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormula(String value) {
        this.formula = value;
    }

    /**
     * Gets the value of the columnAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumnAttribute() {
        return columnAttribute;
    }

    /**
     * Sets the value of the columnAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnAttribute(String value) {
        this.columnAttribute = value;
    }

    /**
     * Gets the value of the force property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isForce() {
        if (force == null) {
            return false;
        } else {
            return force;
        }
    }

    /**
     * Sets the value of the force property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setForce(Boolean value) {
        this.force = value;
    }

    /**
     * Gets the value of the formulaAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormulaAttribute() {
        return formulaAttribute;
    }

    /**
     * Sets the value of the formulaAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormulaAttribute(String value) {
        this.formulaAttribute = value;
    }

    /**
     * Gets the value of the insert property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isInsert() {
        if (insert == null) {
            return true;
        } else {
            return insert;
        }
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
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLength(Integer value) {
        this.length = value;
    }

    /**
     * Gets the value of the notNull property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isNotNull() {
        if (notNull == null) {
            return true;
        } else {
            return notNull;
        }
    }

    /**
     * Sets the value of the notNull property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNotNull(Boolean value) {
        this.notNull = value;
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
            return "string";
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

}
