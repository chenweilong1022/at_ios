package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.AtAvatarGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 头像分组
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-28 22:45:51
 */
@Mapper
public interface AtAvatarGroupDao extends BaseMapper<AtAvatarGroupEntity> {

    /**
     *
     * 模糊检索根据分组名称
     * @param searchWord
     * @return
     */
    List<AtAvatarGroupEntity> queryByFuzzyName(@Param("searchWord") String searchWord,
                                               @Param("sysUserId") Long sysUserId);
}
