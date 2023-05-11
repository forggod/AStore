package com.example.android_store.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android_store.data.Store
import repository.StoreRepository

class StoreViewModel : ViewModel() {
    var store: MutableLiveData<List<Store>> = MutableLiveData()
    init{
        StoreRepository.get().store.observeForever{
            store.postValue(it)
        }
    }
    fun newStore(name:String)=
        StoreRepository.get().newStore(name)
}