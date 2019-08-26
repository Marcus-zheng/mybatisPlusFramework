package com.marcus.system.controller;

import com.marcus.base.controller.BaseController;
import com.marcus.base.vo.ResultMessage;
import com.marcus.system.service.SystemMailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Author Marcus.zheng
 * @Date 2019/7/22 13:49
 **/
@Controller
@RequestMapping("/systemMail")
public class SystemMailController extends BaseController {
    @Autowired
    private SystemMailService systemMailService;

    @RequestMapping("/index")
    public String index(){
        Map<String, String> mailParams = systemMailService.getMailParams();
        request.setAttribute("mailParams", mailParams);
        request.setAttribute("mailTest", "mailTest");
        return "mail/mailManage";
    }

    /**
     * @Description 保存邮箱服务器
     * @Author Marcus.zheng
     * @Date 2019/7/22 16:08
     * @Param params
     * @Return com.zzl.common.vo.Result
     */
    @RequestMapping(value = "/saveMailParam")
    @ResponseBody
    public ResultMessage saveMailParam(@RequestParam Map<String, String> params) {
        systemMailService.saveMailParams(params);
        return ResultMessage.resultSuccess();
    }

    // 发送测试邮件
    @RequestMapping(value = "/testSendMail")
    @ResponseBody
    public ResultMessage testSendMail(@RequestParam Map<String, String> params){
        // 邮箱服务器地址
        String serverHost = params.get("system.mailServerHost");
        // 邮件服务器端口
        String serverPort = params.get("system.mailServerPort");
        // 邮箱用户名
        String userName = params.get("system.mailUserName");
        // 邮箱密码
        String password = params.get("system.mailPassword");
        if (StringUtils.isAnyBlank(serverHost, serverPort, userName, password)){
            return ResultMessage.returnDefaultFail("邮箱信息不全！");
        }
        systemMailService.testSendMail(serverHost, userName, password, Integer.parseInt(serverPort));
        return ResultMessage.resultSuccess();
    }
}
