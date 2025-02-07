package com.itmoji.itmojiserver.api.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Value("${swagger.production.url}")
  private String productionServerUrl;

  @Bean
  public OpenAPI openAPI() {

    Server localServer = new Server(); // 로컬 서버 설정
    localServer.setUrl("http://localhost:8080");
    localServer.setDescription("Local server");

    Server productionServer = new Server(); // 운영 서버 설정
    productionServer.setUrl(productionServerUrl);
    productionServer.setDescription("Production server");

    Info info = new Info()
        .title("It-Moji Server API")
        .version("v1.0.0")
        .description("개선중");

    return new OpenAPI()
        .info(info)
        .servers(List.of(localServer, productionServer));
  }
}
