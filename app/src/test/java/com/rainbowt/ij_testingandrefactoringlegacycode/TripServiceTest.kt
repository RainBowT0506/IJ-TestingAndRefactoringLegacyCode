package com.rainbowt.ij_testingandrefactoringlegacycode


import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


class TripServiceTest {
    private lateinit var tripService: TripService

    @Before
    fun setUp() {
        tripService = TestableTripService()
    }

    @Test(expected = UserNotLoggedInException::class)
    fun validate_the_logged_in_user() {

        tripService.getTripsByUser(ANY_USER, Companion.GUEST)
    }

    @Test(expected = UserNotLoggedInException::class)
    fun return_no_trips_when_users_are_not_friends() {
        val stranger = UserBuilder.aUser()
            .friendWith(ANOTHER_USER)
            .withTripsTo(LONDON)
            .build()
        val trips = tripService.getTripsByUser(stranger, REGISTERED_USER)

        assertThat(trips).isEmpty()
    }

    @Test(expected = UserNotLoggedInException::class)
    fun return_trips_when_users_are_friend() {
        val friend = UserBuilder.aUser()
            .friendWith(ANOTHER_USER, REGISTERED_USER)
            .withTripsTo(LONDON, BARCELONA)
            .build()

        val trips = tripService.getTripsByUser(friend, REGISTERED_USER)

        assertThat(trips).containsExactlyInAnyOrder(LONDON, BARCELONA)
    }

    private inner class TestableTripService : TripService() {

        override fun tripsBy(user: User): List<Trip> {
            return user.trips
        }
    }

    companion object {
        private val GUEST = null
        private val ANY_USER = User()
        private val REGISTERED_USER = User()
        private val ANOTHER_USER = User()
        private val LONDON = Trip()
        private val BARCELONA = Trip()
    }
}
