/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.param.cache;

/**
 * 参数缓存异常
 *
 * @author obullxl 2023年09月17日: 新增
 */
public class NTParamCacheException extends RuntimeException {

    public NTParamCacheException(Throwable cause, String message) {
        super(message, cause);
    }
}
