package pl.termosteam.kinex.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Objects;
import java.util.Properties;

/**
 * EmailConfiguration: class setups the email service environments.
 *
 * @author Dmytyro Lumelskyj
 * @version 1.0
 * @since 2020-01-01
 */
@Configuration
@AllArgsConstructor
public class EmailConfiguration {

    private final Environment environment;

    /**
     * getMailSender(): this spring bean configure JavaMailSender object
     * basing on the properties defined in the application.properties file:
     * <p>
     * Email password have been encrypted using secret password defined externally as program variable:
     * jasypt.encryptor.password={SECRET PASSWORD}
     * <p>
     * In the application properties are following related properties:
     * Sample setup for the gmail mail sender:
     * spring.mail.host=smtp.gmail.com
     * spring.mail.port=587
     * spring.mail.username={EMAIL USERNAME}
     * spring.mail.password=ENC({ENCRYPTED EMAIL PASSWORD})
     * # Other properties
     * spring.mail.properties.mail.smtp.auth=true
     * spring.mail.properties.mail.smtp.connectiontimeout=5000
     * spring.mail.properties.mail.smtp.timeout=5000
     * spring.mail.properties.mail.smtp.writetimeout=5000
     * # TLS , port 587
     * spring.mail.properties.mail.smtp.starttls.enable=true
     *
     * @return JavaMailSender class object
     */
    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(environment.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty("spring.mail.port"))));
        mailSender.setUsername(environment.getProperty("spring.mail.username"));
        mailSender.setPassword(environment.getProperty("spring.mail.password"));

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }
}