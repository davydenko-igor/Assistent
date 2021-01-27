package com.example.assistent.view.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assistent.dao.AssistentDAO
import com.example.assistent.viewmodel.InventoryViewModel
import java.lang.IllegalArgumentException

class InventoryViewModelFactory(private val dao : AssistentDAO, val id_Mol:Int): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass == InventoryViewModel::class.java)
            InventoryViewModel(dao, id_Mol) as T
        else
            throw IllegalArgumentException("Wrong ViewModel")
    }
}