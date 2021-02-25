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
//			TimeUnit.SECONDS.sleep(1); // convenient time to wait for everything to proceed correctly
		} catch (InterruptedException | ExecutionException e) {
			logger.debug("The get() method of the Future class have been interrupted or the result is not available.");
			Thread.currentThread().interrupt();
		}

		stopExecutorService(executorService);
		stopExecutorService(executorService2);
		stopExecutorService(executorService3);
		stopExecutorService(executorService4);
		logger.debug("The users have been successfully tracked.");
	}

	public void calculateUsersRewardsThreadPool() {
		logger.debug("Submitting the rewards calculation to the different ThreadPools ...");
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);
		executorService2.submit(userRewardsRunnable);

		try {
			executorService2.submit(userRewardsRunnable).get();
//			TimeUnit.SECONDS.sleep(1); // convenient time to wait for everything to proceed correctly
		} catch (InterruptedException | ExecutionException e) {
			logger.debug("The get() method of the Future class have been interrupted or the result is not available.");
			Thread.currentThread().interrupt();
		}

		stopExecutorService(executorService);
		stopExecutorService(executorService2);
		logger.debug("The users have their rewards been successfully calculated.");
	}

	/**
	 * Assures to shutdown the ThreadPool. <br>
	 * It firstly use the shutdown() method and then proceed with the shutdownNow()
	 * if the time waited for the completion have been reached. <br>
	 * 
	 * @param executorService : the executorService to shutdown.
	 */
	public void stopExecutorService(ExecutorService executorService) {
		executorService.shutdown();

		try {
			if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
				logger.debug("The await termination time is overdue.");
				executorService.shutdownNow();
			}
		} catch (InterruptedException interruptedException) {
			logger.debug("The thread have been interrupted during the await termination time.");
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}
