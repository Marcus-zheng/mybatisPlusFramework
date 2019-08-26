package com.marcus.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.marcus.auth.service.AuthPermissionService;
import com.marcus.auth.vo.AuthPermissionVo;
import com.marcus.base.bean.SecuritySubject;
import com.marcus.base.controller.BaseController;
import com.marcus.base.vo.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassName AuthPermissionController
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/10 23:01
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "/authPermission")
public class AuthPermissionController extends BaseController {

    @Autowired
    private AuthPermissionService authPermissionService;

    @RequestMapping(value = "/getMenus")
    @ResponseBody
    public ResultMessage getMenus(){
        SecuritySubject loginUser = getLoginSubject();
        List<AuthPermissionVo> allMenus = authPermissionService.getAllMenus(loginUser.getUserId(), loginUser.getIsSuperuser());
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(allMenus));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("menus", jsonArray);
        jsonObject.put("userName", loginUser.getLoginName());
        jsonObject.put("id",loginUser.getUserId());
        return ResultMessage.resultSuccess(jsonObject);
    }

}
