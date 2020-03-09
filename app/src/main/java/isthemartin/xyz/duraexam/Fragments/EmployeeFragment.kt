package isthemartin.xyz.duraexam.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import isthemartin.xyz.duraexam.Model.Employee

import isthemartin.xyz.duraexam.R
import kotlinx.android.synthetic.main.fragment_employee.*
import org.json.JSONArray
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EmployeeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EmployeeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmployeeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Toast.makeText(activity, param1.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View? = inflater.inflate(R.layout.fragment_employee, container, false)
        getDataFromServer()
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun getDataFromServer() {
        val url = activity!!.resources.getString(R.string.employee) + param1.toString()

        val cache = DiskBasedCache(activity!!.cacheDir, 1024 * 1024)
        val network = BasicNetwork(HurlStack())

        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                parsingJsonToList(response.getJSONArray("data"))
            },
            Response.ErrorListener { error ->
                Log.d("Error", error.message)
            }
        )
        requestQueue.add(request)
    }

    private fun parsingJsonToList(jArray: JSONArray) {
        var jObject: JSONObject = jArray.getJSONObject(0)
        tvName.text = jObject["employee_name"].toString()
        tvAge.text = jObject["employee_age"].toString() + " years old"
        tvSalary.text = jObject["employee_salary"].toString()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EmployeeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            EmployeeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
