package com.rainbowt.ij_testingandrefactoringlegacycode

import org.junit.Test

class TripServiceTest {

    @Test(expected = UserNotLoggedInException::class)
    fun validate_the_logged_in_user() {
        val tripService = TestableTripService()

        tripService.getTripsByUser(null)
    }

    private class TestableTripService : TripService(){

        override fun loggedInUser(): User? {
            return null
        }
    }
}