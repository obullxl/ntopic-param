/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.param.impl;

/**
 * SQLite-参数DAO实现
 *
 * @author obullxl 2023年09月10日: 新增
 */
public class NTParamDAOSQLite extends NTPAbstractDAO {

    /**
     * 当前时间函数
     */
    public String nowStampFunction() {
        return "strftime('%Y-%m-%d %H:%M:%f', 'now')";
    }

}
