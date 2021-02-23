package tourguide.tracker;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackUserService {

	private Logger logger = LoggerFactory.getLogger(TrackUserService.class);

	private Tracker tracker = new Tracker();
	private UserRewardsRunnable userRewardsRunnable = new UserRewardsRunnable();
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private ExecutorService executorService2 = Executors.newCachedThreadPool();
	private ExecutorService executorService3 = Executors.newCachedThreadPool();
	private ExecutorService executorService4 = Executors.newCachedThreadPool();

	public void trackUsersLocationsThreadPool() {
		logger.debug("Submitting the tracking request to the different ThreadPools ...");
		executorService.submit(tracker);
		executorService.submit(tracker);
		executorService.submit(tracker);
		executorService.submit(tracker);
		executorService2.submit(tracker);
		executorService2.submit(tracker);
		executorService2.submit(tracker);
		executorService2.submit(tracker);
		executorService3.submit(tracker);
		executorService3.submit(tracker);
		executorService3.submit(tracker);
		executorService3.submit(tracker);
		executorService4.submit(tracker);
		executorService4.submit(tracker);

		try {
			executorService4.submit(tracker).get();
			TimeUnit.SECONDS.sleep(1); // convenient time to wait for everything to proceed correctly
		} catch (InterruptedException | ExecutionException e) {
			logger.debug("The thread have been interrupted or the time waited is not long enough.");
			Thread.currentThread().interrupt();
		}
		logger.debug("The users have been successfully tracked.");
	}

	public void calculateUsersRewardsThreadPool() {
		logger.debug("Submitting the rewards calculation to the different ThreadPools ...");
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService3.submit(userRewardsRunnable);
		executorService3.submit(userRewardsRunnable);
		executorService3.submit(userRewardsRunnable);
		executorService3.submit(userRewardsRunnable);
		executorService4.submit(userRewardsRunnable);
		executorService4.submit(userRewardsRunnable);

		try {
			executorService4.submit(userRewardsRunnable).get();
			TimeUnit.SECONDS.sleep(1); // convenient time to wait for everything to proceed correctly
		} catch (InterruptedException | ExecutionException e) {
			logger.debug("The thread have been interrupted or the time waited is not long enough.");
			Thread.currentThread().interrupt();
		}
		logger.debug("The users have their rewards been successfully calculated.");
	}
}
