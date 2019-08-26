package com.marcus.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marcus.auth.entity.AuthUser;
import com.marcus.auth.mapper.AuthUserMapper;
import com.marcus.auth.service.AuthUserRoleService;
import com.marcus.auth.service.AuthUserService;
import com.marcus.auth.vo.AuthUserVo;
import com.marcus.auth.vo.ModifyPasswordVo;
import com.marcus.base.bean.PageBean;
import com.marcus.base.exception.BusinessException;
import com.marcus.base.vo.ResultMessage;
import com.marcus.core.utils.EntityUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName AuthUserServiceImpl
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/10 22:46
 * @Version 1.0
 **/
@Service
@Transactional
public class AuthUserServiceImpl implements AuthUserService {
    @Resource
    private AuthUserMapper authUserMapper;
    @Autowired
    private AuthUserRoleService authUserRoleService;

    @Override
    public AuthUserVo saveUser(AuthUserVo authUserVo) {
        AuthUser authUser = new AuthUser();
        EntityUtil.copyPropertiesIgnoreNull(authUserVo, authUser);
        if (Objects.isNull(authUserVo.getId())){
            // 新增用户默认初始化密码为123456
            authUser.setLoginPwd(DigestUtils.md5Hex("123456"));
            // 新增
            authUserMapper.insert(authUser);
        } else {
            // 编辑
//            authUser = authUserMapper.selectById(authUserVo.getId());
            // 先保存原来的登录密码
//            String oldLoginPwd = authUser.getLoginPwd();
            // 编辑时如果没有修改密码，则包留原来的密码
//            if (StringUtils.isBlank(authUserVo.getLoginPwd())){
//                authUser.setLoginPwd(oldLoginPwd);
//            }
            authUserMapper.updateById(authUser);
        }
        // 处理关联角色
        List<Long> roleIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(authUserVo.getRoleIds())){
            roleIdList = Arrays.asList(authUserVo.getRoleIds().split(",")).stream()
                    .map(roleId -> Long.parseLong(roleId.trim())).collect(Collectors.toList());
        }
        authUserRoleService.saveUserRole(authUser.getId(), roleIdList);
        authUserVo.setId(authUser.getId());
        return authUserVo;
    }

    @Override
    public void initUserData(AuthUserVo authUserVo) {
        AuthUser authUser = authUserMapper.selectOne(new QueryWrapper<AuthUser>().eq("loginName", authUserVo.getLoginName()));
        if (Objects.isNull(authUser)){
            authUser = new AuthUser();
            EntityUtil.copyPropertiesIgnoreNull(authUserVo, authUser);
            authUserMapper.insert(authUser);
        }
    }

    @Override
    public AuthUserVo getByLoginName(String loginName) {
        AuthUserVo authUserVo = null;
        AuthUser authUser = authUserMapper.selectOne(new QueryWrapper<AuthUser>().eq("loginName", loginName));
        if (Objects.nonNull(authUser)){
            authUserVo = new AuthUserVo();
            authUserVo = EntityUtil.copyPropertiesIgnoreNull(authUser, authUserVo);
        }
        return authUserVo;
    }

    @Override
    public PageBean getUserList(AuthUserVo vo, PageBean pageBean) {

        Page<AuthUser> authUserPage = new Page<>(pageBean.getOffset(), pageBean.getLimit());
        QueryWrapper<AuthUser> condition = new QueryWrapper<>();

        condition.like(StringUtils.isNoneBlank(vo.getLoginName()),"loginName",vo.getLoginName());
        condition.eq(StringUtils.isNoneBlank(vo.getEmail()),"email",vo.getEmail());
        condition.eq(StringUtils.isNoneBlank(vo.getPhone()),"phone",vo.getPhone());
        condition.eq("delStatus",0);
        condition.orderByDesc("CreateTime");
        IPage<AuthUser> authUserIPage = authUserMapper.selectPage(authUserPage, condition);

        pageBean.setTotal(authUserIPage.getTotal());
        pageBean.setRows(authUserIPage.getRecords());


        return pageBean;
    }

    @Override
    public int modifyPassword(ModifyPasswordVo vo) {

        AuthUser authUser = authUserMapper.selectById(vo.getId());

        if (authUser == null)
        {
            throw new BusinessException(999,"未查询到对应用户");
        }
        if (!authUser.getLoginPwd().equals(vo.getCurrentPassword()))
        {
            throw new BusinessException(999,"密码不符");
        }
        authUser.setLoginPwd(vo.getNewPassword());
        return authUserMapper.updateById(authUser);

    }

    @Override
    public void delete(Long id, Long loginUserId) {
        AuthUser authUser = authUserMapper.selectById(id);
        if (id.equals(loginUserId)){
            // 不能删除自己
            throw new BusinessException(ResultMessage.FAIL, "不能删除自己");
        }
        else if (authUser.getInitFlag() == 1){
            // 初始化用户不让删除
            throw new BusinessException(ResultMessage.FAIL, "初始化用户不能删除");
        }
        // 先根据用户id删除用户角色关联的中间表
        authUserRoleService.deleteByUserId(id);
        // 逻辑删除
        authUser.setDelStatus(1);
        authUserMapper.updateById(authUser);
    }

    @Override
    public boolean duplicateName(String userName) {
        QueryWrapper<AuthUser> query = new QueryWrapper<>();

        query.eq("loginName",userName);
        return authUserMapper.selectCount(query) > 0;
    }


}
