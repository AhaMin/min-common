package codec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * created by ewang on 2018/3/20.
 */
public class JSONCodec {
    static ObjectMapper mapper = null;

    static {
        mapper = new ObjectMapper();
        //禁用未知属性打断反序列化功能
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    public static <T> T decode(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException( //
                    "parse json fail: requiredType=" + typeReference + ", json=" + json, e);
        }
    }

    public static <T> T decode(String json, Type type) {
        try {
            return mapper.readValue(json, (Class<T>) type);
        } catch (Exception e) {
            throw new RuntimeException( //
                    "parse json fail: requiredType=" + type + ", json=" + json, e);
        }
    }

    public static <T> T decode(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException( //
                    "parse json fail: requiredType=" + clazz + ", json=" + json, e);
        }
    }

    public static <T> String encode(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("write json fail: " + object, e);
        }
    }

    public static <T> void encode(T object, OutputStream out) throws Exception {
        try {
            mapper.writeValue(out, object);
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw e;
            }
            throw new RuntimeException("write json fail: " + object, e);
        }
    }
}
