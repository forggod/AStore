package com.example.android_store.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_store.data.Category
import com.example.android_store.data.Product
import com.example.android_store.R
import com.example.android_store.databinding.FragmentGroupListBinding
import java.util.*


class GroupList(private val category: Category) : Fragment() {
    private var _binding: FragmentGroupListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GroupListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupListBinding.inflate(inflater, container, false)
        binding.recycleViewGroupList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleViewGroupList.adapter = GroupListAdapter(category?.product ?: emptyList())
        viewModel = GroupListViewModel()
    }

    private inner class GroupHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var product: Product
        fun bind(product: Product) {
            this.product = product
            val s = "${product.lastname}. ${product.firstname.get(0)}. ${product.midlename.get(0)}."
            itemView.findViewById<TextView>(R.id.tvElement).text = s
            itemView.findViewById<ConstraintLayout>(R.id.edButtons).visibility = View.GONE
            itemView.findViewById<ImageButton>(R.id.ibDelete).setOnClickListener {
                showDeleteDialog(product)
            }
            itemView.findViewById<ImageButton>(R.id.ibEdit).setOnClickListener {
                callbacks?.showStudent(category.id, product)
            }
        }


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val cl = itemView.findViewById<ConstraintLayout>(R.id.edButtons)
            cl.visibility = View.VISIBLE
            lastItemView?.findViewById<ConstraintLayout>(R.id.edButtons)?.visibility = View.GONE
            lastItemView = if (lastItemView == itemView) null else itemView
        }
    }

    private var lastItemView: View? = null

    private fun showDeleteDialog(product: Product) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setMessage("Удалить студента ${product.lastname} ${product.firstname} ${product.midlename} из списка?")
        builder.setTitle("Подтверждение")
        builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
            viewModel.deleteStudent(category.id, product)
        }
        builder.setNegativeButton("Отмена", null)
        val alert = builder.create()
        alert.show()
    }

    private inner class GroupListAdapter(private val items: List<Product>) :
        RecyclerView.Adapter<GroupHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
            val view = layoutInflater.inflate(R.layout.layout_student_listelement, parent, false)

            return GroupHolder(view)
        }

        override fun getItemCount(): Int = items.size
        override fun onBindViewHolder(holder: GroupHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    interface Callbacks {
        fun showStudent(groupID: UUID, product: Product?)
    }

    var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }
}