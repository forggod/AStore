package com.example.android_store.data

import java.util.*

class Product (
    val id: UUID = UUID.randomUUID(),
    var firstname: String="",
    var lastname: String="",
    var midlename: String="",
    var phonenumber: String="",
    var birthdate: Date =Date(0L)
)
