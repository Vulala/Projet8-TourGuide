package tourguide.thread.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourguide.thread.Tracker;
import tourguide.thread.UserRewardsRunnable;

/**
 * Class used to execute the different thread defined in the application. <br>
 * Neither it is runnable or callable, it make use of the
 * {@link ExecutorService} interface. <br>
 * The amount of thread used is set with {@link threadAmount} variable. <br>
 * The ExecutorService is shutdown with the {@link stopExecutorService} method.
 * <br>
 */
public class ThreadUserService {

	private Logger logger = LoggerFactory.getLogger(ThreadUserService.class);

	private Tracker tracker = new Tracker();
	private UserRewardsRunnable userRewardsRunnable = new UserRewardsRunnable();
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private int threadAmount = 500;

	/**
	 * trackUsersLocationsThreadPool is used to submit the tracking of every user to
	 * an ExecutorService. <br>
	 * It submit a certain amount of threads set with the {@link threadAmount}
	 * integer. <br>
	 * The ExecutorService is correcly shutdown at the end of the track with the
	 * {@link stopExecutorService} method. <br>
	 * 
	 */
	public void trackUsersLocationsThreadPool() {
		logger.debug("Submitting the tracking request to the different ThreadPools ...");
		for (int t = 1; t < threadAmount; t++) {
			executorService.submit(tracker);
		}

		try {
			executorService.submit(tracker).get();
		} catch (InterruptedException | ExecutionException e) {
			logger.debug("The get() method of the Future class have been interrupted or the result is not available.");
			Thread.currentThread().interrupt();
		}

		stopExecutorService(executorService);
		logger.debug("The users have been successfully tracked.");
	}

	/**
	 * calculateUsersRewardsThreadPool is used to submit the calculation of the
	 * rewards for every user to an ExecutorService. <br>
	 * It submit a certain amount of threads set with the {@link threadAmount}
	 * integer. <br>
	 * The ExecutorService is correcly shutdown at the end of the calculation with
	 * the {@link stopExecutorService} method. <br>
	 * 
	 */
	public void calculateUsersRewardsThreadPool() {
		logger.debug("Submitting the rewards calculation to the different ThreadPools ...");
		for (int t = 1; t < threadAmount; t++) {
			executorService.submit(userRewardsRunnable);
		}

		try {
			executorService.submit(userRewardsRunnable).get();
		} catch (InterruptedException | ExecutionException e) {
			logger.debug("The get() method of the Future class have been interrupted or the result is not available.");
			Thread.currentThread().interrupt();
		}

		stopExecutorService(executorService);
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

	public int getThreadAmount() {
		return threadAmount;
	}

	public void setThreadAmount(int threadAmount) {
		this.threadAmount = threadAmount;
	}
}
