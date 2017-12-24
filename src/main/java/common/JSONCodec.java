package common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * created by ewang on 2017/12/24.
 */
public abstract class JSONCodec {
    static ObjectMapper mapper = null;

    static {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static <T> String encode(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("write json fail: " + object, e);
        }
    }

    public static <T> T decode(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("parse json fail: requiredType=" + clazz + ", json=" + json, e);
        }
    }
}
