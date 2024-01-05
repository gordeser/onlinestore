package cz.cvut.fit.onlinestore.configuration;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${onlinestore.openapi.dev-url}")
    private String devUrl;
    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("gordeser@fit.cvut.cz");
        contact.setName("Sergei Gordeev");

        Info info = new Info()
                .title("Online store Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage online store.");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
