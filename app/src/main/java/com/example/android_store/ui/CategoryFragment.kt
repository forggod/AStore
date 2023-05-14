package com.example.android_store.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android_store.R
import com.example.android_store.data.Category
import com.example.android_store.data.Store
import com.example.android_store.data.Product
import com.example.android_store.databinding.FragmentCategoryBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.selects.select
import repository.StoreRepository
import java.util.*

const val CATEGORY_TAG = "CategoryFragment"

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    companion object {
        private lateinit var _storeID: UUID
        fun newInstance(storeID: UUID): CategoryFragment {
            _storeID = storeID
            return CategoryFragment()
        }

        val getStoreID get() = _storeID
    }

    private lateinit var viewModel: StoreCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(StoreCategoryViewModel::class.java)
        viewModel.setStore(_storeID)
        viewModel.store.observe(viewLifecycleOwner) {
            updateUI(it)
            callbacks?.setTitle(it?.name ?: "")
        }
    }

    private var tabPosition: Int = 0

    private fun updateUI(Store: Store?) {
        binding.tabLayoutCaregory.clearOnTabSelectedListeners()
        binding.tabLayoutCaregory.removeAllTabs()
        for (i in 0 until (Store?.categories?.size ?: 0)) {
            binding.tabLayoutCaregory.addTab(binding.tabLayoutCaregory.newTab().apply {
                text = i.toString()
            })
        }

        binding.fabAddProduct.visibility =
            if ((Store?.categories?.size ?: 0) == 0)
                View.GONE
            else {
                binding.fabAddProduct.setOnClickListener {
                    callbacks?.showProduct(Store!!.categories!!.get(tabPosition).id, null)
                }
                View.VISIBLE
            }

        binding.fabActionCategory.visibility =
            if ((Store?.categories?.size ?: 0) == 0)
                View.GONE
            else {
                binding.fabActionCategory.setOnClickListener {
                    showAEDialog(Store)
                }
                View.VISIBLE
            }

        val adapter = CategoryPageAdapter(requireActivity(), Store!!)
        binding.viewPageCategory.adapter = adapter
        TabLayoutMediator(
            binding.tabLayoutCaregory,
            binding.viewPageCategory,
            true,
            true
        ) { tab, pos ->
            tab.text = Store?.categories?.get(pos)?.name
        }.attach()
        if (tabPosition < binding.tabLayoutCaregory.tabCount) {
            binding.tabLayoutCaregory.setScrollPosition(tabPosition, 0f, true)
            binding.viewPageCategory.currentItem = tabPosition
        }

        binding.tabLayoutCaregory.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPosition = tab?.position!!
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun showAEDialog(Store: Store?) {
        val tabName =
            binding.tabLayoutCaregory.getTabAt(binding.tabLayoutCaregory.selectedTabPosition)?.text.toString()
        val category = Store?.categories?.find { it.name == tabName }
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setMessage("Что сделать с категорией ${category?.name}?")
        builder.setPositiveButton("Удалить") { _, _ ->
            val list = Store?.categories as ArrayList<Category>
            list.remove(category)
            Store.categories = list
            updateUI(Store)
        }
        builder.setNegativeButton("Изменить") { _, _ ->
            val builder = context?.let { AlertDialog.Builder(it) }
            if (builder != null) {
                builder.setCancelable(true)
                val dialogView = LayoutInflater.from(context).inflate(R.layout.input_name, null)
                builder.setView(dialogView)
                val nameInput = dialogView.findViewById(R.id.tv_name) as EditText
                val tvInfo = dialogView.findViewById(R.id.tv_info) as TextView
                builder.setTitle(getString(R.string.inputTitle))
                tvInfo.text = getString(R.string.inputGroup)
                if (category != null) {
                    nameInput.setText(category.name)
                }
                builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
                    val s = nameInput.text.toString()
                    if (s.isNotBlank()) {
                        val list = Store?.categories as ArrayList<Category>
                        if (category != null) {
                            category.name = nameInput.text.toString()
                            list.set(list.indexOf(category), category)
                        }
                        Store.categories = list
                    }
                    updateUI(Store)
                }
                builder.setNegativeButton(getString(R.string.cancel), null)
                val alert = builder.create()
                alert.show()
            }
        }
        val alert = builder.create()
        alert.show()
    }

    private inner class CategoryPageAdapter(fa: FragmentActivity, private val store: Store) :
        FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return store.categories?.size ?: 0
        }

        override fun createFragment(position: Int): Fragment {
            return CategoryList(store.categories?.get(position)!!)
        }
    }

    interface Callbacks {
        fun setTitle(_title: String)
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