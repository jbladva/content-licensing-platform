package com.clp.service.impl;

import com.clp.dto.MailRequest;
import com.clp.service.NotificationService;
import freemarker.template.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration config;


    @Override
    public void sendActionNotification(MailRequest request, Map<String, String> model,String template) {

        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Template t = config.getTemplate(template);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            sender.send(message);


        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Error while sending mail : ", e.getCause(), e);
        }

    }
}
