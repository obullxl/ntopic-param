/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.param.impl;

import cn.ntopic.param.NTParamDAO;
import cn.ntopic.param.model.NTParamDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * NTParamDAO基类
 *
 * @author obullxl 2023年09月17日: 新增
 */
public abstract class NTPAbstractDAO implements NTParamDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(NTParamDAO.class);

    /**
     * JDBC模板
     */
    @Autowired
    @Qualifier("ntParamJdbcTemplate")
    protected JdbcTemplate ntParamJdbcTemplate;

    /**
     * 当前时间函数
     */
    public abstract String nowStampFunction();

    /**
     * 更新参数记录
     */
    private int update(String category, String module, String name, String content) {
        return this.ntParamJdbcTemplate.update(
                String.format("UPDATE nt_param SET modify_time=%s, content=? WHERE category=? AND module=? AND name=?", this.nowStampFunction())
                , content, category, module, name);
    }

    /**
     * 新增参数记录
     */
    private int insert(String category, String module, String name, String content) {
        return this.ntParamJdbcTemplate.update(
                String.format("INSERT INTO nt_param(category, module, name, content, create_time, modify_time) VALUES(?, ?, ?, ?, %s, %s)"
                        , this.nowStampFunction(), this.nowStampFunction()), category, module, name, content);
    }

    @Override
    public int save(String category, String module, String name, String content) {
        Assert.hasText(category, "Category参数为空.");
        Assert.hasText(category, "Module参数为空.");
        Assert.hasText(category, "Name参数为空.");

        // 1. 更新
        int update = this.update(category, module, name, content);
        if (update >= 1) {
            return update;
        }

        // 2. 插入
        try {
            int insert = this.insert(category, module, name, content);
            if (insert >= 1) {
                return insert;
            }
        } catch (Throwable e) {
            LOGGER.warn("新增参数记录异常[{}, {}, {}]-[{}].", category, module, name, content, e);
        }

        return this.update(category, module, name, content);
    }


    @Override
    public int delete(String category, String module, String name) {
        Assert.hasText(category, "Category参数为空.");
        Assert.hasText(category, "Module参数为空.");
        Assert.hasText(category, "Name参数为空.");

        return this.ntParamJdbcTemplate.update(
                "DELETE FROM nt_param WHERE category=? AND module=? AND name=?", category, module, name);
    }

    @Override
    public List<NTParamDO> selectByCategory(String category) {
        Assert.hasText(category, "Category参数为空.");

        // 查询记录
        List<Map<String, Object>> recordList = this.ntParamJdbcTemplate.queryForList(
                "SELECT * FROM nt_param WHERE category=?", category);

        // 模型转换
        return recordList.stream().map(this::toNTParamDO).collect(Collectors.toList());
    }

    @Override
    public List<NTParamDO> selectByModule(String category, String module) {
        Assert.hasText(category, "Category参数为空.");
        Assert.hasText(category, "Module参数为空.");

        // 查询记录
        List<Map<String, Object>> recordList = this.ntParamJdbcTemplate.queryForList(
                "SELECT * FROM nt_param WHERE category=? AND module=?", category, module);

        // 模型转换
        return recordList.stream().map(this::toNTParamDO).collect(Collectors.toList());
    }

    @Override
    public NTParamDO selectByName(String category, String module, String name) {
        Assert.hasText(category, "Category参数为空.");
        Assert.hasText(category, "Module参数为空.");
        Assert.hasText(category, "Name参数为空.");

        // 查询记录
        Map<String, Object> record = this.ntParamJdbcTemplate.queryForMap(
                "SELECT * FROM nt_param WHERE category=? AND module=? AND name=?", category, module, name);

        return this.toNTParamDO(record);
    }

    /**
     * 模型转换
     */
    private NTParamDO toNTParamDO(Map<String, Object> record) {
        NTParamDO ntParamDO = new NTParamDO();
        ntParamDO.setId(NumberUtils.toLong(Objects.toString(record.get("id"))));
        ntParamDO.setCategory(Objects.toString(record.get("category")));
        ntParamDO.setModule(Objects.toString(record.get("module")));
        ntParamDO.setName(Objects.toString(record.get("name")));
        ntParamDO.setContent(Objects.toString(record.get("content")));
        ntParamDO.setCreateTime(this.toDate(record.get("create_time")));
        ntParamDO.setModifyTime(this.toDate(record.get("modify_time")));

        return ntParamDO;
    }

    /**
     * 日期类型转换
     */
    private Date toDate(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof String) {
            String date = (String) object;
            int valueLength = StringUtils.length(date);

            String[] patterns = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS"};
            for (String pattern : patterns) {
                if (valueLength == StringUtils.length(pattern)) {
                    try {
                        return new SimpleDateFormat(pattern).parse((String) object);
                    } catch (ParseException e) {
                        throw new RuntimeException("Date解析异常-" + e.getMessage(), e);
                    }
                }
            }
        }

        return (Date) object;
    }
}
