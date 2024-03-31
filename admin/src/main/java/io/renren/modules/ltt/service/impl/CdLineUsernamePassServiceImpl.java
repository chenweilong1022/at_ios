package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.RandomUtil;
import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdLineUsernamePassDao;
import io.renren.modules.ltt.entity.CdLineUsernamePassEntity;
import io.renren.modules.ltt.dto.CdLineUsernamePassDTO;
import io.renren.modules.ltt.vo.CdLineUsernamePassVO;
import io.renren.modules.ltt.service.CdLineUsernamePassService;
import io.renren.modules.ltt.conver.CdLineUsernamePassConver;

import java.io.Serializable;
import java.util.Collection;



@Service("cdLineUsernamePassService")
@Game
public class CdLineUsernamePassServiceImpl extends ServiceImpl<CdLineUsernamePassDao, CdLineUsernamePassEntity> implements CdLineUsernamePassService {

    @Override
    public PageUtils<CdLineUsernamePassVO> queryPage(CdLineUsernamePassDTO cdLineUsernamePass) {
        IPage<CdLineUsernamePassEntity> page = baseMapper.selectPage(
                new Query<CdLineUsernamePassEntity>(cdLineUsernamePass).getPage(),
                new QueryWrapper<CdLineUsernamePassEntity>()
        );

        return PageUtils.<CdLineUsernamePassVO>page(page).setList(CdLineUsernamePassConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public CdLineUsernamePassVO getById(Integer id) {
        return CdLineUsernamePassConver.MAPPER.conver(baseMapper.selectById(id));
    }

    public String getPass() {
        String s1 = RandomUtil.randomString("abcdefghijklmnopqrstuvwxyz", 1).toUpperCase();
        String s = RandomUtil.randomString(10);
        return s1 + s + ".";
    }

    @Override
    public boolean save(CdLineUsernamePassDTO cdLineUsernamePass) {
        cdLineUsernamePass.setUsername(RandomUtil.randomString(10));
        cdLineUsernamePass.setPass(getPass());
        CdLineUsernamePassEntity cdLineUsernamePassEntity = CdLineUsernamePassConver.MAPPER.converDTO(cdLineUsernamePass);
        return this.save(cdLineUsernamePassEntity);
    }

    @Override
    public boolean updateById(CdLineUsernamePassDTO cdLineUsernamePass) {
        CdLineUsernamePassEntity cdLineUsernamePassEntity = CdLineUsernamePassConver.MAPPER.converDTO(cdLineUsernamePass);
        return this.updateById(cdLineUsernamePassEntity);
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
