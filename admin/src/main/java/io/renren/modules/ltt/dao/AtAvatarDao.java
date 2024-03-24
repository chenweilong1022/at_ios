package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.AtAvatarEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ltt.vo.AtAvatarGroupAvatarGroupIdVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 头像
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-29 17:21:50
 */
@Mapper
public interface AtAvatarDao extends BaseMapper<AtAvatarEntity> {

    List<AtAvatarGroupAvatarGroupIdVO> avatarGroupId(@Param("avatarGroupId") List<Integer> avatarGroupId);
}
