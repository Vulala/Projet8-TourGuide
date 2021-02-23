package tourguide.tracker;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;
import tourguide.helper.InternalTestHelper;
import tourguide.service.RewardsService;
import tourguide.user.User;

/**
 * Implements {@link Runnable} and is used to create a thread that will then
 * calculate the user's rewards by using the calculateRewards method from
 * {@link RewardsService}. <br>
 *
 */
public class UserRewardsRunnable implements Runnable {

	private Logger logger = LoggerFactory.getLogger(UserRewardsRunnable.class);

	private RewardsService rewardsService = new RewardsService(new GpsUtil(), new RewardCentral());
	private InternalTestHelper internalTestHelper = new InternalTestHelper();
	private boolean stop = false;

	/**
	 * Assures to shutdown the UserRewardsRunnable thread. <br>
	 */
	public void stopThread() {
		stop = true;
	}

	/**
	 * Run the thread to calculate the rewards for every users. <br>
	 * Here it calculate those set in the internalUserMap = users for test purpose.
	 * <br>
	 */
	@Override
	public void run() {
		while (true) {
			if (Thread.currentThread().isInterrupted() || stop) {
				logger.debug("The UserRewardsRunnable is interrupted or have been asked to stop.");
				Thread.currentThread().interrupt();
				break;
			}

			internalTestHelper.initializeTheInternalUsers();
			List<User> users = internalTestHelper.getAllUsers();
			logger.debug("Begin calculating the rewards; Calculating {} rewards...", users.size());
			for (User user : users) {
				rewardsService.calculateRewards(user);
			}
//			users.parallelStream().forEach(rewardsService::calculateRewards);
			this.stopThread();
		}
	}

}
