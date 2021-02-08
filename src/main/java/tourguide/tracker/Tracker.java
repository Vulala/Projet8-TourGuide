package tourguide.tracker;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourguide.helper.InternalTestHelper;
import tourguide.service.TourGuideService;
import tourguide.user.User;

/**
 * Extends {@link Thread} and is used to create a thread that will then track
 * the user's location by using the trackUserLocation method from
 * {@link TourGuideService}. <br>
 *
 */
public class Tracker extends Thread {

	private static final long TRACKING_POLLING_INTERVAL = TimeUnit.MINUTES.toSeconds(5);

	private Logger logger = LoggerFactory.getLogger(Tracker.class);

	private final ExecutorService executorService = Executors.newCachedThreadPool(); // Was newSingleThreadExecutor()
	private TourGuideService tourGuideService;
	private final InternalTestHelper internalTestHelper; // both were final
	private boolean stop = false;

//	public Tracker(TourGuideService tourGuideService) {
//		this.tourGuideService = tourGuideService;
//		this.executorService.submit(this);
//	}

	public Tracker(InternalTestHelper internalTestHelper) {
		this.internalTestHelper = internalTestHelper;
		this.executorService.submit(this);
	}

	/**
	 * Assures to shut down the Tracker thread. <br>
	 * It firstly use the shutdown() method and then proceed with the shudownNow()
	 * method in order to correctly end the thread as it is recommended by Oracle.
	 * <br>
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(700, TimeUnit.MILLISECONDS)) {
				executorService.shutdownNow();
			}
		} catch (InterruptedException interruptedException) {
			Thread.currentThread().interrupt();
			executorService.shutdownNow();
		}
	}

	/**
	 * Build a map with the user's UUID as key and the user as value. <br>
	 * 
	 * @param listUser
	 * @return a Map with the UUID of each user; the {@link User} list.
	 */
	public Map<UUID, User> listToMap(List<User> listUser) {
		return listUser.stream().collect(Collectors.toMap(User::getUserId, user -> user));
	}

	/**
	 * Run the tracker to track all the user. <br>
	 * Here it track thoses set in the internalUserMap -> users for test purpose.
	 * <br>
	 */
	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while (true) {
			if (Thread.currentThread().isInterrupted() || stop) {
				logger.debug("The tracker is interrupted or have been asked to stop.");
				break;
			}

			List<User> users = internalTestHelper.getAllUsers();
			logger.debug("Begin Tracker; Tracking {} users...", users.size());
			stopWatch.start();
			users.forEach(tourGuideService::trackUserLocation);
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed in seconds: {} .", TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
			stopWatch.reset();

			try {
				logger.debug("The tracker is now sleeping for {} seconds.", TRACKING_POLLING_INTERVAL);
				TimeUnit.SECONDS.sleep(TRACKING_POLLING_INTERVAL);
			} catch (InterruptedException e) {
				logger.debug("The thread have been interrupted.");
				Thread.currentThread().interrupt(); // Restore interrupted state...
			}
		}
	}
}
