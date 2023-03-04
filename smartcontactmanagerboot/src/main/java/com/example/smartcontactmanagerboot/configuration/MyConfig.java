package com.example.smartcontactmanagerboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig{
		
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new  BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		 
		DaoAuthenticationProvider dAuthenticationProvider = new DaoAuthenticationProvider();
		dAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		dAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
		return dAuthenticationProvider;
	}
	
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers("/smartContactManager/admin/**").hasRole("ADMIN")
                .requestMatchers("/smartContactManager/user/**").hasRole("USER")
                .requestMatchers("/**").permitAll().and().formLogin().loginPage("/smartContactManager/login").and().csrf().disable();

        http.formLogin().defaultSuccessUrl("/smartContactManager/user/index", true);

        return http.build();
    }
	
	
}
