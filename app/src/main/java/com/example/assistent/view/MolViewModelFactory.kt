package com.example.assistent.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assistent.dao.AssistentDAO
import com.example.assistent.viewmodel.MoleViewModel
import java.lang.IllegalArgumentException

class MolViewModelFactory(private val dao : AssistentDAO): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass == MoleViewModel::class.java)
            MoleViewModel(dao) as T
        else
            throw IllegalArgumentException("Wrong ViewModel")
    }
}