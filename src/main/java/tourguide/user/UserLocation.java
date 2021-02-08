package tourguide.user;

import java.util.UUID;

import gpsUtil.location.Location;

/**
 * Entity used to set the location of the user and the user's UUID. <br>
 */
public class UserLocation {

	private UUID userUUID;
	private Location userCoordinates;

	public UserLocation(UUID userUUID, Location userCoordinates) {
		this.userUUID = userUUID;
		this.userCoordinates = userCoordinates;
	}

	public UUID getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(UUID userUUID) {
		this.userUUID = userUUID;
	}

	public Location getUserCoordinates() {
		return userCoordinates;
	}

	public void setUserCoordinates(Location userCoordinates) {
		this.userCoordinates = userCoordinates;
	}

}