package tourguide.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourguide.helper.InternalTestHelper;
import tourguide.service.RewardsService;
import tourguide.service.TourGuideService;
import tourguide.user.User;

/**
 * Implements {@link Callable} and is used to create a thread that will then
 * track the user's location by using the trackUserLocation method from
 * {@link TourGuideService}. <br>
 *
 */
public class Tracker implements Callable<List<VisitedLocation>> {

	private static final long TRACKING_POLLING_INTERVAL = TimeUnit.MILLISECONDS.toMillis(1);

	private Logger logger = LoggerFactory.getLogger(Tracker.class);

//	private final ExecutorService executorService = Executors.newCachedThreadPool(); // Was newSingleThreadExecutor()
	private GpsUtil gpsUtil = new GpsUtil();
	private TourGuideService tourGuideService = new TourGuideService(gpsUtil,
			new RewardsService(gpsUtil, new RewardCentral()));
	private InternalTestHelper internalTestHelper = new InternalTestHelper(); // both were final
	private boolean stop = false;
	private StopWatch stopWatch = new StopWatch();

	public StopWatch getStopWatch() {
		return stopWatch;
	}

	/**
	 * Assures to shut down the Tracker thread. <br>
	 * It firstly use the shutdown() method and then proceed with the shudownNow()
	 * method in order to correctly end the thread as it is recommended by Oracle.
	 * <br>
	 */
	public void stopTracking() {
		stop = true;
//		executorService.shutdown();
//		try {
//			if (!executorService.awaitTermination(700, TimeUnit.MILLISECONDS)) {
//				executorService.shutdownNow();
//			}
//		} catch (InterruptedException interruptedException) {
//			Thread.currentThread().interrupt();
//			executorService.shutdownNow();
//		}
	}

//	private void addShutDownHook() {
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//			@Override
//			public void run() {
//				this.stopTracking();
//			}
//		});
//	}

	/**
	 * Run the thread to track all the user. <br>
	 * Here it track those set in the internalUserMap = users for test purpose. <br>
	 * 
	 * @return List<VisitedLocation>
	 */
	@Override
	public List<VisitedLocation> call() {
		List<VisitedLocation> listOfVisitedLocation = new ArrayList<>();
		while (true) {
			if (Thread.currentThread().isInterrupted() || stop) {
				logger.debug("The tracker is interrupted or have been asked to stop.");
				Thread.currentThread().interrupt();
				break;
			}

			internalTestHelper.initializeTheInternalUsers();
			List<User> users = internalTestHelper.getAllUsers();
			logger.debug("Begin Tracker; Tracking {} users...", users.size());
//			users.parallelStream().forEach(tourGuideService::trackUserLocation);
			for (User user : users) {
				listOfVisitedLocation.add(tourGuideService.trackUserLocation(user));
			}
//		logger.debug("Tracker Time Elapsed in seconds: {} .", TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
			this.stopTracking();

//			try {
//				logger.debug("The tracker is now sleeping for {} seconds.", TRACKING_POLLING_INTERVAL);
//				TimeUnit.SECONDS.sleep(TRACKING_POLLING_INTERVAL);
//				// this.stopTracking();
//// That because cannot/don't work to send interrupt order from the class where the tracker is used
//			} catch (InterruptedException e) {
//				logger.debug("The thread have been interrupted.");
//				Thread.currentThread().interrupt(); // Restore interrupted state...
//			}
		}
		return listOfVisitedLocation;
	}

}
