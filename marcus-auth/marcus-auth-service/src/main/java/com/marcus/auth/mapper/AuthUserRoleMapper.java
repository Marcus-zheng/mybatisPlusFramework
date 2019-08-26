package com.marcus.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marcus.auth.entity.AuthUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthUserRoleMapper extends BaseMapper<AuthUserRole> {

    @Select("SELECT roleId FROM auth_user_role where userId = #{userId}")
    List<Long> getRoleIdsByUserId(@Param("userId") Long userId);
}
