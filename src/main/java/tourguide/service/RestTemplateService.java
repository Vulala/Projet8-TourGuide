package tourguide.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tourguide.TourGuideController;

/**
 * Class used to build http request to communicate with the different
 * micro-service. <br>
 * Will be soon removed because of the use of Feign; see
 * {@link ServiceLocationFeignClient} and {@link ServiceRewardsFeignClient}. <br>
 */
@Service
public class RestTemplateService {

	private final RestTemplate restTemplate;

	public RestTemplateService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	/**
	 * See {@link TourGuideController} for more details. <br>
	 * 
	 * @param userName
	 * @return a String from the micro-service.
	 */
	public String getRewards(String userName) {
		return this.restTemplate.getForObject("http://localhost:8082/getRewards/{userName}", String.class, userName);
	}
}
