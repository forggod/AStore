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
            binding.editTextTextPersonNameFirstName.setText(product!!.firstname)
            binding.editTextTextPersonNameName.setText(product!!.midlename)
            binding.editTextTextPersonNameLastName.setText(product!!.lastname)
            binding.editTextPhone.setText(product!!.phonenumber)
            val dt = GregorianCalendar().apply {
                time = product!!.birthdate

            }
            binding.calendarID.init(
                dt.get(Calendar.YEAR), dt.get(Calendar.MONTH),
                dt.get(Calendar.DAY_OF_MONTH), null
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
            binding.editTextTextPersonNameFirstName.text.toString().ifBlank {
                p = false
                binding.editTextTextPersonNameFirstName.error = "Укажите значение"
            }
            binding.editTextTextPersonNameLastName.text.toString().ifBlank {
                p = false
                binding.editTextTextPersonNameLastName.error = "Укажите значение"
            }
            binding.editTextTextPersonNameName.text.toString().ifBlank {
                p = false
                binding.editTextTextPersonNameName.error = "Укажите значение"
            }
            binding.editTextPhone.text.toString().ifBlank {
                p = false
                binding.editTextPhone.error = "Укажите значение"
            }

            if (GregorianCalendar().get(GregorianCalendar.YEAR) - binding.calendarID.year < 10) {
                p = false
                Toast.makeText(context, "Укажите правильно возраст", Toast.LENGTH_LONG).show()
            }

            if (p) {
                val selectedDate = GregorianCalendar().apply {
                    set(GregorianCalendar.YEAR, binding.calendarID.year)
                    set(GregorianCalendar.YEAR, binding.calendarID.month)
                    set(GregorianCalendar.YEAR, binding.calendarID.dayOfMonth)
                }
                if (product == null) {
                    product = Product()
                    product?.apply {
                        firstname = binding.editTextTextPersonNameFirstName.text.toString()
                        lastname = binding.editTextTextPersonNameLastName.text.toString()
                        midlename = binding.editTextTextPersonNameName.text.toString()
                        phonenumber = binding.editTextPhone.text.toString()
                        birthdate = Date(selectedDate.time.time)
                    }
                    viewModel.newProduct(categoryID!!, product!!)
                } else {
                    product?.apply {
                        firstname = binding.editTextTextPersonNameFirstName.text.toString()
                        lastname = binding.editTextTextPersonNameLastName.text.toString()
                        midlename = binding.editTextTextPersonNameName.text.toString()
                        phonenumber = binding.editTextPhone.text.toString()
                        birthdate = Date(selectedDate.time.time)
                    }
                    viewModel.editProduct(categoryID!!, product!!)
                }
                backPressedCallback.isEnabled = false
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