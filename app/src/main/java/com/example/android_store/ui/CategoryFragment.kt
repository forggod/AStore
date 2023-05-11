package com.example.android_store.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android_store.data.Store
import com.example.android_store.data.Product
import com.example.android_store.databinding.FragmentGroupBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

const val CATEGORY_TAG = "CategoryFragment"

class CategoryFragment : Fragment() {

    private var _binding: FragmentGroupBinding? = null
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
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
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
        binding.tabLayoutGroup.clearOnTabSelectedListeners()
        binding.tabLayoutGroup.removeAllTabs()
        for (i in 0 until (Store?.categories?.size ?: 0)) {
            binding.tabLayoutGroup.addTab(binding.tabLayoutGroup.newTab().apply {
                text = i.toString()
            })
        }

        binding.faBtnAddStudent.visibility =
            if ((Store?.categories?.size ?: 0) == 0)
                View.GONE
            else {
                binding.faBtnAddStudent.setOnClickListener {
                    callbacks?.showProduct(Store!!.categories!!.get(tabPosition).id, null)
                }
                View.VISIBLE
            }
        val adapter = CategoryPageAdapter(requireActivity(), Store!!)
        binding.viewPageGroups.adapter = adapter
        TabLayoutMediator(binding.tabLayoutGroup, binding.viewPageGroups, true, true) { tab, pos ->
            tab.text = Store?.categories?.get(pos)?.name
        }.attach()

        binding.tabLayoutGroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPosition = tab?.position!!
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
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