package common;

/**
 * created by ewang on 2017/12/24.
 */
public class DataAttribute<T> {
    private final DataAttributeKey<T> key;
    private final Object value;

    public DataAttribute(DataAttributeKey<T> key, Object value) {
        this.key = key;
        this.value = value;
    }

    public T get() {
        return key.getConverter() != null ? key.getConverter().decode(value) : (T) value;
    }
}
