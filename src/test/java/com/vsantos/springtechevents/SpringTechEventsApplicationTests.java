package com.vsantos.springtechevents;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Transactional
class SpringTechEventsApplicationTests {

	@Test
	void contextLoads() {
	}

}
