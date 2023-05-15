package com.example.android_store.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_store.data.Store
import repository.StoreRepository
import java.util.UUID

class StoreViewModel : ViewModel() {
    var storeNet: MutableLiveData<List<Store>> = MutableLiveData()

    init {
        StoreRepository.get().storeNet.observeForever {
            storeNet.postValue(it)
        }
    }

    fun newStore(name: String) =
        StoreRepository.get().newStore(name)

}