package tourguide.helper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourguide.user.User;

/**
 * Class used to set internal test. <br>
 * It permit to define the number of user to create for the tests. <br>
 * The internalUserNumber variable is directly set in each tests. <br>
 * It also give methods to retrieve values from the internal map replacing a DB.
 * <br>
 */
@Service
public class InternalTestHelper {

	private Logger logger = LoggerFactory.getLogger(InternalTestHelper.class);
	private static int internalUserNumber = 1000; // Set this default up to 100,000 for testing

	public static void setInternalUserNumber(int internalUserNumber) {
		InternalTestHelper.internalUserNumber = internalUserNumber;
	}

	public static int getInternalUserNumber() {
		return internalUserNumber;
	}

//	private void addShutDownHook() {
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//			@Override
//			public void run() {
//				tracker.stopTracking();
//			}
//		});
//	}

	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new ConcurrentHashMap<>(); // Was HashMap

	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created {} internal test users.", InternalTestHelper.getInternalUserNumber());
	}

	private void generateUserLocationHistory(User user) {
		Attraction attraction = new GpsUtil().getAttractions().get(0);
		IntStream.range(0, 1).forEach(
				i -> user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date())));
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

	/**
	 * getUser ; getAllUsers ; addUser ; are methods used for test purpose only.
	 * <br>
	 * get a specific user from the internal map.
	 */
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	/**
	 * Convert the internalUserMap into a list.
	 * 
	 * @return a list containing all the users from the local map.
	 */
	public List<User> getAllUsers() {
		return internalUserMap.values().parallelStream().collect(Collectors.toList());
	}

	/**
	 * Add a user to the internal map. <br>
	 * 
	 * @param user
	 */
	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	/**
	 * Initialize users to be used internally for test purpose. <br>
	 */
	public void initializeTheInternalUsers() {
		initializeInternalUsers();
	}
}
