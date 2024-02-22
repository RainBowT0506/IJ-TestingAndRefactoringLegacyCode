package com.rainbowt.ij_testingandrefactoringlegacycode

class UserSession private constructor() {
    private var isUserLogged = false

    companion object {
        // 使用伴生对象创建单例
        private var instance: UserSession? = null

        fun getInstance(): UserSession {
            if (instance == null) {
                instance = UserSession()
            }
            return instance as UserSession
        }
    }

    fun login() {
        // 模拟用户登录操作
        isUserLogged = true
    }

    fun logout() {
        // 模拟用户登出操作
        isUserLogged = false
    }

    fun getLogged(): Boolean {
        return isUserLogged
    }
}

