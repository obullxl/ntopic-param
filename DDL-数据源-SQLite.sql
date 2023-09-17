--
-- 序列表
--
CREATE TABLE nt_sequence
(
    name  VARCHAR(64) NOT NULL,
    value bigint      NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE nt_user_base
(
    id          varchar(64)  NOT NULL,
    name        varchar(128) NOT NULL,
    password    varchar(64),
    role_list   varchar(256),
    ext_map     varchar(4096),
    create_time timestamp,
    modify_time timestamp,
    PRIMARY KEY (id),
    UNIQUE (name)
);

--
-- 参数表
--
CREATE TABLE nt_param
(
    id          bigint unsigned auto_increment,
    category    varchar(64) NOT NULL,
    module      varchar(64) NOT NULL,
    name        varchar(64) NOT NULL,
    content     varchar(4096) DEFAULT '',
    create_time timestamp,
    modify_time timestamp,
    PRIMARY KEY (id),
    UNIQUE (category, module, name)
);

--
-- 插入数据
--
INSERT INTO nt_param (category, module, name, content, create_time, modify_time)
VALUES ('CONFIG', 'USER', 'minAge', '18', strftime('%Y-%m-%d %H:%M:%f', 'now'), strftime('%Y-%m-%d %H:%M:%f', 'now')),
       ('CONFIG', 'USER', 'maxAge', '60', strftime('%Y-%m-%d %H:%M:%f', 'now'), strftime('%Y-%m-%d %H:%M:%f', 'now'));
