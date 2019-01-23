package pl.darenie.dns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("pl.darenie")
@EnableAsync
@EnableScheduling
public class DnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DnsApplication.class, args);
	}
}
