package tourguide.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourguide.helper.InternalTestHelper;
import tourguide.service.AttractionUtility;
import tourguide.service.LocationService;
import tourguide.user.User;

/**
 * Implements {@link Callable} and is used to create a thread that will then
 * track the user's location by using the trackUserLocation method from
 * {@link LocationService}. <br>
 *
 */
public class Tracker implements Callable<List<VisitedLocation>> {

	private Logger logger = LoggerFactory.getLogger(Tracker.class);

	private GpsUtil gpsUtil = new GpsUtil();
	private LocationService tourGuideService = new LocationService(gpsUtil,
			new AttractionUtility(new RewardCentral()));
	private InternalTestHelper internalTestHelper = new InternalTestHelper();

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
