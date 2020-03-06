package isthemartin.xyz.duraexam

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import isthemartin.xyz.duraexam.Classes.CheckInternet
import isthemartin.xyz.duraexam.Classes.RetrieveEmployees

class MainActivity : AppCompatActivity(), CheckInternet.ConnectivityReceiverListener {

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(CheckInternet(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        getEmployeesList()
    }

    override fun onResume() {
        super.onResume()
        CheckInternet.connectivityReceiverListener = this
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackbar = Snackbar.make(
                findViewById(R.id.mainLayout),
                "You are offline",
                Snackbar.LENGTH_LONG
            )
            snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackbar?.show()
        } else {
            snackbar?.dismiss()
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    fun getEmployeesList(){
        val retrieve = RetrieveEmployees(this)
        retrieve.getDataFromServer()
    }


}
