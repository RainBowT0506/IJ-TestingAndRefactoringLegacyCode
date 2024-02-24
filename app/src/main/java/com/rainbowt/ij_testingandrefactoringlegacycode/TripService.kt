package com.rainbowt.ij_testingandrefactoringlegacycode

open class TripService {

    @Throws(UserNotLoggedInException::class)
    fun getTripsByUser(user: User?): List<Trip> {

        val loggedUser = loggedInUser()

        if (user != null && loggedUser != null) {
            var trips: List<Trip> = listOf()
            if (user.isFriendWith(loggedUser)) {
                trips = TripDAO.findTripsByUser(user);
            }
            return trips
        } else {
            throw UserNotLoggedInException()
        }
    }

    open fun tripsBy(user: User): List<Trip> {
        return TripDAO.findTripsByUser(user);
    }

    open fun loggedInUser(): User? {
        return UserSession.getInstance().getLoggedUser()
    }
}