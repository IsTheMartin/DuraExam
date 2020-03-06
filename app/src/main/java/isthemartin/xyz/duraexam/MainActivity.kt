package isthemartin.xyz.duraexam

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import isthemartin.xyz.duraexam.Classes.CheckInternet
import isthemartin.xyz.duraexam.Classes.EmployeesAdapter
import isthemartin.xyz.duraexam.Classes.RetrieveEmployees
import isthemartin.xyz.duraexam.Model.Employee

class MainActivity : AppCompatActivity(), CheckInternet.ConnectivityReceiverListener {

    private var snackbar: Snackbar? = null
    private var recyclerView: RecyclerView? = null
    private var employeesAdapter: EmployeesAdapter? = null
    private var employeesList: List<Employee>? = null
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(CheckInternet(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        settingRecyclerView()

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
                "Check the Internet connection",
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

    private fun settingRecyclerView() {
        if(employeesAdapter == null){
            employeesList = ArrayList<Employee>()
            employeesAdapter = EmployeesAdapter(employeesList)
        }

        recyclerView = findViewById(R.id.rcvEmployees)
        if (recyclerView != null) {
            recyclerView!!.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = employeesAdapter
            }
        }
    }

    fun getEmployeesList(){
        val retrieve = RetrieveEmployees(this)
        retrieve.getDataFromServer()
    }


}
