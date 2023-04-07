package net.provera.cartserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CartservApplication {
	public static void main(String[] args) {
		SpringApplication.run(CartservApplication.class, args);
	}

}
