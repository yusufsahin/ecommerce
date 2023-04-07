package net.provera.productserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProductservApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductservApplication.class, args);
    }

}
