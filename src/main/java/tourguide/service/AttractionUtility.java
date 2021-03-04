package tourguide.service;

import org.springframework.stereotype.Component;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourguide.user.Coordinates;
import tourguide.user.User;

/**
 * Class used to calculate the distance between two locations. <br>
 * The getRewardPoints methods permit to get the amount of point that an
 * attraction give. <br>
 */
@Component
public class AttractionUtility {

	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	private int defaultProximityBuffer = 10; // in miles
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = Integer.MAX_VALUE;
	private final RewardCentral rewardsCentral;

	public AttractionUtility(RewardCentral rewardsCentral) {
		this.rewardsCentral = rewardsCentral;
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
	 * isWithinAttractionProximity is used to know if the distance between the
	 * attraction and the location of the user is smaller than the
	 * attractionProximityRange which is a variable set at the class field. <br>
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
	 * and the attraction is smaller than the proximity buffer which is a variable
	 * set at the class field. <br>
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
	 * @return int : the points rewarded
	 */
	public int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	/**
	 * getDistance return the distance between two locations in nautical miles. <br>
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

	/**
	 * getDistanceCoordinates return the distance between two coordinates in
	 * nautical miles. <br>
	 * It use the {@link Coordinates} model instead of the {@link Location} model,
	 * while remaining the exact same thing, it allow us to not use this last, not
	 * accessible class without constructor and proper basic methods as getters.
	 * <br>
	 * Which can result in deserialization issues. <br>
	 * 
	 * @param coordinates1
	 * @param coordinates2
	 * @return double : distance in nautical mile
	 */
	public double getDistanceCoordinates(Coordinates coordinates1, Coordinates coordinates2) {
		double lat1 = Math.toRadians(coordinates1.getLatitude());
		double lon1 = Math.toRadians(coordinates1.getLongitude());
		double lat2 = Math.toRadians(coordinates2.getLatitude());
		double lon2 = Math.toRadians(coordinates2.getLongitude());

		double angle = Math
				.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	}
}
