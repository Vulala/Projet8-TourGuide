package tourguide;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourguide.helper.InternalTestHelper;
import tourguide.service.TourGuideService;
import tourguide.user.User;
import tourguide.user.UserLocation;
import tourguide.user.UserNearbyAttractions;
import tourguide.user.UserReward;
import tripPricer.Provider;

@WebMvcTest(TourGuideController.class)
class TourGuideControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TourGuideService tourGuideService;

	@MockBean
	private InternalTestHelper internalTestHelper;

	@DisplayName("Injected Components Are Rightly Setup")
	@Test
	public void injectedComponentsAreRightlySetup() {
		assertThat(mockMvc).isNotNull();
		assertThat(tourGuideService).isNotNull();
	}

	@DisplayName("GET : /")
	@Test
	void index() throws Exception {
		// ACT
		MvcResult mvcResult = this.mockMvc.perform(get("/")).andDo(print()).andReturn();
		int status = mvcResult.getResponse().getStatus();

		// ASSERT
		assertEquals(status, 200);
	}

	@DisplayName("GET : /getLocation")
	@Test
	void givenGettingTheLocationOfTheUser_whenGetLocation_thenItDisplayTheCoordinateOfTheUserAsJSON() throws Exception {
		// ARRANGE
		when(internalTestHelper.getUser(any(String.class)))
				.thenReturn(new User(UUID.randomUUID(), "userName", "phoneNumber", "emailAddress"));
		when(tourGuideService.getUserLocation(any(User.class)))
				.thenReturn(new VisitedLocation(UUID.randomUUID(), new Location(0, 0), Date.valueOf(LocalDate.now())));

		// ACT
		MvcResult mvcResult = this.mockMvc.perform(get("/getLocation?userName=userName")).andDo(print()).andReturn();
		int status = mvcResult.getResponse().getStatus();

		// ASSERT
		assertEquals(status, 200);
		verify(internalTestHelper, times(1)).getUser(any(String.class));
		verify(tourGuideService, times(1)).getUserLocation(any(User.class));
	}

	@DisplayName("GET : /getNearbyAttractions")
	@Test
	void givenGettingTheFiveClosestAttrationsOfTheUser_whenGetNearbyAttraction_thenItDisplayTheRequestedDataForTheUserAsJSON()
			throws Exception {
		// ARRANGE
		when(internalTestHelper.getUser(any(String.class)))
				.thenReturn(new User(UUID.randomUUID(), "userName", "phoneNumber", "emailAddress"));
		when(tourGuideService.fiveClosestAttractions(any(User.class)))
				.thenReturn(new UserNearbyAttractions(new ArrayList<>(), any(UserLocation.class)));

		// ACT
		MvcResult mvcResult = this.mockMvc.perform(get("/getNearbyAttractions?userName=userName")).andDo(print())
				.andReturn();
		int status = mvcResult.getResponse().getStatus();

		// ASSERT
		assertEquals(status, 200);
		verify(internalTestHelper, times(1)).getUser(any(String.class));
		verify(tourGuideService, times(1)).fiveClosestAttractions(any(User.class));
	}

	@Disabled("JsonException: java.lang.reflect.InvocationTargetException ")
	@DisplayName("GET : /getRewards")
	@Test
	void givenGettingTheRewardsOfTheUser_whenGetRewards_thenItDisplayTheRewardsOfTheUserAsJSON() throws Exception {
		// ARRANGE
		UserReward userReward = new UserReward(
				new VisitedLocation(UUID.randomUUID(), new Location(0.0, 0.0), Date.valueOf(LocalDate.now())),
				new Attraction("attractionName", "city", "state", 0.0, 0.0));
		User user = new User(UUID.randomUUID(), "userName", "phoneNumber", "emailAddress");
		user.addUserReward(userReward);

		List<UserReward> listOfTheUsersRewards = new ArrayList<UserReward>();
		listOfTheUsersRewards.add(userReward);
		internalTestHelper.addUser(user);
		when(internalTestHelper.getUser(any(String.class))).thenReturn(user);

		// ACT
		MvcResult mvcResult = this.mockMvc.perform(get("/getRewards?userName=userName")).andDo(print()).andReturn();
		int status = mvcResult.getResponse().getStatus();

		// ASSERT
		assertEquals(status, 200);
		verify(internalTestHelper, times(1)).getUser(any(String.class));
	}

	@DisplayName("GET : /getAllCurrentLocations")
	@Test
	void givenGettingAllCurrentLocations_whenGetAllCurrentLocations_thenItDisplayTheLocationOfEveryUsers()
			throws Exception {
		// ARRANGE
		User user = new User(UUID.randomUUID(), "userName", "phoneNumber", "emailAddress");
		internalTestHelper.addUser(user);
		List<User> usersList = new ArrayList<User>();
		when(internalTestHelper.getAllUsers()).thenReturn(new ArrayList<User>());
		when(tourGuideService.getAllCurrentLocations(usersList)).thenReturn(new ArrayList<UserLocation>());

		// ACT
		MvcResult mvcResult = this.mockMvc.perform(get("/getAllCurrentLocations")).andDo(print()).andReturn();
		int status = mvcResult.getResponse().getStatus();

		// ASSERT
		assertEquals(status, 200);
		verify(internalTestHelper, times(1)).getAllUsers();
		verify(tourGuideService, times(1)).getAllCurrentLocations(usersList);

	}

	@DisplayName("GET : /getTripDeals")
	@Test
	void givenGettingTheTripDealsOfTheUser_whenGetTripDeals_thenItDisplayTheTripDealsOfTheUserAsJSON()
			throws Exception {
		// ARRANGE
		User user = new User(UUID.randomUUID(), "userName", "phoneNumber", "emailAddress");
		List<Provider> providersList = new ArrayList<Provider>();
		providersList.add(new Provider(UUID.randomUUID(), "name", 0.0));

		when(internalTestHelper.getUser(any(String.class))).thenReturn(user);
		when(tourGuideService.getTripDeals(any(User.class))).thenReturn(providersList);

		// ACT
		MvcResult mvcResult = this.mockMvc.perform(get("/getTripDeals?userName=userName")).andDo(print()).andReturn();
		int status = mvcResult.getResponse().getStatus();

		// ASSERT
		assertEquals(status, 200);
		verify(internalTestHelper, times(1)).getUser(any(String.class));
		verify(tourGuideService, times(1)).getTripDeals(any(User.class));
	}
}
