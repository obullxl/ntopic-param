/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.param.model;

import cn.ntopic.param.builder.ToString;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 单个参数对象
 *
 * @author obullxl 2023年09月10日: 新增
 */
public class NTParam extends ToString {
    private static final Logger LOGGER = LoggerFactory.getLogger(NTParam.class);

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date modifyTime;

    // ~~~~~~~~~~~~~~~~ 表字段访问器 ~~~~~~~~~~~~~~~~~ //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    // ~~~~~~~~~~~~~~~~ 工具类参数方法 ~~~~~~~~~~~~~~~~~ //

    /**
     * `int`类型值
     */
    public int toInt() {
        return toInt(0);
    }

    /**
     * `int`类型值（默认值）
     */
    public int toInt(int defaultValue) {
        return NumberUtils.toInt(this.getContent(), defaultValue);
    }

    /**
     * `long`类型值
     */
    public long toLong() {
        return toLong(0L);
    }

    /**
     * `long`类型值（默认值）
     */
    public long toLong(long defaultValue) {
        return NumberUtils.toLong(this.getContent(), defaultValue);
    }

    /**
     * `BigDecimal`类型值
     */
    public BigDecimal toBigDecimal() {
        return toBigDecimal(BigDecimal.ZERO);
    }

    /**
     * `long`类型值（默认值）
     */
    public BigDecimal toBigDecimal(BigDecimal defaultValue) {
        try {
            return new BigDecimal(this.getContent());
        } catch (Throwable e) {
            LOGGER.warn("BigDecimal异常[{}]-[{}].", this.getContent(), e.getMessage());
            return defaultValue;
        }
    }
}
