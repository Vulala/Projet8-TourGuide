package tourguide.user;

import java.util.List;

/**
 * Entity used to set the nearby attractions of the user. <br>
 */
public class UserNearbyAttractions {

	private List<NearbyAttractions> nearbyAttractions;
	private UserCoordinates userCoordinates;

	public UserNearbyAttractions() {
	}

	public UserNearbyAttractions(List<NearbyAttractions> nearbyAttractions, UserCoordinates userCoordinates) {
		this.nearbyAttractions = nearbyAttractions;
		this.userCoordinates = userCoordinates;
	}

	public List<NearbyAttractions> getNearbyAttractions() {
		return nearbyAttractions;
	}

	public void setNearbyAttractions(List<NearbyAttractions> nearbyAttractions) {
		this.nearbyAttractions = nearbyAttractions;
	}

	public UserCoordinates getUserCoordinates() {
		return userCoordinates;
	}

	public void setUserCoordinates(UserCoordinates userCoordinates) {
		this.userCoordinates = userCoordinates;
	}

	@Override
	public String toString() {
		return "UserNearbyAttractions [nearbyAttractions=" + nearbyAttractions + ", userCoordinates=" + userCoordinates
				+ "]";
	}

}
