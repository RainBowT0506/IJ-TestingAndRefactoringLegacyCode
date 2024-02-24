package com.rainbowt.ij_testingandrefactoringlegacycode

class User {
    fun addFriend(anotherUser: User) {
        friends.toMutableList().add(anotherUser)
    }

    fun addTrip(trip: Trip) {
        trips.toMutableList().add(trip)
    }

    var friends: List<User> = listOf()
        get() = field
        set(value) {
            field = value
        }
    var trips: List<Trip> = listOf()
        get() = field
        set(value) {
            field = value
        }
}