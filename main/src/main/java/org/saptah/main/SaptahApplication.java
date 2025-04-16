package org.saptah.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = {"org.saptah.main", "org.saptah.main.exception"})
public class SaptahApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaptahApplication.class, args);
	}

}
