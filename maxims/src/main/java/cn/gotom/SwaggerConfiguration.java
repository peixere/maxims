package cn.gotom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {
	@Bean
	public Docket swaggerApi() {
		List<RequestParameter> pars = new ArrayList<>();
		RequestParameterBuilder builder = new RequestParameterBuilder();
		builder.name("access")//
				.description("token")//
				.in(ParameterType.HEADER)//
				.required(false)//
				.build(); //
		pars.add(builder.build());
		builder = new RequestParameterBuilder();
		builder.name("refresh")//
				.description("token")//
				.in(ParameterType.HEADER)//
				.required(false)//
				.build(); //
		pars.add(builder.build());
		return new Docket(DocumentationType.OAS_30)//
				.apiInfo(swaggerApiInfo()).select()//
				.apis(RequestHandlerSelectors.basePackage(CustomException.class.getPackageName()))//
				.apis(RequestHandlerSelectors.any())//
				.paths(PathSelectors.any())//
				.build()//
				.globalRequestParameters(pars);
	}

	private ApiInfo swaggerApiInfo() {
		return new ApiInfoBuilder().title("API doc")//
				.description("how to use this")//
				.termsOfServiceUrl("https://localhost")
				.contact(new Contact("xere", "https://localhost", "[email protected]"))//
				.version("1.0").build();
	}
}
