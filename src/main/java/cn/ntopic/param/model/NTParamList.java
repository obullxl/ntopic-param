/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.param.model;

import cn.ntopic.param.builder.ToString;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 参数对象列表
 *
 * @author obullxl 2023年09月10日: 新增
 */
public class NTParamList extends ToString {

    /**
     * 分类
     */
    private final String category;

    /**
     * 模块
     */
    private final String module;

    /**
     * 参数列表
     */
    private List<NTParam> paramList;

    public NTParamList(String category, String module) {
        if (StringUtils.isBlank(category)) {
            throw new IllegalArgumentException("Category参数为空.");
        }

        if (StringUtils.isBlank(module)) {
            throw new IllegalArgumentException("Module参数为空.");
        }

        // 参数
        this.category = category;
        this.module = module;
    }

    // ~~~~~~~~~~~~~~~~ 表字段访问器 ~~~~~~~~~~~~~~~~~ //

    public String getCategory() {
        return category;
    }

    public String getModule() {
        return module;
    }

    public List<NTParam> getParamList() {
        if (paramList == null) {
            paramList = new ArrayList<>();
        }

        return paramList;
    }

    public void setParamList(List<NTParam> paramList) {
        this.paramList = paramList;
    }

    // ~~~~~~~~~~~~~~~~ 工具类参数方法 ~~~~~~~~~~~~~~~~~ //

    /**
     * 获取制定参数
     */
    public Optional<NTParam> fetchParam(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name参数为空.");
        }

        return this.getParamList().stream().filter(t -> StringUtils.equals(name, t.getName())).findAny();
    }

    /**
     * `int`类型值
     */
    public int toInt(String name) {
        return toInt(name, 0);
    }

    /**
     * `int`类型值（默认值）
     */
    public int toInt(String name, int defaultValue) {
        return this.fetchParam(name).map(ntParam -> ntParam.toInt(defaultValue)).orElse(defaultValue);
    }

    /**
     * `long`类型值
     */
    public long toLong(String name) {
        return toLong(name, 0L);
    }

    /**
     * `long`类型值（默认值）
     */
    public long toLong(String name, long defaultValue) {
        return this.fetchParam(name).map(ntParam -> ntParam.toLong(defaultValue)).orElse(defaultValue);
    }

    /**
     * `BigDecimal`类型值
     */
    public BigDecimal toBigDecimal(String name) {
        return toBigDecimal(name, BigDecimal.ZERO);
    }

    /**
     * `long`类型值（默认值）
     */
    public BigDecimal toBigDecimal(String name, BigDecimal defaultValue) {
        return this.fetchParam(name).map(ntParam -> ntParam.toBigDecimal(defaultValue)).orElse(defaultValue);
    }
}
