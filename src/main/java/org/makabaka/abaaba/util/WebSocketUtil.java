package org.makabaka.abaaba.util;

import javax.websocket.Session;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;

public class WebSocketUtil {
    public static String getIp(Session session) {
        InetSocketAddress address = (InetSocketAddress) getFieldInstance(session.getAsyncRemote(), "base#socketWrapper#socket#sc#remoteAddress");
        return address.getAddress().toString().replaceAll("/", "");
    }

    private static Object getFieldInstance(Object obj, String fieldPath) {
        String[] fields = fieldPath.split("#");
        for (String field : fields) {
            obj = getField(obj, obj.getClass(), field);
            if (obj == null) {
                return null;
            }
        }
        return obj;
    }

    private static Object getField(Object obj, Class<?> clazz, String fieldName) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
        return null;
    }
}
