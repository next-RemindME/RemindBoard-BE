package com.remind.board.common.config;

import io.swagger.models.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  //추후 front 도메인 변경 시 확정된 도메인으로 사용하기
//  @Value("${cors.allowed-origins}")
//  private String allowedOrigins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {

    registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods(HttpMethod.GET.name(),
            HttpMethod.HEAD.name(),
            HttpMethod.POST.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.PUT.name(),
            HttpMethod.OPTIONS.name());
  }
}
