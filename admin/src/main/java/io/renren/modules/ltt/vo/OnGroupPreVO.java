package io.renren.modules.ltt.vo;

import cn.hutool.core.collection.CollUtil;
import io.renren.modules.ltt.entity.AtDataTaskEntity;
import io.renren.modules.ltt.entity.AtGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/3/30 15:09
 */
@Data
public class OnGroupPreVO {

    private List<String> navyTextLists;
    private List<String> materialUrls;

    private String groupName;

    private AtGroupEntity atGroupTaskEntity;
    private AtDataTaskEntity atDataTask;

    public String getNavyTextListsStr() {
        if (CollUtil.isNotEmpty(navyTextLists)) {
            return String.format("水军%s-等(%s)人",navyTextLists.get(0),navyTextLists.size());
        }
        return "";
    }
    public String getMaterialUrlsStr() {
        if (CollUtil.isNotEmpty(materialUrls)) {
            return String.format("料子%s-等(%s)人",materialUrls.get(0),materialUrls.size());
        }
        return "";
    }
    public String getTotal() {
        if (CollUtil.isNotEmpty(materialUrls) && CollUtil.isNotEmpty(navyTextLists)) {
            return String.format("料子水军共(%s)", materialUrls.size() + navyTextLists.size());
        }
        return "";
    }
}
