package tourguide.user;

import gpsUtil.location.Location;

/**
 * Entity used to set the nearby attractions with the requested data. <br>
 */
public class NearbyAttractions {

	private String attractionName;
	private Location attractionLocation;
	private Double distanceFromTheAttraction;
	private int pointsRewarded;

	public NearbyAttractions(String attractionName, Location attractionLocation, Double distanceFromTheAttraction,
			int pointsRewarded) {
		this.attractionName = attractionName;
		this.attractionLocation = attractionLocation;
		this.distanceFromTheAttraction = distanceFromTheAttraction;
		this.pointsRewarded = pointsRewarded;
	}

	public String getAttractionName() {
		return attractionName;
	}

	public void setAttractionName(String attractionName) {
		this.attractionName = attractionName;
	}

	public Location getAttractionLocation() {
		return attractionLocation;
	}

	public void setAttractionLocation(Location attractionLocation) {
		this.attractionLocation = attractionLocation;
	}

	public Double getDistanceFromTheAttraction() {
		return distanceFromTheAttraction;
	}

	public void setDistanceFromTheAttraction(Double distanceFromTheAttraction) {
		this.distanceFromTheAttraction = distanceFromTheAttraction;
	}

	public int getPointsRewarded() {
		return pointsRewarded;
	}

	public void setPointsRewarded(int pointsRewarded) {
		this.pointsRewarded = pointsRewarded;
	}

}
