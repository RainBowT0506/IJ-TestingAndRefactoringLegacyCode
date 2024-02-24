package com.rainbowt.ij_testingandrefactoringlegacycode

open class TripDAO {
    fun tripsBy(user: User): List<Trip> {
        return findTripsByUser(user)
    }

    companion object {
        private var trips: List<Trip> = listOf()

        fun findTripsByUser(user: User): List<Trip> {
            return trips
        }
    }
}
