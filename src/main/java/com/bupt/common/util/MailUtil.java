package com.bupt.common.util;

import com.bupt.pojo.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Component
public class MailUtil {
    @Autowired
    public MailProperties mailProperties;
    public Session createSession() {
        //	创建一个配置文件，并保存
        Properties props = new Properties();

        //	SMTP服务器连接信息
        props.put("mail.smtp.host", mailProperties.getHost());//	SMTP主机名
        props.put("mail.smtp.port", mailProperties.getPort());//	主机端口号
        props.put("mail.smtp.auth", mailProperties.getAuth());//	是否需要用户认证
        props.put("mail.smtp.starttls.enable", mailProperties.getTls());//	启用TlS加密

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailProperties.getUsername(), mailProperties.getPassword());
            }
        });

        //  控制台打印调试信息
        session.setDebug(true);
        return session;
    }
}