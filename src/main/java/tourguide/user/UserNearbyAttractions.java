package tourguide.user;

import java.util.List;

/**
 * Entity used to set the nearby attractions of the user. <br>
 */
public class UserNearbyAttractions {

	private List<NearbyAttractions> nearbyAttractions;
	private UserLocation userLocation;

	public UserNearbyAttractions(List<NearbyAttractions> nearbyAttractions, UserLocation userLocation) {
		this.nearbyAttractions = nearbyAttractions;
		this.userLocation = userLocation;
	}

	public List<NearbyAttractions> getNearbyAttractions() {
		return nearbyAttractions;
	}

	public void setNearbyAttractions(List<NearbyAttractions> nearbyAttractions) {
		this.nearbyAttractions = nearbyAttractions;
	}

	public UserLocation getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(UserLocation userLocation) {
		this.userLocation = userLocation;
	}

}
