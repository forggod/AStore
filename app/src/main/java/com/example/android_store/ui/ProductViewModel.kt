package com.example.android_store.ui

import androidx.lifecycle.ViewModel
import com.example.android_store.data.Product
import repository.StoreRepository
import java.util.UUID

class ProductViewModel : ViewModel() {

    fun newProduct(categoryID: UUID, product: Product) =
        StoreRepository.get().newProduct(categoryID, product)

    fun editProduct(categoryID: UUID, product: Product) =
        StoreRepository.get().editProduct(categoryID, product)
}