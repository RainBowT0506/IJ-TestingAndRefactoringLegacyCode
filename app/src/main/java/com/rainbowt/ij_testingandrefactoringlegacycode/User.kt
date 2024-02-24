package com.rainbowt.ij_testingandrefactoringlegacycode

class User {

    private var friends: List<User> = listOf()
        get() = field
        set(value) {
            field = value
        }
    private var trips: List<Trip> = listOf()
        get() = field
        set(value) {
            field = value
        }
}