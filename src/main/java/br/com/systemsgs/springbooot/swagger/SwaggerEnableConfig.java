package br.com.systemsgs.springbooot.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerEnableConfig {

    private Contact contact(){
        return new Contact("Guilherme Santos", "https://www.linkedin.com/in/guilherme-santos-136936157/", "guiromanno@gmail.com");
    }

    @Bean
    public Docket docket(){

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.systemsgs.springbooot.controller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .apiInfo(apiInfo());

    }

    private List<SecurityReference> defaultAuthorization(){

        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = authorizationScope;
        SecurityReference reference = new SecurityReference("JWT", scopes);
        List<SecurityReference> authorizations = new ArrayList<>();
        authorizations.add(reference);

        return authorizations;

    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Api Gerencimento de Pedidos")
                .description("Api do Sistema de Vendas")
                .version("1.0").contact(contact())
                .build();

    }



    public ApiKey apiKey(){
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext(){
        return SecurityContext.builder().securityReferences(defaultAuthorization())
                .forPaths(PathSelectors.any()).build();
    }



}
