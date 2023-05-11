package com.example.android_store

import android.app.Application
import android.content.Context
import repository.StoreRepository

class android_storeApplication:Application() {
    override  fun onCreate(){
        super.onCreate()
        StoreRepository.newInstance()
    }

    init{
        instance=this
    }
    companion object{
        private  var instance:android_storeApplication?=null

        fun applicationContex(): Context {
            return instance!!.applicationContext
        }
    }
}