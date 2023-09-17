/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.param;

import cn.ntopic.param.model.NTParamDO;

import java.util.List;

/**
 * 参数DAO
 *
 * @author obullxl 2023年09月10日: 新增
 */
public interface NTParamDAO {

    /**
     * 存储参数记录（如果参数已经存在则更新，不存在则新增）
     */
    int save(String category, String module, String name, String content);

    /**
     * 删除参数记录
     */
    int delete(String category, String module, String name);

    /**
     * 根据分类查询参数列表
     */
    List<NTParamDO> selectByCategory(String category);

    /**
     * 根据分类+模块查询参数列表
     */
    List<NTParamDO> selectByModule(String category, String module);

    /**
     * 根据分类+模块+参数名查询单个参数
     */
    NTParamDO selectByName(String category, String module, String name);
}
