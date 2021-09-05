package com.amos.scene.service;

import org.springframework.stereotype.Service;

/**
 * Doc
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/9/5
 */
@Service("docService")
public class DocServiceImpl implements DocOpsService, DocQueryService {

    private String value;

    @Override
    public String getAndSave(String value) {
        String oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    @Override
    public String query() {
        return value;
    }
}
