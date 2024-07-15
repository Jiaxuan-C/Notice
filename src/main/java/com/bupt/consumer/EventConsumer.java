package com.bupt.consumer;

import cn.hutool.json.JSONUtil;
import com.bupt.common.constant.ApplicationConstant;
import com.bupt.mapper.EventMapper;
import com.bupt.pojo.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@Slf4j
@Component
public class EventConsumer {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    EventMapper eventMapper;

    @KafkaListener(topics = ApplicationConstant.KAFKA_NOTICE_TOPIC, groupId = ApplicationConstant.KAFKA_NOTICE_GROUP_ID)
    public void consumeKafka(ConsumerRecord<String, String> record, Acknowledgment ack) throws Exception {

        // 发送告警邮件
        this.sendMail(record);
        // 落CK
        Event event = JSONUtil.toBean(record.value(), Event.class);
        // 将Unix时间戳转换为Date对象
        Date date = new Date(event.getTimestamp()); // 注意：Unix时间戳是以秒为单位，而Date构造函数需要毫秒
        // 创建一个SimpleDateFormat对象来定义日期时间的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 如果需要指定时区（例如，转换为UTC时间），可以设置TimeZone
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        // 使用SimpleDateFormat将Date对象格式化为字符串
        String formattedDate = sdf.format(date);
        eventMapper.insert(event.getMetric(), event.getDescription(), formattedDate, event.getTimestamp());
        ack.acknowledge();
    }


    public void sendMail(ConsumerRecord<String, String> record) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom("1261821731@qq.com");//发送者
        smm.setTo("ysuchenjiaxuan@163.com");//收件人
        smm.setSubject("【车间告警-BUPT-MARS】 请及时修复！");//邮件主题

        String description = null;
        try {
            description = (String) JSONUtil.getByPath(JSONUtil.parse(record.value()), "$.description");
        } catch (Exception e) {
            description = "";
        }

        smm.setText(description);//邮件内容
        javaMailSender.send(smm);//发送邮件

        log.info("邮件发送成功");
    }

    public void saveCK(ConsumerRecord<String, String> record) {

    }
}
