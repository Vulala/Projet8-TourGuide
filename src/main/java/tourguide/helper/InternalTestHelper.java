package tourguide.helper;

/**
 * Class used to define the number of user to create for the tests. <br>
 * The internalUserNumber variable is directly set in each tests.
 */
public class InternalTestHelper {

	// Set this default up to 100,000 for testing
	private static int internalUserNumber = 100;

	public static void setInternalUserNumber(int internalUserNumber) {
		InternalTestHelper.internalUserNumber = internalUserNumber;
	}

	public static int getInternalUserNumber() {
		return internalUserNumber;
	}
}
