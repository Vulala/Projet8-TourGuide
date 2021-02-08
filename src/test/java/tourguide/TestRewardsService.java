package tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourguide.service.RewardsService;
import tourguide.user.User;
import tourguide.user.UserReward;

public class TestRewardsService {

	private GpsUtil gpsUtil = new GpsUtil();
	private RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
	private Attraction attraction = gpsUtil.getAttractions().get(0);

	@BeforeEach
	void setup() {
		gpsUtil = new GpsUtil();
		rewardsService = new RewardsService(gpsUtil, new RewardCentral());
	}

	@Test
	public void userGetRewards() {
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));

		rewardsService.calculateRewards(user);
		List<UserReward> userRewards = user.getUserRewards();
		assertEquals(userRewards.size(), 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

//	@Disabled // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions() {
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));

		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
		boolean nearAttraction = rewardsService.nearAttraction(user.getLastVisitedLocation(), attraction);

		assertEquals(nearAttraction, true);
	}

}
