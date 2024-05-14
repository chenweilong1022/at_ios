package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.service.AtUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUsernameSubtaskDao;
import io.renren.modules.ltt.entity.AtUsernameSubtaskEntity;
import io.renren.modules.ltt.dto.AtUsernameSubtaskDTO;
import io.renren.modules.ltt.vo.AtUsernameSubtaskVO;
import io.renren.modules.ltt.service.AtUsernameSubtaskService;
import io.renren.modules.ltt.conver.AtUsernameSubtaskConver;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("atUsernameSubtaskService")
@Game
public class AtUsernameSubtaskServiceImpl extends ServiceImpl<AtUsernameSubtaskDao, AtUsernameSubtaskEntity> implements AtUsernameSubtaskService {

    @Resource
    private AtUserService atUserService;
    @Override
    public PageUtils<AtUsernameSubtaskVO> queryPage(AtUsernameSubtaskDTO subtaskDTO) {
        IPage<AtUsernameSubtaskEntity> page = baseMapper.selectPage(
                new Query<AtUsernameSubtaskEntity>(subtaskDTO).getPage(),
                new QueryWrapper<AtUsernameSubtaskEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(subtaskDTO.getSysUserId()),
                                AtUsernameSubtaskEntity::getSysUserId, subtaskDTO.getSysUserId())
                        .eq(ObjectUtil.isNotNull(subtaskDTO.getUsernameTaskId()),
                                AtUsernameSubtaskEntity::getUsernameTaskId, subtaskDTO.getUsernameTaskId())
                        .eq(ObjectUtil.isNotNull(subtaskDTO.getTaskStatus()),
                                AtUsernameSubtaskEntity::getTaskStatus, subtaskDTO.getTaskStatus())
                        .orderByDesc(AtUsernameSubtaskEntity::getId)
        );
        List<AtUsernameSubtaskVO> resultList = AtUsernameSubtaskConver.MAPPER.conver(page.getRecords());
        if (CollectionUtils.isNotEmpty(resultList)) {
            List<Integer> userIdList = resultList.stream().map(AtUsernameSubtaskVO::getUserId).distinct().collect(Collectors.toList());
            //查询联系人手机号
            Map<Integer, String> phoneMap = atUserService.queryTelephoneByIds(userIdList);
            resultList.forEach(i->i.setUserTelephone(phoneMap.get(i.getUserId())));
        }
        return PageUtils.<AtUsernameSubtaskVO>page(page).setList(resultList);
    }
    @Override
    public AtUsernameSubtaskVO getById(Integer id) {
        return AtUsernameSubtaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUsernameSubtaskDTO atUsernameSubtask) {
        AtUsernameSubtaskEntity atUsernameSubtaskEntity = AtUsernameSubtaskConver.MAPPER.converDTO(atUsernameSubtask);
        return this.save(atUsernameSubtaskEntity);
    }

    @Override
    public boolean updateById(AtUsernameSubtaskDTO atUsernameSubtask) {
        AtUsernameSubtaskEntity atUsernameSubtaskEntity = AtUsernameSubtaskConver.MAPPER.converDTO(atUsernameSubtask);
        return this.updateById(atUsernameSubtaskEntity);
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
