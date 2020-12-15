package tourguide.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourguide.user.User;
import tourguide.user.UserReward;

/**
 * Service used to calculate the rewards of the user. <br>
 * It also permit to calculate the distance between two locations. <br>
 * The getRewardPoints methods permit to get the amount of point that an
 * attraction give. <br>
 */
@Service
public class RewardsService {

	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
	private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = Integer.MAX_VALUE; // was 200
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;

	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	public void setDefaultProximityBuffer(int defaultProximityBuffer) {
		this.defaultProximityBuffer = defaultProximityBuffer;
	}

	public void setAttractionProximityRange(int attractionProximityRange) {
		this.attractionProximityRange = attractionProximityRange;
	}

	/**
	 * calculateRewards is used to add an {@link UserReward} to the user who visited
	 * a location.
	 * 
	 * @param user
	 */
	public void calculateRewards(User user) {
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsUtil.getAttractions();

		for (VisitedLocation visitedLocation : userLocations) {
			for (Attraction attraction : attractions) {
				if (user.getUserRewards().parallelStream()
						.filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0
						&& nearAttraction(visitedLocation, attraction)) {
					// why does it need to know if the user is close to an attraction?
					user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));

				}
			}
		}
	}

	/**
	 * isWithinAttractionProximity is used to know if the distance between the
	 * attraction and the location of the user is smaller than the
	 * attractionProximityRange which is a variable set at the class field.
	 * 
	 * @param attraction
	 * @param location
	 * @return true, if the user distance from the attraction location is less than
	 *         the attractionProximityRange.
	 */
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) < attractionProximityRange;
	}

	/**
	 * nearAttraction is used to know if the distance between the visited location
	 * and the attraction is smaller than the proximity buffer; which is a variable
	 * set at the class field.
	 * 
	 * @param visitedLocation
	 * @param attraction
	 * @return true if the distance is smaller than the proximity buffer, false
	 *         otherwise.
	 */
	public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) < proximityBuffer;
	}

	/**
	 * getRewardPoints is used to get the points rewarded by visiting a specific
	 * attraction.
	 * 
	 * @param attraction : visited
	 * @param user       : who have visited the attraction
	 * @return int, the points rewarded
	 */
	private int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	/**
	 * getDistance return the distance between two locations in nautical miles.
	 * 
	 * @param loc1 : location 1
	 * @param loc2 : location 2
	 * @return double : distance in nautical mile
	 */
	public double getDistance(Location loc1, Location loc2) {
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);

		double angle = Math
				.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	}

}
