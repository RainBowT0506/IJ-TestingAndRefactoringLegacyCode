package com.rainbowt.ij_testingandrefactoringlegacycode

open class TripService(private val tripDAO: TripDAO) {



    @Throws(UserNotLoggedInException::class)
    fun getTripsByUser(user: User?, loggedInUser: User?): List<Trip> {

        validate(loggedInUser)
        
        return if (user != null && user.isFriendWith(loggedInUser!!)) {
            TripDAO.findTripsByUser(user);
        } else {
            noTrips()
        }
    }

    private fun noTrips(): List<Trip> =
        listOf()

    private fun validate(loggedInUser: User?) {
        if (loggedInUser == null) {
            throw UserNotLoggedInException()
        }
    }

    open fun tripsBy(user: User): List<Trip> {
        return tripDAO.tripsBy(user);
    }
}