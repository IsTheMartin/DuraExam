package isthemartin.xyz.duraexam.Classes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import isthemartin.xyz.duraexam.Model.Employee
import isthemartin.xyz.duraexam.R

class EmployeesAdapter(private val employeesList: List<Employee>?):
    RecyclerView.Adapter<EmployeeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EmployeeViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        if(employeesList != null)
            return employeesList.size
        return 0
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee: Employee = employeesList!![position]
        holder.bind(employee)
    }
}

class EmployeeViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.employee_item, parent, false)){

    private var tvName: TextView? = null
    private var tvAge : TextView? = null
    private var tvSalary: TextView? = null

    init {
        tvName = itemView.findViewById(R.id.tvName)
        tvAge = itemView.findViewById(R.id.tvAge)
        tvSalary = itemView.findViewById(R.id.tvSalary)
    }

    fun bind(employee: Employee){
        tvName?.text = employee.name
        tvAge?.text = employee.age.toString() + " years old"
        tvSalary?.text = employee.salary.toString()
    }
}