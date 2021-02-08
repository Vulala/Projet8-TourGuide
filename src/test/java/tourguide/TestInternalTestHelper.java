package tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tourguide.helper.InternalTestHelper;
import tourguide.user.User;

public class TestInternalTestHelper {

	private InternalTestHelper internalTestHelper = new InternalTestHelper();
	private User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

	@DisplayName("Shut down the Tracker thread - Can make the tests much slower")
	@AfterEach
	void teardown() {
		internalTestHelper.tracker.stopTracking();
	}

	@Test
	public void addUser() {
		internalTestHelper.addUser(user2);

		User retrievedUser = internalTestHelper.getUser(user2.getUserName());

		assertEquals(user2, retrievedUser);
	}

	@Test
	public void getAllUsers() {
		internalTestHelper.addUser(user2);

		List<User> allUsers = internalTestHelper.getAllUsers();

		assertTrue(allUsers.contains(user2));
	}
}
