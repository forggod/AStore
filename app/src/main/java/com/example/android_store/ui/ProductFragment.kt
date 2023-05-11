package com.example.android_store.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.example.android_store.data.Product
import com.example.android_store.R
import com.example.android_store.databinding.FragmentProductBinding
import java.util.*

const val PRODUCT_TAG = "ProductFragment"

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    companion object {
        private var product: Product? = null
        private var categoryID: UUID? = null
        fun newInstance(categoryID: UUID, product: Product?): ProductFragment {
            this.product = product
            this.categoryID = categoryID
            return ProductFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var viewModel: ProductViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (product != null) {
            binding.editTextName.setText(product!!.name)
            binding.editTextCount.setText(product!!.count)
            binding.editTextPrice.setText(product!!.price)
            val dtm = GregorianCalendar().apply { time = product!!.dayManufacture }
            val dte = GregorianCalendar().apply { time = product!!.dayExpiring }
            binding.calendarIDDayManufacture.init(
                dtm.get(Calendar.YEAR),
                dtm.get(Calendar.MONTH),
                dtm.get(Calendar.DAY_OF_MONTH),
                null,
            )
            binding.calendarIDDayExpiring.init(
                dte.get(Calendar.YEAR),
                dte.get(Calendar.MONTH),
                dte.get(Calendar.DAY_OF_MONTH),
                null,
            )
        }
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showCommitDialog()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun showCommitDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setCancelable(true)
        builder.setMessage("Сохранить изменения?")
        builder.setTitle("Подтверждение")
        builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
            var p = true
            binding.editTextName.text.toString().ifBlank {
                p = false
                binding.editTextName.error = R.string.entry.toString()
            }
            binding.editTextCount.text.toString().ifBlank {
                p = false
                binding.editTextCount.error = R.string.entry.toString()
            }
            binding.editTextPrice.text.toString().ifBlank {
                p = false
                binding.editTextPrice.error = R.string.entry.toString()
            }
            if (binding.calendarIDDayManufacture.year > binding.calendarIDDayExpiring.year) {
                p = false
                Toast.makeText(context, R.string.errorDM.toString(), Toast.LENGTH_LONG).show()
            }

            if (p) {
                val selectedDM = GregorianCalendar().apply {
                    set(GregorianCalendar.YEAR, binding.calendarIDDayManufacture.year)
                    set(GregorianCalendar.MONTH, binding.calendarIDDayManufacture.month)
                    set(GregorianCalendar.DAY_OF_MONTH, binding.calendarIDDayManufacture.dayOfMonth)
                }
                val selectedDE = GregorianCalendar().apply {
                    set(GregorianCalendar.YEAR, binding.calendarIDDayExpiring.year)
                    set(GregorianCalendar.MONTH, binding.calendarIDDayExpiring.month)
                    set(GregorianCalendar.DAY_OF_MONTH, binding.calendarIDDayExpiring.dayOfMonth)
                }
                if (product == null) {
                    product = Product()
                    product?.apply {
                        name = binding.editTextName.text.toString()
                        count = binding.editTextCount.text.toString()
                        price = binding.editTextPrice.text.toString()
                        dayManufacture = Date(selectedDM.time.time)
                        dayExpiring = Date(selectedDE.time.time)
                    }
                    viewModel.newProduct(categoryID!!, product!!)
                } else {
                    product?.apply {
                        name = binding.editTextName.text.toString()
                        count = binding.editTextCount.text.toString()
                        price = binding.editTextPrice.text.toString()
                        dayManufacture = Date(selectedDM.time.time)
                        dayExpiring = Date(selectedDE.time.time)
                    }
                    viewModel.editProduct(categoryID!!, product!!)
                }
                backPressedCallback.isEnabled=false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        builder.setNegativeButton("Отмена") { _, _ ->
            backPressedCallback.isEnabled = true
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        val alert = builder.create()
        alert.show()
    }
}