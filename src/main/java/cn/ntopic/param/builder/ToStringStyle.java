package cn.ntopic.param.builder;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * ToString类型
 */
public class ToStringStyle extends org.apache.commons.lang3.builder.ToStringStyle {
    private static final long serialVersionUID = -4063924270218289279L;

    /**
     * 日期格式
     */
    private static final String DT_FMT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 单例
     */
    protected static final ToStringStyle INSTANCE = new ToStringStyle();

    /**
     * CTOR
     */
    protected ToStringStyle() {
        super();
        this.setUseFieldNames(true);
        this.setDefaultFullDetail(true);
        this.setUseShortClassName(true);
        this.setUseIdentityHashCode(false);
    }

    /**
     * 确保单例
     */
    private Object readResolve() {
        return ToStringStyle.INSTANCE;
    }

    /**
     * @see org.apache.commons.lang3.builder.ToStringStyle#appendDetail(StringBuffer, String, Object)
     */
    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        if (value instanceof Date) {
            value = new SimpleDateFormat(DT_FMT).format(value);
        }

        buffer.append(value);
    }

    /**
     * @see org.apache.commons.lang3.builder.ToStringStyle#append(StringBuffer, String, Object, Boolean)
     */
    @Override
    public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
        // NULL不显示
        if (StringUtils.isBlank(fieldName) || value == null) {
            return;
        }

        // 空串不展示
        if (value instanceof String && StringUtils.isBlank((String) value)) {
            return;
        }

        // false不展示
        if (value instanceof Boolean && !((Boolean) value)) {
            return;
        }

        // 空映射不展示
        if (value instanceof Map && isEmpty((Map) value)) {
            return;
        }

        // 空集合不展示
        if (value instanceof Collection && isEmpty((Collection) value)) {
            return;
        }

        appendFieldStart(buffer, fieldName);
        appendInternal(buffer, fieldName, value, Boolean.TRUE);
        appendFieldEnd(buffer, fieldName);
    }

    /**
     * Map空判断
     */
    private boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Collection空判断
     */
    private boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }
}
