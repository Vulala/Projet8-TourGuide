package tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourguide.service.AttractionUtility;
import tourguide.user.User;

public class AttractionUtilityTest {

	private GpsUtil gpsUtil = new GpsUtil();
	private AttractionUtility attractionUtilitary = new AttractionUtility(new RewardCentral());
	private Attraction attraction = gpsUtil.getAttractions().get(0);

	@BeforeEach
	void setup() {
		gpsUtil = new GpsUtil();
		attractionUtilitary = new AttractionUtility(new RewardCentral());
	}

	@Test
	public void isWithinAttractionProximity() {
		assertTrue(attractionUtilitary.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAllAttractions() {
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));

		attractionUtilitary.setProximityBuffer(Integer.MAX_VALUE);
		boolean nearAttraction = attractionUtilitary.nearAttraction(user.getLastVisitedLocation(), attraction);

		assertEquals(nearAttraction, true);
	}

}
