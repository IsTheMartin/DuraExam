package isthemartin.xyz.duraexam.Classes

import android.app.Application

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun setConnectionListener(listener: CheckInternet.ConnectionReceiverListener){
        CheckInternet.connectionReceiverListener = listener
    }

    companion object{
        @get:Synchronized
        lateinit var instance: MyApp
    }
}