package com.rainbowt.ij_testingandrefactoringlegacycode

open class TripService {

    @Throws(UserNotLoggedInException::class)
    fun getTripsByUser(user: User?): List<Trip> {
        var trips: List<Trip> = listOf()
        val loggedUser = loggedInUser()
        var isFriend = false

        user?.let {
            for (friend in user.getFriends()) {
                if (friend.equals(loggedUser)) {
                    isFriend = true
                    break
                }
            }
            if (isFriend) {
                trips = TripDAO.findTripsByUser(user);
            }

            return trips
        } ?: throw UserNotLoggedInException()
    }

    open fun loggedInUser(): User? {
        return UserSession.getInstance().getLoggedUser()
    }
}