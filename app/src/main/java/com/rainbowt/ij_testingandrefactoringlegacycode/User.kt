package com.rainbowt.ij_testingandrefactoringlegacycode

class User {

    private var friends: List<User> = listOf()

    fun getFriends(): List<User> {
        return friends
    }
}