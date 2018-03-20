package common;

import codec.JSONCodec;
import com.google.common.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by ewang on 2017/12/24.
 */
public abstract class AbstractDataAttributeEntity {

    private static final TypeToken<Long> TYPE_TOKEN_LONG = TypeToken.of(Long.class);

    private static final TypeToken<Float> TYPE_TOKEN_FLOAT = TypeToken.of(Float.class);

    private static final TypeToken<List<Long>> TYPE_TOKEN_LONG_LIST = new TypeToken<List<Long>>() {

        //TODO
        private static final long serialVersionUID = 1471039654610180699L;
    };

    private transient volatile Map<String, Object> dataMap;

    public AbstractDataAttributeEntity() {
        super();
    }

    protected abstract String getData();

    @SuppressWarnings("unchecked")
    private Map<String, Object> getDataMap() {
        if (dataMap == null) {
            synchronized (this) {
                if (dataMap == null) {
                    try {
                        if (StringUtils.isBlank(getData())) {
                            dataMap = Collections.emptyMap();
                        } else {
                            dataMap = Collections.unmodifiableMap((Map<String, Object>) JSONCodec
                                    .decode(getData(), Map.class));
                        }
                    } catch (Exception e) {
                        dataMap = Collections.emptyMap();
                    }
                }
            }
        }
        return dataMap;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> DataAttribute<T> getDataAttr(DataAttributeKey<T> key) {
        Object rawValue = getDataMap().get(key.getName());
        if (rawValue != null) {
            // patch: jackons把未超过int范围的long都转换成int
            if (key.getTypeToken().equals(TYPE_TOKEN_LONG)) {
                return new DataAttribute<T>(key, ((Number) rawValue).longValue());
            } else if (key.getTypeToken().equals(TYPE_TOKEN_FLOAT)) {
                return new DataAttribute<T>(key, ((Number) rawValue).floatValue());
            } else if (key.getTypeToken().equals(TYPE_TOKEN_LONG_LIST)) {
                return new DataAttribute<T>(key, ((List<Number>) rawValue).stream()
                        .map(e -> e.longValue()).collect(Collectors.toList()));
            }
        } else {
            rawValue = key.getDefaultValue();
        }
        return new DataAttribute(key, rawValue);
    }
}

