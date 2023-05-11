package com.example.android_store.ui

import androidx.lifecycle.ViewModel
import com.example.android_store.data.Product
import repository.StoreRepository
import java.util.UUID

class StudentViewModel : ViewModel() {

    fun newStudent(groupID: UUID, product: Product) =
        StoreRepository.get().newStudent(groupID, product)

    fun editStudent(groupID: UUID, product: Product)=
        StoreRepository.get().editStudent(groupID,product)
}