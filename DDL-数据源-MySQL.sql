--
-- 参数表
--
CREATE TABLE nt_param
(
    id          bigint unsigned NOT NULL auto_increment COMMENT '自增ID',
    category    varchar(64)     NOT NULL COMMENT '分类，如：SYSTEM-系统参数，CONFIG-业务配置等',
    module      varchar(64)     NOT NULL COMMENT '模块，如：USER-用户配置等',
    name        varchar(64)     NOT NULL COMMENT '参数名，如：minAge-最新年龄等',
    content     varchar(4096)            DEFAULT '' COMMENT '参数值，如：18-18岁等',
    create_time timestamp(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    modify_time timestamp(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_nt_param_m_c_n (category, module, name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='NTopicX-参数配置数据表';

--
-- 插入数据
--
INSERT INTO nt_param (category, module, name, content, create_time, modify_time)
VALUES ('CONFIG', 'USER', 'minAge', '18', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3)),
       ('CONFIG', 'USER', 'maxAge', '60', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3));
