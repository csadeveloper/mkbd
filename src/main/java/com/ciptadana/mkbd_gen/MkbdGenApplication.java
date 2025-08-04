package com.ciptadana.mkbd_gen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MkbdGenApplication {

	public static void main(String[] args) {
		SpringApplication.run(MkbdGenApplication.class, args);
	}

}
