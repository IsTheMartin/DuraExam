package isthemartin.xyz.duraexam

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import isthemartin.xyz.duraexam.Classes.CheckInternet
import isthemartin.xyz.duraexam.Classes.ConnectServer
import isthemartin.xyz.duraexam.Classes.EmployeesAdapter
import isthemartin.xyz.duraexam.Classes.RetrieveEmployees
import isthemartin.xyz.duraexam.Model.Employee
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), CheckInternet.ConnectivityReceiverListener {

    private var snackbar: Snackbar? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefresh: SwipeRefreshLayout? = null
    private var employeesAdapter: EmployeesAdapter? = null
    private var employeesList: MutableList<Employee>? = null
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipeRefresh = findViewById(R.id.srlRefresh)
        registerReceiver(CheckInternet(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    }

    override fun onResume() {
        super.onResume()
        CheckInternet.connectivityReceiverListener = this
    }

    override fun onStart() {
        super.onStart()
        employeesList = mutableListOf()
        settingRecyclerView()
        getDataFromServer()
        srlRefresh.setOnRefreshListener {
            getDataFromServer()
            swipeRefresh?.isRefreshing = false
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(CheckInternet())
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
        if (employeesAdapter == null) {
            employeesAdapter = EmployeesAdapter(employeesList, context)
            employeesAdapter!!.onItemClick = {
                employee ->   
            }
        }

        recyclerView = findViewById(R.id.rcvEmployees)
        if (recyclerView != null) {
            recyclerView!!.apply {
                layoutManager = LinearLayoutManager(
                    context,
                    RecyclerView.VERTICAL,
                    false
                )
                adapter = employeesAdapter
            }
        }
    }

    private fun getDataFromServer(){
        val url = context.resources.getString(R.string.employeeList)

        val cache = DiskBasedCache(cacheDir, 1024*1024)
        val network = BasicNetwork(HurlStack())

        val requestQueue = RequestQueue(cache,network).apply {
            start()
        }

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                response -> Toast.makeText(context, "OK", Toast.LENGTH_LONG).show()
                parsingJsonToList(response.getJSONArray("data"))
            },
            Response.ErrorListener {
                error -> Log.d("Error", error.message)
            }
        )
            requestQueue.add(request)
    }

    private fun parsingJsonToList(jArray: JSONArray){
        if (employeesList != null) {
            employeesList = mutableListOf()
            for (i in 0 until jArray.length()) {
                var jObject: JSONObject = jArray.getJSONObject(i)
                var e = Employee(
                    id = jObject["id"].toString().toInt(),
                    name = jObject["employee_name"].toString(),
                    age = jObject["employee_age"].toString().toInt(),
                    salary = jObject["employee_salary"].toString().toDouble(),
                    profile_image = ""
                )
                employeesList!!.add(e)
            }
            employeesAdapter!!.notifyDataSetChanged()
        }
    }
}
