package tourguide.user;

/**
 * Entity used to set the nearby attractions with the requested data (fields).
 * <br>
 */
public class NearbyAttractions {

	private String attractionName;
	private Coordinates coordinates;
	private Double distanceFromTheAttraction;
	private int pointsRewarded;

	public NearbyAttractions() {
	}

	public NearbyAttractions(String attractionName, Coordinates coordinates, Double distanceFromTheAttraction,
			int pointsRewarded) {
		this.attractionName = attractionName;
		this.coordinates = coordinates;
		this.distanceFromTheAttraction = distanceFromTheAttraction;
		this.pointsRewarded = pointsRewarded;
	}

	public String getAttractionName() {
		return attractionName;
	}

	public void setAttractionName(String attractionName) {
		this.attractionName = attractionName;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
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

	@Override
	public String toString() {
		return "NearbyAttractions [attractionName=" + attractionName + ", coordinates=" + coordinates
				+ ", distanceFromTheAttraction=" + distanceFromTheAttraction + ", pointsRewarded=" + pointsRewarded
				+ "]";
	}

}
