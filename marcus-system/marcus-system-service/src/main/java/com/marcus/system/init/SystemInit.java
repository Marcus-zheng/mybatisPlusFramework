package com.marcus.system.init;

import com.marcus.auth.contants.AuthContants;
import com.marcus.auth.service.AuthPermissionService;
import com.marcus.auth.service.AuthRoleService;
import com.marcus.auth.service.AuthUserService;
import com.marcus.auth.vo.AuthPermissionVo;
import com.marcus.auth.vo.AuthRoleVo;
import com.marcus.auth.vo.AuthUserVo;
import com.marcus.system.service.SystemMailService;
import com.marcus.system.service.SystemParamService;
import com.marcus.system.vo.SystemParamVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName SystemInit
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/23 10:37
 * @Version 1.0
 **/
@Component
@Order(value=99)
public class SystemInit implements CommandLineRunner {
    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private AuthRoleService authRoleService;
    @Autowired
    private AuthPermissionService authPermissionService;
    @Autowired
    private SystemParamService systemParamService;
    @Autowired
    private SystemMailService systemMailService;

    @Override
    public void run(String... args) throws Exception {
        // 初始化邮箱设置
        initMail();

        boolean alreadyInit = systemParamService.getAlreadyInitModule("SystemInit");
        if (!alreadyInit){
            // 初始化权限
            initAuthPermission();
            // 初始化用户权限管理角色
            initAuthRole();
            // 初始化用户
            initAuthUser();
            // 初始化系统参数
            initParam();
            // 设置初始化标识
            systemParamService.setAlreadyInitModule("SystemInit");
        }
    }

    // 初始化权限
    private void initAuthPermission(){
        AuthPermissionVo systemVo = null;
        AuthPermissionVo parentMenuVo = null;
        AuthPermissionVo menuVo = null;
//        AuthPermissionVo buttonVo = null;

        // 系统管理
        systemVo = new AuthPermissionVo("System", "系统管理", "system",
                AuthContants.RESOURCE_TYPE_SYSTEM, 1);
        systemVo = authPermissionService.initPermission(systemVo);

        // 权限管理菜单
        parentMenuVo = new AuthPermissionVo("AuthManager", "权限管理", "auth:manager",
                AuthContants.RESOURCE_TYPE_MENU, 1);
        parentMenuVo.setParentId(systemVo.getId());
        parentMenuVo = authPermissionService.initPermission(parentMenuVo);

        // 用户
        menuVo = new AuthPermissionVo("AuthUser", "用户", "auth:user",
                AuthContants.RESOURCE_TYPE_MENU, 1);
        menuVo.setParentId(parentMenuVo.getId());
        menuVo.setResourceLink("/authUser/index");
        menuVo = authPermissionService.initPermission(menuVo);
        /*// 用户- 刷新
        buttonVo = new AuthPermissionVo("AuthUserRefresh", "刷新", "auth:user:refresh",
                AuthContants.RESOURCE_TYPE_BUTTON, 1);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 用户- 新增
        buttonVo = new AuthPermissionVo("AuthUserAdd", "新增", "auth:user:add",
                AuthContants.RESOURCE_TYPE_BUTTON, 2);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 用户- 编辑
        buttonVo = new AuthPermissionVo("AuthUserEdit", "编辑", "auth:user:edit",
                AuthContants.RESOURCE_TYPE_BUTTON, 3);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 用户- 删除
        buttonVo = new AuthPermissionVo("AuthUserDel", "删除", "auth:user:del",
                AuthContants.RESOURCE_TYPE_BUTTON, 4);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);*/

        // 角色
        menuVo = new AuthPermissionVo("AuthRole", "角色", "auth:role",
                AuthContants.RESOURCE_TYPE_MENU, 2);
        menuVo.setParentId(parentMenuVo.getId());
        menuVo.setResourceLink("/authRole/index");
        menuVo = authPermissionService.initPermission(menuVo);
        /*// 角色- 刷新
        buttonVo = new AuthPermissionVo("AuthRoleRefresh", "刷新", "auth:role:refresh",
                AuthContants.RESOURCE_TYPE_BUTTON, 1);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 角色- 新增
        buttonVo = new AuthPermissionVo("AuthRoleAdd", "新增", "auth:role:add",
                AuthContants.RESOURCE_TYPE_BUTTON, 2);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 角色- 编辑
        buttonVo = new AuthPermissionVo("AuthRoleEdit", "编辑", "auth:role:edit",
                AuthContants.RESOURCE_TYPE_BUTTON, 3);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);
        // 角色- 删除ai
        buttonVo = new AuthPermissionVo("AuthRoleDel", "删除", "auth:role:del",
                AuthContants.RESOURCE_TYPE_BUTTON, 4);
        buttonVo.setParentId(menuVo.getId());
        authPermissionService.initPermission(buttonVo);*/

        // 邮箱管理
        parentMenuVo = new AuthPermissionVo("SystemMail", "邮箱管理", "base:mail",
                AuthContants.RESOURCE_TYPE_MENU, 2);
        parentMenuVo.setParentId(systemVo.getId());
        parentMenuVo.setResourceLink("/systemMail/index");
        parentMenuVo = authPermissionService.initPermission(parentMenuVo);

        // VIP用户管理
//        parentMenuVo = new AuthPermissionVo("VipUser", "VIP用户管理", "vip:user",
//                AuthContants.RESOURCE_TYPE_MENU, 3);
//        parentMenuVo.setParentId(systemVo.getId());
//        parentMenuVo.setResourceLink("/vipUser/index");
//        parentMenuVo = authPermissionService.initPermission(parentMenuVo);
//
//        // 广告管理
//        parentMenuVo = new AuthPermissionVo("AdBanner", "广告管理", "ad:banner",
//                AuthContants.RESOURCE_TYPE_MENU, 4);
//        parentMenuVo.setParentId(systemVo.getId());
//        parentMenuVo.setResourceLink("/adBanner/index");
//        parentMenuVo = authPermissionService.initPermission(parentMenuVo);
//
    }

    // 初始化角色
    private void initAuthRole(){
        authRoleService.initRoleData(new AuthRoleVo("administrator", "管理员", "administrator"));
    }

    // 初始化用户
    private void initAuthUser(){
        AuthUserVo authUserVo = new AuthUserVo();
        authUserVo.setLoginName("admin")
                .setLoginPwd(DigestUtils.md5Hex("admin"))
                .setName("admin")
                .setIsSuperuser(1)
                .setInitFlag(1);
        authUserService.initUserData(authUserVo);
    }

    // 初始化系统参数
    private void initParam(){
        // 邮箱服务器参数初始化
        SystemParamVo systemParamVo = new SystemParamVo("system.mailServerHost", "", "邮箱服务器地址");
        systemParamService.initData(systemParamVo);

        systemParamVo = new SystemParamVo("system.mailServerPort", "465", "邮件服务器端口");
        systemParamService.initData(systemParamVo);

        systemParamVo = new SystemParamVo("system.mailUserName", "", "邮箱用户名");
        systemParamService.initData(systemParamVo);

        systemParamVo = new SystemParamVo("system.mailPassword", "", "邮箱密码");
        systemParamService.initData(systemParamVo);
    }

    /**
     * @Description 初始化邮箱设置
     * @Author Marcus.zheng
     * @Date 2019/7/22 17:42
     * @Return void
     */
    private void initMail() {
        systemMailService.initMail();
    }
}
