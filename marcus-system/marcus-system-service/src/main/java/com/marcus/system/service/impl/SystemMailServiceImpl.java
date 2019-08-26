package com.marcus.system.service.impl;

import com.marcus.system.service.SystemMailService;
import com.marcus.system.service.SystemParamService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author Marcus.zheng
 * @Date 2019/7/22 11:47
 **/
@Service
@Transactional
public class SystemMailServiceImpl extends JavaMailSenderImpl implements SystemMailService, JavaMailSender {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SystemParamService systemParamService;

    @Override
    public void saveMailParams(Map<String, String> params) {
        systemParamService.saveParams(params);
        // 重新初始化邮箱设置
        initMail();
    }

    @Override
    public Map<String, String> getMailParams() {
        return systemParamService.getParamsByModule("system.mail");
    }

    @Override
    public void testSendMail(String host, String userName, String password, Integer port) {
        setParams(host, userName, password, port);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(userName);
        msg.setTo(userName);
        msg.setSubject("测试邮件");
        msg.setText("这是一封测试邮件");
        send(msg);
    }

    @Override
    public void sendSimpleMail(String to, String title, String content) {
        String from = systemParamService.getValueByName("system.mailUserName");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        send(message);
        logger.info("===邮件发送成功===");
    }

    @Override
    public void sendAttachmentsMail(String to, String title, String cotent, List<File> fileList) {
        String from = systemParamService.getValueByName("system.mailUserName");
        MimeMessage message = createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(cotent);
            if (fileList != null && fileList.size() > 0){
                String fileName = null;
                for (File file:fileList) {
                    fileName = MimeUtility.encodeText(file.getName(), "GB2312", "B");
                    helper.addAttachment(fileName, file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        send(message);
        logger.info("邮件发送成功");
    }

    @Override
    public void initMail() {
        Map<String, String> systemMailParams = systemParamService.getParamsByModule("system.mail");
        String host = systemMailParams.get("system.mailServerHost");
        Integer port = Integer.parseInt(StringUtils.defaultString(systemMailParams.get("system.mailServerPort"), "25"));
        String userName = systemMailParams.get("system.mailUserName");
        String password = systemMailParams.get("system.mailPassword");
        setParams(host, userName, password, port);
    }

    /**
     * @Description 设置邮件发送参数
     * @Author Marcus.zheng
     * @Date 2019/7/22 15:30
     * @Param host
     * @Param userName
     * @Param password
     * @Param port
     * @Return void
     */
    private void setParams(String host, String userName, String password, int port) {
        setDefaultEncoding("UTF-8");
        setHost(host);
        setUsername(userName);
        setPassword(password);
        setPort(port);
        getJavaMailProperties().setProperty("mail.smtp.auth", "true");
        getJavaMailProperties().setProperty("mail.smtp.timeout", "25000");
        // 开始配置ssl
        getJavaMailProperties().setProperty("mail.smtp.socketFactory.port", port + "");
        getJavaMailProperties().setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // 完成配置ssl
        getJavaMailProperties().setProperty("mail.smtp.socketFactory.fallback", "false");
        getJavaMailProperties().setProperty("mail.smtp.starttls.enable", "true");
    }
}
