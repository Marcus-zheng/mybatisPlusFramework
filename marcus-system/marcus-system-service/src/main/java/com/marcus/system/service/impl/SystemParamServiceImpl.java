package com.marcus.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marcus.core.utils.EntityUtil;
import com.marcus.system.entity.SystemParam;
import com.marcus.system.mapper.SystemParamMapper;
import com.marcus.system.service.SystemParamService;
import com.marcus.system.vo.SystemParamVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName BaseParamServiceImpl
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/21 18:31
 * @Version 1.0
 **/
@Service
@Transactional
public class SystemParamServiceImpl implements SystemParamService {
    @Resource
    private SystemParamMapper systemParamMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public SystemParamVo save(SystemParamVo vo) {
        SystemParam systemParam = new SystemParam();
        EntityUtil.copyPropertiesIgnoreNull(vo, systemParam);
        if (Objects.isNull(vo.getId())){
            // 新增
            systemParamMapper.insert(systemParam);
        } else {
            // 编辑
            systemParamMapper.updateById(systemParam);
        }
        stringRedisTemplate.opsForValue().set("system:param:" + systemParam.getParamName(), systemParam.getParamValue());
        vo.setId(systemParam.getId());
        return vo;
    }

    @Override
    public void saveValueByName(String paramName, String paramValue) {
        SystemParam systemParam = systemParamMapper.selectOne(new QueryWrapper<SystemParam>()
                .eq("paramName", paramName));
        if (Objects.isNull(systemParam)){
            // 新增
            systemParam = new SystemParam();
            systemParam.setParamName(paramName);
            systemParam.setParamValue(paramValue);
            systemParamMapper.insert(systemParam);
        } else {
            // 编辑
            systemParam.setParamValue(paramValue);
            systemParamMapper.updateById(systemParam);
        }
        stringRedisTemplate.opsForValue().set("system:param:" + paramName, paramValue);
    }

    @Override
    public void initData(SystemParamVo vo) {
        SystemParam systemParam = systemParamMapper.selectOne(new QueryWrapper<SystemParam>()
                .eq("paramName", vo.getParamName()));
        if (Objects.isNull(systemParam)){
            save(vo);
        }
    }

    @Override
    public Map<String, String> getParamsByModule(String module) {
        List<SystemParam> systemParamList = systemParamMapper.selectList(new QueryWrapper<SystemParam>()
                .like("paramName", module));
        Map<String, String> map = new HashMap<>();
        systemParamList.forEach(systemParam -> {
            map.put(systemParam.getParamName(), systemParam.getParamValue());
        });
        return map;
    }

    @Override
    public String getValueByName(String paramName) {
        String value = stringRedisTemplate.opsForValue().get("system:param:" + paramName);
        if (StringUtils.isBlank(value)){
            SystemParam systemParam = systemParamMapper.selectOne(new QueryWrapper<SystemParam>()
                    .eq("paramName", paramName));
            if (Objects.nonNull(systemParam)){
                value = systemParam.getParamValue();
                stringRedisTemplate.opsForValue().set("system:param:" + paramName, value);
            }
        }
        return value;
    }

    @Override
    public void saveParams(Map<String, String> params) {
        params.forEach((name,value) -> {
            saveValueByName(name, value);
        });
    }

    @Override
    public void setAlreadyInitModule(String paramName) {
        SystemParam systemParam = systemParamMapper.selectOne(new QueryWrapper<SystemParam>()
                .eq("paramName", paramName));
        if (Objects.isNull(systemParam)){
            // 新增
            systemParam = new SystemParam();
            systemParam.setParamName(paramName);
            systemParam.setParamValue("true");
            systemParam.setDescription(paramName+" Module Init Flag");
            systemParamMapper.insert(systemParam);
        } else {
            // 编辑
            systemParam.setParamValue("true");
            systemParam.setDescription(paramName+" Module Init Flag");
            systemParamMapper.updateById(systemParam);
        }
    }

    @Override
    public boolean getAlreadyInitModule(String paramName) {
        SystemParam systemParam = systemParamMapper.selectOne(new QueryWrapper<SystemParam>()
                .eq("paramName", paramName));
        return Objects.nonNull(systemParam) && "true".equals(systemParam.getParamValue());
    }
}
