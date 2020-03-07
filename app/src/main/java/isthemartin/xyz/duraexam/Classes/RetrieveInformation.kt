package isthemartin.xyz.duraexam.Classes

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import isthemartin.xyz.duraexam.Model.Employee
import isthemartin.xyz.duraexam.R
import org.json.JSONArray
import org.json.JSONObject

class RetrieveEmployees(context: Context) {
    var ctx = context

    fun getDataFromServer(employeesAdapter: EmployeesAdapter?): List<Employee> {
        val url = ctx.resources.getString(R.string.employeeList)
        var employeesList: List<Employee> = emptyList()
        if (!url.equals("")) {
            employeesList = createVolleyRequest(url, employeesAdapter)

        }
        return employeesList
    }

    fun createVolleyRequest(url: String, employeesAdapter: EmployeesAdapter?): List<Employee> {
        var queue = Volley.newRequestQueue(ctx)
        var employeesList: List<Employee> = emptyList()

        val stringRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                employeesList = convertJsonToList(response.getJSONArray("data"))
                employeesAdapter!!.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    ctx,
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        queue.add(stringRequest)
        return employeesList
    }

    private fun convertJsonToList(jArray: JSONArray): List<Employee> {
        var employeesList: MutableList<Employee> = mutableListOf()
        for (i in 0 until jArray.length()) {
            var jObject: JSONObject = jArray.getJSONObject(i)
            var e = Employee(
                id = jObject["id"].toString().toInt(),
                name = jObject["employee_name"].toString(),
                age = jObject["employee_age"].toString().toInt(),
                salary = jObject["employee_salary"].toString().toDouble(),
                profile_image = ""
            )
            employeesList.add(e)
        }
        return employeesList
    }

}