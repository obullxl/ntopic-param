/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.param.cache;

import cn.ntopic.param.builder.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 参数缓存Key
 *
 * @author obullxl 2023年09月17日: 新增
 */
public class NTParamCacheKey extends ToString {

    /**
     * 分类
     */
    private final String category;

    /**
     * 模块
     */
    private String module;

    /**
     * 名称
     */
    private String name;

    /**
     * 缓存Key
     */
    private String cacheKey;

    /**
     * 分类构造函数
     *
     * @param category 分类
     */
    private NTParamCacheKey(String category) {
        Assert.hasText(category, "Category参数为空.");

        this.category = category;
        this.cacheKey = category;
    }

    /**
     * Category构建缓存Key
     */
    private static NTParamCacheKey makeKey(String category) {
        Assert.hasText(category, "Category参数为空.");

        NTParamCacheKey ntCacheKey = new NTParamCacheKey(category);
        ntCacheKey.cacheKey = category;

        return ntCacheKey;
    }

    /**
     * Category+Module构建缓存Key
     */
    public static NTParamCacheKey makeKey(String category, String module) {
        Assert.hasText(category, "Category参数为空.");
        Assert.hasText(module, "Module参数为空.");

        NTParamCacheKey ntCacheKey = makeKey(category);
        ntCacheKey.module = module;
        ntCacheKey.cacheKey = category + "#" + module;

        return ntCacheKey;
    }

    /**
     * Category+Module+Name构建缓存Key
     */
    public static NTParamCacheKey makeKey(String category, String module, String name) {
        Assert.hasText(category, "Category参数为空.");
        Assert.hasText(module, "Module参数为空.");
        Assert.hasText(name, "Name参数为空.");

        NTParamCacheKey ntCacheKey = makeKey(category, module);
        ntCacheKey.name = name;
        ntCacheKey.cacheKey = category + "#" + module + "#" + name;

        return ntCacheKey;
    }

    @Override
    public int hashCode() {
        return this.cacheKey.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NTParamCacheKey
                && StringUtils.equals(this.cacheKey, ((NTParamCacheKey) other).cacheKey);
    }

    // ~~~~~~~~~~~~~~~~ 表字段访问器 ~~~~~~~~~~~~~~~~~ //

    public String getCategory() {
        return category;
    }

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public String getCacheKey() {
        return cacheKey;
    }
}
