package com.example.android_store.ui

import androidx.lifecycle.ViewModel
import com.example.android_store.data.Product
import repository.StoreRepository
import java.util.*

class CategoryListViewModel : ViewModel() {
    fun deleteProduct(categoryID: UUID, product: Product) =
        StoreRepository.get().deleteProduct(categoryID, product)
}