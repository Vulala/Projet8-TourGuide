package tourguide;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourguide.helper.InternalTestHelper;
import tourguide.service.RewardsService;
import tourguide.tracker.Tracker;
import tourguide.user.User;

public class TestPerformance {

	/*
	 * A note on performance improvements:
	 * 
	 * The number of users generated for the high volume tests can be easily
	 * adjusted via this method:
	 * 
	 * InternalTestHelper.setInternalUserNumber(100000);
	 * 
	 * 
	 * These tests can be modified to suit new solutions, just as long as the
	 * performance metrics at the end of the tests remains consistent.
	 * 
	 * These are performance metrics that we are trying to hit:
	 * 
	 * highVolumeTrackLocation: 100,000 users within 15 minutes:
	 * assertTrue(TimeUnit.MINUTES.toSeconds(15) >=
	 * TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 *
	 * highVolumeGetRewards: 100,000 users within 20 minutes:
	 * assertTrue(TimeUnit.MINUTES.toSeconds(20) >=
	 * TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */

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

	@Test
	public void highVolumeTrackLocation() throws InterruptedException, ExecutionException {
		InternalTestHelper.setInternalUserNumber(100);
// Users should be incremented up to 100,000, and test finishes within 15 minutes
		Tracker tracker = new Tracker();
		ExecutorService executorService = Executors.newCachedThreadPool();
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		Future<List<VisitedLocation>> result = executorService.submit(tracker);
		List<VisitedLocation> listOfTrackedUsers = result.get();
//		tracker.stopTracking(); // AfterEach?
		System.out.println(listOfTrackedUsers);
		stopWatch.stop();

		System.out.format("highVolumeTrackLocation: Time Elapsed: %d seconds.",
				TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	public void highVolumeGetRewards() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(100);
// Users should be incremented up to 100,000, and test finishes within 20 minutes

		InternalTestHelper internalTestHelper = new InternalTestHelper();
		List<User> allUsers = new ArrayList<>();
		allUsers = internalTestHelper.getAllUsers();
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		Attraction attraction = gpsUtil.getAttractions().get(0);
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));
		// Should create a Date variable once and for all (for each !)
		// Add VisitedLocations to each user as it is needed to calculate a reward.
		allUsers.parallelStream().forEach(rewardsService::calculateRewards);

		for (User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		stopWatch.stop();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
				+ " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}
