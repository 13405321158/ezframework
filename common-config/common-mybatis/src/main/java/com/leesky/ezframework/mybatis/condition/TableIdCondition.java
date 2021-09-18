package com.leesky.ezframework.mybatis.condition;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;

@Data
public class TableIdCondition {
    private TableId tableId;
    private Field fieldOfTableId;

    public TableIdCondition(Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(TableId.class)) {
                    tableId = field.getDeclaredAnnotation(TableId.class);
                    fieldOfTableId = field;
                    break;
                }

            }
        }
    }

}
