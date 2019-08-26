package com.marcus.auth.vo;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ModifyPasswordVo {
//    @NotBlank(message = "新密码不能为空")
    private String newPassword;

//    @NotBlank(message = "旧密码不能为空")
    private String currentPassword;

//    @NotNull(message = "用户ID不能为空")
    private Long id;
}
