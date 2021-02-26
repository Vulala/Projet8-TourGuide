package tourguide;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourguide.helper.InternalTestHelper;
import tourguide.service.TourGuideService;
import tourguide.user.User;
import tourguide.user.UserLocation;
import tourguide.user.UserNearbyAttractions;
import tripPricer.Provider;

/**
 * TourGuideController is the main controller of the application. <br>
 * Each endpoints are responding to a specific functionnality. <br>
 */
@RestController
public class TourGuideController {

	@Autowired
	private TourGuideService tourGuideService;

	@Autowired
	private InternalTestHelper internalTestHelper;

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
	 * 
	 * @param userName
	 * @return json
	 */
	@GetMapping("/getLocation")
	public String getLocation(@RequestParam String userName) {
		internalTestHelper.initializeTheInternalUsers();
		User user = internalTestHelper.getUser(userName);
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
		return JsonStream.serialize(visitedLocation.location);
	}

	/**
	 * GET mapping to get the five closest tourist attractions from the user. <br>
	 * It return : the attractionName, the coordinates, the distance of the
	 * attraction from the user and the points rewarded by the attraction. <br>
	 * It also return the user's UUID and the user's coordinates. <br>
	 * 
	 * @param userName
	 * @return {@link UserNearbyAttractions} as json
	 */
	@GetMapping("/getNearbyAttractions")
	public UserNearbyAttractions getNearbyAttractions(@RequestParam String userName) {
		internalTestHelper.initializeTheInternalUsers();
		User user = internalTestHelper.getUser(userName);
		return tourGuideService.fiveClosestAttractions(user);

	}

	/**
	 * GET mapping to get the user's rewards.
	 * 
	 * @param userName
	 * @return json
	 */
	@GetMapping("/getRewards")
	public String getRewards(@RequestParam String userName) {
		internalTestHelper.initializeTheInternalUsers();
		return JsonStream.serialize(internalTestHelper.getUser(userName).getUserRewards());
	}

	/**
	 * GET mapping to get the most recent location of every users.
	 * 
	 * @return a json containing the UUID and the coordinates of each users.
	 */
	@GetMapping("/getAllCurrentLocations")
	public List<UserLocation> getAllCurrentLocations() {
		internalTestHelper.initializeTheInternalUsers();
		List<User> userList = internalTestHelper.getAllUsers();
		return tourGuideService.getAllCurrentLocations(userList);
	}

	/**
	 * GET mapping to get the user's trip deals.
	 * 
	 * @param userName
	 * @return json
	 */
	@GetMapping("/getTripDeals")
	public String getTripDeals(@RequestParam String userName) {
		internalTestHelper.initializeTheInternalUsers();
		List<Provider> providers = tourGuideService.getTripDeals(internalTestHelper.getUser(userName));
		return JsonStream.serialize(providers);
	}

}