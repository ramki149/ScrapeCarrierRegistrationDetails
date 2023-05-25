package com.census.cargo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka	
@SpringBootApplication
public class CargointerviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(CargointerviewApplication.class, args);
	}

}
