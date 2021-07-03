package io.csrohit.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class ErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErpApplication.class, args);
	}
	@Bean
	public Docket swaggerConfiguration(){
		return new Docket(DocumentationType.OAS_30)
				.select()
				.paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("io.csrohit.erp"))
				.build()
				.apiInfo(apiDetails())
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()));
	}

	private ApiInfo apiDetails(){
		return new ApiInfo(
				"ERP I2IT",
				"ERP system for I2IT using spring boot",
				"1.0",
				"Free to use",
				new Contact("Rohit Nimkar", "github.com/csrohit", "rohit.nimkar@outlook.com"),
				"No Liscence",
				"no liscence url",
				Collections.emptyList()
		);
	}

	private ApiKey apiKey(){
		return new ApiKey("Authorization", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return springfox.documentation.spi.service.contexts.SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
	}
}
