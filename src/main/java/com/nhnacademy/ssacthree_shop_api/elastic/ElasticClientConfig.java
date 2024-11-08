package com.nhnacademy.ssacthree_shop_api.elastic;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticClientConfig {

  @Value("${elasticsearch.username}")
  private String username;

  @Value("${elasticsearch.password}")
  private String password;

  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
    return new BasicAuthRequestInterceptor(username, password);
  }
}
