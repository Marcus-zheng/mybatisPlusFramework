package com.marcus.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.marcus.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName BaseParam
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/21 18:23
 * @Version 1.0
 **/
@TableName(value = "system_param")
@Getter
@Setter
@Accessors(chain=true)
public class SystemParam extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -2707562861149893522L;

    // 参数名称
    private String paramName;
    // 参数值
    private String paramValue;
    // 描述
    private String description;
}
