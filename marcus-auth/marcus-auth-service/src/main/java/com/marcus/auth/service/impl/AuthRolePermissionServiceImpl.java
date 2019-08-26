package com.marcus.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marcus.auth.entity.AuthRolePermission;
import com.marcus.auth.mapper.AuthRolePermissionMapper;
import com.marcus.auth.service.AuthRolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName AuthRolePermissionServiceImpl
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/13 22:38
 * @Version 1.0
 **/
@Service
@Transactional
public class AuthRolePermissionServiceImpl implements AuthRolePermissionService {

    @Resource
    private AuthRolePermissionMapper authRolePermissionMapper;

    @Override
    public void saveRolePermission(Long roleId, List<Long> permissionIdList) {
        // 根据角色id获取此角色的权限id集合
        List<Long> oldPermissionIdList = authRolePermissionMapper.getPermissionIdsByRoleId(roleId);
        permissionIdList.forEach(permissionId -> {
            // 不存在则新增
            if (!oldPermissionIdList.contains(permissionId)){
                AuthRolePermission authRolePermission = new AuthRolePermission();
                authRolePermission.setRoleId(roleId);
                authRolePermission.setPermissionId(permissionId);
                authRolePermissionMapper.insert(authRolePermission);
            }
        });
        // 获取需要删除的权限ids
        oldPermissionIdList.removeAll(permissionIdList);
        if (!CollectionUtils.isEmpty(oldPermissionIdList)){
            QueryWrapper<AuthRolePermission> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("roleId", roleId)
                    .in("permissionId", oldPermissionIdList);
            authRolePermissionMapper.delete(queryWrapper);
        }
    }
}
