package uz.OLXCone;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "A website similar to olx.uz",
                description = DemoApplication.DESCRIPTION
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    public static final String DESCRIPTION = """
            <p><b> I used jwt token to authenticate. </b> <br></p>
            <a href="https://gitlab.com/portfolio-projects5572035">
                <b> Gitlab link of this site. <b> </a>
            <p><b>Contact me to get superAdmin or admin email and password </b></p>
           """;
}
