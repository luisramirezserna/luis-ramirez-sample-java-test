package com.clip.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
@EnableAspectJAutoProxy
public class ClipTransactionApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClipTransactionApiApplication.class, args);
	}

}
