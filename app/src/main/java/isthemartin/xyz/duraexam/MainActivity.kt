package isthemartin.xyz.duraexam

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import isthemartin.xyz.duraexam.Classes.CheckInternet

import isthemartin.xyz.duraexam.Classes.EmployeesAdapter
import isthemartin.xyz.duraexam.Classes.MyApp
import isthemartin.xyz.duraexam.Fragments.EmployeeFragment
import isthemartin.xyz.duraexam.Model.Employee
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), CheckInternet.ConnectionReceiverListener,
    EmployeeFragment.OnFragmentInteractionListener {


    private var snackbar: Snackbar? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefresh: SwipeRefreshLayout? = null
    private var employeesAdapter: EmployeesAdapter? = null
    private var employeesList: MutableList<Employee>? = null
    private val context: Context = this

    private var backPressedTime: Long = 0
    private var backToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipeRefresh = findViewById(R.id.srlRefresh)
        baseContext.registerReceiver(CheckInternet(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        MyApp.instance.setConnectionListener(this)

    }

    override fun onStart() {
        super.onStart()
        employeesList = mutableListOf()
        settingRecyclerView()
        getDataFromServer()
        srlRefresh.setOnRefreshListener {
            if(isOnline(context)) {
                getDataFromServer()
                swipeRefresh?.isRefreshing = false
                showNetworkMessage(true)
            } else {
                showNetworkMessage(false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(CheckInternet())
    }

    //    override fun onBackPressed() {
//        if (backPressedTime + 2000 > System.currentTimeMillis()){
//            backToast!!.cancel()
//            super.onBackPressed()
//        } else {
//            backToast = Toast.makeText(baseContext, "Press back again", Toast.LENGTH_SHORT)
//            backToast!!.show()
//        }
//    }

    private fun showNetworkMessage(isConnected: Boolean) {
        swipeRefresh!!.isEnabled = isConnected
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
            employeesAdapter = EmployeesAdapter(employeesList)
            employeesAdapter!!.onItemClick = {
                employee -> supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainLayout, EmployeeFragment.newInstance(
                    employee.id, employee.name, employee.age, employee.salary
                ), "employee")
                .addToBackStack(null)
                .commit()
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
                response -> parsingJsonToList(response.getJSONArray("data"))
            },
            Response.ErrorListener {
                error -> Log.d("Error", error.message)
            }
        )
            requestQueue.add(request)
    }

    private fun parsingJsonToList(jArray: JSONArray){
        if (employeesList != null) {
            employeesList!!.clear()
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

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
