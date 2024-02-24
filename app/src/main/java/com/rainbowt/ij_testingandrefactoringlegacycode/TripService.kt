package com.rainbowt.ij_testingandrefactoringlegacycode

open class TripService {

    @Throws(UserNotLoggedInException::class)
    fun getTripsByUser(user: User?, loggedInUser: User?): List<Trip> {

        if (loggedInUser == null){
            throw UserNotLoggedInException()
        }

        var trips: List<Trip> = listOf()
        if (user != null && user.isFriendWith(loggedInUser)) {
            trips = TripDAO.findTripsByUser(user);
        }
        return trips
    }

    open fun tripsBy(user: User): List<Trip> {
        return TripDAO.findTripsByUser(user);
    }
}