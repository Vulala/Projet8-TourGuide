package tourguide;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gpsUtil.GpsUtil;
import tourguide.helper.InternalTestHelper;
import tourguide.tracker.TrackUserService;

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
	public void highVolumeTrackLocation() {
		InternalTestHelper.setInternalUserNumber(66);
// Users should be incremented up to 100,000, and test finishes within 15 minutes
// The current amount of threads used is 15, so the total user tracked will be the previous value x 15.		
		TrackUserService trackUserService = new TrackUserService();
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		trackUserService.trackUsersLocationsThreadPool();
		stopWatch.stop();

		System.out.format("highVolumeTrackLocation: Time Elapsed: %d seconds.",
				TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	public void highVolumeGetRewards() {
		InternalTestHelper.setInternalUserNumber(66);
// Users should be incremented up to 100,000, and test finishes within 20 minutes
// The current amount of threads used is 15, so the total user tracked will be the previous value x 15.	
		TrackUserService trackUserService = new TrackUserService();
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		trackUserService.calculateUsersRewardsThreadPool();
		stopWatch.stop();

		System.out.format("highVolumeGetRewards: Time Elapsed: %d seconds.",
				TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}
