package repository

import androidx.lifecycle.MutableLiveData
import com.example.android_store.data.Store
import com.example.android_store.data.Category
import com.example.android_store.data.Product
import java.util.*
import kotlin.collections.ArrayList

class StoreRepository private constructor() {
    var storeNet: MutableLiveData<List<Store>?> = MutableLiveData()

    companion object {
        private var INSTANCE: StoreRepository? = null

        fun newInstance() {
            if (INSTANCE == null) {
                INSTANCE = StoreRepository()
            }
        }

        fun get(): StoreRepository {
            return INSTANCE
                ?: throw java.lang.IllegalStateException("Репозиторий Store Repository не иницилизирован")
        }

        fun deleteProduct(id: UUID, product: Product) {
            deleteProduct(id, product)
        }
    }

    fun newStore(name: String) {
        val store = Store(name = name)
        val list: ArrayList<Store> =
            if (storeNet.value != null) {
                (storeNet.value as ArrayList<Store>)
            } else
                ArrayList<Store>()
        list.add(store)
        storeNet.postValue(list)
    }

    fun newCategory(storeID: UUID, name: String) {
        val st = storeNet.value ?: return
        val store = st.find { it.id == storeID } ?: return
        val category = Category(name = name)
        val list: ArrayList<Category> = if (store.categories.isEmpty())
            ArrayList()
        else
            store.categories as ArrayList<Category>
        list.add(category)
        store.categories = list
        storeNet.postValue(st)
    }

    fun newProduct(categoryID: UUID, product: Product) {
        val st = storeNet.value ?: return
        val store = st.find { it?.categories?.find { it.id == categoryID } != null } ?: return
        val category = store.categories?.find { it.id == categoryID }
        val list: ArrayList<Product> = if (category!!.product.isEmpty())
            ArrayList()
        else
            category.product as ArrayList<Product>
        list.add(product)
        category.product = list
        storeNet.postValue(st)
    }

    fun deleteProduct(categoryID: UUID, product: Product) {
        val st = storeNet.value ?: return
        val store = st.find { it?.categories?.find { it.id == categoryID } != null } ?: return
        val category = store.categories?.find { it.id == categoryID } ?: return
        if (category!!.product.isEmpty()) return
        val list = category.product as ArrayList<Product>
        list.remove(product)
        category.product = list
        this.storeNet.postValue(st)
    }

    fun editProduct(categoryID: UUID, product: Product) {
        val st = storeNet.value ?: return
        val store = st.find { it?.categories?.find { it.id == categoryID } != null } ?: return
        val category = store.categories?.find { it.id == categoryID } ?: return
        val _product = category.product.find { it.id == product.id }
        if (_product == null) {
            newProduct(categoryID, product)
            return
        }

        val list = category.product as ArrayList<Product>
        val i = list.indexOf(_product)
        list.remove(_product)
        list.add(i, product)
        category.product = list
        storeNet.postValue(st)
    }

    fun deleteStore(storeID: UUID) {
        val st = storeNet.value ?: return
        val store = st.find { it.id == storeID } ?: return
        val storeList = storeNet.value?.toMutableList()
        storeList?.remove(store)
        storeNet.value = storeList
    }
}