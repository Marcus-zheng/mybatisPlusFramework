package com.marcus.system.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName BaseParamVo
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/21 18:34
 * @Version 1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class SystemParamVo implements Serializable {
    private static final long serialVersionUID = 6939094693835136269L;

    // 主键id
    private Long id;
    // 参数名称
    private String paramName;
    // 参数值
    private String paramValue;
    // 描述
    private String description;

    public SystemParamVo(){
        super();
    }

    public SystemParamVo(String paramName, String paramValue, String description){
        super();
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.description = description;
    }
}
