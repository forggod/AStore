package repository

import androidx.lifecycle.MutableLiveData
import com.example.android_store.data.Store
import com.example.android_store.data.Category
import com.example.android_store.data.Product
import java.util.*
import kotlin.collections.ArrayList

class StoreRepository private constructor() {
    var store: MutableLiveData<List<Store>> = MutableLiveData()

    companion object {
        private var INSTANCE: StoreRepository? = null

        fun newInstance() {
            if (INSTANCE == null) {
                INSTANCE = StoreRepository()
            }
        }

        fun get(): StoreRepository {
            return INSTANCE
                ?: throw java.lang.IllegalStateException("Репозиторий Faculty Repository не иницилизирован")
        }

        fun deleteStudent(id: UUID, product: Product) {
            deleteStudent(id, product)
        }
    }

    fun newStore(name: String) {
        val store = Store(name = name)
        val list: ArrayList<Store> =
            if (this.store.value != null) {
                (this.store.value as ArrayList<Store>)
            } else
                ArrayList<Store>()
        list.add(store)
        this.store.postValue(list)
    }

    fun newGroup(facultyID: UUID, name: String) {
        val u = store.value ?: return
        val faculty = u.find { it.id == facultyID } ?: return
        val category = Category(name = name)
        val list: ArrayList<Category> = if (faculty.categories.isEmpty())
            ArrayList()
        else
            faculty.categories as ArrayList<Category>
        list.add(category)
        faculty.categories = list
        store.postValue(u)
    }

    fun newStudent(groupID: UUID, product: Product) {
        val u = store.value ?: return


        val faculty = u.find { it?.categories?.find { it.id == groupID } != null } ?: return
        val group = faculty.categories?.find { it.id == groupID }
        val list: ArrayList<Product> = if (group!!.product.isEmpty())
            ArrayList()
        else
            group.product as ArrayList<Product>
        list.add(product)
        group.product = list
        store.postValue(u)
    }

    fun deleteStudent(groupID: UUID, product: Product) {
        val u = store.value ?: return
        val faculty = u.find { it?.categories?.find { it.id == groupID } != null } ?: return
        val group = faculty.categories?.find { it.id == groupID } ?: return
        if (group!!.product.isEmpty()) return
        val list = group.product as ArrayList<Product>
        list.remove(product)
        group.product = list
        store.postValue(u)
    }

    fun editStudent(groupID: UUID, product: Product) {
        val u = store.value ?: return
        val faculty = u.find { it?.categories?.find { it.id == groupID } != null } ?: return
        val group = faculty.categories?.find { it.id == groupID } ?: return
        val _student = group.product.find { it.id == product.id }
        if (_student == null) {
            newStudent(groupID, product)
            return
        }

        val list = group.product as ArrayList<Product>
        val i = list.indexOf(_student)
        list.remove(product)
        list.add(i, product)
        group.product = list
        store.postValue(u)
    }

}