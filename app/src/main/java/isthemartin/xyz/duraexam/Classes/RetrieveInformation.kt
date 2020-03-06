package isthemartin.xyz.duraexam.Classes

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import isthemartin.xyz.duraexam.R

class RetrieveEmployees(context: Context) {
    var ctx = context

    fun getDataFromServer() {
        val url = ctx.resources.getString(R.string.employee)
        if (!url.equals("")) {
            createVolleyRequest(url)
        }
    }

    fun createVolleyRequest(url: String) {
        //val queue = Volley.newRequestQueue(ctx)

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> {
                    response -> Log.d("Volley", response.toString()) },
            Response.ErrorListener { Log.e("Volley", "Error") }
        )
        //queue.add(stringRequest)
    }

}