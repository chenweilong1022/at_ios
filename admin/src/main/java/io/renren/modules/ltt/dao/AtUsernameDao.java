package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.AtUsernameEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ltt.vo.AtUsernameGroupUsernameCountGroupIdVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 昵称
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-28 22:56:04
 */
@Mapper
public interface AtUsernameDao extends BaseMapper<AtUsernameEntity> {

    List<AtUsernameGroupUsernameCountGroupIdVO> usernameCountGroupId(List<Integer> groupIdList);
}
