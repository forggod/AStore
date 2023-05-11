package com.example.android_store.data

import java.util.*

class Product(
    val id: UUID = UUID.randomUUID(),
    var name: String = "",
    var count: String = "",
    var price: String = "",
    var dayManufacture: Date = Date(0L),
    var dayExpiring: Date = Date(0L),
)
