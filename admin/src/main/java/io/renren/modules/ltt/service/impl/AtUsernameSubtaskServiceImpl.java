package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
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

import java.io.Serializable;
import java.util.Collection;


@Service("atUsernameSubtaskService")
@Game
public class AtUsernameSubtaskServiceImpl extends ServiceImpl<AtUsernameSubtaskDao, AtUsernameSubtaskEntity> implements AtUsernameSubtaskService {

    @Override
    public PageUtils<AtUsernameSubtaskVO> queryPage(AtUsernameSubtaskDTO subtaskDTO) {
        IPage<AtUsernameSubtaskEntity> page = baseMapper.selectPage(
                new Query<AtUsernameSubtaskEntity>(subtaskDTO).getPage(),
                new QueryWrapper<AtUsernameSubtaskEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(subtaskDTO.getSysUserId()),
                                AtUsernameSubtaskEntity::getSysUserId, subtaskDTO.getSysUserId())
                        .orderByDesc(AtUsernameSubtaskEntity::getId)
        );

        return PageUtils.<AtUsernameSubtaskVO>page(page).setList(AtUsernameSubtaskConver.MAPPER.conver(page.getRecords()));
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
