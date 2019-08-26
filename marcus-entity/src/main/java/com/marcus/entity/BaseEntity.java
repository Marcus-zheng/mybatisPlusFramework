package com.marcus.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/26 9:48
 **/
@Setter
@Getter
@Accessors(chain = true)
public abstract class BaseEntity {

    // 主键id
    public Long id;
    // 创建时间
    protected Date createTime;
    // 更新时间
    protected Date updateTime;
    // 操作人id
    protected Long operatorId;
    // 操作人账号
    protected Long operatorName;
    // 版本号
    protected String opVersion;
}
