package pl.termosteam.kinex.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.mail")
@Getter
public class EmailProperties {

    @Value("${application.mail.host}")
    private String host;
    @Value("${application.mail.port}")
    private int port;
    @Value("${application.mail.username}")
    private String username;
    @Value("${application.mail.password}")
    private String password;

    @Value("${application.mail.properties.mail.transport.protocol}")
    private String transportProtocol;
    @Value("${application.mail.properties.mail.smtp.auth}")
    private Boolean smtpAuthorisation;

    @Value("#{new Boolean('${application.mail.properties.mail.smtp.starttls.enable}')}")
    private Boolean startTls;

    @Value("${application.mail.properties.mail.debug}")
    private Boolean debug;

}
