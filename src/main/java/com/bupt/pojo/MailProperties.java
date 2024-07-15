package com.bupt.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "mail")
public class MailProperties {
    private String username;

    private String password;

    private String host;

    private String port;

    private String auth;

    private String tls;

    private String from;

    private String to;
}
