package org.nsu.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.dev-url:http://localhost:8080}")
    private String devUrl;

    @Value("${openapi.prod-url:https://api.example.com}")
    private String prodUrl;

    @Value("${spring.security.oauth2.client.provider.github.authorization-uri:https://github.com/login/oauth/authorize}")
    private String authorizationUrl;

    @Value("${spring.security.oauth2.client.provider.github.token-uri:https://github.com/login/oauth/access_token}")
    private String tokenUrl;

    private static final String OAUTH_SCHEME_NAME = "GitHubOAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .servers(createServers())
                .components(createComponents())
                .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME));
    }

    private Info createApiInfo() {
        Contact contact = new Contact()
                .email("a.kardash@g.nsu.ru")
                .name("NSU SQLPlayground team")
                .url("https://www.nsu.ru/");

        License license = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        return new Info()
                .title("SQL Playground API")
                .version("1.0.0")
                .description("Secure SQL Playground API with GitHub OAuth integration for query execution and Git repository management")
                .termsOfService("https://www.nsu.org/terms")
                .contact(contact)
                .license(license);
    }

    private List<Server> createServers() {
        Server devServer = new Server()
                .url(devUrl)
                .description("Development Environment");

        Server prodServer = new Server()
                .url(prodUrl)
                .description("Production Environment");

        return List.of(devServer, prodServer);
    }

    private Components createComponents() {
        OAuthFlow authorizationCodeFlow = new OAuthFlow()
                .authorizationUrl(authorizationUrl)
                .tokenUrl(tokenUrl);

        OAuthFlows oauthFlows = new OAuthFlows()
                .authorizationCode(authorizationCodeFlow);

        SecurityScheme oauthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("GitHub OAuth2 Authentication")
                .flows(oauthFlows);

        return new Components()
                .addSecuritySchemes(OAUTH_SCHEME_NAME, oauthScheme);
    }
}
