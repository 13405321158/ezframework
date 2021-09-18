package com.leesky.ezframework.mybatis.utils;

import com.leesky.ezframework.mybatis.condition.FieldCondition;
import com.leesky.ezframework.mybatis.annotation.InverseJoinColumn;

public class InverseJoinColumnUtil {
	public static <T> String getInverseRefColumn(FieldCondition<T> fc) {
		InverseJoinColumn inverseJoinColumn = fc.getInverseJoinColumn();
		String inverseRefColumn = null;
		if (inverseJoinColumn != null) {
			if (!inverseJoinColumn.referencedColumnName().equals("")) {
				inverseRefColumn = inverseJoinColumn.referencedColumnName();
			} else {
				if (!inverseJoinColumn.name().equals("")) {
					inverseRefColumn = inverseJoinColumn.name();
				} else {
					inverseRefColumn = fc.getFieldClass().getSimpleName();
					inverseRefColumn = inverseRefColumn.substring(0, 1).toLowerCase() + inverseRefColumn.substring(1);
					inverseRefColumn = inverseRefColumn + "_id";
				}
			}
		} else {
			inverseRefColumn = fc.getFieldClass().getSimpleName();
			inverseRefColumn = inverseRefColumn.substring(0, 1).toLowerCase() + inverseRefColumn.substring(1);
			inverseRefColumn = inverseRefColumn + "_id";
		}

		return inverseRefColumn;
	}

	public static <T> String getInverseColumn(FieldCondition<T> fc) {
		InverseJoinColumn inverseJoinColumn = fc.getInverseJoinColumn();
		String inverseColumn = null;
		if (inverseJoinColumn != null) {
			if (!inverseJoinColumn.referencedColumnName().equals("")) {
				inverseColumn = inverseJoinColumn.referencedColumnName();
			} else {
				if (!inverseJoinColumn.name().equals("")) {
					inverseColumn = inverseJoinColumn.name();
				} else {
					inverseColumn = fc.getFieldClass().getSimpleName();
					inverseColumn = inverseColumn.substring(0, 1).toLowerCase() + inverseColumn.substring(1);
					inverseColumn = inverseColumn + "_id";
				}
			}
		} else {
			inverseColumn = fc.getFieldClass().getSimpleName();
			inverseColumn = inverseColumn.substring(0, 1).toLowerCase() + inverseColumn.substring(1);
			inverseColumn = inverseColumn + "_id";
		}

		return inverseColumn;
	}

	public static <T> String getInverseColumnProperty(FieldCondition<T> fc) {
		InverseJoinColumn inverseJoinColumn = fc.getInverseJoinColumn();
		String inverseColumnProperty = null;

		if (inverseJoinColumn != null && !inverseJoinColumn.property().equals("")) {
			inverseColumnProperty = inverseJoinColumn.property();
		} else {
			if (fc.getIsCollection()) {
				if (fc.getInverseTableId() != null) {
					inverseColumnProperty = fc.getFieldOfInverseTableId().getName();
				} else {
					inverseColumnProperty = getInverseColumn(fc);
				}
			} else {
				inverseColumnProperty = fc.getFieldOfInverseTableId().getName();
			}
		}
		return inverseColumnProperty;
	}
}
