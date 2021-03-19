package tourguide.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import tourguide.TourGuideController;
import tourguide.user.UserCoordinates;
import tourguide.user.UserNearbyAttractions;
import tourguide.user.UserPreferences;

/**
 * ServiceLocationFeignClient is used by Feign to build http request. <br>
 * The target of the request is the Service-Location service. <br>
 * For more details concerning the request, see {@link TourGuideController}.
 * <br>
 */
@FeignClient(name = "tourguide-service-location", url = "localhost:8081")
public interface ServiceLocationFeignClient {

	@GetMapping(value = "/getLocation")
	String getLocation(@RequestParam("userName") String userName);

	@GetMapping(value = "/getNearbyAttractions")
	UserNearbyAttractions getNearbyAttractions(@RequestParam("userName") String userName);

	@GetMapping(value = "/getAllCurrentLocations")
	List<UserCoordinates> getAllCurrentLocations();

	@GetMapping(value = "/getTripDeals")
	String getTripDeals(@RequestParam("userName") String userName);

	@PutMapping(value = "/setUserPreferences")
	String setUserPreferences(@RequestParam("userName") String userName, @RequestBody UserPreferences userPreferences);
}
