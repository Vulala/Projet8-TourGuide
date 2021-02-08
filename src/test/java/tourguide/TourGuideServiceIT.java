package tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourguide.service.RewardsService;
import tourguide.service.TourGuideService;
import tourguide.user.User;
import tripPricer.Provider;

public class TourGuideServiceIT {

	private GpsUtil gpsUtil = new GpsUtil();
	private RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
	private TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
	private User user = new User(UUID.randomUUID(), "jon1", "000", "jon1@tourGuide.com");

	/**
	 * Change the Locale to match the Locale defined in the jar files. <br>
	 * The use of the jar file: {@link GpsUtil} can throw a
	 * <b>NumberFormatException</b> when used with a number format (locale)
	 * different than EN/US type. <br>
	 */
	@BeforeAll
	static void setupLocale() {
		Locale.setDefault(Locale.ENGLISH);
		Locale.setDefault(Locale.UK);
	}

	@BeforeEach
	void setup() {
		gpsUtil = new GpsUtil();
		rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		tourGuideService = new TourGuideService(gpsUtil, rewardsService);
	}
// Not a UT, should create an attraction instead or mock it, same for gpsUtil

	@Test
	public void getUserLocation() {
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);

		assertEquals(visitedLocation.userId, (user.getUserId()));
	}

	@Test
	public void getTripDeals() {
		List<Provider> providers = tourGuideService.getTripDeals(user);

		assertEquals(5, providers.size());
	}

	@Test
	public void trackUserLocation() {
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	@Test
	public void getNearbyAttractions() {
		Attraction attraction = gpsUtil.getAttractions().get(0);
		VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), attraction, new Date());

		List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation);

		assertEquals(26, attractions.size());
	}

	@Test
	public void distanceBetweenUserAndAttraction() {
		Map<Double, String> distanceBetweenUserAndAttraction = tourGuideService.distanceBetweenUserAndAttraction(user);
		Double firstDistance = distanceBetweenUserAndAttraction.keySet().parallelStream().collect(Collectors.toList())
				.get(0);
		Double secondDistance = distanceBetweenUserAndAttraction.keySet().parallelStream().collect(Collectors.toList())
				.get(1);
		assertTrue(firstDistance < secondDistance);
		// It show that we have calculed the distance and that the distance are sorted.
	}
}
