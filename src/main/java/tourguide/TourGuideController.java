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
		User user = internalTestHelper.getUser(userName);
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
		return JsonStream.serialize(visitedLocation.location);
	}

//	 TODO: Change this method to no longer return a List of Attractions.
//	 Instead: Get the closest five tourist attractions to the user - no matter how
//	 far away they are.
//	 Return a new JSON object that contains:
//	 Name of Tourist attraction,
//	 Tourist attractions lat/long,
//	 The user's location lat/long,
//	 The distance in miles between the user's location and each of the
//	 attractions.
//	 The reward points for visiting each Attraction. Note: Attraction reward
//	 points can be gathered from RewardsCentral
//	  "attractionName" : "zyx"
//		 {
//			 "latitude" : xxx,
//			 "longitude" : yyy,
//			 "distanceFromUser" : zzz,
//			 "pointsRewarded" : xyz
//		 }
//	 "user" : "UUID"
//		 {
//		 "latitude" : xxx,
//		 "longitude" : yyy
//		 }
//	 

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
		return JsonStream.serialize(internalTestHelper.getUser(userName).getUserRewards());
	}

	/**
	 * GET mapping to get the most recent location of every users.
	 * 
	 * @return a json containing the UUID and the coordinates of each users.
	 */
	@GetMapping("/getAllCurrentLocations")
	public List<UserLocation> getAllCurrentLocations() {
//		 TODO: Get a list of every user's most recent location as JSON
//		 - Note: does not use gpsUtil to query for their current location,
//		 but rather gathers the user's current location from their stored location
//		 history.
//		
//		 Return object should be the just a JSON mapping of userId to Locations
//		 similar to:
//		 { "019b04a9-067a-4c76-8817-ee75088c3822": {"latitude":74.84371,
//		 "longitude":-48.188821}
//		 ...
//		 }

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
		List<Provider> providers = tourGuideService.getTripDeals(internalTestHelper.getUser(userName));
		return JsonStream.serialize(providers);
	}

}