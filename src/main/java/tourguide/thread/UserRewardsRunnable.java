package tourguide.thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;
import tourguide.helper.InternalTestHelper;
import tourguide.service.AttractionUtility;
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

	private RewardsService rewardsService = new RewardsService(new GpsUtil(),
			new AttractionUtility(new RewardCentral()));
	private InternalTestHelper internalTestHelper = new InternalTestHelper();

	/**
	 * Run the thread to calculate the rewards for every users. <br>
	 * Here it calculate those set in the internalUserMap = users for test purpose.
	 * <br>
	 */
	@Override
	public void run() {
		internalTestHelper.initializeTheInternalUsers();
		List<User> users = internalTestHelper.getAllUsers();
		logger.debug("Begin calculating the rewards; Calculating {} rewards...", users.size());
		for (User user : users) {
			rewardsService.calculateRewards(user);
		}
	}

}
