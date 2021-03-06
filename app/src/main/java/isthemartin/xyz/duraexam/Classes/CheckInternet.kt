package isthemartin.xyz.duraexam.Classes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class CheckInternet: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        var isConnected = checkConnection(context)
        if(connectionReceiverListener != null)
            connectionReceiverListener!!.onNetworkConnectionChanged(isConnected)
    }

    interface ConnectionReceiverListener{
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    private fun checkConnection(context: Context) :Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting)
    }

    companion object {
        var connectionReceiverListener: ConnectionReceiverListener? = null
        val isConnected: Boolean
        get() {
            val cm = MyApp.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return (activeNetwork != null && activeNetwork.isConnectedOrConnecting)
        }
    }
}