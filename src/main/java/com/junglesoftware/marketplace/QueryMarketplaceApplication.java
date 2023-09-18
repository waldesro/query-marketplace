package com.junglesoftware.marketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
		basePackages = {"com.junglesoftware.marketplace"}
)
public class QueryMarketplaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueryMarketplaceApplication.class, args);
	}

}
