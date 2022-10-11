package aurora.report.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {

    private static final String DEFAULT_MESSAGE_200_SUCCESS = "Sucesso, Operação realizada.";
    private static final String DEFAULT_MESSAGE_201_CREATED = "Sucesso, Recurso Criado.";
    private static final String DEFAULT_MESSAGE_204_NOCONTENT = "Sucesso, Recurso Removido.";
    private static final String DEFAULT_MESSAGE_401_NOTAUTHORIZED = "Erro, Você não está autorizado a ver o recurso.";
    private static final String DEFAULT_MESSAGE_403_FORBIDDEN = "Erro, O acesso ao recurso que você estava tentando acessar é proibido.";
    private static final String DEFAULT_MESSAGE_404_NOT_FOUND = "Erro, O recurso que você estava tentando acessar não foi encontrado.";
    private static final String DEFAULT_MESSAGE_500_SERVER_ERROR = "Erro, Ocorreu algo inesperado no servidor, contate o suporte.";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.aurora.report"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET,
                        List.of(new ResponseBuilder().code("200").description(DEFAULT_MESSAGE_200_SUCCESS).build(),
                                new ResponseBuilder().code("401").description(DEFAULT_MESSAGE_401_NOTAUTHORIZED).build(),
                                new ResponseBuilder().code("403").description(DEFAULT_MESSAGE_403_FORBIDDEN).build(),
                                new ResponseBuilder().code("404").description(DEFAULT_MESSAGE_404_NOT_FOUND).build(),
                                new ResponseBuilder().code("500").description(DEFAULT_MESSAGE_500_SERVER_ERROR).build()
                        ))
                .globalResponses(HttpMethod.POST,
                        List.of(new ResponseBuilder().code("201").description(DEFAULT_MESSAGE_201_CREATED).build(),
                                new ResponseBuilder().code("401").description(DEFAULT_MESSAGE_401_NOTAUTHORIZED).build(),
                                new ResponseBuilder().code("403").description(DEFAULT_MESSAGE_403_FORBIDDEN).build(),
                                new ResponseBuilder().code("404").description(DEFAULT_MESSAGE_404_NOT_FOUND).build(),
                                new ResponseBuilder().code("500").description(DEFAULT_MESSAGE_500_SERVER_ERROR).build()
                        )
                )
                .globalResponses(HttpMethod.PUT,
                        List.of(new ResponseBuilder().code("200").description(DEFAULT_MESSAGE_200_SUCCESS).build(),
                                new ResponseBuilder().code("401").description(DEFAULT_MESSAGE_401_NOTAUTHORIZED).build(),
                                new ResponseBuilder().code("403").description(DEFAULT_MESSAGE_403_FORBIDDEN).build(),
                                new ResponseBuilder().code("404").description(DEFAULT_MESSAGE_404_NOT_FOUND).build(),
                                new ResponseBuilder().code("500").description(DEFAULT_MESSAGE_500_SERVER_ERROR).build()
                        )
                )
                .globalResponses(HttpMethod.DELETE,
                        List.of(
                                new ResponseBuilder().code("204").description(DEFAULT_MESSAGE_204_NOCONTENT).build(),
                                new ResponseBuilder().code("401").description(DEFAULT_MESSAGE_401_NOTAUTHORIZED).build(),
                                new ResponseBuilder().code("403").description(DEFAULT_MESSAGE_403_FORBIDDEN).build(),
                                new ResponseBuilder().code("404").description(DEFAULT_MESSAGE_404_NOT_FOUND).build(),
                                new ResponseBuilder().code("500").description(DEFAULT_MESSAGE_500_SERVER_ERROR).build()
                        )
                )
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Aurora Report").description("API de Relatórios").version("1.0.0").build();
    }

}