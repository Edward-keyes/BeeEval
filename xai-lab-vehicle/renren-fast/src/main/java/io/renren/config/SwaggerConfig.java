/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("人人开源")
                        .description("renren-fast文档")
                        .version("3.0.0")
                        .termsOfService("https://www.renren.io"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("token", new SecurityScheme()
                                .type(Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("token")));
    }

}
