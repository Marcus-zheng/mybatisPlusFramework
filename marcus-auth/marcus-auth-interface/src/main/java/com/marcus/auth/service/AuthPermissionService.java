package com.marcus.auth.service;

import com.marcus.auth.vo.AuthPermissionVo;
import lombok.NonNull;

import java.util.List;

public interface AuthPermissionService {

    AuthPermissionVo initPermission(AuthPermissionVo vo);

    AuthPermissionVo save(AuthPermissionVo authPermissionVo);

    /**
     * @Description 根据用户id获取系统菜单
     * @Author mingfa.zheng@zkteco.com
     * @Date 2019/7/15 15:59
     * @Param userId
     * @Return java.util.List<com.zzl.auth.vo.AuthPermissionVo>
     */
    List<AuthPermissionVo> getSystemMenus(@NonNull Long userId);

    /**
     * @Description: 根据parentId获取其底下的菜单
     * @author: Marcus.zheng
     * @date:  2019/7/14 10:39 
     * @param: [parentId]
     * @return: java.util.List<com.zzl.auth.entity.AuthPermission>
     **/
    List<AuthPermissionVo> getAuthPermissionByParentId(Long userId, String resourceType, Long parentId);

    /**
     * @Description 获取当前登录用户的菜单
     * @Author mingfa.zheng@zkteco.com
     * @Date 2019/7/15 15:58
     * @Param userId
     * @Return java.util.List<com.zzl.auth.vo.AuthPermissionVo>
     */
    List<AuthPermissionVo> getAllMenus(@NonNull Long userId, Integer isSuperuser);
}
