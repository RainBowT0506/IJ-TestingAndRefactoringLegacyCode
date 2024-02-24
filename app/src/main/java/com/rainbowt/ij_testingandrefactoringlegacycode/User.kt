package com.rainbowt.ij_testingandrefactoringlegacycode

class User {
    private val _friends: MutableList<User> = mutableListOf()
    val friends: List<User>
        get() = _friends

    private val _trips: MutableList<Trip> = mutableListOf()
    val trips: List<Trip>
        get() = _trips

    fun addFriend(anotherUser: User) {
        _friends.add(anotherUser)
    }

    fun addTrip(trip: Trip) {
        _trips.add(trip)
    }
}
