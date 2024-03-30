package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpUtil;
import io.renren.common.utils.PhoneUtil;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.DeleteFlag;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtGroupTaskDao;
import io.renren.modules.ltt.entity.AtGroupTaskEntity;
import io.renren.modules.ltt.dto.AtGroupTaskDTO;
import io.renren.modules.ltt.vo.AtGroupTaskVO;
import io.renren.modules.ltt.service.AtGroupTaskService;
import io.renren.modules.ltt.conver.AtGroupTaskConver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service("atGroupTaskService")
@Game
public class AtGroupTaskServiceImpl extends ServiceImpl<AtGroupTaskDao, AtGroupTaskEntity> implements AtGroupTaskService {

    @Override
    public PageUtils<AtGroupTaskVO> queryPage(AtGroupTaskDTO atGroupTask) {
        IPage<AtGroupTaskEntity> page = baseMapper.selectPage(
                new Query<AtGroupTaskEntity>(atGroupTask).getPage(),
                new QueryWrapper<AtGroupTaskEntity>()
        );

        return PageUtils.<AtGroupTaskVO>page(page).setList(AtGroupTaskConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtGroupTaskVO getById(Integer id) {
        return AtGroupTaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtGroupTaskDTO atGroupTask) {
        atGroupTask.setDeleteFlag(DeleteFlag.NO.getKey());
        atGroupTask.setCreateTime(DateUtil.date());
        AtGroupTaskEntity atGroupTaskEntity = AtGroupTaskConver.MAPPER.converDTO(atGroupTask);
        return this.save(atGroupTaskEntity);
    }

    @Override
    public boolean updateById(AtGroupTaskDTO atGroupTask) {
        AtGroupTaskEntity atGroupTaskEntity = AtGroupTaskConver.MAPPER.converDTO(atGroupTask);
        return this.updateById(atGroupTaskEntity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public List<String> getGroupNameList(AtGroupTaskDTO atGroupTask) {
        Assert.isNull(atGroupTask.getGroupCount(),"请输入拉群数量");
        Assert.isBlank(atGroupTask.getGroupName(),"请输入群名称");
        Integer groupCount = atGroupTask.getGroupCount();
        List<String> groupNames = new ArrayList<>();
        for (Integer i = 0; i < groupCount; i++) {
            String groupName = String.format("%s-%s", atGroupTask.getGroupName(), i);
            groupNames.add(groupName);
        }
        return groupNames;
    }

    @Override
    public void onGroupPre(AtGroupTaskDTO atGroupTask) {
        Integer groupCount = atGroupTask.getGroupCount();
        Assert.isNull(groupCount,"请输入拉群数量");
        Assert.isBlank(atGroupTask.getGroupName(),"请输入群名称");
        List<String> navyUrlList = atGroupTask.getNavyUrlList();
        Assert.isTrue(CollUtil.isEmpty(navyUrlList),"水军不能为空");
        Assert.isTrue(CollUtil.isEmpty(atGroupTask.getMaterialUrlList()),"料子不能为空");

        List<List<String>> navyTextListsList = new ArrayList<>();
        //获取所有的水军列表
        for (String navyUrl : navyUrlList) {
            String navyText = HttpUtil.downloadString(navyUrl, "UTF-8");
            String[] navyTextLines = navyText.split("\n");

            List<String> navyTextLists = new ArrayList<>();
            for (String navyTextLine : navyTextLines) {
                String phone = containsInternationalPhoneNumber(navyTextLine);
                try {
                    PhoneCountryVO phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(phone);
                    navyTextLists.add(navyTextLine);
                } catch (Exception e) {
                    if (CollUtil.isNotEmpty(navyTextLists)) {
                        navyTextListsList.add(navyTextLists);
                    }
                    navyTextLists = new ArrayList<>();
                }
            }
            if (CollUtil.isNotEmpty(navyTextLists)) {
                navyTextListsList.add(navyTextLists);
            }
        }
        System.out.println(navyTextListsList);

        //遍历所有的群
        for (Integer i = 0; i < groupCount; i++) {

        }


    }


    private String containsInternationalPhoneNumber(String text) {
        // 正则表达式匹配一个或多个连续的数字
        String regex = "\\d+";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        // 获取matcher对象
        Matcher matcher = pattern.matcher(text);
        System.out.println("Found numbers:");
        while (matcher.find()) {
            // 打印出找到的每一个数字
            return matcher.group();
        }
        return "1";
    }

}
