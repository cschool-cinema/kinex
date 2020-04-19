package pl.termosteam.kinex.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import pl.termosteam.kinex.configuration.properties.ApplicationProperties;
import pl.termosteam.kinex.configuration.properties.EmailProperties;

import java.util.Properties;

/**
 * EmailConfiguration: class setups the email service environments.
 *
 * @author Dmytyro Lumelskyj
 * @version 1.0
 * @since 2020-01-01
 */
@Configuration
@RequiredArgsConstructor
public class EmailConfiguration {

    private final ApplicationProperties applicationProperties;

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        EmailProperties properties = applicationProperties.getEmailProperties();

        mailSender.setHost(properties.getHost());
        mailSender.setPort(properties.getPort());
        mailSender.setUsername(properties.getUsername());
        mailSender.setPassword(properties.getPassword());

        Properties javaMailProperties = new Properties();

        javaMailProperties.put("mail.smtp.starttls.enable", properties.getStartTls());
        javaMailProperties.put("mail.smtp.auth", properties.getSmtpAuthorisation());
        javaMailProperties.put("mail.transport.protocol", properties.getTransportProtocol());
        javaMailProperties.put("mail.debug", properties.getDebug());

        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }
}