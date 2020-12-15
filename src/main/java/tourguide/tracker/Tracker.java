package tourguide.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourguide.service.TourGuideService;
import tourguide.user.User;

/**
 * Extends {@link Thread} and is used to create a thread that will then track
 * the user location by using the trackUserLocation method from
 * {@link TourGuideService}.
 *
 */
public class Tracker extends Thread {

	private static final long TRACKING_POLLING_INTERVAL = TimeUnit.MINUTES.toSeconds(5);

	private Logger logger = LoggerFactory.getLogger(Tracker.class);

	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	private boolean stop = false;

	public Tracker(TourGuideService tourGuideService) {
		this.tourGuideService = tourGuideService;
		this.executorService.submit(this);
	}

	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}

	/**
	 * Run the tracker to track all the user, here it track thoses set as the
	 * internalUserMap -> users for test purpose.
	 */
	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while (true) {
			if (Thread.currentThread().isInterrupted() || stop) {
				logger.debug("The tracker is interrupted or have been asked to stop");
				break;
			}

			List<User> users = tourGuideService.getAllUsers();
			logger.debug("Begin Tracker. Tracking {} users.", users.size());
			stopWatch.start();
			users.forEach(tourGuideService::trackUserLocation);
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed in seconds: {} .", TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
			stopWatch.reset();

			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(TRACKING_POLLING_INTERVAL);
			} catch (InterruptedException e) {
				logger.error("The thread have been interrupted!", e);
				// Restore interrupted state...
				Thread.currentThread().interrupt();
			}
		}
	}
}
