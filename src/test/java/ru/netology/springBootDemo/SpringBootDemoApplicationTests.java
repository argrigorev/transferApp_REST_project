package ru.netology.springBootDemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.springBootDemo.dto.TransferRequest;
import ru.netology.springBootDemo.model.Amount;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class SpringBootDemoApplicationTests {

	@Container
	private static final GenericContainer<?> appContainer = new GenericContainer<>("spring-demo-app:latest")
			.withExposedPorts(5500);

	@Test
	void contextLoads() {
	}

	@Test
	void transferEndpointTest() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:" + appContainer.getMappedPort(5500) + "/transfer";

		TransferRequest request = new TransferRequest(
				"1111222233334444",
				"123",
				"12/25",
				"5555666677778888",
				new Amount(1000, "RUB")
		);

		String response = restTemplate.postForObject(url, request, String.class);
		assertNotNull(response);
		assertTrue(response.contains("operationId"));
	}

}
