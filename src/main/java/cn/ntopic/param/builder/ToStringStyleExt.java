package cn.ntopic.param.builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * ToString类型
 */
public final class ToStringStyleExt extends ToStringStyle {
    private static final long serialVersionUID = -4063924270218289279L;

    /** 过滤字段 */
    private final Set<String> extFields = new HashSet<String>();

    /**
     * CTOR
     */
    public ToStringStyleExt(String[] extFields) {
        super();
        this.extFields.addAll(Arrays.asList(extFields));
    }

    /**
     * @see ToStringStyle#append(StringBuffer, String, Object, Boolean)
     */
    @Override
    public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
        // 过滤不显示
        if (this.extFields.contains(fieldName)) {
            return;
        }

        // NULL不显示
        super.append(buffer, fieldName, value, fullDetail);
    }

}
