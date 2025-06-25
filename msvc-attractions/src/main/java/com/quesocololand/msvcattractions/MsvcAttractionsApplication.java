package com.quesocololand.msvcattractions;

import org.springframework.batch.core.configuration.support.ScopeConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MsvcAttractionsApplication {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(MsvcAttractionsApplication.class);
		//Manually add the configuration for steps
		application.addInitializers((ApplicationContextInitializer<GenericApplicationContext>) context -> {
			context.registerBean(ScopeConfiguration.class);
		});
		//Run the application
		application.run(args);
	}
}

