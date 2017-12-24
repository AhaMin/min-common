package common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * created by ewang on 2017/12/24.
 */
public class DataAttributeKey<T> {
    private final String name;
    private final T defaultValue;
    private final TypeToken<T> typeToken;
    private final Converter<T> converter;
    /**
     * Class<T>在实例化的时候，T要替换成具体类
     * Class<?>它是个通配泛型，?可以代表任何类型
     */
    private final Multimap<Class<?>, String> ATTR_MAP = HashMultimap.create();

    public interface Converter<T> {

        T decode(Object raw);

        Object encode(T value);
    }

    /**
     * @param entityClass 实体类的类型
     * @param name
     * @param valueClass  简单型的用{@link TypeToken#of(Class)},复杂型的使用new {@link TypeToken}(){}
     */
    public DataAttributeKey(Class<?> entityClass, String name, Class<T> valueClass) {
        this(entityClass, name, TypeToken.of(valueClass), null, null);
    }

    public DataAttributeKey(Class<?> entityClass, String name, TypeToken<T> typeToken) {
        this(entityClass, name, typeToken, null, null);
    }

    /**
     * @param entityClass
     * @param name
     * @param typeToken
     * @param converter   转换器（可选），用来支持一些复杂的类型，例如[int]->[enum]
     */
    public DataAttributeKey(Class<?> entityClass, String name, TypeToken<T> typeToken,
                            Converter<T> converter) {
        this(entityClass, name, typeToken, converter, null);
    }

    public DataAttributeKey(Class<?> entityClass, String name, TypeToken<T> typeToken,
                            T defaultValue) {
        this(entityClass, name, typeToken, null, defaultValue);
    }

    public DataAttributeKey(Class<?> entityClass, String name, TypeToken<T> typeToken,
                            Converter<T> converter, T defaultValue) {
        super();
        if (ATTR_MAP.containsEntry(entityClass, name)) {
            throw new IllegalStateException("属性重复：" + entityClass.getClass().getName() + " " + name);
        }
        ATTR_MAP.put(entityClass, name);
        this.name = name;
        this.defaultValue = defaultValue;
        this.typeToken = typeToken;
        this.converter = converter;
    }

    public String getName() {
        return name;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public TypeToken<T> getTypeToken() {
        return typeToken;
    }

    public Converter<T> getConverter() {
        return converter;
    }
}
