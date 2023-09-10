/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.param.impl;

import cn.ntopic.param.NTParamDAO;
import cn.ntopic.param.model.NTParamDO;

import java.util.List;

/**
 * SQLite-参数DAO实现
 *
 * @author obullxl 2023年09月10日: 新增
 */
public class NTParamDAOSQLite implements NTParamDAO {

    @Override
    public List<NTParamDO> selectByCategory(String category) {
        return null;
    }

    @Override
    public List<NTParamDO> selectByModule(String category, String module) {
        return null;
    }

    @Override
    public NTParamDO selectByName(String category, String module, String name) {
        return null;
    }
}
