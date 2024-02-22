package com.rainbowt.ij_testingandrefactoringlegacycode

class TripDAO {

    companion object {
        private var trips: List<Trip> = listOf()

        fun findTripsByUser(user: User): List<Trip> {
            return trips
        }
    }
}
