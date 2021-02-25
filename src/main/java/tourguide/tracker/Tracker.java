package tourguide.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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

	private GpsUtil gpsUtil = new GpsUtil();
	private TourGuideService tourGuideService = new TourGuideService(gpsUtil,
			new RewardsService(gpsUtil, new RewardCentral()));
	private InternalTestHelper internalTestHelper = new InternalTestHelper(); // both were final

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

		internalTestHelper.initializeTheInternalUsers();
		List<User> users = internalTestHelper.getAllUsers();
		logger.debug("Begin Tracker; Tracking {} users...", users.size());
		for (User user : users) {
			listOfVisitedLocation.add(tourGuideService.trackUserLocation(user));
		}

		return listOfVisitedLocation;
	}

}
