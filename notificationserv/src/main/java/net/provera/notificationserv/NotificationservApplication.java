package net.provera.notificationserv;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;

@SpringBootApplication
public class NotificationservApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationservApplication.class, args);
	}

	@Bean
	public CqlSession cqlSession(){
		return CqlSession.builder()
				.addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
				.withLocalDatacenter("datacenter1")  // Set the local data center
				.build();
	}


}
