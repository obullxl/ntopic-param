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
     * 根据分类查询参数列表
     */
    // @Select("SELECT * FROM nt_param WHERE category=#{category,jdbcType=VARCHAR}")
    List<NTParamDO> selectByCategory(String category);

    /**
     * 根据分类+模块查询参数列表
     */
    // @Select("SELECT * FROM nt_param WHERE category=#{category,jdbcType=VARCHAR} AND module=#{module,jdbcType=VARCHAR}")
    List<NTParamDO> selectByModule(String category, String module);

    /**
     * 根据分类+模块+参数名查询单个参数
     */
//    @Select("SELECT * FROM nt_param WHERE category=#{category,jdbcType=VARCHAR} AND module=#{module,jdbcType=VARCHAR} AND name=#{name,jdbcType=VARCHAR}")
    NTParamDO selectByName(String category, String module, String name);
}
