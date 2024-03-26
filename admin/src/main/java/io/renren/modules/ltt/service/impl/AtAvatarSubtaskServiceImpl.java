package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.conver.AtUsernameSubtaskConver;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.vo.AtUsernameSubtaskVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtAvatarSubtaskDao;
import io.renren.modules.ltt.entity.AtAvatarSubtaskEntity;
import io.renren.modules.ltt.dto.AtAvatarSubtaskDTO;
import io.renren.modules.ltt.vo.AtAvatarSubtaskVO;
import io.renren.modules.ltt.service.AtAvatarSubtaskService;
import io.renren.modules.ltt.conver.AtAvatarSubtaskConver;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("atAvatarSubtaskService")
@Game
public class AtAvatarSubtaskServiceImpl extends ServiceImpl<AtAvatarSubtaskDao, AtAvatarSubtaskEntity> implements AtAvatarSubtaskService {

    @Resource
    private AtUserService atUserService;

    @Override
    public PageUtils<AtAvatarSubtaskVO> queryPage(AtAvatarSubtaskDTO atAvatarSubtask) {
        IPage<AtAvatarSubtaskEntity> page = baseMapper.selectPage(
                new Query<AtAvatarSubtaskEntity>(atAvatarSubtask).getPage(),
                new QueryWrapper<AtAvatarSubtaskEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atAvatarSubtask.getSysUserId()),AtAvatarSubtaskEntity::getSysUserId,atAvatarSubtask.getSysUserId())
                        .eq(ObjectUtil.isNotNull(atAvatarSubtask.getAvatarTaskId()),AtAvatarSubtaskEntity::getAvatarTaskId,atAvatarSubtask.getAvatarTaskId())
                        .eq(ObjectUtil.isNotNull(atAvatarSubtask.getTaskStatus()),AtAvatarSubtaskEntity::getTaskStatus,atAvatarSubtask.getTaskStatus())
                        .orderByDesc(AtAvatarSubtaskEntity::getId)
        );

        List<AtAvatarSubtaskVO> resultList = AtAvatarSubtaskConver.MAPPER.conver(page.getRecords());
        if (CollectionUtils.isNotEmpty(resultList)) {
            List<Integer> userIdList = resultList.stream().map(AtAvatarSubtaskVO::getUserId).distinct().collect(Collectors.toList());
            //查询联系人手机号
            Map<Integer, String> phoneMap = atUserService.queryTelephoneByIds(userIdList);
            resultList.forEach(i->i.setUserTelephone(phoneMap.get(i.getUserId())));
        }
        return PageUtils.<AtAvatarSubtaskVO>page(page).setList(resultList);
    }
    @Override
    public AtAvatarSubtaskVO getById(Integer id) {
        return AtAvatarSubtaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtAvatarSubtaskDTO atAvatarSubtask) {
        AtAvatarSubtaskEntity atAvatarSubtaskEntity = AtAvatarSubtaskConver.MAPPER.converDTO(atAvatarSubtask);
        return this.save(atAvatarSubtaskEntity);
    }

    @Override
    public boolean updateById(AtAvatarSubtaskDTO atAvatarSubtask) {
        AtAvatarSubtaskEntity atAvatarSubtaskEntity = AtAvatarSubtaskConver.MAPPER.converDTO(atAvatarSubtask);
        return this.updateById(atAvatarSubtaskEntity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

}
