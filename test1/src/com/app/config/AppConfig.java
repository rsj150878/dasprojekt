package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.app.auth.AuthManager;
import com.app.bean.MyBean;
import com.app.service.UserService;

/**
 * @author Ondrej Kvasnovsky
 */
@Configuration
@ComponentScan(basePackages = { "com.app.ui", "com.app.Auth", "com.app.service","com.app.bean","com.app.Components"})
public class AppConfig {

	
	@Bean
	public AuthManager authManager() {
		AuthManager res = new AuthManager();
		return res;
	}

	@Bean
	public UserService userService() {
		UserService res = new UserService();
		return res;
	}


}
