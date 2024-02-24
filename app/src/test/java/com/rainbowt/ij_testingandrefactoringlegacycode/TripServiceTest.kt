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
    }

    @Test(expected = UserNotLoggedInException::class)
    fun validate_the_logged_in_user() {

        _loggedInUser = Companion.GUEST


        tripService.getTripsByUser(ANY_USER)
    }

    @Test(expected = UserNotLoggedInException::class)
    fun return_no_trips_when_users_are_not_friends() {

        _loggedInUser = REGISTERED_USER

        val stranger = User()
        stranger.addFriend(ANOTHER_USER)
        stranger.addTrip(LONDON)
        val trips = tripService.getTripsByUser(stranger)

        assertThat(trips).isEmpty()

    }

    private inner class TestableTripService : TripService() {

        override fun loggedInUser(): User? {
            return _loggedInUser
        }
    }

    companion object {
        private val GUEST = null
        private val ANY_USER = User()
        private val REGISTERED_USER = User()
        private val ANOTHER_USER = User()
        private val LONDON = Trip()
    }
}