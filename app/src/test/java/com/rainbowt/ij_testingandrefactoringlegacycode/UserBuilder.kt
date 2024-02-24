package com.rainbowt.ij_testingandrefactoringlegacycode

class UserBuilder {
    private var friends: List<User> = listOf()
    private var trips: List<Trip> = listOf()

    fun friendWith(vararg friends: User): UserBuilder {
        this.friends = friends.toList()
        return this
    }

    fun withTripsTo(vararg trips: Trip): UserBuilder {
        this.trips = trips.toList()
        return this
    }

    fun build(): User {
        val user = User()
        addFriendsTo(user)
        addTripsTo(user)
        return user
    }

    companion object {
        fun aUser(): UserBuilder {
            return UserBuilder()
        }
    }

    private fun addFriendsTo(user: User) {
        friends.forEach {
            user.addFriend(it)
        }
    }

    private fun addTripsTo(user: User) {
        trips.forEach {
            user.addTrip(it)
        }
    }
}