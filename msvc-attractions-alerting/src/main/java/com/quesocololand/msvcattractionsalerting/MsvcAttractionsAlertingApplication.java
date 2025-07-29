package com.quesocololand.msvcattractionsalerting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling	//For the repetitive tasks
public class MsvcAttractionsAlertingApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsvcAttractionsAlertingApplication.class, args);
	}
}
