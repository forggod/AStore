package com.example.android_store.ui

import androidx.lifecycle.ViewModel
import com.example.android_store.data.Product
import repository.StoreRepository
import java.util.*

class GroupListViewModel: ViewModel() {
    fun deleteStudent(groupID: UUID, product: Product)=
        StoreRepository.get().deleteStudent(groupID,product)
}