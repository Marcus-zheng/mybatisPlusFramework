package com.marcus.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marcus.auth.contants.AuthContants;
import com.marcus.auth.entity.AuthPermission;
import com.marcus.auth.mapper.AuthPermissionMapper;
import com.marcus.auth.service.AuthPermissionService;
import com.marcus.auth.vo.AuthPermissionVo;
import com.marcus.core.utils.EntityUtil;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName AuthPermissionServiceImpl
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/10 22:47
 * @Version 1.0
 **/
@Service
@Transactional
public class AuthPermissionServiceImpl implements AuthPermissionService {

    @Resource
    private AuthPermissionMapper authPermissionMapper;

    @Override
    public AuthPermissionVo initPermission(AuthPermissionVo vo) {
        AuthPermission authPermission = authPermissionMapper
                .selectOne(new QueryWrapper<AuthPermission>().eq("code", vo.getCode()));
        if (Objects.isNull(authPermission)){
            return save(vo);
        } else {
            vo.setId(authPermission.getId());
            return vo;
        }
    }

    @Override
    public AuthPermissionVo save(AuthPermissionVo authPermissionVo) {
        AuthPermission authPermission = new AuthPermission();
        EntityUtil.copyPropertiesIgnoreNull(authPermissionVo, authPermission);
        if (Objects.isNull(authPermissionVo.getId())){
            // 新增
            authPermissionMapper.insert(authPermission);
        } else {
            // 编辑
            authPermissionMapper.updateById(authPermission);
        }
        authPermissionVo.setId(authPermission.getId());
        return authPermissionVo;
    }

    @Override
    public List<AuthPermissionVo> getSystemMenus(@NonNull Long userId) {
        List<AuthPermission> systemMenus = authPermissionMapper.getSystemMenus(userId, AuthContants.RESOURCE_TYPE_SYSTEM);
        if (!CollectionUtils.isEmpty(systemMenus)) {
            // 对i系统首页菜单列表根据orderNo进行升序排序
            Collections.sort(systemMenus, (o1, o2) -> {
                if (o1.getOrderNo() > o2.getOrderNo()) {
                    return 1;
                }
                return -1;
            });
        }
        return EntityUtil.copyListProperties(systemMenus, AuthPermissionVo.class);
    }

    @Override
    public List<AuthPermissionVo> getAuthPermissionByParentId(Long userId, String resourceType, Long parentId) {
        List<AuthPermission> authPermissionList = authPermissionMapper.getChildMenus(userId, resourceType, parentId);
        return EntityUtil.copyListProperties(authPermissionList, AuthPermissionVo.class);
    }

    @Override
    public List<AuthPermissionVo> getAllMenus(@NonNull Long userId, Integer isSuperuser) {
        List<AuthPermissionVo> allMenus = new ArrayList<>();
        if (isSuperuser == 1){
            // 超级用户则加载所有菜单
            List<AuthPermission> authPermissionList = authPermissionMapper.selectList(new QueryWrapper<>());
            allMenus = EntityUtil.copyListProperties(authPermissionList, AuthPermissionVo.class);
        } else {
            // 获取系统一级菜单
            List<AuthPermissionVo> systemMenus = getSystemMenus(userId);
            if (!CollectionUtils.isEmpty(systemMenus)){
                for (AuthPermissionVo systemMenu : systemMenus) {
                    allMenus.add(systemMenu);
                    // 根据一级菜单获取二级菜单
                    List<AuthPermissionVo> parentMenus = getAuthPermissionByParentId(userId,
                            AuthContants.RESOURCE_TYPE_MENU, systemMenu.getId());
                    if (!CollectionUtils.isEmpty(parentMenus)){
                        // 根据二级菜单获取三级菜单
                        for (AuthPermissionVo parentMenu : parentMenus) {
                            allMenus.add(parentMenu);
                            List<AuthPermissionVo> menus = getAuthPermissionByParentId(userId,
                                    AuthContants.RESOURCE_TYPE_MENU, parentMenu.getId());
                            if (!CollectionUtils.isEmpty(menus)){
                                allMenus.addAll(menus);
                            }
                        }
                    }
                }
            }
        }
        return allMenus;
    }
}
