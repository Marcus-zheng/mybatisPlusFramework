package com.marcus.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marcus.auth.entity.AuthUserRole;
import com.marcus.auth.mapper.AuthUserRoleMapper;
import com.marcus.auth.service.AuthUserRoleService;
import com.marcus.auth.vo.AuthUserRoleVo;
import com.marcus.core.utils.EntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author Marcus.zheng
 * @Date 2019/7/12 14:17
 **/
@Service
@Transactional
public class AuthUserRoleServiceImpl implements AuthUserRoleService {

    @Resource
    private AuthUserRoleMapper authUserRoleMapper;

    @Override
    public void saveUserRole(Long userId, List<Long> roleIdList) {
        // 根据用户id获取此用户的角色id集合
        List<Long> oldRoleIdList = authUserRoleMapper.getRoleIdsByUserId(userId);
        roleIdList.forEach(roleId -> {
            // 不存在则新增
            if (!oldRoleIdList.contains(roleId)){
                AuthUserRoleVo authUserRoleVo = new AuthUserRoleVo();
                authUserRoleVo.setUserId(userId);
                authUserRoleVo.setRoleId(roleId);
                saveUserRole(authUserRoleVo);
            }
        });
        // 获取要删除的角色ids
        oldRoleIdList.removeAll(roleIdList);
        if (!CollectionUtils.isEmpty(oldRoleIdList)){
            QueryWrapper<AuthUserRole> condition = new QueryWrapper<>();
            condition.eq("userId", userId)
                    .in("roleId", oldRoleIdList);
            authUserRoleMapper.delete(condition);
        }
    }

    @Override
    public AuthUserRoleVo saveUserRole(AuthUserRoleVo vo) {
        AuthUserRole authUserRole = new AuthUserRole();
        EntityUtil.copyPropertiesIgnoreNull(vo, authUserRole);
        if (Objects.isNull(vo.getId())){
            // 新增
            authUserRoleMapper.insert(authUserRole);
        } else {
            // 编辑
            authUserRoleMapper.updateById(authUserRole);
        }
        vo.setId(authUserRole.getId());
        return vo;
    }

    @Override
    public void deleteByUserId(Long userId) {
        authUserRoleMapper.delete(new QueryWrapper<AuthUserRole>().eq("userId", userId));
    }
}
