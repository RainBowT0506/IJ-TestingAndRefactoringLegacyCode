package com.rainbowt.ij_testingandrefactoringlegacycode


import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


class TripServiceTest {
    private var _loggedInUser: User? = null
    private lateinit var tripService: TripService

    @Before
    fun setUp() {
        tripService = TestableTripService()
        _loggedInUser = REGISTERED_USER
    }

    @Test(expected = UserNotLoggedInException::class)
    fun validate_the_logged_in_user() {

        _loggedInUser = Companion.GUEST


        tripService.getTripsByUser(ANY_USER)
    }

    @Test(expected = UserNotLoggedInException::class)
    fun return_no_trips_when_users_are_not_friends() {
        val stranger = User()
        stranger.addFriend(ANOTHER_USER)
        stranger.addFriend(REGISTERED_USER)
        stranger.addTrip(LONDON)
        stranger.addTrip(BARCELONA)
        val trips = tripService.getTripsByUser(stranger)

        assertThat(trips).isEmpty()
    }

    @Test(expected = UserNotLoggedInException::class)
    fun return_trips_when_users_are_friend() {
        val friend = User()
        friend.addFriend(ANOTHER_USER)
        friend.addFriend(REGISTERED_USER)
        friend.addTrip(LONDON)
        friend.addTrip(BARCELONA)
        val trips = tripService.getTripsByUser(friend)

        assertThat(trips).containsExactlyInAnyOrder(LONDON, BARCELONA)
    }

    private inner class TestableTripService : TripService() {

        override fun loggedInUser(): User? {
            return _loggedInUser
        }

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