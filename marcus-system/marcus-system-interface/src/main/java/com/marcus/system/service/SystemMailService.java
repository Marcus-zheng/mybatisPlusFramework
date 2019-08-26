package com.marcus.system.service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author Marcus.zheng
 * @Date 2019/7/22 11:47
 **/
public interface SystemMailService {

    /**
     * @Description 保存邮箱服务器参数
     * @Author Marcus.zheng
     * @Date 2019/7/22 11:49
     * @Param params
     * @Return void
     */
    void saveMailParams(Map<String, String> params);

    /**
     * @Description 获取邮箱服务器参数
     * @Author Marcus.zheng
     * @Date 2019/7/22 11:49
     * @Return java.util.Map<java.lang.String,java.lang.String>
     */
    Map<String, String> getMailParams();

    /**
     * @Description 发送测试邮件
     * @Author Marcus.zheng
     * @Date 2019/7/22 11:56
     * @Param params
     * @Return void
     */
    void testSendMail(String host, String userName, String password, Integer port);

    /**
     * @Description 发送普通邮件
     * @Author Marcus.zheng
     * @Date 2019/7/22 14:38
     * @Param to
     * @Param title
     * @Param content
     * @Return void
     */
    void sendSimpleMail(String to, String title, String content);

    /**
     * @Description 发送带附件的邮件
     * @Author Marcus.zheng
     * @Date 2019/7/22 14:38
     * @Param to
     * @Param title
     * @Param cotent
     * @Param fileList
     * @Return void
     */
    void sendAttachmentsMail(String to, String title, String cotent, List<File> fileList);

    /**
     * @Description 初始化邮箱设置
     * @Author Marcus.zheng
     * @Date 2019/7/22 17:43
     * @Return void
     */
    void initMail();
}
