package ru.netology.springBootDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoneyTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyTransferApplication.class, args);
	}

}

// для запуска в консоли:
// сначала set NODE_OPTIONS=--openssl-legacy-provider
// потом npm run start