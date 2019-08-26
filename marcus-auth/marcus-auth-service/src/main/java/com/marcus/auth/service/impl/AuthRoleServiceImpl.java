package com.marcus.auth.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marcus.auth.contants.AuthContants;
import com.marcus.auth.entity.AuthPermission;
import com.marcus.auth.entity.AuthRole;
import com.marcus.auth.entity.AuthRolePermission;
import com.marcus.auth.entity.AuthUserRole;
import com.marcus.auth.mapper.AuthPermissionMapper;
import com.marcus.auth.mapper.AuthRoleMapper;
import com.marcus.auth.mapper.AuthRolePermissionMapper;
import com.marcus.auth.mapper.AuthUserRoleMapper;
import com.marcus.auth.service.AuthPermissionService;
import com.marcus.auth.service.AuthRolePermissionService;
import com.marcus.auth.service.AuthRoleService;
import com.marcus.auth.vo.AuthPermissionVo;
import com.marcus.auth.vo.AuthRoleVo;
import com.marcus.base.bean.PageBean;
import com.marcus.base.exception.BusinessException;
import com.marcus.base.vo.ResultMessage;
import com.marcus.base.vo.SelectBean;
import com.marcus.core.utils.EntityUtil;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName AuthRoleServiceImpl
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/10 22:47
 * @Version 1.0
 **/
@Service
@Transactional
public class AuthRoleServiceImpl implements AuthRoleService {

    @Resource
    private AuthRoleMapper authRoleMapper;
    @Resource
    private AuthPermissionMapper authPermissionMapper;
    @Resource
    private AuthRolePermissionMapper authRolePermissionMapper;
    @Resource
    private AuthUserRoleMapper authUserRoleMapper;
    @Autowired
    private AuthRolePermissionService authRolePermissionService;
    @Autowired
    private AuthPermissionService authPermissionService;

    @Override
    public void initRoleData(AuthRoleVo authRoleVo) {
        AuthRole authRole = authRoleMapper.selectOne(new QueryWrapper<AuthRole>().eq("code", authRoleVo.getCode()));
        if (Objects.isNull(authRole)){
            // 保存角色
            authRole = new AuthRole();
            EntityUtil.copyPropertiesIgnoreNull(authRoleVo, authRole);
            authRoleMapper.insert(authRole);
            List<AuthPermission> authPermissionList = null;
            QueryWrapper<AuthPermission> condition = new QueryWrapper<>();
            condition.notIn("code", "AuthManager","AuthUser","AuthRole");
            if ("administrator".equals(authRole.getCode())){
                // 管理员角色
                // 管理员获取所有权限
                authPermissionList = authPermissionMapper.selectList(condition);
                for (AuthPermission authPermission : authPermissionList){
                    AuthRolePermission authRolePermission = new AuthRolePermission();
                    authRolePermission.setPermissionId(authPermission.getId());
                    authRolePermission.setRoleId(authRole.getId());
                    authRolePermissionMapper.insert(authRolePermission);
                }
            }
        }
    }

    @Override
    public AuthRoleVo save(AuthRoleVo authRoleVo) {
        AuthRole authRole = new AuthRole();
        EntityUtil.copyPropertiesIgnoreNull(authRoleVo, authRole);
        if (Objects.isNull(authRoleVo.getId())){
            // 新增
            authRoleMapper.insert(authRole);
        } else {
            // 编辑
            authRoleMapper.updateById(authRole);
        }
        // 处理关联权限
        List<Long> permissionIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(authRoleVo.getPermissionIds())){
            permissionIdList = Arrays.asList(authRoleVo.getPermissionIds().split(",")).stream()
                    .map(permissionId -> Long.parseLong(permissionId.trim())).collect(Collectors.toList());
        }
        authRolePermissionService.saveRolePermission(authRole.getId(), permissionIdList);
        authRoleVo.setId(authRole.getId());
        return authRoleVo;
    }

    @Override
    public PageBean list(AuthRoleVo vo, PageBean pageBean) {
        QueryWrapper<AuthRole> condition = new QueryWrapper<>();
        condition.like(StringUtils.isNotBlank(vo.getCode()),"code", vo.getCode())
                .like(StringUtils.isNotBlank(vo.getName()),"name", vo.getName())
                .eq("delStatus", 0);
        Page<AuthRole> authRolePage = new Page<>(pageBean.getOffset(), pageBean.getLimit());
        IPage<AuthRole> authRoleIPage = authRoleMapper.selectPage(authRolePage, condition);
        pageBean.setTotal(authRoleIPage.getTotal());
        pageBean.setRows(authRoleIPage.getRecords());
        return pageBean;
    }

    @Override
    public void delete(Long id) {
        // 判断是否有用户在使用此角色，若在使用则不能删除
        Integer count = authUserRoleMapper.selectCount(new QueryWrapper<AuthUserRole>().eq("roleId", id));
        if (count > 0){
            throw new BusinessException(ResultMessage.FAIL, "角色已经在使用中，不能删除！");
        }
        // 先根据角色id删除角色权限关联的中间表
        authRolePermissionMapper.delete(new QueryWrapper<AuthRolePermission>().eq("roleId", id));
        // 删除角色
        authRoleMapper.deleteById(id);
    }

    @Override
    public AuthRoleVo getByCode(String code) {
        AuthRoleVo authRoleVo = null;
        AuthRole authRole = authRoleMapper.selectOne(new QueryWrapper<AuthRole>().eq("code", code));
        if (Objects.nonNull(authRole)){
            authRoleVo = new AuthRoleVo();
            EntityUtil.copyPropertiesIgnoreNull(authRole, authRoleVo);
        }
        return authRoleVo;
    }

    @Override
    public AuthRoleVo getByName(String name) {
        AuthRoleVo authRoleVo = null;
        AuthRole authRole = authRoleMapper.selectOne(new QueryWrapper<AuthRole>().eq("name", name));
        if (Objects.nonNull(authRole)){
            authRoleVo = new AuthRoleVo();
            EntityUtil.copyPropertiesIgnoreNull(authRole, authRoleVo);
        }
        return authRoleVo;
    }

    @Override
    public List<SelectBean> getRoleSelectList(Long userId) {
        // 获取用户选择的角色id集合
        List<Long> selectRoleIdList = authUserRoleMapper.getRoleIdsByUserId(userId);
        // 获取全部的角色集合
        List<AuthRole> allAuthRoleList = authRoleMapper.selectList(new QueryWrapper<>());
        List<SelectBean> selectBeanList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allAuthRoleList)){
            allAuthRoleList.forEach(authRole -> {
                SelectBean selectBean = new SelectBean();
                selectBean.setValue(authRole.getId());
                selectBean.setText(authRole.getName());
                if (!CollectionUtils.isEmpty(selectRoleIdList) && selectRoleIdList.contains(authRole.getId())){
                    selectBean.setSelected(true);
                } else {
                    selectBean.setSelected(false);
                }
                selectBeanList.add(selectBean);
            });
        }
        return selectBeanList;
    }

    @Override
    public ResultMessage getRolePermissionList(@NonNull Long userId, Long roleId, Integer isSuperuser) {
        ResultMessage result = ResultMessage.resultSuccess();
        // 获取当前登录用户的全部菜单权限
        List<AuthPermissionVo> allMenus = authPermissionService.getAllMenus(userId, isSuperuser);
        // 获取权限角色的菜单权限
        List<String> roleMenuCodes = authPermissionMapper.getRoleMenuCodes(roleId);
        JSONObject jsonObject = null;
        JSONArray jsonArray = new JSONArray();
        for (AuthPermissionVo menu : allMenus) {
            // 根节点和权限管理菜单不显示
            if (AuthContants.RESOURCE_TYPE_SYSTEM.equals(menu.getResourceType()) || menu.getPermission().startsWith("auth:")){
                continue;
            }
            jsonObject = new JSONObject();
            jsonObject.put("id", menu.getId());
            jsonObject.put("text", menu.getName());
            // 判断角色权限是否选中
            if (!CollectionUtils.isEmpty(roleMenuCodes) && roleMenuCodes.contains(menu.getCode())){
                jsonObject.put("checked", "1");
            }
            jsonArray.add(jsonObject);
        }
        result.setData(jsonArray);
        return result;
    }

}
