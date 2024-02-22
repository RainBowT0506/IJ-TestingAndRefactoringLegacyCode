package com.rainbowt.ij_testingandrefactoringlegacycode

import org.junit.Test

class TripServiceTest {

    @Test(expected = UserNotLoggedInException::class)
    fun validate_the_logged_in_user() {
        val tripService = TripService()

        tripService.getTripsByUser(null)
    }
}