package tourguide;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tourguide.service.ServiceLocationFeignClient;
import tourguide.service.ServiceRewardsFeignClient;
import tourguide.user.UserCoordinates;
import tourguide.user.UserNearbyAttractions;
import tourguide.user.UserPreferences;

/**
 * TourGuideController is the main controller of the application. <br>
 * Each endpoints are responding to a specific functionnality. <br>
 */
@RestController
public class TourGuideController {

	@Autowired
	private ServiceRewardsFeignClient serviceRewardsFeignClient;

	@Autowired
	private ServiceLocationFeignClient serviceLocationFeignClient;

	/**
	 * Get : /
	 * 
	 * @return "Greetings from TourGuide!"
	 */
	@GetMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}

	/**
	 * GET mapping to get the user's location as json. <br>
	 * URI : http://localhost:8080/getLocation?userName={internalUser0} <br>
	 * 
	 * @param userName
	 * @return json
	 */
	@GetMapping("/getLocation")
	public String getLocation(@RequestParam String userName) {
		return serviceLocationFeignClient.getLocation(userName);
	}

	/**
	 * GET mapping to get the five closest tourist attractions from the user. <br>
	 * It return : the attractionName, the coordinates, the distance of the
	 * attraction from the user and the points rewarded by the attraction. <br>
	 * It also return the user's UUID and the user's coordinates. <br>
	 * 
	 * URI : http://localhost:8080/getNearbyAttractions?userName={internalUser0}
	 * <br>
	 * 
	 * @param userName
	 * @return {@link UserNearbyAttractions} as json
	 */
	@GetMapping("/getNearbyAttractions")
	public UserNearbyAttractions getNearbyAttractions(@RequestParam String userName) {
		return serviceLocationFeignClient.getNearbyAttractions(userName);

	}

	/**
	 * GET mapping to get the user's rewards. <br>
	 * URI : http://localhost:8080/getRewards?userName={internalUser0} <br>
	 * 
	 * @param userName
	 * @return json
	 */
	@GetMapping("/getRewards")
	public String getRewards(@RequestParam("userName") String userName) {
		return serviceRewardsFeignClient.getRewards(userName);
	}

	/**
	 * GET mapping to get the most recent location of every users. <br>
	 * URI : http://localhost:8080/getAllCurrentLocations <br>
	 * 
	 * @return a json containing the UUID and the coordinates of each users.
	 */
	@GetMapping("/getAllCurrentLocations")
	public List<UserCoordinates> getAllCurrentLocations() {
		return serviceLocationFeignClient.getAllCurrentLocations();
	}

	/**
	 * GET mapping to get the user's trip deals. <br>
	 * URI : http://localhost:8080/getTripDeals <br>
	 * 
	 * @param userName
	 * @return json
	 */
	@GetMapping("/getTripDeals")
	public String getTripDeals(@RequestParam String userName) {
		return serviceLocationFeignClient.getTripDeals(userName);
	}

	/**
	 * PUT mapping to set the user's preferences to refine the trip deals. <br>
	 * URI : http://localhost:8080/setUserPreferences?userName={internalUser0} <br>
	 * 
	 * @param userName
	 * @return json
	 */
	@PutMapping("/setUserPreferences")
	public String setUserPreferences(@RequestParam String userName, @RequestBody UserPreferences userPreferences) {
		return serviceLocationFeignClient.setUserPreferences(userName, userPreferences);
	}
}