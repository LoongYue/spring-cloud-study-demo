package jzm.micro.study.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Adam.jiang on 2017/8/10.
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

//    @Value("${spring.profiles.active}")
//    private String env;

    @Value("${server.port}")
    private String port;

//    @Value("${serverurl}")
//    private String serverUrl;
    
    @Bean
    public Docket createRestApi() {

        String host = /*"local".equals(env) ? ("localhost:"+port) : serverUrl*/"http://192.168.31.35:"+port;

        return new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("jzm.micro.study"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("小明swagger")
                .description("")
                .termsOfServiceUrl("")
                .contact("jzm 287671545@qq.com")
                .version("1.0")
                .build();
    }

}
