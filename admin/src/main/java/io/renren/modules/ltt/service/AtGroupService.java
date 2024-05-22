package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.client.dto.ImportZipDTO;
import io.renren.modules.ltt.dto.AtGroupDTO;
import io.renren.modules.ltt.vo.AtGroupTaskVO;
import io.renren.modules.ltt.vo.AtGroupVO;
import io.renren.modules.ltt.entity.AtGroupEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:07:23
 */
public interface AtGroupService extends IService<AtGroupEntity> {

    /**
     * 分页查询
     * @param atGroup
     * @return
     */
    PageUtils queryPage(AtGroupDTO atGroup);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtGroupVO getById(Integer id);
    /**
     * 保存
     * @param atGroup
     * @return
     */
    boolean save(AtGroupDTO atGroup);
    /**
     * 根据id修改
     * @param atGroup
     * @return
     */
    boolean updateById(AtGroupDTO atGroup);
    /**
     * 根据id删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @Override
    boolean removeByIds(Collection<? extends Serializable> ids);

    /**
     * 导出zip
     * @param importZipDTO
     */
    byte[] importZip(ImportZipDTO importZipDTO);

    /**
     * 重新分配token
     * @param atGroup
     */
    void reallocateToken(AtGroupDTO atGroup);

    /**
     * 启动任务
     * @param atGroup
     */
    void startTask(AtGroupDTO atGroup);

    Integer updateGroupName(AtGroupDTO atGroup);

    /**
     * 获取缓存
     * @param groupId
     * @return
     */
    AtGroupEntity getByIdCache(Integer groupId);

    /**
     * 错误重试
     * @param groupId
     * @return
     */
    Boolean errRetryGroup(Integer groupId);

    /**
     * 拉群失败的，重新注册拉群账号
     * @param groupIdList
     */
    List<String> groupFailRegisterAgains(List<Integer> groupIdList);

    /**
     * 数据汇总
     * @param groupTaskIdList
     * @return
     */
    Map<Integer, AtGroupTaskVO> groupDataSummary(List<Integer> groupTaskIdList);

    Boolean startGroup(List<Integer> list);

    /**
     * 获取真实群名称
     * @param atGroup
     */
    void getRealGroupName(AtGroupDTO atGroup);

    /**
     * 推动一下拉群加粉数据
     * @param groupIdList
     * @return
     */
    Boolean pushGroupSubtask(List<Integer> groupIdList);
}

