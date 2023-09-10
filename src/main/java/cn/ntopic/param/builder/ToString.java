package cn.ntopic.param.builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 对象ToString基类。
 * <p/>
 * 过滤过NULL属性，格式化日期。
 */
public class ToString implements Serializable {
    private static final long serialVersionUID = -4942036796614978945L;

    /** 空字符串 */
    public static final String EMPTY = StringUtils.EMPTY;

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return toString(this);
    }

    /**
     * 对象转换为字符串
     */
    public static String toString(Object object) {
        return ToStringBuilder.reflectionToString(object, ToStringStyle.INSTANCE);
    }

    /**
     * 对象转换为字符串
     */
    public static String toString(Object object, String... extFields) {
        if (extFields == null || extFields.length < 1) {
            return toString(object);
        } else {
            return ToStringBuilder.reflectionToString(object, new cn.ntopic.core.builder.ToStringStyleExt(extFields));
        }
    }

}
