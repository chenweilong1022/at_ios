package io.renren.modules.ltt.dao;

import io.renren.modules.ltt.entity.CdIpConfigEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * 
 * 
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-04-26 12:10:55
 */
@Mapper
public interface CdIpConfigDao extends BaseMapper<CdIpConfigEntity> {

    /**
     * 更新使用次数
     * @param id
     * @param updateTime
     * @return
     */
    int updateUsedCountById(@RequestParam Integer id,
                            @RequestParam Date updateTime);
}
