package com.marcus.auth.service;

import com.marcus.auth.vo.AuthUserRoleVo;
import lombok.NonNull;

import java.util.List;

public interface AuthUserRoleService {

    void saveUserRole(@NonNull Long userId, List<Long> roleIdList);

    AuthUserRoleVo saveUserRole(AuthUserRoleVo vo);

    void deleteByUserId(Long userId);
}
