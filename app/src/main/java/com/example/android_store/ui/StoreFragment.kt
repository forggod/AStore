package com.example.android_store.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_store.data.Store
import com.example.android_store.R
import java.util.*

const val STORE_TAG = "StoreFragment"
const val STORE__TITLE = "Магазин"

class StoreFragment : Fragment() {
    private lateinit var rvStore: RecyclerView
    private lateinit var viewModel: StoreViewModel
    private var adapter: StoreListAdapter? = StoreListAdapter(emptyList())

    companion object {
        fun newInstance() = StoreFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_faculty, container, false)
        rvStore = view.findViewById(R.id.rvFaculty)
        rvStore.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(StoreViewModel::class.java)
        viewModel.store.observe(viewLifecycleOwner) {
            adapter = StoreListAdapter(it)
            rvStore.adapter = adapter
        }
        callbacks?.setTitle(STORE__TITLE)
    }

    private inner class StoreHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var store: Store

        fun bind(store: Store) {
            this.store = store
            itemView.findViewById<TextView>(R.id.tv_FacultyElement).text = store.name
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            callbacks?.showCategoryFragment(store.id)
        }
    }

    private inner class StoreListAdapter(private val items: List<Store>) :
        RecyclerView.Adapter<StoreHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        )
                : StoreHolder {
            val view = layoutInflater.inflate(R.layout.layout_faculty_listelement, parent, false)
            return StoreHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: StoreHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    interface Callbacks {
        fun setTitle(_title: String)
        fun showCategoryFragment(StoreID: UUID)
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