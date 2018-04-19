package org.fmarin.admintournoi;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class MainProperties {

  private String environment;

  public boolean isProd() {
    return "production".equalsIgnoreCase(environment);
  }

  public String getEnvironment() {
    return environment;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }
}
