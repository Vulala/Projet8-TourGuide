package tourguide;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gpsUtil.GpsUtil;
import tourguide.helper.InternalTestHelper;
import tourguide.thread.service.ThreadUserService;

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

	@DisplayName("Track the user to get his location for an high volume of users.")
	@Test
	public void highVolumeTrackLocation() {
		ThreadUserService threadUserService = new ThreadUserService();
		StopWatch stopWatch = new StopWatch();

		InternalTestHelper.setInternalUserNumber(10);
// Users should be incremented up to 100,000, and test finishes within 15 minutes
// Note : the amount of users tracked depends of the number of users set and the amount of threads used.
		threadUserService.setThreadAmount(1000);

		System.out.format("Number of user's locations to track : %d \n",
				threadUserService.getThreadAmount() * InternalTestHelper.getInternalUserNumber());

		stopWatch.start();
		threadUserService.trackUsersLocationsThreadPool();
		stopWatch.stop();

		System.out.format("highVolumeTrackLocation: Time Elapsed: %d seconds.",
				TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@DisplayName("Calculate the reward for an high volume of users.")
	@Test
	public void highVolumeGetRewards() {
		ThreadUserService threadUserService = new ThreadUserService();
		StopWatch stopWatch = new StopWatch();

		InternalTestHelper.setInternalUserNumber(10);
// Users should be incremented up to 100,000, and test finishes within 20 minutes
// Note : the amount of rewards calculated depends of the number of users set and the amount of threads used.
		threadUserService.setThreadAmount(1000);

		System.out.format("Number of user's rewards to calculate : %d \n",
				threadUserService.getThreadAmount() * InternalTestHelper.getInternalUserNumber());

		stopWatch.start();
		threadUserService.calculateUsersRewardsThreadPool();
		stopWatch.stop();

		System.out.format("highVolumeGetRewards: Time Elapsed: %d seconds.",
				TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}
