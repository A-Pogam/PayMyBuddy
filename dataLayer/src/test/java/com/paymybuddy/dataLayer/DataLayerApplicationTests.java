package com.paymybuddy.dataLayer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.paymybuddy.dataLayer.controller.LoginController;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
class DataLayerApplicationTests {

	@Autowired
	private LoginController controller;

	@Test
	void contextLoads()throws Exception {
		assertThat(controller).isNotNull();
	}

}
