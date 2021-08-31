//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.05.24 at 12:01:01 PM UTC 
//


package org.hibernate.boot.jaxb.cfg.spi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="session-factory"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="property" type="{http://www.hibernate.org/xsd/orm/cfg}ConfigPropertyType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                   &lt;element name="mapping" type="{http://www.hibernate.org/xsd/orm/cfg}MappingReferenceType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                   &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;element name="class-cache" type="{http://www.hibernate.org/xsd/orm/cfg}EntityCacheType"/&gt;
 *                     &lt;element name="collection-cache" type="{http://www.hibernate.org/xsd/orm/cfg}CollectionCacheType"/&gt;
 *                   &lt;/choice&gt;
 *                   &lt;element name="event" type="{http://www.hibernate.org/xsd/orm/cfg}EventListenerGroupType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                   &lt;element name="listener" type="{http://www.hibernate.org/xsd/orm/cfg}EventListenerType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="security" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="grant" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;attribute name="actions" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="entity-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="role" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="context" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sessionFactory",
    "security"
})
@XmlRootElement(name = "hibernate-configuration", namespace = "http://www.hibernate.org/xsd/orm/cfg")
public class JaxbCfgHibernateConfiguration {

    @XmlElement(name = "session-factory", namespace = "http://www.hibernate.org/xsd/orm/cfg", required = true)
    protected JaxbCfgHibernateConfiguration.JaxbCfgSessionFactory sessionFactory;
    @XmlElement(namespace = "http://www.hibernate.org/xsd/orm/cfg")
    protected JaxbCfgHibernateConfiguration.JaxbCfgSecurity security;

    /**
     * Gets the value of the sessionFactory property.
     * 
     * @return
     *     possible object is
     *     {@link JaxbCfgHibernateConfiguration.JaxbCfgSessionFactory }
     *     
     */
    public JaxbCfgHibernateConfiguration.JaxbCfgSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Sets the value of the sessionFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link JaxbCfgHibernateConfiguration.JaxbCfgSessionFactory }
     *     
     */
    public void setSessionFactory(JaxbCfgHibernateConfiguration.JaxbCfgSessionFactory value) {
        this.sessionFactory = value;
    }

    /**
     * Gets the value of the security property.
     * 
     * @return
     *     possible object is
     *     {@link JaxbCfgHibernateConfiguration.JaxbCfgSecurity }
     *     
     */
    public JaxbCfgHibernateConfiguration.JaxbCfgSecurity getSecurity() {
        return security;
    }

    /**
     * Sets the value of the security property.
     * 
     * @param value
     *     allowed object is
     *     {@link JaxbCfgHibernateConfiguration.JaxbCfgSecurity }
     *     
     */
    public void setSecurity(JaxbCfgHibernateConfiguration.JaxbCfgSecurity value) {
        this.security = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="grant" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;attribute name="actions" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="entity-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                 &lt;attribute name="role" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="context" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "grant"
    })
    public static class JaxbCfgSecurity {

        @XmlElement(namespace = "http://www.hibernate.org/xsd/orm/cfg")
        protected List<JaxbCfgHibernateConfiguration.JaxbCfgSecurity.JaxbCfgGrant> grant;
        @XmlAttribute(name = "context", required = true)
        protected String context;

        /**
         * Gets the value of the grant property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the grant property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGrant().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JaxbCfgHibernateConfiguration.JaxbCfgSecurity.JaxbCfgGrant }
         * 
         * 
         */
        public List<JaxbCfgHibernateConfiguration.JaxbCfgSecurity.JaxbCfgGrant> getGrant() {
            if (grant == null) {
                grant = new ArrayList<JaxbCfgHibernateConfiguration.JaxbCfgSecurity.JaxbCfgGrant>();
            }
            return this.grant;
        }

        /**
         * Gets the value of the context property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getContext() {
            return context;
        }

        /**
         * Sets the value of the context property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setContext(String value) {
            this.context = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;attribute name="actions" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="entity-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *       &lt;attribute name="role" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class JaxbCfgGrant {

            @XmlAttribute(name = "actions", required = true)
            protected String actions;
            @XmlAttribute(name = "entity-name", required = true)
            protected String entityName;
            @XmlAttribute(name = "role", required = true)
            protected String role;

            /**
             * Gets the value of the actions property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getActions() {
                return actions;
            }

            /**
             * Sets the value of the actions property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setActions(String value) {
                this.actions = value;
            }

            /**
             * Gets the value of the entityName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getEntityName() {
                return entityName;
            }

            /**
             * Sets the value of the entityName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setEntityName(String value) {
                this.entityName = value;
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

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="property" type="{http://www.hibernate.org/xsd/orm/cfg}ConfigPropertyType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *         &lt;element name="mapping" type="{http://www.hibernate.org/xsd/orm/cfg}MappingReferenceType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;element name="class-cache" type="{http://www.hibernate.org/xsd/orm/cfg}EntityCacheType"/&gt;
     *           &lt;element name="collection-cache" type="{http://www.hibernate.org/xsd/orm/cfg}CollectionCacheType"/&gt;
     *         &lt;/choice&gt;
     *         &lt;element name="event" type="{http://www.hibernate.org/xsd/orm/cfg}EventListenerGroupType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *         &lt;element name="listener" type="{http://www.hibernate.org/xsd/orm/cfg}EventListenerType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "property",
        "mapping",
        "classCacheOrCollectionCache",
        "event",
        "listener"
    })
    public static class JaxbCfgSessionFactory {

        @XmlElement(namespace = "http://www.hibernate.org/xsd/orm/cfg")
        protected List<JaxbCfgConfigPropertyType> property;
        @XmlElement(namespace = "http://www.hibernate.org/xsd/orm/cfg")
        protected List<JaxbCfgMappingReferenceType> mapping;
        @XmlElements({
            @XmlElement(name = "class-cache", namespace = "http://www.hibernate.org/xsd/orm/cfg", type = JaxbCfgEntityCacheType.class),
            @XmlElement(name = "collection-cache", namespace = "http://www.hibernate.org/xsd/orm/cfg", type = JaxbCfgCollectionCacheType.class)
        })
        protected List<Object> classCacheOrCollectionCache;
        @XmlElement(namespace = "http://www.hibernate.org/xsd/orm/cfg")
        protected List<JaxbCfgEventListenerGroupType> event;
        @XmlElement(namespace = "http://www.hibernate.org/xsd/orm/cfg")
        protected List<JaxbCfgEventListenerType> listener;
        @XmlAttribute(name = "name")
        protected String name;

        /**
         * Gets the value of the property property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the property property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProperty().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JaxbCfgConfigPropertyType }
         * 
         * 
         */
        public List<JaxbCfgConfigPropertyType> getProperty() {
            if (property == null) {
                property = new ArrayList<JaxbCfgConfigPropertyType>();
            }
            return this.property;
        }

        /**
         * Gets the value of the mapping property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the mapping property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMapping().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JaxbCfgMappingReferenceType }
         * 
         * 
         */
        public List<JaxbCfgMappingReferenceType> getMapping() {
            if (mapping == null) {
                mapping = new ArrayList<JaxbCfgMappingReferenceType>();
            }
            return this.mapping;
        }

        /**
         * Gets the value of the classCacheOrCollectionCache property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the classCacheOrCollectionCache property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getClassCacheOrCollectionCache().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JaxbCfgEntityCacheType }
         * {@link JaxbCfgCollectionCacheType }
         * 
         * 
         */
        public List<Object> getClassCacheOrCollectionCache() {
            if (classCacheOrCollectionCache == null) {
                classCacheOrCollectionCache = new ArrayList<Object>();
            }
            return this.classCacheOrCollectionCache;
        }

        /**
         * Gets the value of the event property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the event property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEvent().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JaxbCfgEventListenerGroupType }
         * 
         * 
         */
        public List<JaxbCfgEventListenerGroupType> getEvent() {
            if (event == null) {
                event = new ArrayList<JaxbCfgEventListenerGroupType>();
            }
            return this.event;
        }

        /**
         * Gets the value of the listener property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the listener property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getListener().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JaxbCfgEventListenerType }
         * 
         * 
         */
        public List<JaxbCfgEventListenerType> getListener() {
            if (listener == null) {
                listener = new ArrayList<JaxbCfgEventListenerType>();
            }
            return this.listener;
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

    }

}
