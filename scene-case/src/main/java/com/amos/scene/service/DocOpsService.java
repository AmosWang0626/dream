package com.amos.scene.service;

/**
 * ops
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/9/5
 */
public interface DocOpsService {

    /**
     * 获取原来的值并更新
     *
     * @param value 新值
     * @return 旧值
     */
    String getAndSave(String value);

}
