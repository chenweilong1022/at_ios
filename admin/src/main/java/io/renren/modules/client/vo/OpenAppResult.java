// OpenAppResult.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.modules.client.vo;
import lombok.Data;

@Data

public class OpenAppResult {
    private String msg;
    private long code;
    private Data data;

    public String getMsg() { return msg; }
    public void setMsg(String value) { this.msg = value; }

    public long getCode() { return code; }
    public void setCode(long value) { this.code = value; }

    public Data getData() { return data; }
    public void setData(Data value) { this.data = value; }

    @lombok.Data
    public class Data {
        private String taskType;
        private long createTime;
        private String remark;
        private String taskId;
        private long status;
    }
}

