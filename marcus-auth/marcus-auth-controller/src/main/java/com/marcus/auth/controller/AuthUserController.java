package com.marcus.auth.controller;

import com.marcus.auth.service.AuthUserService;
import com.marcus.auth.vo.AuthUserVo;
import com.marcus.auth.vo.ModifyPasswordVo;
import com.marcus.base.bean.PageBean;
import com.marcus.base.bean.SecuritySubject;
import com.marcus.base.controller.BaseController;
import com.marcus.base.vo.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AuthUserController
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/10 23:00
 * @Version 1.0
 **/
@Controller
@RequestMapping("/authUser")
public class AuthUserController extends BaseController {
    @Autowired
    private AuthUserService authUserService;


    @RequestMapping(value = "/index")
    public String index(){
        return "user/userManage";
    }

    @RequestMapping(value = "/edit")
    public String editPage(){
        return "user/userEdit";
    }


    @RequestMapping(value = "/list")
    @ResponseBody
    public ResultMessage list(AuthUserVo vo, PageBean pageBean){
        return ResultMessage.resultSuccess(authUserService.getUserList(vo,pageBean));
    }

    @RequestMapping(value = "/duplicateName")
    @ResponseBody
    public Map duplicateName(@RequestParam("loginName") String loginName){
        Map<String, Object> result = new HashMap<>();
        result.put("valid",!authUserService.duplicateName(loginName));
        return result;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public ResultMessage save(AuthUserVo user){
        return ResultMessage.resultSuccess(authUserService.saveUser(user));
    }

    @RequestMapping(value = "/modifyPassword")
    @ResponseBody
    public ResultMessage modifyPassword(ModifyPasswordVo passwordVo){
        return ResultMessage.resultSuccess(authUserService.modifyPassword(passwordVo));
    }

    @RequestMapping(value = "/deleteUser")
    @ResponseBody
    public ResultMessage deleteUser(Long id){
        SecuritySubject loginUser = getLoginSubject();
        authUserService.delete(id, loginUser.getUserId());
        return ResultMessage.resultSuccess();
    }
}
