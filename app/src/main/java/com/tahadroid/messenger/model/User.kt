package com.tahadroid.messenger.model

data class User(val name: String, val profileImage: String) {
    constructor() : this("", "")
}
