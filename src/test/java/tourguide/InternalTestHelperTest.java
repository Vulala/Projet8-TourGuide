package tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import tourguide.helper.InternalTestHelper;
import tourguide.user.User;

public class InternalTestHelperTest {

	private InternalTestHelper internalTestHelper = new InternalTestHelper();
	private User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

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

	@Test
	public void setAndInitializeTheInternalUsers() {
		InternalTestHelper.setInternalUserNumber(10);
		internalTestHelper.initializeTheInternalUsers();
		List<User> allUsers = internalTestHelper.getAllUsers();

		assertEquals(allUsers.size(), 10);
	}

}
