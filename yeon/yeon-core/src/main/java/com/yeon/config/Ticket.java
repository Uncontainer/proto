package com.yeon.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yeon.lang.ResourceIdentifiable;
import com.yeon.lang.impl.MapResource;

public class Ticket extends MapResource {
    private static final Logger LOG = LoggerFactory.getLogger(Ticket.class);

    protected static final String PARAM_NAME = "_name";
    protected static final String PARAM_STATUS = "_status";

    public static final List<String> EMPTY_LIST = Collections.emptyList();

    protected Ticket(ResourceIdentifiable classId) {
        super(classId);
    }

    protected Ticket(ResourceIdentifiable classId, Ticket ticket) {
        // TODO [LOW] field 값까지 복사하는 코드 추가.
        super(classId, ticket == null ? null : ticket.getValues());
    }

    protected Ticket(ResourceIdentifiable classId, Map<String, Object> properties) {
        super(classId, properties);
    }

    @Override
    public void setValues(Map<String, Object> properties) {
        super.setValues(properties);
        Class<?> clazz = getClass();
        while (!Ticket.class.equals(clazz)) {
            initializeFields(clazz, this);
            clazz = clazz.getSuperclass();
        }
    }

    protected static void initializeFields(Class<?> clazz, Object obj) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }

            Class<?> type = field.getType();
            if (Object.class.isAssignableFrom(type)) {
                try {
                    if (field.isAccessible()) {
                        field.set(obj, null);
                    } else {
                        field.setAccessible(true);
                        field.set(obj, null);
                        field.setAccessible(false);
                    }
                } catch (IllegalArgumentException e) {
                    LOG.error("[YEON] Fail to clear field.(" + field.getName() + ")", e);
                } catch (IllegalAccessException e) {
                    LOG.error("[YEON] Fail to clear field.(" + field.getName() + ")", e);
                }
            } else if (type.isPrimitive()) {
                // TODO [LOW] 기본 값으로 초기화할 지 여부 결정.
            }
        }
    }

    @Override
    public int hashCode() {
        return data == null ? 0 : data.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Ticket)) {
            return false;
        }

        Ticket other = (Ticket) obj;

        if (data == null) {
            return other.getValues() == null;
        } else {
            return data.equals(other.getValues());
        }
    }
}
