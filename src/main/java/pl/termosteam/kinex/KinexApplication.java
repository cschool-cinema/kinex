package pl.termosteam.kinex;

import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import com.indvd00m.ascii.render.elements.PseudoText;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.termosteam.kinex.configuration.properties.ApplicationProperties;

/**
 * The Cinema backend api with
 * JWT authentication system and basic crud and report functionality
 *
 * @author Paweł Szopiński {Entities, Repository and DTO with controller requests}
 * @author Dmytyro Lumelskyj {Security module and related entities, DTO, rest controller and email services}
 * @author Jacek Gas {Application unit and integration tests}
 * @version 1.0
 * @since 2020-01-01
 */

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class KinexApplication {

    public static void main(String[] args) {
        showAppLogo();
        SpringApplication.run(KinexApplication.class, args);
    }

    private static void showAppLogo() {
        IRender render = new Render();
        IContextBuilder builder = render.newBuilder();
        builder.width(120).height(20);
        builder.element(new PseudoText("KinEX API"));
        ICanvas canvas = render.render(builder.build());
        String s = canvas.getText();
        System.out.println(s);
    }
}
