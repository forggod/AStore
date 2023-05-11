package com.example.android_store.data

import java.util.*

class Store(
    val id: UUID = UUID.randomUUID(),
    var name: String = ""
) {
    constructor() : this(UUID.randomUUID())

    var categories: List<Category> = emptyList()
}
