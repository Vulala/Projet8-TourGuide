package tourguide.user;

import java.util.UUID;

/**
 * Entity used to set the user's coordinates and the user's UUID. <br>
 */
public class UserCoordinates {

	private UUID userUUID;
	private Coordinates coordinates;

	public UserCoordinates() {
	}

	public UserCoordinates(UUID userUUID, Coordinates coordinates) {
		this.userUUID = userUUID;
		this.coordinates = coordinates;
	}

	public UUID getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(UUID userUUID) {
		this.userUUID = userUUID;
	}

	public Coordinates getUserCoordinates() {
		return coordinates;
	}

	public void setUserCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public String toString() {
		return "UserCoordinates [userUUID=" + userUUID + ", userCoordinates=" + coordinates + "]";
	}

}