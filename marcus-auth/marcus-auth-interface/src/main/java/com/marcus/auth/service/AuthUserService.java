package com.marcus.auth.service;


import com.marcus.auth.vo.AuthUserVo;
import com.marcus.auth.vo.ModifyPasswordVo;
import com.marcus.base.bean.PageBean;

public interface AuthUserService {

    AuthUserVo saveUser(AuthUserVo authUserVo);

    void initUserData(AuthUserVo authUserVo);

    AuthUserVo getByLoginName(String loginName);

    PageBean getUserList(AuthUserVo vo, PageBean pageBean);

    int modifyPassword(ModifyPasswordVo vo);

    void delete(Long id, Long loginUserId);

    boolean duplicateName(String userName);
}
