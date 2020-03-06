package isthemartin.xyz.duraexam

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

//    @Test
//    fun createVolleyRequest(url: String) {
//        val queue = Volley.newRequestQueue(this)
//
//        val stringRequest = StringRequest(
//            Request.Method.GET,
//            url,
//            Response.Listener<String> {
//                    response -> Log.d("Volley", response.toString()) },
//            Response.ErrorListener { Log.e("Volley", "Error") }
//        )
//        queue.add(stringRequest)
//    }
}
