package com.marcus.auth.service;

import lombok.NonNull;

import java.util.List;

public interface AuthRolePermissionService {

    void saveRolePermission(@NonNull Long roleId, List<Long> permissionIdList);
}
