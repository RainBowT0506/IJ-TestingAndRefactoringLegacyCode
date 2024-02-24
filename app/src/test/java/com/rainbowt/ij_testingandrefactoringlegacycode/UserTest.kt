package com.rainbowt.ij_testingandrefactoringlegacycode

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UserTest {

    @Test
    fun inform_when_friends_with_another_user() {
        val user = UserBuilder.aUser().friendWith(TRISHA).build()

        assertThat(user.isFriendWith(PAUL))
        assertThat(user.isFriendWith(TRISHA))
    }

    companion object {
        private val TRISHA = User()
        private val PAUL = User()
    }
}