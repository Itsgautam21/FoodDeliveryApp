package com.example.loginApp.models

class KeyModel(var key: String?, var categoryKey: String?) {
    @JvmName("getKey1")
    fun getKey(): String? {
        return key
    }

    @JvmName("getCategoryKey1")
    fun getCategoryKey(): String? {
        return categoryKey
    }
}