package com.seamfix.common

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import kotlinx.coroutines.delay
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/*** A class that helps check the network status of the device ***/

class NetworkChecker @Inject constructor (@ApplicationContext var context: Context) {


    // this live data will be true if the app can connect to a  network
    companion object{
        val canConnect =  MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    suspend fun listenForNetworkChanges() {
        while (true){
            if(haveNetworkConnection(context)){
                canConnect.postValue(true)
            }else{
               canConnect.postValue(false)
            }
            Log.d(NetworkChecker::class.simpleName,"Checking network health...")
            delay(5_000)
        }
    }


    /***  Returns true of the device has a network or wifi connection ***/
    private fun haveNetworkConnection(context: Context): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true) && ni.isConnected) {
                haveConnectedWifi = true
            }
            if (ni.typeName.equals("MOBILE", ignoreCase = true) && ni.isConnected) {
                haveConnectedMobile = true
            }
        }
        return haveConnectedWifi || haveConnectedMobile
    }
}