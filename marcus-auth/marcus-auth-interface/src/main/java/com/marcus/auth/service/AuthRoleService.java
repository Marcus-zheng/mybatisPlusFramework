package com.marcus.auth.service;

import com.marcus.auth.vo.AuthRoleVo;
import com.marcus.base.bean.PageBean;
import com.marcus.base.vo.ResultMessage;
import com.marcus.base.vo.SelectBean;
import lombok.NonNull;

import java.util.List;

public interface AuthRoleService {

    /**
     * @Description 初始化角色
     * @Author Marcus.zheng
     * @Date 2019/7/16 14:49
     * @Param authRoleVo
     * @Return void
     */
    void initRoleData(AuthRoleVo authRoleVo);

    /**
     * @Description 保存角色
     * @Author Marcus.zheng
     * @Date 2019/7/16 14:50
     * @Param authRoleVo
     * @Return com.zzl.auth.vo.AuthRoleVo
     */
    AuthRoleVo save(AuthRoleVo authRoleVo);

    /**
     * @Description 获取角色列表数据
     * @Author Marcus.zheng
     * @Date 2019/7/16 14:51
     * @Param vo
     * @Param pageBean
     * @Return com.zzl.common.vo.PageBean
     */
    PageBean list(AuthRoleVo vo, PageBean pageBean);

    /**
     * @Description 根据id删除角色
     * @Author Marcus.zheng
     * @Date 2019/7/16 14:57
     * @Param id
     * @Return void
     */
    void delete(@NonNull Long id);

    /**
     * @Description 根据角色编码获取角色
     * @Author Marcus.zheng
     * @Date 2019/7/16 15:30
     * @Param code
     * @Return com.zzl.auth.vo.AuthRoleVo
     */
    AuthRoleVo getByCode(String code);

    /**
     * @Description 根据角色名称获取角色
     * @Author Marcus.zheng
     * @Date 2019/7/16 15:30
     * @Param name
     * @Return com.zzl.auth.vo.AuthRoleVo
     */
    AuthRoleVo getByName(String name);

    /**
     * @Description: 获取角色的下拉列表
     * @author: Marcus.zheng
     * @date:  2019/7/14 11:39
     * @param: []
     * @return: java.util.List<com.zzl.base.vo.SelectBean>
     **/
    List<SelectBean> getRoleSelectList(Long userId);

    /**
     * @Description 根据角色id获取权限列表
     * @Author Marcus.zheng
     * @Date 2019/8/26 15:52
     * @Param userId
     * @Param roleId
     * @Param isSuperuser
     * @Return com.marcus.base.vo.ResultMessage
     */
    ResultMessage getRolePermissionList(@NonNull Long userId, Long roleId, Integer isSuperuser);
}
