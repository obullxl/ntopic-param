/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.param;

import cn.ntopic.param.cache.NTParamCacheException;
import cn.ntopic.param.cache.NTParamCacheKey;
import cn.ntopic.param.model.NTParam;
import cn.ntopic.param.model.NTParamDO;
import cn.ntopic.param.model.NTParamList;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 参数工具类
 *
 * @author obullxl 2023年09月10日: 新增
 */
public class NTParamUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(NTParamUtils.class);

    /**
     * 参数DAO
     */
    private static NTParamDAO PARAM_DAO;

    /**
     * Category级别缓存
     */
    private static LoadingCache<String, List<NTParamList>> CATEGORY_CACHE;

    /**
     * Category+Module级别缓存
     */
    private static LoadingCache<NTParamCacheKey, NTParamList> MODULE_CACHE;

    /**
     * Category+Module+Name级别缓存
     */
    private static LoadingCache<NTParamCacheKey, Optional<NTParam>> NAME_CACHE;

    /**
     * 依赖注入
     *
     * @param ntParamDAO 参数DAO
     */
    public NTParamUtils(@Qualifier("ntParamDAO") NTParamDAO ntParamDAO) {
        if (ntParamDAO == null) {
            throw new IllegalArgumentException("NTParamDAO参数为NULL.");
        }

        LOGGER.info("NTParamUtils初始化[{}].", ntParamDAO.getClass());

        // 初始化
        PARAM_DAO = ntParamDAO;

        // 初始化缓存
        CATEGORY_CACHE = this.initCategoryCache();
        MODULE_CACHE = this.initModuleCache();
        NAME_CACHE = this.initNameCache();
    }

    /**
     * `Category`缓存初始化
     */
    private LoadingCache<String, List<NTParamList>> initCategoryCache() {
        return CacheBuilder.newBuilder()
                // 30分钟过期（写入开始计时）
                .expireAfterWrite(30L, TimeUnit.MINUTES)
                // 最多缓存10万个Key
                .maximumSize(100 * 1000L)
                .build(new CacheLoader<String, List<NTParamList>>() {
                    @Override
                    public List<NTParamList> load(String key) {
                        // DB查询
                        List<NTParamDO> recordList = PARAM_DAO.selectByCategory(key);

                        // 模型转换
                        Map<String, NTParamList> paramList = new HashMap<>();
                        for (NTParamDO ntParamDO : recordList) {
                            NTParamList ntParamList = paramList.computeIfAbsent(ntParamDO.getModule()
                                    , t -> new NTParamList(ntParamDO.getCategory(), ntParamDO.getModule()));
                            ntParamList.getParamList().add(convert(ntParamDO));
                        }

                        // 返回结果
                        return new ArrayList<>(paramList.values());
                    }
                });
    }

    /**
     * `Category+Module`缓存初始化
     */
    private LoadingCache<NTParamCacheKey, NTParamList> initModuleCache() {
        return CacheBuilder.newBuilder()
                // 30分钟过期（写入开始计时）
                .expireAfterWrite(30L, TimeUnit.MINUTES)
                // 最多缓存10万个Key
                .maximumSize(100 * 1000L)
                .build(new CacheLoader<NTParamCacheKey, NTParamList>() {
                    @Override
                    public NTParamList load(NTParamCacheKey cacheKey) {
                        // DB查询
                        List<NTParamDO> recordList = PARAM_DAO.selectByModule(cacheKey.getCategory(), cacheKey.getModule());

                        // 模型转换
                        NTParamList ntParamList = new NTParamList(cacheKey.getCategory(), cacheKey.getModule());
                        for (NTParamDO ntParamDO : recordList) {
                            ntParamList.getParamList().add(convert(ntParamDO));
                        }

                        // 返回结果
                        return ntParamList;
                    }
                });
    }

    /**
     * `Category+Module+Name`缓存初始化
     */
    private LoadingCache<NTParamCacheKey, Optional<NTParam>> initNameCache() {
        return CacheBuilder.newBuilder()
                // 30分钟过期（写入开始计时）
                .expireAfterWrite(30L, TimeUnit.MINUTES)
                // 最多缓存10万个Key
                .maximumSize(100 * 1000L)
                .build(new CacheLoader<NTParamCacheKey, Optional<NTParam>>() {
                    @Override
                    public Optional<NTParam> load(NTParamCacheKey cacheKey) {
                        // DB查询
                        NTParamDO ntParamDO = PARAM_DAO.selectByName(cacheKey.getCategory(), cacheKey.getModule(), cacheKey.getName());

                        // 模型转换
                        return Optional.ofNullable(ntParamDO == null ? null : convert(ntParamDO));
                    }
                });
    }

    /**
     * 模型转换
     */
    private static NTParam convert(NTParamDO ntParamDO) {
        if (ntParamDO == null) {
            throw new IllegalArgumentException("NTParamDO参数为NULL.");
        }

        NTParam ntParam = new NTParam();
        ntParam.setName(ntParamDO.getName());
        ntParam.setContent(ntParamDO.getContent());
        ntParam.setCreateTime(ntParamDO.getCreateTime());
        ntParam.setModifyTime(ntParamDO.getModifyTime());

        return ntParam;
    }

    /**
     * 存储参数记录（如果参数已经存在则更新，不存在则新增）
     */
    public static int save(String category, String module, String name, String content) {
        return PARAM_DAO.save(category, module, name, content);
    }

    /**
     * 删除参数记录
     */
    public static int delete(String category, String module, String name) {
        int delete = PARAM_DAO.delete(category, module, name);

        if (delete >= 1) {
            // 删除缓存
            NAME_CACHE.invalidate(NTParamCacheKey.makeKey(category, module, name));
        }

        return delete;
    }

    /**
     * 获取参数列表（分类）
     */
    public static List<NTParamList> findList(String category) {
        Assert.hasText(category, "Category参数为空.");

        try {
            return CATEGORY_CACHE.get(category);
        } catch (ExecutionException e) {
            throw new NTParamCacheException(e
                    , String.format("Category获取参数异常[%s]-%s.", category, e.getMessage()));
        }
    }

    /**
     * 获取参数列表（分类+模块）
     */
    public static NTParamList findList(String category, String module) {
        Assert.hasText(category, "Category参数为空.");
        Assert.hasText(module, "Module参数为空.");

        try {
            return MODULE_CACHE.get(NTParamCacheKey.makeKey(category, module));
        } catch (ExecutionException e) {
            throw new NTParamCacheException(e
                    , String.format("Category获取参数异常[%s][%s]-%s.", category, module, e.getMessage()));
        }
    }

    /**
     * 获取参数列表（分类+模块+参数名）
     */
    public static Optional<NTParam> find(String category, String module, String name) {
        Assert.hasText(category, "Category参数为空.");
        Assert.hasText(module, "Module参数为空.");
        Assert.hasText(name, "Name参数为空.");

        try {
            NTParamCacheKey cacheKey = NTParamCacheKey.makeKey(category, module, name);
            Optional<NTParam> optParam = NAME_CACHE.get(cacheKey);
            if (!optParam.isPresent()) {
                // 缓存不存在，则不进行缓存
                NAME_CACHE.invalidate(cacheKey);
            }

            return optParam;
        } catch (ExecutionException e) {
            throw new NTParamCacheException(e
                    , String.format("Category获取参数异常[%s][%s][%s]-%s.", category, module, name, e.getMessage()));
        }
    }

}
