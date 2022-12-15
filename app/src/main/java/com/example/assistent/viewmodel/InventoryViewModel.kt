package com.example.assistent.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assistent.dao.AssistentDAO
import com.example.assistent.entity.Inventory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InventoryViewModel(val dao: AssistentDAO, val id_Mol: Int) : ViewModel() {
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    var inventoryLiveData = MutableLiveData<List<Inventory>>()

    fun getInventory() {
        coroutineScope.launch {
            val inventoryById = dao.getInventoryById(id_Mol)
            inventoryLiveData.postValue(inventoryById)
        }

    }


}