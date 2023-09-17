/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.param;

import cn.ntopic.param.model.NTParam;
import cn.ntopic.param.model.NTParamDO;
import cn.ntopic.param.model.NTParamList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

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
     * 依赖注入
     * @param ntParamDAO 参数DAO
     */
    public NTParamUtils(@Qualifier("ntParamDAO") NTParamDAO ntParamDAO) {
        if (ntParamDAO == null) {
            throw new IllegalArgumentException("NTParamDAO参数为NULL.");
        }

        LOGGER.info("NTParamUtils初始化[{}].", ntParamDAO.getClass());

        // 初始化
        PARAM_DAO = ntParamDAO;
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
     * 获取参数列表（分类）
     */
    public static List<NTParamList> findList(String category) {
        if (StringUtils.isBlank(category)) {
            throw new IllegalArgumentException("Category参数为空.");
        }

        // DB查询
        List<NTParamDO> recordList = PARAM_DAO.selectByCategory(category);

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

    /**
     * 获取参数列表（分类+模块）
     */
    public static NTParamList findList(String category, String module) {
        if (StringUtils.isBlank(category)) {
            throw new IllegalArgumentException("Category参数为空.");
        }

        if (StringUtils.isBlank(module)) {
            throw new IllegalArgumentException("Module参数为空.");
        }

        // DB查询
        List<NTParamDO> recordList = PARAM_DAO.selectByModule(category, module);

        // 模型转换
        NTParamList ntParamList = new NTParamList(category, module);
        for (NTParamDO ntParamDO : recordList) {
            ntParamList.getParamList().add(convert(ntParamDO));
        }

        // 返回结果
        return ntParamList;
    }

    /**
     * 获取参数列表（分类+模块+参数名）
     */
    public static Optional<NTParam> find(String category, String module, String name) {
        if (StringUtils.isBlank(category)) {
            throw new IllegalArgumentException("Category参数为空.");
        }

        if (StringUtils.isBlank(module)) {
            throw new IllegalArgumentException("Module参数为空.");
        }

        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name参数为空.");
        }

        // DB查询
        NTParamDO ntParamDO = PARAM_DAO.selectByName(category, module, name);

        // 模型转换
        return Optional.ofNullable(ntParamDO == null ? null : convert(ntParamDO));
    }

}
