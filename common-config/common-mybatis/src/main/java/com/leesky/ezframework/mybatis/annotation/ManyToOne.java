package com.leesky.ezframework.mybatis.annotation;

import com.leesky.ezframework.mybatis.enums.CascadeType;
import com.leesky.ezframework.mybatis.enums.FetchType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)

public @interface ManyToOne {

    /**
     * (Optional) The entity class that is the target of
     * the association.
     *
     * <p> Defaults to the type of the field or property
     * that stores the association.
     */
    Class<?> targetEntity() default void.class;

    /**
     * (Optional) The operations that must be cascaded to
     * the target of the association.
     *
     * <p> By default no operations are cascaded.
     */
    CascadeType[] cascade() default {};

    /**
     * (Optional) Whether the association should be lazily
     * loaded or must be eagerly fetched. The EAGER
     * strategy is a requirement on the persistence provider runtime that
     * the associated entity must be eagerly fetched. The LAZY
     * strategy is a hint to the persistence provider runtime.
     */
    FetchType fetch() default FetchType.EAGER;

    /**
     * (Optional) Whether the association is optional. If set
     * to false then a non-null relationship must always exist.
     */
    boolean optional() default true;
}