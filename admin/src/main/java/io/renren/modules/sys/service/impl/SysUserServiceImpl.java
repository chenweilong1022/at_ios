package io.renren.modules.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.Constant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.validator.Assert;
import io.renren.modules.sys.dao.SysUserDao;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysRoleService;
import io.renren.modules.sys.service.SysUserRoleService;
import io.renren.modules.sys.service.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;

	@Override
	public PageUtils queryPage(SysUserEntity sysUserEntity) {
        IPage<SysUserEntity> page = baseMapper.selectPage(
				new Query<SysUserEntity>(sysUserEntity).getPage(),
				new QueryWrapper<SysUserEntity>().lambda()
						.like(StringUtils.isNotBlank(sysUserEntity.getUsername()),SysUserEntity::getUsername, sysUserEntity.getUsername())
						.eq(ObjectUtil.isNotNull(sysUserEntity.getCreateUserId()),SysUserEntity::getCreateUserId, sysUserEntity.getCreateUserId())
		);
		return new PageUtils(page);
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return baseMapper.queryByUserName(username);
	}

	@Override
	public List<SysUserEntity> queryBySearchWord(String searchWord, Long sysUserId) {
		return baseMapper.queryBySearchWord(searchWord, sysUserId);
	}

	@Override
	@Transactional
	public boolean save(SysUserEntity user) {
		//校验username是否重复
		SysUserEntity sysUserEntity = this.queryByUserName(user.getUsername());
		Assert.isTrue(ObjectUtil.isNotNull(sysUserEntity), "用户名已存在，请更换");

		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		baseMapper.insert(user);
		
		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
		return true;
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		this.updateById(user);
		
		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	public void deleteBatch(Long[] userId) {
		this.removeByIds(Arrays.asList(userId));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(newPassword);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().lambda()
                        .eq(SysUserEntity::getUserId, userId)
                        .eq(SysUserEntity::getPassword, password));
	}

	@Override
	public List<SysUserEntity> queryByUserIdList(List<Long> userIdList) {
		if (CollectionUtil.isEmpty(userIdList)) {
			return Collections.emptyList();
		}
		return baseMapper.queryByUserIdList(userIdList);
	}

	@Override
	public Map<Long, String> queryUserNameByUserIdList(List<Long> userIdList) {
		List<SysUserEntity> sysUserEntityList = this.queryByUserIdList(userIdList);
		if (CollectionUtil.isEmpty(sysUserEntityList)) {
			return Collections.emptyMap();
		}
		return 	sysUserEntityList.stream().collect(Collectors
				.toMap(SysUserEntity::getUserId, SysUserEntity::getUsername, (v1, v2) -> v1));
	}

	/**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUserEntity user){
		if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
			return;
		}
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		if(user.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}
		
		//查询用户创建的角色列表
		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

		//判断是否越权
		if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new RRException("新增用户所选角色，不是本人创建");
		}
	}
}
