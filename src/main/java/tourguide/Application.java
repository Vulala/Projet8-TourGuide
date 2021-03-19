package tourguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.zalando.jackson.datatype.money.MoneyModule;

@SpringBootApplication
@EnableFeignClients("tourguide")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Jackson module to support JSON serialization and deserialization of
	 * JavaMoney. <br>
	 * 
	 * @return MoneyModule
	 */
	@Bean
	public MoneyModule moneyModule() {
		return new MoneyModule();
	}
}
