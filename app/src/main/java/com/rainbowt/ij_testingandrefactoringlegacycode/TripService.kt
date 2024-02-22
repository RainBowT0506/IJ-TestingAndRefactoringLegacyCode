package com.rainbowt.ij_testingandrefactoringlegacycode

class TripService {

    @Throws(UserNotLoggedInException::class)
    fun getTripsByUser(user: User): List<Trip> {
        var trips: List<Trip> = listOf()
        val loggedUser = UserSession.getInstance().getLogged()
        var isFriend = false
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
    }
}