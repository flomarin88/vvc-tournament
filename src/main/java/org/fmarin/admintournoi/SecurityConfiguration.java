package org.fmarin.admintournoi;

import org.fmarin.admintournoi.admin.AdminProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final AdminProperties admin;

  @Autowired
  public SecurityConfiguration(AdminProperties admin) {
    this.admin = admin;
  }

  @Override
	protected void configure(HttpSecurity http) throws Exception {
		http
      .csrf()
        .disable()
			.authorizeRequests()
			  .antMatchers("/admin", "/admin/**").authenticated()
			  .anyRequest().permitAll()
			.and()
			.formLogin().failureUrl("/login?error")
        .defaultSuccessUrl("/admin")
			  .permitAll()
			.and()
			.logout()
			  .permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
      .withUser(admin.getUsername())
      .password(admin.getPassword())
      .roles("ADMIN");
	}
}
