package common;

import java.util.HashMap;
import java.util.Map;

/**
 * created by ewang on 2017/12/24.
 */
public class DataAttributeBuilder {
    private Map<String, Object> dataMap;

    public DataAttributeBuilder() {
        dataMap = new HashMap<String, Object>();
    }

    public DataAttributeBuilder(String data) {
        dataMap = JSONCodec.decode(data, Map.class);
    }

    /**
     * 增加/替换一个key的值
     *
     * @param key
     * @param value 可以传null，不会写进dataMap
     * @return
     */
    public <T> DataAttributeBuilder add(DataAttributeKey<T> key, T value) {
        if (value != null) {
            Object raw = key.getConverter() != null ? key.getConverter().encode(value) : value;
            if (raw != null) {
                dataMap.put(key.getName(), raw);
            }
        }
        return this;
    }

    public <T> DataAttributeBuilder del(DataAttributeKey<T> key) {
        dataMap.remove(key.getName());
        return this;
    }

    public <T> boolean containsKey(DataAttributeKey<T> key) {
        return dataMap.containsKey(key.getName());
    }

    public Map<String, Object> build() {
        return dataMap;
    }

    public String buildString() {
        return JSONCodec.encode(dataMap);
    }
}
