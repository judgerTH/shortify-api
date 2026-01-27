package jade.product.shortifyapi.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("https://readworld.co.kr/api"))
                .info(new Info()
                        .title("Shortify API")
                        .description("Shortify API Documentation")
                        .version("v1.0.0"));
    }
}
