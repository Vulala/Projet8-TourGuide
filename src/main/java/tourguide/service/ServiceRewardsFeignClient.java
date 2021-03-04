package tourguide.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tourguide.TourGuideController;

/**
 * ServiceLocationFeignClient is used by Feign to build http request. <br>
 * The target of the request is the Service-Rewards service. <br>
 * For more details concerning the request, see {@link TourGuideController}.
 * <br>
 */
@FeignClient(name = "tourguide-service-rewards", url = "localhost:8082")
public interface ServiceRewardsFeignClient {

	@GetMapping(value = "/getRewards")
	String getRewards(@RequestParam("userName") String userName);

}