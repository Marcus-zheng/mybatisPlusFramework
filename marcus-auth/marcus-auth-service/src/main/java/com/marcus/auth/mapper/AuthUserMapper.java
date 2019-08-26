package com.marcus.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marcus.auth.entity.AuthUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUser> {
}
