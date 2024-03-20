package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUsernameTaskDao;
import io.renren.modules.ltt.entity.AtUsernameTaskEntity;
import io.renren.modules.ltt.dto.AtUsernameTaskDTO;
import io.renren.modules.ltt.vo.AtUsernameTaskVO;
import io.renren.modules.ltt.service.AtUsernameTaskService;
import io.renren.modules.ltt.conver.AtUsernameTaskConver;

import java.io.Serializable;
import java.util.Collection;


@Service("atUsernameTaskService")
@Game
public class AtUsernameTaskServiceImpl extends ServiceImpl<AtUsernameTaskDao, AtUsernameTaskEntity> implements AtUsernameTaskService {

    @Override
    public PageUtils<AtUsernameTaskVO> queryPage(AtUsernameTaskDTO atUsernameTask) {
        IPage<AtUsernameTaskEntity> page = baseMapper.selectPage(
                new Query<AtUsernameTaskEntity>(atUsernameTask).getPage(),
                new QueryWrapper<AtUsernameTaskEntity>()
        );

        return PageUtils.<AtUsernameTaskVO>page(page).setList(AtUsernameTaskConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtUsernameTaskVO getById(Integer id) {
        return AtUsernameTaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUsernameTaskDTO atUsernameTask) {
        AtUsernameTaskEntity atUsernameTaskEntity = AtUsernameTaskConver.MAPPER.converDTO(atUsernameTask);
        return this.save(atUsernameTaskEntity);
    }

    @Override
    public boolean updateById(AtUsernameTaskDTO atUsernameTask) {
        AtUsernameTaskEntity atUsernameTaskEntity = AtUsernameTaskConver.MAPPER.converDTO(atUsernameTask);
        return this.updateById(atUsernameTaskEntity);
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
