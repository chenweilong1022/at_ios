package io.renren.modules.client.vo;

import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/1 00:57
 */
public class SyncLineTokenVO {
    private Long code;
    private String msg;
    private List<SyncLineTokenVOData> data;

    public SyncLineTokenVO(Long code, String msg, List<SyncLineTokenVOData> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Long getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public List<SyncLineTokenVOData> getData() {
        return this.data;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<SyncLineTokenVOData> data) {
        this.data = data;
    }
}
