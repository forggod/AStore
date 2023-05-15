package com.example.android_store.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_store.data.Store
import repository.StoreRepository
import java.util.UUID

class StoreCategoryViewModel : ViewModel() {
    var store: MutableLiveData<Store?> = MutableLiveData()
    private lateinit var _storeID: UUID
    fun setStore(storeID: UUID) {
        _storeID = storeID
        StoreRepository.get().storeNet.observeForever {
            if (it != null) {
                store.postValue(it.find { it.id == _storeID })
            }
        }
    }
}