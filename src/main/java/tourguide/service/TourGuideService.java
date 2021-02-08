package tourguide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourguide.user.NearbyAttractions;
import tourguide.user.User;
import tourguide.user.UserLocation;
import tourguide.user.UserNearbyAttractions;
import tripPricer.Provider;
import tripPricer.TripPricer;

/**
 * Core Service of the application. <br>
 * It is used to track the user's location, to get trip deals, to get nearby
 * locations and to get the last location of a user. <br>
 */
@Service
public class TourGuideService {

	private static final String TRIP_PRICER_API_KEY = "test-server-api-key";

	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();

	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;
	}

	/**
	 * Track or get the last visited location of the user set as parameter. <br>
	 * 
	 * @param user
	 * @return user.getVisitedLocations().isEmpty() => true -> track the user ;
	 *         false -> get the last visited location.
	 */
	public VisitedLocation getUserLocation(User user) {
		return user.getVisitedLocations().isEmpty() ? trackUserLocation(user) : user.getLastVisitedLocation();
	}

	/**
	 * Define a trip deals for the user set as parameter. <br>
	 * 
	 * @param user
	 * @return {@link Provider} from TripPricer.jar
	 */
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().parallelStream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(TRIP_PRICER_API_KEY, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	/**
	 * Track the user's location and add it to the user's visited location. <br>
	 * 
	 * @param {@link User}
	 * @return {@link VisitedLocation}
	 */
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
//		calculateRewards(user); // What's for? -> the calculateRewards method is already defined in RewardsService
		return visitedLocation;
	}

	/**
	 * Get the list of the nearby attractions. <br>
	 * The nearby distance is defined with the attractionProximityRange field
	 * variable of the {@link RewardsService} class. <br>
	 * 
	 * @param {@link VisitedLocation}
	 * @return the list of the {@link Attraction} nearby
	 */
	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for (Attraction attraction : gpsUtil.getAttractions()) {
			if (rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
			}
		}

		return nearbyAttractions;
	}

	/**
	 * The distance between the user's location and the attraction's location. <br>
	 * It sort the distance by making use of a TreeMap. <br>
	 * 
	 * @param user
	 * @return Map<Double, String>, double = distance from the : String = name of
	 *         the attraction
	 */
	public Map<Double, String> distanceBetweenUserAndAttraction(User user) {
		Map<Double, String> attractionNameAndTheUserDistanceToIt = new TreeMap<>();
		VisitedLocation userVisitedLocation = getUserLocation(user);
		List<Attraction> nearbyAttractionsFromUser = getNearByAttractions(userVisitedLocation);
		Location userLocation = userVisitedLocation.location;

		for (int i = 0; i < nearbyAttractionsFromUser.size(); i++) {
			Attraction attraction = nearbyAttractionsFromUser.get(i);
			double distance = rewardsService.getDistance(userLocation, attraction);
			attractionNameAndTheUserDistanceToIt.put(distance, attraction.attractionName);
		}
		return attractionNameAndTheUserDistanceToIt;
	}

	/**
	 * Retrieve the 5 closest attraction's location from the user set as parameter.
	 * 
	 * @param user
	 * @return {@link UserNearbyAttractions} with the five closest attractions from
	 *         the user's location.
	 */
	public UserNearbyAttractions fiveClosestAttractions(User user) {
		UserLocation userLocation = new UserLocation(user.getUserId(), user.getLastVisitedLocation().location);
		Map<Double, String> distanceBetweenUserAndAttraction = distanceBetweenUserAndAttraction(user);
		List<NearbyAttractions> fiveClosestAttractions = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			String attractionName = distanceBetweenUserAndAttraction.values().parallelStream()
					.collect(Collectors.toList()).get(i);
			Double distance = distanceBetweenUserAndAttraction.keySet().parallelStream().collect(Collectors.toList())
					.get(i);

			List<Attraction> listAttraction = gpsUtil.getAttractions();
			Optional<Attraction> attraction = listAttraction.stream()
					.filter(x -> x.attractionName.equals(attractionName))
					.findFirst(); // That is done to retrieve the attraction's coordinates.

			if (!attraction.isPresent()) {
				throw new NullPointerException(
						"The attraction is not present in the attractions's list." + listAttraction);
			}

			Location attractionLocation = new Location(attraction.get().latitude, attraction.get().longitude);
			int pointsRewarded = rewardsService.getRewardPoints(attraction.get(), user);

			NearbyAttractions userNearbyAttractions = new NearbyAttractions(attractionName, attractionLocation,
					distance, pointsRewarded);
			fiveClosestAttractions.add(userNearbyAttractions);

		}
		return new UserNearbyAttractions(fiveClosestAttractions, userLocation);
	}

	/**
	 * Method used to retrieve all the current users's locations, it return a list
	 * of type {@link UserLocation}. <br>
	 * 
	 * @param userList containing all the users.
	 * @return a list of all the current locations of every users.
	 */
	public List<UserLocation> getAllCurrentLocations(List<User> userList) {
		List<UserLocation> getAllCurrentLocations = new ArrayList<>();

		for (int i = 0; i < userList.size(); i++) {
			Location location = userList.get(i).getLastVisitedLocation().location;
			UUID uuid = userList.get(i).getUserId();

			UserLocation userLocation = new UserLocation(uuid, location);
			getAllCurrentLocations.add(userLocation);
		}

		return getAllCurrentLocations;
	}

}
