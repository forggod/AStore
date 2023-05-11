package com.example.android_store.data

import java.util.*

class Category(
    val id: UUID = UUID.randomUUID(),
    var name: String = ""
) {
    constructor() : this(UUID.randomUUID())

    var product: List<Product> = emptyList()
}
