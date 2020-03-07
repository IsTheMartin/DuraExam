package isthemartin.xyz.duraexam.Classes

import android.app.Application
import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class ConnectServer: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val requestQueue: RequestQueue? = null
    get(){
        if(field == null){
            return Volley.newRequestQueue(applicationContext)
        }
        return field
    }

    fun <T> addToRequestQueue(request: Request<T>){
        request.tag = TAG
        requestQueue?.add(request)
    }

    companion object{
        private val TAG = ConnectServer::class.java.simpleName
        @get:Synchronized var instance: ConnectServer? = null
        private set
    }
}