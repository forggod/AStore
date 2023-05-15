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
import com.example.android_store.databinding.FragmentCategoryListBinding
import java.util.*


class CategoryList(private val category: Category) : Fragment() {
    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CategoryListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        binding.rvCategoryList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCategoryList.adapter = CategoryListAdapter(category?.product ?: emptyList())
        viewModel = CategoryListViewModel()
    }

    private inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var product: Product
        fun bind(product: Product) {
            this.product = product
            val s = product.name
            itemView.findViewById<TextView>(R.id.tvElement).text = s

            itemView.findViewById<ConstraintLayout>(R.id.edButtons).visibility = View.GONE
            itemView.findViewById<ImageButton>(R.id.ibDelete).setOnClickListener {
                showDeleteDialog(product)
            }
            itemView.findViewById<ImageButton>(R.id.ibEdit).setOnClickListener {
                callbacks?.showProduct(category.id, product)
            }
            itemView.setOnLongClickListener{
                onLongClick(itemView)
            }
        }


        init {
            itemView.setOnClickListener(this)
        }

        fun onLongClick(v:View?): Boolean {
            showDeleteDialog(product)
            return true
        }
        override fun onClick(v: View) {
            callbacks?.showProduct(category.id, product)
        }
    }

    private var lastItemView: View? = null

    private fun showDeleteDialog(product: Product) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setMessage("Удалить товар ${product.name} из списка?")
        builder.setTitle("Подтверждение")
        builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
            viewModel.deleteProduct(category.id, product)
        }
        builder.setNegativeButton("Отмена", null)
        val alert = builder.create()
        alert.show()
    }

    private inner class CategoryListAdapter(private val items: List<Product>) :
        RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            val view = layoutInflater.inflate(R.layout.product_listelement, parent, false)
            return CategoryHolder(view)
        }

        override fun getItemCount(): Int = items.size
        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    interface Callbacks {
        fun showProduct(categoryID: UUID, product: Product?)
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